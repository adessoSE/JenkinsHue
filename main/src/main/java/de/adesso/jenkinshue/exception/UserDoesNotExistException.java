package de.adesso.jenkinshue.exception;

/**
 * @author wennier
 */
public class UserDoesNotExistException extends NotFoundException {

	private static final long serialVersionUID = -1673274070316831102L;

	public UserDoesNotExistException(long userId) {
		super("Ein Benutzer mit der ID '" + userId + "' existiert nicht!");
	}
}
