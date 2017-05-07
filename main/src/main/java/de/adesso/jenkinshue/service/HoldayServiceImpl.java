package de.adesso.jenkinshue.service;

import de.adesso.jenkinshue.common.service.HolidayService;
import de.jollyday.HolidayCalendar;
import de.jollyday.HolidayManager;
import de.jollyday.ManagerParameters;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * 
 * @author wennier
 *
 */
@Service
public class HoldayServiceImpl implements HolidayService {
	
	@Override
	public boolean isHoliday(DateTime day) {
		/*
		 * arbeitet mit Calendar statt LocalDate, weil bei LocalDate die Chronology manuell gesetzt werden muesste -> fehleranfaellig
		 * (GregorianChronology)
		 */
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(day.toDate());
		HolidayManager m = HolidayManager.getInstance(ManagerParameters.create(HolidayCalendar.GERMANY, null));
		boolean isHoliday = m.isHoliday(cal, "nw");
		if(!isHoliday && day.getMonthOfYear() == 12) {
			if(day.getDayOfMonth() == 24 || day.getDayOfMonth() == 31) {
				isHoliday = true;
			}
		}
		return isHoliday;
	}

	@Override
	public boolean isWeekend(DateTime day) {
		return day.getDayOfWeek() == DateTimeConstants.SATURDAY || day.getDayOfWeek() == DateTimeConstants.SUNDAY;
	}
	
	@Override
	public boolean isValidWorkingPeriod(DateTime workingStart, DateTime workingEnd) {
		if(workingStart.isAfter(workingEnd)) {
			return false;
		} else {
			if(workingStart.getMinuteOfDay() >= 15 && workingEnd.getMinuteOfDay() <= 1425) {
				return true;
			} else {
				return false;
			}
		}
	}

}
