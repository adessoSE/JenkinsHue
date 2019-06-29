package de.adesso.jenkinshue;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import de.adesso.jenkinshue.common.dto.job.JobDTO;
import de.adesso.jenkinshue.common.dto.lamp.LampDTO;
import de.adesso.jenkinshue.common.dto.lamp.LampHueDTO;
import de.adesso.jenkinshue.common.dto.lamp.LampDTO.LampDTO_TeamDTO;
import de.adesso.jenkinshue.common.dto.lamp.LampNameDTO;
import de.adesso.jenkinshue.common.dto.lamp.LampTurnOffDTO;
import de.adesso.jenkinshue.common.dto.lamp.LampUpdateLastShownScenarioDTO;
import de.adesso.jenkinshue.common.dto.scenario_config.ScenarioConfigDTO;
import de.adesso.jenkinshue.common.enumeration.BuildState;
import de.adesso.jenkinshue.common.enumeration.Scenario;
import de.adesso.jenkinshue.common.jenkins.dto.JenkinsBuildDTO;
import de.adesso.jenkinshue.common.jenkins.dto.JenkinsDTO;
import de.adesso.jenkinshue.common.jenkins.dto.JenkinsJobDTO;
import de.adesso.jenkinshue.common.service.HueService;
import de.adesso.jenkinshue.common.service.JenkinsService;
import de.adesso.jenkinshue.common.service.LampService;
import de.adesso.jenkinshue.exception.JenkinsJobDoesNotExistException;
import lombok.extern.log4j.Log4j2;

import static de.adesso.jenkinshue.util.ListUtil.nullSafe;

/**
 * @author wennier
 */
@Log4j2
@Component
public class JobListener {

	private final JenkinsService jenkinsService;

	private final LampService lampService;

	private final HueService hueService;

	@Autowired
	public JobListener(JenkinsService jenkinsService, LampService lampService, HueService hueService) {
		this.jenkinsService = jenkinsService;
		this.lampService = lampService;
		this.hueService = hueService;
	}

	@Async
	void updateLamps() {
		JenkinsDTO jenkinsDTO = jenkinsService.getJenkins();
		List<JenkinsJobDTO> jenkinsJobs = jenkinsDTO.getJobs();

		log.debug("Anzahl Jenkins-Jobs: " + jenkinsJobs.size());

		List<LampDTO> lamps = lampService.findAll();
		log.debug("Anzahl Lampen: " + lamps.size());

		List<LampHueDTO> connectedLamps = hueService.findAllLamps();
		log.debug("Gefundene Lampen: " + connectedLamps.size());

		for (LampDTO lamp : lamps) {
			updateLamp(lamp, jenkinsJobs, connectedLamps);
		}
	}

	void updateLamp(LampDTO lamp, List<JenkinsJobDTO> jenkinsJobs, List<LampHueDTO> connectedLamps) {
		if (isTurnOffTime(lamp, DateTime.now())) {
			LampNameDTO turnOffThisLamp = new LampNameDTO(lamp.getId(), lamp.getHueUniqueId(), lamp.getName());
			hueService.turnOff(new LampTurnOffDTO(Collections.singletonList(turnOffThisLamp)));
		} else {
			LampDTO_TeamDTO team = lamp.getTeam();
			List<Scenario> scenarioPriority = team.getScenarioPriority();

			int highestPriority = Integer.MAX_VALUE;
			Scenario highestPrioritizedScenario = null;

			List<JobDTO> jobs = nullSafe(lamp.getJobs());
			log.debug("Der Lampe " + lamp.getHueUniqueId() + " zugewiesene Jobs: " + jobs.size());

			// bestimme das wichtigste Szenario
			for (JobDTO job : jobs) {
				try {
					JenkinsJobDTO newJobInformation = determineJenkinsJob(jenkinsJobs, job);
					Scenario scenario = determineLastScenario(newJobInformation);
					if (scenario != null) { // es existiert min. ein Build
						int priority = scenarioPriority.indexOf(scenario);
						if (priority < highestPriority && priority > -1) {
							highestPrioritizedScenario = scenario;
							highestPriority = priority;
						}
					}
				} catch (JenkinsJobDoesNotExistException jjdnee) {
					if (job.getName().contains("/")) { // it is a job of the NEW jenkins (in a folder)
						log.error(jjdnee);
					}
				}
			}

			if (highestPrioritizedScenario != null) { // at least one build with build state BUILDING, FAILURE, UNSTABLE or SUCCESS
				ScenarioConfigDTO config = null;

				for (ScenarioConfigDTO tmp : lamp.getScenarioConfigs()) {
					if (highestPrioritizedScenario.equals(tmp.getScenario())) {
						config = tmp;
					}
				}

				Scenario fallback = null;
				if (config == null) { // keine passende ScenarioConfig -> Plan B
					if (highestPrioritizedScenario.toString().startsWith("BUILDING")) {
						fallback = Scenario.BUILDING;
					} else if (highestPrioritizedScenario.toString().startsWith("FAILURE")) {
						fallback = Scenario.FAILURE;
					} else if (highestPrioritizedScenario.toString().startsWith("UNSTABLE")) {
						fallback = Scenario.UNSTABLE;
					} else if (highestPrioritizedScenario.toString().startsWith("SUCCESS")) {
						fallback = Scenario.SUCCESS;
					}
					for (ScenarioConfigDTO tmp : lamp.getScenarioConfigs()) {
						if (tmp.getScenario().equals(fallback)) {
							config = tmp;
						}
					}
				}

				if (!highestPrioritizedScenario.equals(lamp.getLastShownScenario())) {
					log.debug("(priorisiertes) eingetretenes Szenario: " + highestPrioritizedScenario);
					log.debug("anzuzeigende Konfiguration: " + config);
					log.debug("Fallback: " + (fallback != null));

					hueService.updateLamp(lamp, config); // wenn (config == null) wird nichts passieren
				} else if (lampIsOff(lamp, connectedLamps)) { // Lampe wird eingeschaltet
					log.debug("Lampe wird eingeschaltet :-)");
					log.debug("(priorisiertes) eingetretenes Szenario: " + highestPrioritizedScenario);
					log.debug("Fallback: " + (fallback != null));

					hueService.updateLamp(lamp, config);
				} else {
					log.debug("Die Szenarios der überwachten Jobs haben sich nicht geändert!");
				}

				// "shown" ist es auch, wenn die Lampe aus ist!
				lampService.updateLastShownScenario(new LampUpdateLastShownScenarioDTO(lamp.getId(), highestPrioritizedScenario));
			}
		}
	}

	boolean isTurnOffTime(LampDTO lamp, DateTime now) {
		int workingStart = new DateTime(lamp.getWorkingStart()).getMillisOfDay();
		int workingEnd = new DateTime(lamp.getWorkingEnd()).getMillisOfDay();
		int n = now.getMillisOfDay();

		return (workingStart > n) || (n > workingEnd);
	}

	boolean lampIsOff(LampDTO lamp, List<LampHueDTO> connectedLamps) {
		for (LampHueDTO l : connectedLamps) {
			if (l.getUniqueId().equals(lamp.getHueUniqueId())) {
				return !l.isLampOn();
			}
		}
		return false;
	}
	
	/* wird nicht genutzt, weil eine Zeitänderung vom UI nicht beruecksichtigt wird
	boolean workingTimeHasStartedRecently(LampDTO lamp, DateTime now) {
		int workingStart = new DateTime(lamp.getWorkingStart()).getMillisOfDay();
		int workingEnd = new DateTime(lamp.getWorkingEnd()).getMillisOfDay();
		int n = now.getMillisOfDay();

		if ((workingStart <= n) && (n <= workingEnd)) {
			n = n - workingStart;
			return n < Scheduler.INTERVAL;
		} else {
			return false;
		}
	}*/

	/**
	 * @param jenkinsJobs
	 * @param job
	 * @return
	 * @throws JenkinsJobDoesNotExistException
	 */
	private JenkinsJobDTO determineJenkinsJob(List<JenkinsJobDTO> jenkinsJobs, JobDTO job) throws JenkinsJobDoesNotExistException {
		for (JenkinsJobDTO jenkinsJob : jenkinsJobs) {
			if (jenkinsJob.getName().equals(job.getName())) {
				return jenkinsJob;
			}
		}
		throw new JenkinsJobDoesNotExistException(job.getName());
	}

	private Scenario determineLastScenario(JenkinsJobDTO jenkinsJob) {
		List<JenkinsBuildDTO> builds = jenkinsJob.getBuilds();
		if (builds != null) {
			builds = builds.stream().filter(build -> {
				try {
					return !BuildState.ABORTED.equals(extractBuildState(build));
				} catch (IllegalArgumentException e) {
					return false; // unsupported build state
				}
			}).collect(Collectors.toList());
		}

		if (builds == null || builds.isEmpty()) {
			return null;
		} else if (builds.size() == 1) {
			BuildState currentState = extractBuildState(builds.get(0));
			return Scenario.valueOf(currentState.toString() + "_AFTER_SUCCESS");
		} else { // mehr als 1 Build -> ausreichend fuer Szenario
			BuildState previousState = extractBuildState(builds.get(1));
			BuildState currentState = extractBuildState(builds.get(0));
			return buildStatesToScenario(previousState, currentState);
		}
	}

	private Scenario buildStatesToScenario(BuildState previousState, BuildState currentState) {
		if (previousState == null || currentState == null) {
			throw new IllegalArgumentException("previousState oder newState ist null!");
		} else {
			return Scenario.valueOf(currentState.toString() + "_AFTER_" + previousState.toString());
		}
	}

	BuildState extractBuildState(JenkinsBuildDTO build) {
		if (build == null) {
			throw new IllegalArgumentException("build ist null!");
		} else {
			if (build.isBuilding()) {
				return BuildState.BUILDING;
			} else {
				return BuildState.valueOf(build.getResult());
			}
		}
	}

}
