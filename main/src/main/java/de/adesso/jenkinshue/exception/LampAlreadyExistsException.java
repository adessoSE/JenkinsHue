package de.adesso.jenkinshue.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 
 * @author wennier
 *
 */
@ResponseStatus
public class LampAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = -4414373881766460981L;
	
	public LampAlreadyExistsException(String hueUniqueId) {
		super("Die Lampe mit der HueUniqueId '" + hueUniqueId + "' existiert schon!");
	}

}
