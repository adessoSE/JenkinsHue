package de.adesso.jenkinshue.service;

import de.adesso.jenkinshue.TestCase;
import de.adesso.jenkinshue.common.dto.team.TeamCreateDTO;
import de.adesso.jenkinshue.common.dto.team.TeamLampsDTO;
import de.adesso.jenkinshue.common.dto.user.UserCreateDTO;
import de.adesso.jenkinshue.common.dto.user.UserDTO;
import de.adesso.jenkinshue.common.service.TeamService;
import de.adesso.jenkinshue.common.service.UserService;
import de.adesso.jenkinshue.exception.InvalidLoginException;
import de.adesso.jenkinshue.exception.UserAlreadyExistsException;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * @author wennier
 */
@Log4j2
@SuppressWarnings("ConstantConditions")
public class UserServiceImplTest extends TestCase {

	@Autowired
	private TeamService teamService;

	@Autowired
	private UserService userService;

	@Autowired(required = false)
	private UserServiceImplLdapTest userServiceImplLdapTest;

	@Test
	public void testCreate() {
		assertEquals(0, userService.count());
		TeamLampsDTO team = teamService.create(new TeamCreateDTO("Team 1"));
		userService.create(new UserCreateDTO("wennier", team.getId()));
		assertEquals(1, userService.count());
		UserDTO user = userService.findByLogin("wennier");
		assertNotNull(user);
	}

	@Test(expected = UserAlreadyExistsException.class)
	public void testCreateAlreadyExistingUser() {
		TeamLampsDTO team = teamService.create(new TeamCreateDTO("Team 1"));
		userService.create(new UserCreateDTO("wennier", team.getId()));
		assertEquals(1, userService.count());
		userService.create(new UserCreateDTO("wennier", team.getId()));
		fail();
	}

	@Test
	public void testCreateLdap() {
		if (userServiceImplLdapTest != null) {
			userServiceImplLdapTest.testCreate(teamService, userService);
		} else {
			log.info("No ldap connection!");
		}
	}

	@Test(expected = InvalidLoginException.class)
	public void testCreateUserWithInvalidLoginLdap() {
		if (userServiceImplLdapTest != null) {
			userServiceImplLdapTest.testCreateUserWithInvalidLogin(teamService, userService);
		} else {
			log.info("No ldap connection!");
			throw new InvalidLoginException("login");
		}
	}

}
