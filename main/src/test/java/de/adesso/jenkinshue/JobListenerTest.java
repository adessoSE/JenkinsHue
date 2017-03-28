package de.adesso.jenkinshue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.adesso.jenkinshue.common.dto.lamp.LampDTO;
import de.adesso.jenkinshue.common.enumeration.BuildState;
import de.adesso.jenkinshue.common.jenkins.dto.JenkinsBuildDTO;

/**
 * 
 * @author wennier
 *
 */
public class JobListenerTest extends TestCase {

	@Autowired
	private JobListener jobListener;
	
	@Test
	public void testIsTurnOffTime() {
		LampDTO lampDTO = new LampDTO();
		lampDTO.setWorkingStart(DateTime.now().withMillisOfDay(0).withHourOfDay(7).toDate());
		lampDTO.setWorkingEnd(DateTime.now().withMillisOfDay(0).withHourOfDay(19).toDate());
		
		
		
		DateTime beerOClock = DateTime.now().withHourOfDay(20);
		assertTrue(jobListener.isTurnOffTime(lampDTO, beerOClock));
		
		DateTime earlyInTheMorning = DateTime.now().withHourOfDay(5);
		assertTrue(jobListener.isTurnOffTime(lampDTO, earlyInTheMorning));
		
		DateTime hardWorkingTime = DateTime.now().withHourOfDay(10);
		assertFalse(jobListener.isTurnOffTime(lampDTO, hardWorkingTime));
		
		
		
		DateTime workingStart = DateTime.now().withMillisOfDay(0).withHourOfDay(7);
		assertFalse(jobListener.isTurnOffTime(lampDTO, workingStart));
		
		DateTime beforeWorkingStart = workingStart.minusMillis(1);
		assertTrue(jobListener.isTurnOffTime(lampDTO, beforeWorkingStart));
		
		DateTime afterWorkingStart = workingStart.plusMillis(1);
		assertFalse(jobListener.isTurnOffTime(lampDTO, afterWorkingStart));
		
		
		
		DateTime workingEnd = DateTime.now().withMillisOfDay(0).withHourOfDay(19);
		assertFalse(jobListener.isTurnOffTime(lampDTO, workingEnd));
		
		DateTime beforeWorkingEnd = workingEnd.minusMillis(1);
		assertFalse(jobListener.isTurnOffTime(lampDTO, beforeWorkingEnd));
		
		DateTime afterWorkingEnd = workingEnd.plusMillis(1);
		assertTrue(jobListener.isTurnOffTime(lampDTO, afterWorkingEnd));
	}
	
	@Test
	public void testExtractBuildState() {
		JenkinsBuildDTO b1 = new JenkinsBuildDTO(false, "FAILURE", 1, null);
		JenkinsBuildDTO b2 = new JenkinsBuildDTO(false, "UNSTABLE", 2, null);
		JenkinsBuildDTO b3 = new JenkinsBuildDTO(false, "SUCCESS", 3, null);
		JenkinsBuildDTO b4 = new JenkinsBuildDTO(true, "", 4, null);
		assertEquals(BuildState.FAILURE, jobListener.extractBuildState(b1));
		assertEquals(BuildState.UNSTABLE, jobListener.extractBuildState(b2));
		assertEquals(BuildState.SUCCESS, jobListener.extractBuildState(b3));
		assertEquals(BuildState.BUILDING, jobListener.extractBuildState(b4));
	}
	
}
