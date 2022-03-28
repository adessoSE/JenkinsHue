package de.adesso.jenkinshue.exception;

/**
 * @author wennier
 */
public class LampAlreadyExistsException extends BadRequestException {

	private static final long serialVersionUID = -4414373881766460981L;

	public LampAlreadyExistsException(String hueUniqueId) {
		super("Die Lampe mit der HueUniqueId '" + hueUniqueId + "' existiert schon!");
	}
}
