package de.adesso.jenkinshue.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author wennier
 *
 */
@ResponseStatus
public class UserDoesNotExistException extends RuntimeException {

	private static final long serialVersionUID = -1673274070316831102L;

	public UserDoesNotExistException(long userId) {
		super("Ein Benutzer mit der ID '" + userId + "' existiert nicht!");
	}

}
