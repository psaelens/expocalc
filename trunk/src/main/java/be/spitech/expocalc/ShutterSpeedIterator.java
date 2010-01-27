package be.spitech.expocalc;

import java.util.Iterator;

import org.apache.commons.lang.math.Fraction;

public class ShutterSpeedIterator implements Iterator<Fraction> {

	private final ShutterSpeeds shutterSpeeds;
	
	public ShutterSpeedIterator(Fraction from, Fraction to) {
		shutterSpeeds = new ShutterSpeeds(from, to);
	}
	
	@Override
	public boolean hasNext() {
		return shutterSpeeds.getNextValue() != null;
	}

	@Override
	public Fraction next() {
		Fraction next = shutterSpeeds.getValue();
		if (hasNext())
			shutterSpeeds.setValue(shutterSpeeds.getNextValue());
		return next;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
