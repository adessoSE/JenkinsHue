package de.adesso.jenkinshue.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 
 * @author wennier
 *
 */
@ResponseStatus
public class TeamAlreadyExistsException extends RuntimeException {
	
	private static final long serialVersionUID = 3137515481729293588L;

	public TeamAlreadyExistsException(String name) {
		super("Das Team mit dem Namen '" + name + "' existiert bereits!");
	}

}
