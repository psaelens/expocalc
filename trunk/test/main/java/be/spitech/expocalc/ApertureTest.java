package be.spitech.expocalc;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;

import org.junit.Test;

public class ApertureTest {

	@Test
	public void testGetNextValue() {
		assertEquals(new Double(1.4), Aperture.STANDARD.getNextValue());
	}

	@Test
	public void testGetPreviousValue() {
		Aperture aperture = new Aperture(45.0, 1.0, 45.0);
		assertEquals(new Double(32.0),aperture.getPreviousValue());
		
	}

	@Test
	public void testIterator() {
		 Iterator<Double> iterator = Aperture.STANDARD.iterator();
		 assertEquals(new Double(1.0), iterator.next());
		 assertEquals(new Double(1.4), iterator.next());
		assertEquals(new Double(2), iterator.next());
		assertEquals(new Double(2.8), iterator.next());
		assertEquals(new Double(4), iterator.next());
		assertEquals(new Double(5.6), iterator.next());
		assertEquals(new Double(8), iterator.next());
		assertEquals(new Double(11), iterator.next());
		assertEquals(new Double(16), iterator.next());
		assertEquals(new Double(22), iterator.next());
		assertEquals(new Double(32), iterator.next());
		assertEquals(new Double(45), iterator.next());
	}

}
