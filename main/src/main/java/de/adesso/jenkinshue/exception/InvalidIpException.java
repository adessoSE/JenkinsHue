package de.adesso.jenkinshue.exception;

/**
 * @author wennier
 */
public class InvalidIpException extends BadRequestException {

	private static final long serialVersionUID = 3001297143257623862L;

	public InvalidIpException(String ip) {
		super("Die IP-Adresse '" + ip + "' entspricht nicht dem IPv4-Adressformat!");
	}
}
