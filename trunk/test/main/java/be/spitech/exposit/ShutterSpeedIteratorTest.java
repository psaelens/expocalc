/**
 * 
 */
package be.spitech.exposit;

import static org.junit.Assert.*;

import org.apache.commons.lang.math.Fraction;
import org.junit.Test;

/**
 * @author pis
 *
 */
public class ShutterSpeedIteratorTest {

	/**
	 * Test method for {@link be.spitech.exposit.ShutterSpeedIterator#ShutterSpeedIterator(org.apache.commons.lang.math.Fraction, org.apache.commons.lang.math.Fraction)}.
	 */
	@Test
	public void testShutterSpeedIterator() {
		Fraction first = Fraction.getFraction(1, 8000);
		Fraction last = Fraction.getFraction(15, 1);
		ShutterSpeedIterator iterator = new ShutterSpeedIterator(first, last);
		assertTrue(iterator.hasNext());
		assertEquals(first, iterator.next());
		assertEquals(denomainator(4000), iterator.next());
		assertEquals(denomainator(2000), iterator.next());
		assertEquals(denomainator(1000), iterator.next());
		assertEquals(denomainator(500), iterator.next());
		assertEquals(denomainator(250), iterator.next());
		assertEquals(denomainator(125), iterator.next());
		assertEquals(denomainator(60), iterator.next());
		assertEquals(denomainator(30), iterator.next());
		assertEquals(denomainator(15), iterator.next());
		assertEquals(denomainator(8), iterator.next());
		assertEquals(Fraction.ONE_QUARTER, iterator.next());
		assertEquals(Fraction.ONE_HALF, iterator.next());
		assertEquals(Fraction.ONE, iterator.next());
		assertEquals(numerator(2), iterator.next());
		assertEquals(numerator(4), iterator.next());
		assertEquals(numerator(8), iterator.next());
		assertEquals(last, iterator.next());
	}
	
	Fraction numerator(int i) {
		return Fraction.getFraction(i, 1);
	}
	
	Fraction denomainator(int i) {
		return Fraction.getFraction(1, i);
	}
}
