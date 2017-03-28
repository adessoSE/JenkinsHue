package de.adesso.jenkinshue.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 
 * @author wennier
 *
 */
@ResponseStatus
public class InvalidLoginException extends RuntimeException {
	
	private static final long serialVersionUID = 7507416774787395629L;

	public InvalidLoginException(String login) {
		super("Zum Login '" + login + "' existiert kein Benutzer im LDAP!");
	}

}