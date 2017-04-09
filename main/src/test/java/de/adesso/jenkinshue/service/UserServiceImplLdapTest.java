package de.adesso.jenkinshue.service;

import de.adesso.jenkinshue.common.dto.team.TeamCreateDTO;
import de.adesso.jenkinshue.common.dto.team.TeamLampsDTO;
import de.adesso.jenkinshue.common.dto.user.UserCreateDTO;
import de.adesso.jenkinshue.common.dto.user.UserDTO;
import de.adesso.jenkinshue.common.service.TeamService;
import de.adesso.jenkinshue.common.service.UserService;
import de.adesso.jenkinshue.config.LdapValue;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author wennier
 */
@Component
@ConditionalOnBean(LdapValue.class)
public class UserServiceImplLdapTest {

	public void testCreate(TeamService teamService, UserService userService) {
		assertEquals(0, userService.count());

		TeamLampsDTO team = teamService.create(new TeamCreateDTO("Team 1"));

		userService.create(new UserCreateDTO("wennier", team.getId()));
		assertEquals(1, userService.count());
		List<UserDTO> users = userService.findBySearchItem("wennier");
		assertEquals(1, users.size());
		assertEquals("Wennier", users.get(0).getSurname());
		assertEquals("Fiete", users.get(0).getForename());
	}

	public void testCreateUserWithInvalidLogin(TeamService teamService, UserService userService) {
		TeamLampsDTO team = teamService.create(new TeamCreateDTO("Team 1"));
		userService.create(new UserCreateDTO("wennier2", team.getId()));
		fail();
	}

}
