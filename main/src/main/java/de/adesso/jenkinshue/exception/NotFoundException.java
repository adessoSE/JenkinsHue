package de.adesso.jenkinshue.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * @author wennier
 */
public abstract class NotFoundException extends ResponseStatusException {

	public NotFoundException(String reason) {
		super(HttpStatus.NOT_FOUND, reason);
	}
}
