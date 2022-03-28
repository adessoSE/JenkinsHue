package de.adesso.jenkinshue.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * @author wennier
 */
public abstract class BadRequestException extends ResponseStatusException {

	public BadRequestException(String reason) {
		super(HttpStatus.BAD_REQUEST, reason);
	}
}
