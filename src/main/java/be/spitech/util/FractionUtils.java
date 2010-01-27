package be.spitech.util;

import org.apache.commons.lang.math.Fraction;

public class FractionUtils {

	public static Fraction f(int num, int den) {
		return Fraction.getFraction(num, den);
	}
	
	public static Fraction f(double value) {
		return Fraction.getFraction(value);
	}
}
