package de.adesso.jenkinshue.config.security;

import de.adesso.jenkinshue.common.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * @author wennier
 */
@Slf4j
@Component
public class UserAuthenticationProvider implements AuthenticationProvider {

	final UserService userService;

	@Autowired
	public UserAuthenticationProvider(@Qualifier("userServiceImpl") UserService userService) {
		this.userService = userService;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		log.debug("Prüfen, ob sich der Nutzer in der DB befindet...");

		if (userService.findByLogin(authentication.getName().toLowerCase()) == null) {
			log.debug("Der Nutzer {} befindet sich nicht in der DB.", authentication.getName());
			throw new DisabledException(authentication.getName()); // muss eine AccountStatusException sein!!
		}

		log.debug("Der Nutzer {} befindet sich in der DB.", authentication.getName());

		return null; // danach wird Provider gefragt (ldapAuthentication)
	}

}
