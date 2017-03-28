package de.adesso.jenkinshue.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 
 * @author wennier
 *
 */
@ResponseStatus
public class UserAlreadyExistsException extends RuntimeException {
	
	private static final long serialVersionUID = 8000996297037785772L;

	public UserAlreadyExistsException(String email) {
		super("Der Benutzer mit der E-Mail-Adresse '" + email + "' existiert bereits!");
	}

}
