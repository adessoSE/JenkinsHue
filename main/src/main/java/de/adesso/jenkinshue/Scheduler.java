package de.adesso.jenkinshue;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import de.adesso.jenkinshue.common.service.HolidayService;

/**
 * 
 * @author wennier
 *
 */
@Component
public class Scheduler {
	
	public static final int INTERVAL = 20000;
	public static final int LATCH_TIMEOUT = INTERVAL / 4;
	
	private final JobListener jobListener;
	
	private final HolidayService holidayService;

	@Autowired
	public Scheduler(JobListener jobListener, HolidayService holidayService) {
		this.jobListener = jobListener;
		this.holidayService = holidayService;
	}

	@Scheduled(initialDelay = 1000, fixedDelay = Scheduler.INTERVAL)
	public void run() {
		DateTime now = DateTime.now();
		if(!holidayService.isHoliday(now) && !holidayService.isWeekend(now)) {
			jobListener.updateLamps();
		}
	}

}
