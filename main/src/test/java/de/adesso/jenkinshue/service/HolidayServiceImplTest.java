package de.adesso.jenkinshue.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.adesso.jenkinshue.TestCase;
import de.adesso.jenkinshue.common.service.HolidayService;

/**
 * 
 * @author wennier
 *
 */
public class HolidayServiceImplTest extends TestCase {
	
	@Autowired
	private HolidayService holidayService;
	
	@Test
	public void testIsHoliday() {
		assertTrue(holidayService.isHoliday(new DateTime(2016, 1, 1, 0, 0)));
		assertTrue(holidayService.isHoliday(new DateTime(2016, 3, 25, 0, 0)));
		assertTrue(holidayService.isHoliday(new DateTime(2016, 3, 28, 0, 0)));
		assertTrue(holidayService.isHoliday(new DateTime(2016, 5, 1, 0, 0)));
		assertTrue(holidayService.isHoliday(new DateTime(2016, 5, 5, 0, 0)));
		assertTrue(holidayService.isHoliday(new DateTime(2016, 5, 16, 0, 0)));
		assertTrue(holidayService.isHoliday(new DateTime(2016, 5, 26, 0, 0)));
		assertTrue(holidayService.isHoliday(new DateTime(2016, 10, 3, 0, 0)));
		assertTrue(holidayService.isHoliday(new DateTime(2016, 11, 1, 0, 0)));
		assertTrue(holidayService.isHoliday(new DateTime(2016, 12, 25, 0, 0)));
		assertTrue(holidayService.isHoliday(new DateTime(2016, 12, 26, 0, 0)));
		// adesso only
		assertTrue(holidayService.isHoliday(new DateTime(2016, 12, 24, 0, 0)));
		assertTrue(holidayService.isHoliday(new DateTime(2016, 12, 31, 0, 0)));
		
		assertFalse(holidayService.isHoliday(new DateTime(2016, 1, 4, 0, 0)));
	}
	
	@Test
	public void testIsWeekend() {
		assertFalse(holidayService.isWeekend(new DateTime(2016, 1, 1, 0, 0)));
		assertTrue(holidayService.isWeekend(new DateTime(2016, 1, 2, 0, 0)));
		assertTrue(holidayService.isWeekend(new DateTime(2016, 1, 3, 0, 0)));
		assertFalse(holidayService.isWeekend(new DateTime(2016, 1, 4, 0, 0)));
	}
	
	@Test
	public void testIsValidWorkingPeriod() {
		DateTime h00m14 = new DateTime(2016, 1, 1, 0, 14);
		DateTime h00m15 = new DateTime(2016, 1, 1, 0, 15);
		DateTime h00m16 = new DateTime(2016, 1, 1, 0, 16);
		
		DateTime h08m00 = new DateTime(2016, 1, 1, 8, 0);
		DateTime h16m00 = new DateTime(2016, 1, 1, 16, 0);
		
		DateTime h23m44 = new DateTime(2016, 1, 1, 23, 44);
		DateTime h23m45 = new DateTime(2016, 1, 1, 23, 45);
		DateTime h23m46 = new DateTime(2016, 1, 1, 23, 46);
		
		assertFalse(holidayService.isValidWorkingPeriod(h00m14, h16m00));
		assertTrue(holidayService.isValidWorkingPeriod(h00m15, h16m00));
		assertTrue(holidayService.isValidWorkingPeriod(h00m16, h16m00));
		
		assertTrue(holidayService.isValidWorkingPeriod(h08m00, h23m44));
		assertTrue(holidayService.isValidWorkingPeriod(h08m00, h23m45));
		assertFalse(holidayService.isValidWorkingPeriod(h08m00, h23m46));
		
		assertTrue(holidayService.isValidWorkingPeriod(h08m00, h16m00));
		assertFalse(holidayService.isValidWorkingPeriod(h16m00, h08m00));
	}

}
