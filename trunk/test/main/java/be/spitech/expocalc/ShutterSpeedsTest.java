package be.spitech.expocalc;

import static org.junit.Assert.*;

import org.apache.commons.lang.math.Fraction;
import org.junit.Test;

import be.spitech.expocalc.ShutterSpeeds;


public class ShutterSpeedsTest {

	/**
	 * Test method for {@link be.spitech.expocalc.ShutterSpeeds#getNextValue()}.
	 */
	@Test
	public void testGetNextValue() {
		testGetNextValue(ShutterSpeeds.STANDARD, 11);
		testGetNextValue(ShutterSpeeds.EXTENDED_STANDARD, 18);
		testGetNextValue(new ShutterSpeeds(Fraction.getFraction(1, 8000), Fraction.getFraction(30, 1)), 19);
	}
	
	/**
	 * Test method for {@link be.spitech.expocalc.ShutterSpeeds#getPreviousValue()}.
	 */
	@Test
	public void testGetPreviousValue() {
		testGetPreviousValue(ShutterSpeeds.STANDARD, 11);
		testGetPreviousValue(ShutterSpeeds.EXTENDED_STANDARD, 18);
	}

	private void testGetNextValue(ShutterSpeeds shutterSpeeds,
			int expectedSpeeds) {
		int speeds = 0;
		
		assertEquals(expectedSpeeds, shutterSpeeds.size());
		assertEquals(shutterSpeeds.getRange()[0], shutterSpeeds.getValue());
		assertNull(shutterSpeeds.getPreviousValue());
		
		while(shutterSpeeds.getNextValue() != null) {
			speeds++;
//			System.out.println(speeds + ". " +shutterSpeeds.getValue());
			if (speeds > shutterSpeeds.size())
				fail("speeds [" + speeds + "] exceed expected speeds [" + shutterSpeeds.size() + "]");
			try {
				shutterSpeeds.setValue(shutterSpeeds.getNextValue());
			} catch (IllegalArgumentException e) {
				fail(e.getMessage());
			}
		}
		assertEquals(shutterSpeeds.getRange()[1], shutterSpeeds.getValue());
	}
	
	private void testGetPreviousValue(ShutterSpeeds shutterSpeeds,
			int expectedSpeeds) {
		int speeds = 0;
		
		assertEquals(expectedSpeeds, shutterSpeeds.size());
		assertEquals(shutterSpeeds.getRange()[1], shutterSpeeds.getValue());
		assertNull(shutterSpeeds.getNextValue());
		
		while(shutterSpeeds.getPreviousValue() != null) {
			speeds++;
			if (speeds > shutterSpeeds.size())
				fail("speeds [" + speeds + "] exceed expected speeds [" + shutterSpeeds.size() + "]");
			shutterSpeeds.setValue(shutterSpeeds.getPreviousValue());
		}
		assertEquals(shutterSpeeds.getRange()[0], shutterSpeeds.getValue());
	}
}
