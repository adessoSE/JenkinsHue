package de.adesso.jenkinshue.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 
 * @author wennier
 *
 */
@ResponseStatus
public class BridgeAlreadyExistsException extends RuntimeException {
	
	private static final long serialVersionUID = -3515875791317313332L;

	public BridgeAlreadyExistsException(String ip) {
		super("Die Bridge mit der IP '" + ip + "' existiert bereits!");
	}

}
