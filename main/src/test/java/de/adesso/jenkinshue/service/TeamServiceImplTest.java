package de.adesso.jenkinshue.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.adesso.jenkinshue.TestCase;
import de.adesso.jenkinshue.common.dto.team.TeamCreateDTO;
import de.adesso.jenkinshue.common.dto.team.TeamLampsDTO;
import de.adesso.jenkinshue.common.service.TeamService;
import de.adesso.jenkinshue.repository.TeamRepository;

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
		assertEquals("Team 1", teamRepository.findOne(teamDTO1.getId()).getName());
		
		TeamCreateDTO teamCreateDTO2 = new TeamCreateDTO("Team 2");
		TeamLampsDTO teamDTO2 = teamService.create(teamCreateDTO2);
		assertEquals(2, teamService.count());
		assertEquals("Team 2", teamRepository.findOne(teamDTO2.getId()).getName());
	}

}
