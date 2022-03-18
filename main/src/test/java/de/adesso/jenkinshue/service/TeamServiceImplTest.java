package de.adesso.jenkinshue.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import de.adesso.jenkinshue.common.dto.team.TeamRenameDTO;
import de.adesso.jenkinshue.common.dto.team.TeamUpdateDTO;
import de.adesso.jenkinshue.entity.Team;
import de.adesso.jenkinshue.exception.EmptyInputException;
import de.adesso.jenkinshue.exception.TeamDoesNotExistException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.adesso.jenkinshue.TestCase;
import de.adesso.jenkinshue.common.dto.team.TeamCreateDTO;
import de.adesso.jenkinshue.common.dto.team.TeamLampsDTO;
import de.adesso.jenkinshue.common.service.TeamService;
import de.adesso.jenkinshue.repository.TeamRepository;

import java.util.NoSuchElementException;

/**
 * 
 * @author wennier
 *
 */
public class TeamServiceImplTest extends TestCase {
	
	@Autowired
	private TeamService teamService;
	
	@Autowired
	private TeamRepository teamRepository;
	
	@Test
	public void testCreate() {
		assertEquals(0, teamService.count());
		
		TeamCreateDTO teamCreateDTO1 = new TeamCreateDTO("Team 1");
		TeamLampsDTO teamDTO1 = teamService.create(teamCreateDTO1);
		assertEquals(1, teamService.count());
		assertEquals("Team 1", teamRepository.findById(teamDTO1.getId()).map(Team::getName).orElseThrow(NoSuchElementException::new));
		
		TeamCreateDTO teamCreateDTO2 = new TeamCreateDTO("Team 2");
		TeamLampsDTO teamDTO2 = teamService.create(teamCreateDTO2);
		assertEquals(2, teamService.count());
		assertEquals("Team 2", teamRepository.findById(teamDTO2.getId()).map(Team::getName).orElseThrow(NoSuchElementException::new));
	}

	@Test(expected = EmptyInputException.class)
	public void testCreateTeamWithoutName() {
		try {
			teamService.create(new TeamCreateDTO(null));
		} catch (EmptyInputException eie) {
			teamService.create(new TeamCreateDTO(""));
		}
		fail();
	}

	@Test(expected = TeamDoesNotExistException.class)
	public void testUpdateTeamWithoutValidId() {
		teamService.update(new TeamUpdateDTO(-1, null));
		fail();
	}

	@Test(expected = TeamDoesNotExistException.class)
	public void testRenameTeamWithoutValidId() {
		teamService.rename(new TeamRenameDTO(-1, "Team 2"));
		fail();
	}

	@Test(expected = EmptyInputException.class)
	public void testRenameTeamWithEmptyInput() {
		TeamLampsDTO team = teamService.create(new TeamCreateDTO("Team 1"));
		try {
			teamService.rename(new TeamRenameDTO(team.getId(), null));
		} catch(EmptyInputException eie) {
			teamService.rename(new TeamRenameDTO(team.getId(), ""));
		}
	}
}
