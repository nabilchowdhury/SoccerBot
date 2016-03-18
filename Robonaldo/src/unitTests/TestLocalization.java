package unitTests;

import soccerbot.Odometer;
import soccerbot.LightLocalizer;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestLocalization {

	
	public void testUSLocalization() {
		
	}
	
	
	public void testLightLocalization() {
		
	}
	
	@Test
	public void testLocalization() {
		long startTime = System.currentTimeMillis();
		testUSLocalization();
		testLightLocalization();
		long endTime = System.currentTimeMillis();
		assertTrue((endTime - startTime)/1000 < 30.0);
	}
}
