package de.adesso.jenkinshue.exception;

/**
 * @author wennier
 */
public class TeamDoesNotExistException extends NotFoundException {

	private static final long serialVersionUID = -4673244070616831102L;

	public TeamDoesNotExistException(long teamId) {
		super("Ein Team mit der ID '" + teamId + "' existiert nicht!");
	}
}
