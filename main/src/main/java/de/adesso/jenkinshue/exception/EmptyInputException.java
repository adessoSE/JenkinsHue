package de.adesso.jenkinshue.exception;

/**
 * @author wennier
 */
public class EmptyInputException extends BadRequestException {

	private static final long serialVersionUID = -7888613033721178610L;

	public EmptyInputException() {
		super("Eine leere Eingabe ist ungültig!");
	}
}
