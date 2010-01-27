package be.spitech.expocalc;

import static be.spitech.util.CollectionUtils.toLinkedList;
import static be.spitech.util.FractionUtils.f;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import org.apache.commons.lang.math.Fraction;

import com.jgoodies.binding.beans.Model;

/**
 * <p>Shutter Speed : The duration of time for which the shutter 
 * of a camera remains open when exposing photographic film 
 * or other photosensitive material to light for the purpose 
 * of recording an image.
 *  
 * @author pis
 */
public class ShutterSpeeds extends Model /*implements Factor<Fraction>*/ {
	
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
		
		// define some "standardized" Shutter Speeds
		STANDARD = new ShutterSpeeds(Fraction.getFraction(1, 1000), Fraction.ONE);
		EXTENDED_STANDARD = new ShutterSpeeds(Fraction.getFraction(1, 8000), Fraction.getFraction(15, 1));
	}
	
	private final TreeSet<Fraction> speeds;
	private Fraction value;
	
	public ShutterSpeeds(Fraction minimum, Fraction maximum) {
		this(minimum, minimum, maximum);
	}
	


	public ShutterSpeeds(Fraction value, Fraction minimum, Fraction maximum) {
		this.speeds = new TreeSet<Fraction>(ONE.subList(ONE.indexOf(minimum), ONE.indexOf(maximum) + 1));
		setValue(value);
	}
	
	public int size() {
		return this.speeds.size();
	}
	
	public Fraction[] getRange() {
		return new Fraction[] { 
				speeds.first(),
				speeds.last() };
	}
	
	public Fraction getNextValue() {
		return speeds.higher(value);
	}

	public Fraction getPreviousValue() {
		return speeds.lower(value);
	}

	/**
	 * @return the value
	 */
	public Fraction getValue() {
		return value;
	}
	
	public List<Fraction> getSpeeds() {
		return Collections.unmodifiableList(new LinkedList<Fraction>(speeds));
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Fraction value) {
		if (!speeds.contains(value)) {
			throw new IllegalArgumentException("invalid value [" + value + "] - should be in " + speeds);
		}
		Fraction oldValue = getValue();
		this.value = value;
		firePropertyChange("value", oldValue, this.value);
	}

}