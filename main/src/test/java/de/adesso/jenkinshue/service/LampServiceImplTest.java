package de.adesso.jenkinshue.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

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
		assertEquals(1, lampService.count(team.getId()));
		
		assertEquals(1, lampService.count());
		assertEquals(0, lampService.count(784645218));
		
		Lamp lamp = lampRepository.findOne(lampDTO.getId());
		assertEquals(lamp.getHueUniqueId(), lampDTO.getHueUniqueId());
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

}
