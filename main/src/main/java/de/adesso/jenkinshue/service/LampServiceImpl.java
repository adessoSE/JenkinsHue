package de.adesso.jenkinshue.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import de.adesso.jenkinshue.common.dto.lamp.GroupedScenarioConfigsLamp;
import de.adesso.jenkinshue.common.dto.lamp.LampCreateDTO;
import de.adesso.jenkinshue.common.dto.lamp.LampDTO;
import de.adesso.jenkinshue.common.dto.lamp.LampGroupedScenariosDTO;
import de.adesso.jenkinshue.common.dto.lamp.LampHueDTO;
import de.adesso.jenkinshue.common.dto.lamp.LampNameDTO;
import de.adesso.jenkinshue.common.dto.lamp.LampRenameDTO;
import de.adesso.jenkinshue.common.dto.lamp.LampTestDTO;
import de.adesso.jenkinshue.common.dto.lamp.LampTurnOffDTO;
import de.adesso.jenkinshue.common.dto.lamp.LampUpdateDTO;
import de.adesso.jenkinshue.common.dto.lamp.LampUpdateLastShownScenarioDTO;
import de.adesso.jenkinshue.common.dto.scenario_config.ScenarioConfigDTO;
import de.adesso.jenkinshue.common.dto.team.TeamLampsDTO;
import de.adesso.jenkinshue.common.enumeration.BuildState;
import de.adesso.jenkinshue.common.service.HolidayService;
import de.adesso.jenkinshue.common.service.HueService;
import de.adesso.jenkinshue.common.service.LampService;
import de.adesso.jenkinshue.dozer.Mapper;
import de.adesso.jenkinshue.entity.Lamp;
import de.adesso.jenkinshue.entity.ScenarioConfig;
import de.adesso.jenkinshue.entity.Team;
import de.adesso.jenkinshue.exception.InvalidWorkingPeriodException;
import de.adesso.jenkinshue.exception.LampAlreadyExistsException;
import de.adesso.jenkinshue.exception.LampDoesNotExistsException;
import de.adesso.jenkinshue.exception.TeamDoesNotExistException;
import de.adesso.jenkinshue.repository.LampRepository;
import de.adesso.jenkinshue.repository.TeamRepository;
import de.adesso.jenkinshue.util.ScenarioUtil;

/**
 * 
 * @author wennier
 *
 */
@Primary
@Service
@Transactional
public class LampServiceImpl implements LampService {

	private final LampRepository lampRepository;
	
	private final TeamRepository teamRepository;
	
	private final HueService hueService;
	
	private final HolidayService holidayService;
	
	private final Mapper mapper;

	@Autowired
	public LampServiceImpl(LampRepository lampRepository, TeamRepository teamRepository, HueService hueService, HolidayService holidayService, Mapper mapper) {
		this.lampRepository = lampRepository;
		this.teamRepository = teamRepository;
		this.hueService = hueService;
		this.holidayService = holidayService;
		this.mapper = mapper;
	}
	
	private List<ScenarioConfig> map(List<ScenarioConfigDTO> scenarioConfigDTOs) {
		List<ScenarioConfig> scenarioConfigs = new ArrayList<>();
		for(ScenarioConfigDTO dto : scenarioConfigDTOs) {
			ScenarioConfig scenarioConfig = mapper.map(dto, ScenarioConfig.class);
			scenarioConfigs.add(scenarioConfig);
		}
		return scenarioConfigs;
	}
	
	private void setGroupedScenarioConfigs(GroupedScenarioConfigsLamp lampDTO, List<ScenarioConfig> scenarioConfigs) {
		lampDTO.setBuildingConfigs(new ArrayList<>());
		lampDTO.setFailureConfigs(new ArrayList<>());
		lampDTO.setUnstableConfigs(new ArrayList<>());
		lampDTO.setSuccessConfigs(new ArrayList<>());
		
		for(ScenarioConfig config : scenarioConfigs) {
			String scenario = config.getScenario().toString();
			if(scenario.startsWith(BuildState.BUILDING.toString())) {
				lampDTO.getBuildingConfigs().add(mapper.map(config, ScenarioConfigDTO.class));
			} else if(scenario.startsWith(BuildState.FAILURE.toString())) {
				lampDTO.getFailureConfigs().add(mapper.map(config, ScenarioConfigDTO.class));
			} else if(scenario.startsWith(BuildState.UNSTABLE.toString())) {
				lampDTO.getUnstableConfigs().add(mapper.map(config, ScenarioConfigDTO.class));
			} else if(scenario.startsWith(BuildState.SUCCESS.toString())) {
				lampDTO.getSuccessConfigs().add(mapper.map(config, ScenarioConfigDTO.class));
			}
		}
	}
	
	// TODO optimieren
	@Override
	public LampDTO create(LampCreateDTO lamp) throws LampAlreadyExistsException {
		if(lampRepository.findByHueUniqueId(lamp.getHueUniqueId()) == null) {
			Lamp l = mapper.map(lamp, Lamp.class);
			Team team = teamRepository.findOne(lamp.getTeamId());
			
			l.setTeam(team);
			
			DateTime today = DateTime.now();
			today = today.withMillisOfDay(0);
			
			l.setWorkingStart(today.withHourOfDay(7).toDate());
			l.setWorkingEnd(today.withHourOfDay(19).toDate());
			l.setScenarioConfigs(new ScenarioUtil().generateDefaultScenarioConfigs());
			
			l = lampRepository.save(l);
			
			l.getTeam().getScenarioPriority().size();
			l.getScenarioConfigs().size();
			
			/**
			 * andere Moeglichkeit
			 * @see https://sourceforge.net/p/dozer/discussion/452530/thread/bbffc31b/
			 */
			// TODO
			l.setScenarioConfigs(l.getScenarioConfigs());
			
			return mapper.map(l, LampDTO.class);
		} else {
			throw new LampAlreadyExistsException(lamp.getHueUniqueId());
		}
	}

	@Override
	public LampDTO update(LampUpdateDTO lamp) throws InvalidWorkingPeriodException, LampDoesNotExistsException {
		DateTime start = new DateTime(lamp.getWorkingStart());
		DateTime end = new DateTime(lamp.getWorkingEnd());
		if(!holidayService.isValidWorkingPeriod(start, end)) {
			throw new InvalidWorkingPeriodException(start, end);
		}
		
		Lamp l = mapper.map(lamp, Lamp.class);
		if(l != null) {
			l.setTeam(teamRepository.findOne(lamp.getTeamId()));
			
			l.setScenarioConfigs(new ArrayList<>());
			l.getScenarioConfigs().addAll(map(lamp.getBuildingConfigs()));
			l.getScenarioConfigs().addAll(map(lamp.getFailureConfigs()));
			l.getScenarioConfigs().addAll(map(lamp.getUnstableConfigs()));
			l.getScenarioConfigs().addAll(map(lamp.getSuccessConfigs()));
			
			l = lampRepository.save(l);
			
			return mapper.map(l, LampDTO.class);
		} else {
			throw new LampDoesNotExistsException(lamp.getId());
		}
	}
	
	@Override
	public LampDTO updateLastShownScenario(LampUpdateLastShownScenarioDTO lamp) throws LampDoesNotExistsException {
		Lamp l = lampRepository.findOne(lamp.getId());
		if(l != null) {
			l.setLastShownScenario(lamp.getLastShownScenario());
			l = lampRepository.save(l);
			return mapper.map(l, LampDTO.class);
		} else {
			throw new LampDoesNotExistsException(lamp.getId());
		}
		
	}
	
	@Override
	public LampDTO rename(LampRenameDTO lamp) throws LampDoesNotExistsException {
		Lamp l = lampRepository.findOne(lamp.getId());
		if(l != null) {
			if(lamp.getName() != null && !lamp.getName().isEmpty()) {
				l.setName(lamp.getName());
				lampRepository.save(l);
			}
			return mapper.map(l, LampDTO.class);
		} else {
			throw new LampDoesNotExistsException(lamp.getId());
		}
	}
	
	@Override
	public void remove(long id) {
		lampRepository.delete(id);
	}

	@Override
	public List<LampDTO> findAll() {
		List<Lamp> lamps = lampRepository.findAll();
		List<LampDTO> dtos = new ArrayList<>(lamps.size());
		for(Lamp lamp : lamps) {
			LampDTO dto = mapper.map(lamp, LampDTO.class);
			dtos.add(dto);
		}
		return dtos;
	}
	
	@Override
	public LampGroupedScenariosDTO findOne(long id) throws LampDoesNotExistsException {
		Lamp lamp = lampRepository.findOne(id);
		if(lamp != null) {
			LampGroupedScenariosDTO dto = mapper.map(lamp, LampGroupedScenariosDTO.class);
			setGroupedScenarioConfigs(dto, lamp.getScenarioConfigs());
			
			return dto;
		} else {
			throw new LampDoesNotExistsException(id);
		}
	}

	@Override
	public long count() {
		return lampRepository.count();
	}
	
	@Override
	public long count(long teamId) {
		return lampRepository.count(teamId);
	}

	@Override
	public List<LampHueDTO> findAllAvailable() {
		List<LampHueDTO> currentLamps = hueService.findAllLamps();
		List<String> hueUniqueIdsInDB = lampRepository.findAllHueUniqueIds();
		
		List<LampHueDTO> availableLamps = new ArrayList<>();
		for(LampHueDTO lamp : currentLamps) {
			if(!hueUniqueIdsInDB.contains(lamp.getUniqueId())) {
				availableLamps.add(lamp);
			}
		}
		
		return availableLamps;
	}

	@Override
	public TeamLampsDTO findAllOfATeam(long teamId) throws TeamDoesNotExistException {
		Team team = teamRepository.findOne(teamId);
		if(team != null) {
			TeamLampsDTO dto = mapper.map(team, TeamLampsDTO.class);
			for(int i = 0; i < team.getLamps().size(); i++) {
				TeamLampsDTO.TeamLampsDTO_LampDTO lampDTO = dto.getLamps().get(i);
				setGroupedScenarioConfigs(lampDTO, team.getLamps().get(i).getScenarioConfigs());
			}
			return dto;
		} else {
			throw new TeamDoesNotExistException(teamId);
		}
	}
	
	@Override
	public List<LampNameDTO> findAllLampNamesOfATeam(long teamId) throws TeamDoesNotExistException {
		TeamLampsDTO teamLampsDTO = findAllOfATeam(teamId);
		List<LampNameDTO> lamps = new ArrayList<>();
		if(teamLampsDTO.getLamps() != null) {
			for(TeamLampsDTO.TeamLampsDTO_LampDTO l : teamLampsDTO.getLamps()) {
				lamps.add(new LampNameDTO(l.getId(), l.getHueUniqueId(), l.getName()));
			}
		}
		return lamps;
	}

	@Override
	public void testScenario(LampTestDTO lamp) {
		hueService.testScenario(lamp);
	}
	
	@Override
	public void pulseOnce(String hueUniqueId) {
		hueService.pulseOnce(hueUniqueId);
	}

	@Override
	public void turnOff(LampTurnOffDTO lamp) {
		hueService.turnOff(lamp);
	}

}
