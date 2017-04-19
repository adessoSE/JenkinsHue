package de.adesso.jenkinshue.rest;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import de.adesso.jenkinshue.common.dto.DashboardInformationDTO;
import de.adesso.jenkinshue.common.dto.PrincipalDTO;
import de.adesso.jenkinshue.common.dto.lamp.LampNameDTO;
import de.adesso.jenkinshue.common.enumeration.Role;
import de.adesso.jenkinshue.common.enumeration.Scenario;
import de.adesso.jenkinshue.common.service.BridgeService;
import de.adesso.jenkinshue.common.service.JobService;
import de.adesso.jenkinshue.common.service.LampService;
import de.adesso.jenkinshue.common.service.UserService;
import de.adesso.jenkinshue.entity.User;
import de.adesso.jenkinshue.exception.TeamDoesNotExistException;
import de.adesso.jenkinshue.repository.UserRepository;

/**
 * 
 * @author wennier
 *
 */
@RestController
@RequestMapping("/rest/universal")
public class UniversalController {
	
	@Autowired
	private BridgeService bridgeService;
	
	@Autowired
	private LampService lampService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JobService jobService;
	
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping("/scenarios")
	public List<Scenario> scenarios() {
		return Arrays.asList(Scenario.values());
	}
	
	@RequestMapping("/roles")
	public List<Role> roles() {
		return Arrays.asList(Role.values());
	}
	
	@RequestMapping("/dashboard")
	public DashboardInformationDTO getDashboardInformation(@RequestParam(required = true) long teamId) throws TeamDoesNotExistException {
		long bridgeCount = bridgeService.count();
		long availableLampCount = lampService.findAllAvailable().size();
		long teamLampCount = lampService.count(teamId);
		long lampCount = lampService.count();
		long teamUserCount = userService.count(teamId);
		long userCount = userService.count();
		long teamJobCount = jobService.countNameDistinctly(teamId);
		long jobCount = jobService.countNameDistinctly();
		List<LampNameDTO> teamLamps = lampService.findAllLampNamesOfATeam(teamId);
		
		return new DashboardInformationDTO(bridgeCount, availableLampCount, teamLampCount, lampCount, teamUserCount, userCount, teamJobCount, jobCount, teamLamps);
	}
	
	@RequestMapping(value = "/principal")
	public @ResponseBody PrincipalDTO getPrincipal(Principal principal) {
		PrincipalDTO dto = null;
		if(principal != null && principal.getName() != null && !principal.getName().isEmpty()) {
			dto = new PrincipalDTO();
			dto.setPrincipal(principal);
			User user = userRepository.findByLogin(principal.getName().toLowerCase());
			if(user != null) {
				dto.setUserId(user.getId());
				dto.setTeamId(user.getTeam().getId());
			}
		}
		return dto;
	}
	
	
	
	
}
