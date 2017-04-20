package de.adesso.jenkinshue.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import de.adesso.jenkinshue.common.service.UserService;
import lombok.extern.log4j.Log4j2;

/**
 * @author wennier
 */
@Log4j2
@Component
public class UserAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	@Qualifier("userServiceImpl")
	UserService userService;

	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		log.debug("Prüfen, ob sich der Nutzer in der DB befindet...");

		if (userService.findByLogin(authentication.getName().toLowerCase()) == null) {
			log.debug("Der Nutzer " + authentication.getName() + " befindet sich nicht in der DB.");
			throw new DisabledException(authentication.getName()); // muss eine AccountStatusException sein!!
		}

		log.debug("Der Nutzer " + authentication.getName() + " befindet sich in der DB.");

		return null; // danach wird Provider gefragt (ldapAuthentication)
	}

}
