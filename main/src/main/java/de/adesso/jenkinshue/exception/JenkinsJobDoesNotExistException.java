package de.adesso.jenkinshue.exception;

/**
 * @author wennier
 */
public class JenkinsJobDoesNotExistException extends RuntimeException {

	private static final long serialVersionUID = -2064061135073378160L;

	public JenkinsJobDoesNotExistException(String jobName) {
		super("Es existiert kein Jenkins-Job mit dem Namen '" + jobName + "'!");
	}
}
