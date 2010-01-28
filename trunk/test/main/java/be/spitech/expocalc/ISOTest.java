package be.spitech.expocalc;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

public class ISOTest {

	@Test
	public void testGetNextValue() {
		assertEquals(new Integer(200), ISO.STANDARD.getNextValue());
	}

	@Test
	public void testGetPreviousValue() {
		ISO iso = new ISO(6400, 100, 6400);
		assertEquals(new Integer(3200), iso.getPreviousValue());

	}

	@Test
	public void testIterator() {
		Iterator<Integer> iterator = ISO.STANDARD.iterator();
		assertEquals(new Integer(100), iterator.next());
		assertEquals(new Integer(200), iterator.next());
		assertEquals(new Integer(400), iterator.next());
		assertEquals(new Integer(800), iterator.next());
		assertEquals(new Integer(1600), iterator.next());
		assertEquals(new Integer(3200), iterator.next());
		assertEquals(new Integer(6400), iterator.next());
	}

}
