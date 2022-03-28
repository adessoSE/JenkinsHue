package de.adesso.jenkinshue.exception;

/**
 * @author wennier
 */
public class UserAlreadyExistsException extends BadRequestException {

	private static final long serialVersionUID = 8000996297037785772L;

	public UserAlreadyExistsException(String login) {
		super("Der Benutzer mit dem Login '" + login + "' existiert bereits!");
	}
}
