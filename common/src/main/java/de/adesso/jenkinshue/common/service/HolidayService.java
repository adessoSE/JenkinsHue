package de.adesso.jenkinshue.common.service;

import org.joda.time.DateTime;

/**
 * 
 * @author wennier
 *
 */
public interface HolidayService {

	boolean isHoliday(DateTime day);
	
	boolean isWeekend(DateTime day);
	
	boolean isValidWorkingPeriod(DateTime workingStart, DateTime workingEnd);
	
}
