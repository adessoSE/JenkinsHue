package de.adesso.jenkinshue.rest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author wennier
 *
 */
@RestController
@PreAuthorize("hasRole('ROLE_ADMIN') and @universalController.scenarios().size() == 16")
@RequestMapping("/rest")
public class TestController {
	
	
	@RequestMapping("/abc")
	public String abc() {
		return "def";
	}

}
