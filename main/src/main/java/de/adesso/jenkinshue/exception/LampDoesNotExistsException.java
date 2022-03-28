package de.adesso.jenkinshue.exception;

/**
 * @author wennier
 */
public class LampDoesNotExistsException extends NotFoundException {

	private static final long serialVersionUID = -8088537913675286462L;

	public LampDoesNotExistsException(long lampId) {
		super("Eine Lampe mit der ID '" + lampId + "' existiert nicht!");
	}
}
