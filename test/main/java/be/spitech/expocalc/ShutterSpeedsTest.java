package be.spitech.expocalc;

import static be.spitech.util.FractionUtils.f;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;

import org.apache.commons.lang.math.Fraction;
import org.junit.Test;


public class ShutterSpeedsTest {

	/**
	 * Test method for {@link be.spitech.expocalc.ShutterSpeeds#iterator()}.
	 */
	@Test
	public void testShutterSpeedIterator() {
		Fraction first = Fraction.getFraction(1, 8000);
		Fraction last = Fraction.getFraction(15, 1);
		Iterator<Fraction> iterator = new ShutterSpeeds(first, last).iterator();
		assertTrue(iterator.hasNext());
		assertEquals(first, iterator.next());
		assertEquals(f(1, 4000), iterator.next());
		assertEquals(f(1, 2000), iterator.next());
		assertEquals(f(1, 1000), iterator.next());
		assertEquals(f(1, 500), iterator.next());
		assertEquals(f(1, 250), iterator.next());
		assertEquals(f(1, 125), iterator.next());
		assertEquals(f(1, 60), iterator.next());
		assertEquals(f(1, 30), iterator.next());
		assertEquals(f(1, 15), iterator.next());
		assertEquals(f(1, 8), iterator.next());
		assertEquals(Fraction.ONE_QUARTER, iterator.next());
		assertEquals(Fraction.ONE_HALF, iterator.next());
		assertEquals(Fraction.ONE, iterator.next());
		assertEquals(f(2), iterator.next());
		assertEquals(f(4), iterator.next());
		assertEquals(f(8), iterator.next());
		assertEquals(last, iterator.next());
	}
	
	Fraction numerator(int i) {
		return Fraction.getFraction(i, 1);
	}
	
	Fraction denomainator(int i) {
		return Fraction.getFraction(1, i);
	}
	
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
