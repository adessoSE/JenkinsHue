package de.adesso.jenkinshue.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.adesso.jenkinshue.exception.BridgeAlreadyExistsException;
import de.adesso.jenkinshue.exception.InvalidIpException;
import de.adesso.jenkinshue.exception.UserDoesNotExistException;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.adesso.jenkinshue.TestCase;
import de.adesso.jenkinshue.common.dto.bridge.BridgeCreateDTO;
import de.adesso.jenkinshue.common.dto.team.TeamCreateDTO;
import de.adesso.jenkinshue.common.dto.team.TeamLampsDTO;
import de.adesso.jenkinshue.common.dto.user.UserCreateDTO;
import de.adesso.jenkinshue.common.dto.user.UserDTO;
import de.adesso.jenkinshue.common.service.TeamService;
import de.adesso.jenkinshue.common.service.UserService;

/**
 * 
 * @author wennier
 *
 */
public class BridgeServiceImplTest extends TestCase {
	
	@Autowired
	private TeamService teamService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BridgeServiceImpl bridgeService;

	@Test(expected = InvalidIpException.class)
	public void testCreateWithInvalidIp() {
		bridgeService.create(new BridgeCreateDTO("invalid", -1));
		fail();
	}

	@Test(expected = BridgeAlreadyExistsException.class)
	public void testCreateWithDuplicateIp() {
		String ip = "127.0.0.1";
		assertEquals(0, bridgeService.count());
		
		TeamLampsDTO team = teamService.create(new TeamCreateDTO("Team " + DateTime.now().toString("HH:mm:ss")));
		UserDTO user = userService.create(new UserCreateDTO("email", team.getId()));
		bridgeService.create(new BridgeCreateDTO(ip, user.getId()));
		
		assertEquals(1, bridgeService.count());
		
		// same ip
		bridgeService.create(new BridgeCreateDTO(ip, user.getId()));
	}

	@Test(expected = UserDoesNotExistException.class)
	public void testCreateWithoutValidUserId() {
		bridgeService.create(new BridgeCreateDTO("127.0.0.1", -1));
		fail();
	}

	@Test
	public void testIsIPv4Address() {
		assertTrue(bridgeService.isIPv4Address("0.0.0.0"));
		assertTrue(bridgeService.isIPv4Address("48.51.37.15"));
		assertTrue(bridgeService.isIPv4Address("199.125.25.15"));
		assertTrue(bridgeService.isIPv4Address("245.255.2.245"));
		assertTrue(bridgeService.isIPv4Address("255.255.255.255"));
		
		assertFalse(bridgeService.isIPv4Address(""));
		assertFalse(bridgeService.isIPv4Address("fiidfj"));
		assertFalse(bridgeService.isIPv4Address("124"));
		assertFalse(bridgeService.isIPv4Address("10"));
		assertFalse(bridgeService.isIPv4Address("45.5"));
		assertFalse(bridgeService.isIPv4Address("45.87.125"));
		assertFalse(bridgeService.isIPv4Address("256.48.56.1"));
		assertFalse(bridgeService.isIPv4Address("00.00.45.6"));
		assertFalse(bridgeService.isIPv4Address("01.78.56.56"));
		assertFalse(bridgeService.isIPv4Address("001.2.5.2"));
	}

}
