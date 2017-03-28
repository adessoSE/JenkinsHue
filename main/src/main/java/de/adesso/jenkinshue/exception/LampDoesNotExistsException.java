package de.adesso.jenkinshue.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 
 * @author wennier
 *
 */
@ResponseStatus
public class LampDoesNotExistsException extends RuntimeException {
	
	private static final long serialVersionUID = -8088537913675286462L;

	public LampDoesNotExistsException(long lampId) {
		super("Eine Lampe mit der ID '" + lampId + "' existiert nicht!");
	}

}
