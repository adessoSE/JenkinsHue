package de.adesso.jenkinshue.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.adesso.jenkinshue.TestCase;
import de.adesso.jenkinshue.common.dto.team.TeamCreateDTO;
import de.adesso.jenkinshue.common.dto.team.TeamLampsDTO;
import de.adesso.jenkinshue.common.dto.user.UserCreateDTO;
import de.adesso.jenkinshue.common.dto.user.UserDTO;
import de.adesso.jenkinshue.common.service.TeamService;
import de.adesso.jenkinshue.common.service.UserService;
import de.adesso.jenkinshue.exception.InvalidLoginException;
import de.adesso.jenkinshue.exception.UserAlreadyExistsException;

/**
 * 
 * @author wennier
 *
 */
public class UserServiceImplTest extends TestCase {

	@Autowired
	private TeamService teamService;
	
	@Autowired
	private UserService userService;
	
	@Test
	public void testCreate() {
		assertEquals(0, userService.count());
		
		TeamLampsDTO team = teamService.create(new TeamCreateDTO("Team 1"));
		
		userService.create(new UserCreateDTO("wennier", team.getId()));
		assertEquals(1, userService.count());
		List<UserDTO> users = userService.findBySearchItem("wennier");
		assertEquals(1, users.size());
		assertEquals("Wennier", users.get(0).getSurname());
		assertEquals("Fiete", users.get(0).getForename());
	}
	
	@Test(expected = UserAlreadyExistsException.class)
	public void testCreateAlreadyExistingUser() {
		TeamLampsDTO team = teamService.create(new TeamCreateDTO("Team 1"));
		userService.create(new UserCreateDTO("wennier", team.getId()));
		assertEquals(1, userService.count());
		userService.create(new UserCreateDTO("wennier", team.getId()));
		fail();
	}
	
	@Test(expected = InvalidLoginException.class)
	public void testCreateUserWithInvalidLogin() {
		TeamLampsDTO team = teamService.create(new TeamCreateDTO("Team 1"));
		userService.create(new UserCreateDTO("wennier2", team.getId()));
		fail();
	}
	
}
