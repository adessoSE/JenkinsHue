package de.adesso.jenkinshue.service;

import de.adesso.jenkinshue.TestCase;
import de.adesso.jenkinshue.common.dto.team.TeamCreateDTO;
import de.adesso.jenkinshue.common.dto.team.TeamLampsDTO;
import de.adesso.jenkinshue.common.dto.user.UserCreateDTO;
import de.adesso.jenkinshue.common.dto.user.UserDTO;
import de.adesso.jenkinshue.common.dto.user.UserUpdateDTO;
import de.adesso.jenkinshue.common.service.TeamService;
import de.adesso.jenkinshue.common.service.UserService;
import de.adesso.jenkinshue.exception.EmptyInputException;
import de.adesso.jenkinshue.exception.InvalidLoginException;
import de.adesso.jenkinshue.exception.TeamDoesNotExistException;
import de.adesso.jenkinshue.exception.UserAlreadyExistsException;
import de.adesso.jenkinshue.exception.UserDoesNotExistException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * @author wennier
 */
@Slf4j
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

	@Test(expected = EmptyInputException.class)
	public void testCreateWithEmptyInput() {
		TeamLampsDTO team = teamService.create(new TeamCreateDTO("Team 1"));
		try {
			userService.create(new UserCreateDTO(null, team.getId()));
		} catch (EmptyInputException eie) {
			userService.create(new UserCreateDTO("", team.getId()));
		}
		fail();
	}

	@Test(expected = TeamDoesNotExistException.class)
	public void testCreateWithoutExistingTeam() {
		userService.create(new UserCreateDTO("wennier", -1));
		fail();
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

	@Test(expected = UserDoesNotExistException.class)
	public void testUpdateUserWithoutValidId() {
		userService.update(new UserUpdateDTO(-1, null));
		fail();
	}
}
