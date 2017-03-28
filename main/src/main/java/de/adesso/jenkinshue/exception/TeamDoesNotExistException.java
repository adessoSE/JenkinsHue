package de.adesso.jenkinshue.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 
 * @author wennier
 *
 */
@ResponseStatus
public class TeamDoesNotExistException extends RuntimeException {

	private static final long serialVersionUID = -4673244070616831102L;
	
	public TeamDoesNotExistException(long teamId) {
		super("Ein Team mit der ID '" + teamId + "' existiert nicht!");
	}

}
