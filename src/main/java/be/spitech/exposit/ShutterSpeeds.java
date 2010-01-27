package be.spitech.exposit;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Map.Entry;

import org.apache.commons.lang.math.Fraction;
import org.apache.commons.lang.math.NumberRange;

/**
 * <p>Shutter Speed : The duration of time for which the shutter 
 * of a camera remains open when exposing photographic film 
 * or other photosensitive material to light for the purpose 
 * of recording an image.
 *  
 * @author pis
 */
public class ShutterSpeeds {
	
	/**
	 * The agreed standards for shutter speeds are:
	 * <ul>
	 * <li>1/1000 s
     * <li>1/500 s
     * <li>1/250 s
     * <li>1/125 s
     * <li>1/60 s
     * <li>1/30 s
     * <li>1/15 s
     * <li>1/8 s
     * <li>1/4 s
     * <li>1/2 s
     * <li>1 s
     * </ul>
	 * 
	 */
	public final static ShutterSpeeds STANDARD;
	
	/**
	 * The number series for shutter speed is:<br>
	 * 15, 8, 4, 2, 1, 1/2, 1/4, 1/8, 1/15, 1/30, 1/60, 1/125, 1/250, 1/500, 1/1000, 1/2000, 1/4000, 1/8000
	 */
	public final static ShutterSpeeds EXTENDED_STANDARD;
	
	private final static LinkedList<Fraction> ONE;
	private final static LinkedList<Fraction> THIRD;
	
	private final static Fraction TWO = Fraction.getFraction(2, 1);
	
	private final static Map<Fraction, Fraction> NEXT;
	private final static Map<Fraction, Fraction> PREVIOUS;
	
	static {
		// define ONE, HALF and THIRD Sorted Sets
		ONE = toLinkedList(new Fraction[] {
				f(1,8000), f(1,4000), f(1,2000), f(1,1000),
				f(1,500), f(1,250), f(1,125),
				f(1,60), f(1,30), f(1,15), f(1,8),
				f(1,4), f(1,2), f(1), f(2),
				f(4), f(8), f(15), f(30)
		});
		THIRD = toLinkedList(new Fraction[] {
				f(1,6400), f(1,5000), f(1,3200), f(1,2500),
				f(1,1600), f(1,1250), f(1,800), f(1,640),
				f(1,400), f(1,320), f(1,200), f(1,160),
				f(1,100), f(1,80), f(1,50), f(1,40),
				f(1,25), f(1,20), f(1,13), f(1,10),
				f(1,6), f(1,5), f(0.3), f(0.4),
				f(0.6), f(0.8), f(1.3), f(1.6),
				f(2.5), f(3.2), f(5), f(6),
				f(10), f(13), f(20), f(25),
		});
		
		// define NEXT map
		NEXT = toMap(new Fraction[][] {
					{Fraction.getFraction(1, 125), Fraction.getFraction(1, 60)},
					{Fraction.getFraction(1, 15), Fraction.getFraction(1, 8)},
					{Fraction.getFraction(8, 1), Fraction.getFraction(15, 1)}
				});
		
		// "reverse" NEXT map to build PREVIOUS map 
		Map<Fraction, Fraction> map = new HashMap<Fraction, Fraction>();
		for (Entry<Fraction, Fraction>  entry:  NEXT.entrySet()) {
			map.put(entry.getValue(), entry.getKey());
		}
		PREVIOUS = Collections.unmodifiableMap(map);
		
		// define some "standardized" Shutter Speeds
		STANDARD = new ShutterSpeeds(Fraction.getFraction(1, 1000), Fraction.ONE);
		EXTENDED_STANDARD = new ShutterSpeeds(Fraction.getFraction(1, 8000), Fraction.getFraction(15, 1));
	}
	
	private final NumberRange range;
	private LinkedList<Fraction> one;
	private LinkedList<Fraction> third;
	private final int numberOfSpeed;
	private Fraction value;
	
	public ShutterSpeeds(Fraction minimum, Fraction maximum) {
		this(minimum, minimum, maximum);
	}
	


	public ShutterSpeeds(Fraction value, Fraction minimum, Fraction maximum) {
		this.range = minimum.compareTo(maximum) <= 0 ? 
				new NumberRange(minimum, maximum) : 
					new NumberRange(maximum, minimum);
		this.one = new LinkedList<Fraction>(ONE.subList(ONE.indexOf(minimum), ONE.indexOf(maximum) + 1));
		if (!one.contains(value)) {
			throw new IllegalArgumentException("invalid value [" + value + "] - should be in " + one);
		}
		System.out.println("one=" + one);
//		this.numberOfSpeed = computeNumberOfSpeed();
		this.numberOfSpeed = this.one.size();
		setValue(value);
	}
	
	private int computeNumberOfSpeed() {
		int speeds = 0;
		Fraction currentValue = (Fraction) range.getMinimumNumber();
		while(currentValue != null && currentValue.compareTo(range.getMaximumNumber()) <= 0) {
			speeds++;
			currentValue = nextValue(currentValue, (Fraction) range.getMaximumNumber());
		}
		return speeds;
	}
	
	public int getNumberOfSpeed() {
		return this.numberOfSpeed;
	}
	
	public Fraction[] getRange() {
		return new Fraction[] { 
				(Fraction) range.getMinimumNumber(),
				(Fraction) range.getMaximumNumber() };
	}
	
	public Fraction getNextValue() {
		if (value.equals(one.getLast())) {
			return null;
		}
		return one.get(one.indexOf(value) + 1);
//		return nextValue(value, (Fraction) range.getMaximumNumber());
	}

	public Fraction getPreviousValue() {
		if (value.equals(one.getFirst())) {
			return null;
		}
		return one.get(one.indexOf(value) - 1);
//		return previousValue(value, (Fraction) range.getMinimumNumber());
	}

	/**
	 * @return the value
	 */
	public Fraction getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Fraction value) {
		if (!range.containsNumber(value) || !one.contains(value)) {
			throw new IllegalArgumentException("value " + value + " not in range " + range);
		}
		this.value = value;
	}
	
	private static Fraction nextValue(Fraction value, Fraction maximum) {
		if (value == null) {
			return null;
		} else if (NEXT.containsKey(value)) {
			return NEXT.get(value);
		} else  if (value.compareTo(maximum) < 0) {
			return value.multiplyBy(TWO);
		}
		return null;
	}
	
	private static Fraction previousValue(Fraction value, Fraction minimum) {
		if (value == null) {
			return null;
		} else if (PREVIOUS.containsKey(value)) {
			return PREVIOUS.get(value);
		} else  if (value.compareTo(minimum) > 0) {
			return value.divideBy(TWO);
		}
		return null;
	}
	
	private static <T> Map<T, T> toMap(T[][] array) {
		HashMap<T, T> map = new HashMap<T, T>();
		for (int i = 0; i < array.length; i++) {
			if (array[i].length != 2)
				throw new IllegalArgumentException("can't convert array to map" +
						" - wrong array dimension : " + array.length);
			map.put(array[i][0], array[i][1]);
		}
		return Collections.unmodifiableMap(map);
	}
	
	private static <T> SortedSet<T> toSortedSet(T[] fractions) {
		TreeSet<T> set = new TreeSet<T>();
		for (int i = 0; i < fractions.length; i++) {
			set.add(fractions[i]);
		}
		return set;
	}
	private static <T> LinkedList<T> toLinkedList(T[] fractions) {
		LinkedList<T> list = new LinkedList<T>();
		for (int i = 0; i < fractions.length; i++) {
			if (list.contains(fractions[i])) {
				throw new IllegalArgumentException("doublon: " + fractions[i]);
			}
			list.add(fractions[i]);
		}
		return list;
	}
	
	private static Fraction f(int num, int den) {
		return Fraction.getFraction(num, den);
	}
	
	private static Fraction f(int num) {
		return Fraction.getFraction(num, 1);
	}
	
	private static Fraction f(double value) {
		return Fraction.getFraction(value);
	}
}