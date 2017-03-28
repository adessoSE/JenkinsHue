package de.adesso.jenkinshue.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.adesso.jenkinshue.TestCase;
import de.adesso.jenkinshue.common.dto.job.JobDTO;
import de.adesso.jenkinshue.common.dto.lamp.LampCreateDTO;
import de.adesso.jenkinshue.common.dto.lamp.LampDTO;
import de.adesso.jenkinshue.common.dto.lamp.LampUpdateDTO;
import de.adesso.jenkinshue.common.dto.scenario_config.ScenarioConfigDTO;
import de.adesso.jenkinshue.common.dto.team.TeamCreateDTO;
import de.adesso.jenkinshue.common.dto.team.TeamLampsDTO;
import de.adesso.jenkinshue.common.enumeration.BuildState;
import de.adesso.jenkinshue.common.service.JobService;
import de.adesso.jenkinshue.common.service.LampService;
import de.adesso.jenkinshue.common.service.TeamService;
import de.adesso.jenkinshue.dozer.Mapper;
import de.adesso.jenkinshue.entity.Job;
import de.adesso.jenkinshue.repository.JobRepository;

/**
 * 
 * @author wennier
 *
 */
public class JobServiceImplTest extends TestCase {

	@Autowired
	private JobRepository jobRepository;
	
	@Autowired
	private JobService jobService;
	
	@Autowired
	private TeamService teamService;
	
	@Autowired
	private LampService lampService;
	
	@Autowired
	private Mapper mapper;
	
	@Test
	public void testUpdate() {
		assertEquals(0, jobRepository.count());
		
		final String NAME = "Job umbenannt";
		
		Job job = new Job(0, "Job 1");
		job = jobRepository.save(job);
		
		JobDTO jobDTO = mapper.map(job, JobDTO.class);
		jobDTO.setName(NAME);
		jobDTO = jobService.update(jobDTO);
		
		assertEquals(NAME, jobRepository.findOne(job.getId()).getName());
	}
	
	@Test
	public void testCountNameDistinctly() {
		assertEquals(0, jobService.countNameDistinctly());
		
		jobRepository.save(new Job(0, "A"));
		jobRepository.save(new Job(0, "A"));
		jobRepository.save(new Job(0, "B"));
		jobRepository.save(new Job(0, "B"));
		jobRepository.save(new Job(0, "C"));
		
		assertEquals(3, jobService.countNameDistinctly());
	}
	
	@Test
	public void testCountNameDistinctlyWithTeamId() {
		assertEquals(0, jobService.countNameDistinctly());
		
		TeamLampsDTO team = teamService.create(new TeamCreateDTO("Team 1"));
		LampDTO lampDTO = lampService.create(new LampCreateDTO("hueId", "Lampe 1", team.getId()));
		
		assertEquals(0, jobService.countNameDistinctly(team.getId()));
		
		LampUpdateDTO lampUpdateDTO = new LampUpdateDTO();
		lampUpdateDTO.setHueUniqueId(lampDTO.getHueUniqueId());
		lampUpdateDTO.setId(lampDTO.getId());
		lampUpdateDTO.setName(lampDTO.getName());
		
		lampUpdateDTO.setBuildingConfigs(new ArrayList<>());
		lampUpdateDTO.setFailureConfigs(new ArrayList<>());
		lampUpdateDTO.setUnstableConfigs(new ArrayList<>());
		lampUpdateDTO.setSuccessConfigs(new ArrayList<>());
		List<ScenarioConfigDTO> scenarioConfigs2 = lampDTO.getScenarioConfigs();
		for(ScenarioConfigDTO config : scenarioConfigs2) {
			String scenario = config.getScenario().toString();
			if(scenario.startsWith(BuildState.BUILDING.toString())) {
				lampUpdateDTO.getBuildingConfigs().add(config);
			} else if(scenario.startsWith(BuildState.FAILURE.toString())) {
				lampUpdateDTO.getFailureConfigs().add(config);
			} else if(scenario.startsWith(BuildState.UNSTABLE.toString())) {
				lampUpdateDTO.getUnstableConfigs().add(config);
			} else if(scenario.startsWith(BuildState.SUCCESS.toString())) {
				lampUpdateDTO.getSuccessConfigs().add(config);
			}
		}
		
		List<String> jobNames = Arrays.asList(new String[]{ "JobA", "JobB", "JobB", "JobB" });
		List<JobDTO> jobs = new ArrayList<>();
		for(String name : jobNames) {
			jobs.add(new JobDTO(0, name));
		}
		lampUpdateDTO.setJobs(jobs);
		lampUpdateDTO.setWorkingStart(lampDTO.getWorkingStart());
		lampUpdateDTO.setWorkingEnd(lampDTO.getWorkingEnd());
		lampUpdateDTO.setTeamId(lampDTO.getTeam().getId());
		lampService.update(lampUpdateDTO);
		
		assertEquals(2, jobService.countNameDistinctly(team.getId()));
		assertEquals(0, jobService.countNameDistinctly(654846215));
	}
	
}
