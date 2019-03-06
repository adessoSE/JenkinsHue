package de.adesso.jenkinshue.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import de.adesso.jenkinshue.common.dto.job.JobDTO;
import de.adesso.jenkinshue.common.dto.lamp.LampRenameDTO;
import de.adesso.jenkinshue.common.dto.lamp.LampUpdateDTO;
import de.adesso.jenkinshue.common.dto.lamp.LampUpdateLastShownScenarioDTO;
import de.adesso.jenkinshue.common.enumeration.Scenario;
import de.adesso.jenkinshue.exception.EmptyInputException;
import de.adesso.jenkinshue.exception.InvalidWorkingPeriodException;
import de.adesso.jenkinshue.exception.LampAlreadyExistsException;
import de.adesso.jenkinshue.exception.LampDoesNotExistsException;
import de.adesso.jenkinshue.exception.TeamDoesNotExistException;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.adesso.jenkinshue.TestCase;
import de.adesso.jenkinshue.common.dto.lamp.LampCreateDTO;
import de.adesso.jenkinshue.common.dto.lamp.LampDTO;
import de.adesso.jenkinshue.common.dto.team.TeamCreateDTO;
import de.adesso.jenkinshue.common.dto.team.TeamLampsDTO;
import de.adesso.jenkinshue.common.service.LampService;
import de.adesso.jenkinshue.common.service.TeamService;
import de.adesso.jenkinshue.entity.Lamp;
import de.adesso.jenkinshue.repository.LampRepository;

/**
 * 
 * @author wennier
 *
 */
public class LampServiceImplTest extends TestCase {
	
	@Autowired
	private TeamService teamService;
	
	@Autowired
	private LampService lampService;
	
	@Autowired
	private LampRepository lampRepository;
	
	@Test
	public void testCreate() {
		assertEquals(0, lampService.count());
		
		TeamLampsDTO team = teamService.create(new TeamCreateDTO("Team 1"));
		
		LampCreateDTO lampCreateDTO = new LampCreateDTO("hueId", "Name 1", team.getId());
		
		assertEquals(0, lampService.count(team.getId()));

		LampDTO lampDTO = lampService.create(lampCreateDTO);
		assertNull(lampDTO.getJobs());
		assertNull(lampDTO.getLastShownScenario());
		assertEquals(4, lampDTO.getScenarioConfigs().size());
		assertNotNull(lampDTO.getWorkingStart());
		assertNotNull(lampDTO.getWorkingEnd());

		assertEquals(1, lampService.count(team.getId()));
		assertEquals(1, lampService.count());
		assertEquals(0, lampService.count(-1));

		Lamp lamp = lampRepository.findOne(lampDTO.getId());
		assertEquals(lamp.getHueUniqueId(), lampDTO.getHueUniqueId());
	}

	@Test(expected = EmptyInputException.class)
	public void testCreateWithoutValidName() {
		try {
			lampService.create(new LampCreateDTO("unique id", null, 0));
		} catch(EmptyInputException eie) {
			lampService.create(new LampCreateDTO("unique id", "", 0));
		}
		fail();
	}

	@Test(expected = EmptyInputException.class)
	public void testCreateWithoutValidHueUniqueId() {
		try {
			lampService.create(new LampCreateDTO(null, "Lamp 1", 0));
		} catch(EmptyInputException eie) {
			lampService.create(new LampCreateDTO("", "Lamp 1", 0));
		}
		fail();
	}

	@Test(expected = TeamDoesNotExistException.class)
	public void testCreateWithoutValidTeamId() {
		lampService.create(new LampCreateDTO("unique id", "Lamp 1", -1));
		fail();
	}

	@Test(expected = LampAlreadyExistsException.class)
	public void testCreateWithDuplicateHueUniqueId() {
		TeamLampsDTO team = teamService.create(new TeamCreateDTO("Team 1"));
		lampService.create(new LampCreateDTO("unique id", "Lamp 1", team.getId()));
		lampService.create(new LampCreateDTO("unique id", "Lamp 1", team.getId()));
		fail();
	}

	@Test
	public void testUpdate() {
		TeamLampsDTO team = teamService.create(new TeamCreateDTO("Team 1"));
		LampDTO lamp = lampService.create(new LampCreateDTO("unique id", "Lamp 1", team.getId()));

		assertNotNull(lamp.getWorkingStart());
		assertNotNull(lamp.getWorkingEnd());
		assertNull(lamp.getLastShownScenario());
		assertNull(lamp.getJobs());
		assertTrue(lamp.getScenarioConfigs() != null && lamp.getScenarioConfigs().size() > 0);

		LampUpdateDTO lampUpdate = new LampUpdateDTO(lamp.getId(), null, null,
				Collections.singletonList(new JobDTO(0, "Job 1")), null, null,
				null, null);
		lamp = lampService.update(lampUpdate);

		assertNotNull(lamp.getWorkingStart());
		assertNotNull(lamp.getWorkingEnd());
		assertEquals(1, lamp.getJobs().size());
		assertEquals(0, lamp.getScenarioConfigs().size());
	}

	@Test(expected = InvalidWorkingPeriodException.class)
	public void testUpdateWithInvalidWorkingPeriod() {
		Date workingStart = DateTime.now().withMillisOfDay(0).withHourOfDay(16).toDate();
		Date workingEnd = DateTime.now().withMillisOfDay(0).withHourOfDay(6).toDate();

		lampService.update(new LampUpdateDTO(0, workingStart, workingEnd,
				null, null, null,
				null, null));
		fail();
	}

	@Test(expected = LampDoesNotExistsException.class)
	public void testUpdateWithoutValidLampId() {
		lampService.update(new LampUpdateDTO(-1, null, null,
				null, null, null,
				null, null));
		fail();
	}

	@Test(expected = LampDoesNotExistsException.class)
	public void testUpdateLastShownScenarioWithoutValidLampId() {
		lampService.updateLastShownScenario(new LampUpdateLastShownScenarioDTO(-1, Scenario.BUILDING));
		fail();
	}

	@Test(expected = EmptyInputException.class)
	public void testRenameWithEmptyInput() {
		try {
			lampService.rename(new LampRenameDTO(-1, null));
		} catch (EmptyInputException eie) {
			lampService.rename(new LampRenameDTO(-1, ""));
		}
		fail();
	}

	@Test(expected = LampDoesNotExistsException.class)
	public void testRenameWithInvalidLampId() {
		lampService.rename(new LampRenameDTO(-1, "Lamp 1"));
		fail();
	}

	@Test
	public void testFindAll() {
		TeamLampsDTO team = teamService.create(new TeamCreateDTO("Team 1"));

		assertEquals(0, lampService.count(team.getId()));
		
		LampCreateDTO lampCreateDTO1 = new LampCreateDTO("hueId 1", "Name 1", team.getId());
		LampDTO lampDTO1 = lampService.create(lampCreateDTO1);
		
		assertEquals(1, lampService.count(team.getId()));
		assertEquals(lampCreateDTO1.getHueUniqueId(), lampService.findAll().get(0).getHueUniqueId());
		
		LampCreateDTO lampCreateDTO2 = new LampCreateDTO("hueId 2", "Name 2", team.getId());
		LampDTO lampDTO2 = lampService.create(lampCreateDTO2);
		
		assertEquals(2, lampService.count(team.getId()));
		List<LampDTO> lampDTOs = lampService.findAll();
		String hueUniqueId2 = lampDTOs.get(1).getHueUniqueId();
		if(!hueUniqueId2.equals(lampDTO1.getHueUniqueId()) && !hueUniqueId2.equals(lampDTO2.getHueUniqueId())) {
			fail();
		}
	}

	@Test(expected = TeamDoesNotExistException.class)
	public void testFindAllOfATeamWithoutValidTeamId() {
		lampService.findAllOfATeam(-1);
		fail();
	}
}
