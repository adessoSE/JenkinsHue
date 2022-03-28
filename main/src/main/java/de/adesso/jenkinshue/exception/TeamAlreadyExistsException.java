package de.adesso.jenkinshue.exception;

/**
 * @author wennier
 */
public class TeamAlreadyExistsException extends BadRequestException {

	private static final long serialVersionUID = 3137515481729293588L;

	public TeamAlreadyExistsException(String name) {
		super("Das Team mit dem Namen '" + name + "' existiert bereits!");
	}
}
