package de.adesso.jenkinshue.exception;

import org.joda.time.DateTime;

/**
 * @author wennier
 */
public class InvalidWorkingPeriodException extends BadRequestException {

	private static final long serialVersionUID = -6115454552582437790L;

	public InvalidWorkingPeriodException(DateTime workingStart, DateTime workingEnd) {
		super("Der angegebene Zeitraum von " + workingStart.toString("HH:mm") + " Uhr bis " + workingEnd.toString("HH:mm") + " Uhr ist ungültig. Die Arbeitszeit darf frühestens um 00:15 Uhr beginnen und muss spätestens um 23:45 Uhr enden!");
	}
}
