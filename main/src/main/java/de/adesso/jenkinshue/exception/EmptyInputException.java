package de.adesso.jenkinshue.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 
 * @author wennier
 *
 */
@ResponseStatus
public class EmptyInputException extends RuntimeException {

	private static final long serialVersionUID = -7888613033721178610L;

	public EmptyInputException() {
		super("Eine leere Eingabe ist ungültig!");
	}
	
}
