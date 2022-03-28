package de.adesso.jenkinshue.exception;

/**
 * @author wennier
 */
public class BridgeAlreadyExistsException extends BadRequestException {

	private static final long serialVersionUID = -3515875791317313332L;

	public BridgeAlreadyExistsException(String ip) {
		super("Die Bridge mit der IP '" + ip + "' existiert bereits!");
	}
}
