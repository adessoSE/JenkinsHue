package de.adesso.jenkinshue.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 
 * @author wennier
 *
 */
@ResponseStatus
public class InvalidIpException extends RuntimeException {

	private static final long serialVersionUID = 3001297143257623862L;
	
	public InvalidIpException(String ip) {
		super("Die IP-Adresse '" + ip + "' entspricht nicht dem IPv4-Adressformat!");
	}

}
