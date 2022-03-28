package de.adesso.jenkinshue.service;

import de.adesso.jenkinshue.TestCase;
import de.adesso.jenkinshue.common.service.HueService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author wennier
 */
public class HueServiceImplTest extends TestCase {

	@Autowired
	private HueService hueService;

	@Test
	public void testIpAreEqual() {
		String ip1 = "10.10.10.10";
		String ip2 = "10.10.10.10:80";
		String ip3 = "10.10.10.20:80";
		String ip4 = "10.10.10.20:45";
		assertTrue(hueService.ipAreEqual(ip1, ip2));
		assertFalse(hueService.ipAreEqual(ip2, ip3));
		assertTrue(hueService.ipAreEqual(ip3, ip4));
	}
}
