package de.adesso.jenkinshue.exception;

/**
 * @author wennier
 */
public class InvalidLoginException extends BadRequestException {

	private static final long serialVersionUID = 7507416774787395629L;

	public InvalidLoginException(String login) {
		super("Zum Login '" + login + "' existiert kein Benutzer im LDAP!");
	}
}
