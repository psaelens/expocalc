/**
 * 
 */
package be.spitech.expocalc;

import static be.spitech.util.CollectionUtils.toLinkedList;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import com.jgoodies.binding.beans.Model;

/**
 * @author pis
 *
 */
public class Aperture extends Model implements Factor<Double>, Iterable<Double> {

	public final static Aperture STANDARD;
	
	private final static LinkedList<Double> FULL;
	private final static LinkedList<Double> ONE_HALF;
	private final static LinkedList<Double> ONE_THIRD;
	private final static LinkedList<Double> ONE_QUARTER;
	
	static {
		FULL = toLinkedList(new Double[] {
				0.5, 0.7, 1.0, 1.4,
				2.0, 2.8, 4.0, 5.6, 
				8.0, 11.0, 16.0, 22.0, 
				32.0, 45.0, 64.0, 90.0, 
				128.0
		});
		ONE_HALF = toLinkedList(new Double[] {
				1.2, 1.7, 2.4, 3.3,
				4.8, 6.7, 9.5, 13.0,
				19.0
		});
		ONE_THIRD = toLinkedList(new Double[] {
				1.1, 1.2, 
				1.6, 1.8,
				2.2, 2.5, 
				3.2, 3.5,
				4.5, 5.0, 
				6.3, 7.1,
				9.0, 10.0, 
				13.0, 14.0,
				18.0, 20.0
		});
		ONE_QUARTER = toLinkedList(new Double[] {
				1.8, 
				2.2, 2.4, 2.6,
				3.2, 3.4, 3.7,
				4.4, 4.8, 5.2,
				6.2, 6.7, 7.3,
				8.7, 9.5, 10.0,
				12.0, 14.0, 15.0,
				17.0, 19.0, 21.0
		});
		
		STANDARD = new Aperture(1.0, 45.0);
	}
	
	private Double value;
	private final TreeSet<Double> scale;
	
	public Aperture(Double minimum, Double maximum) {
		this(minimum, minimum, maximum);
	}
	
	public Aperture(Double value, Double minimum, Double maximum) {
		super();
		if (value == null) {
		    throw new IllegalArgumentException("value must be non-null");
		}
		if (!(((minimum == null) || (minimum.compareTo(value) <= 0)) && 
		      ((maximum == null) || (maximum.compareTo(value) >= 0)))) {
		    throw new IllegalArgumentException("(minimum <= value <= maximum) is false");
		}
		this.scale = new TreeSet<Double>(
				FULL.subList(
						FULL.indexOf(minimum), 
						FULL.indexOf(maximum) + 1));
		this.value = value;
	}

	@Override
	public Double getNextValue() {
		return scale.higher(value);
	}

	@Override
	public Double getPreviousValue() {
		return scale.lower(value);
	}

	@Override
	public Double getValue() {
		return value;
	}
	
	@Override
	public Double getMinimum() {
		return scale.first();
	}
	
	@Override
	public Double getMaximum() {
		return scale.last();
	}
	
	public List<Double> getScale() {
		return Collections.unmodifiableList(new LinkedList<Double>(scale));
	}

	@Override
	public void setValue(Double value) {
		if (!scale.contains(value)) {
			throw new IllegalArgumentException("invalid value [" + value + "] - should be in " + scale);
		}
		Double oldValue = getValue();
		this.value = value;
		firePropertyChange("value", oldValue, value);
		if (oldValue.compareTo(value)<=0) {
			firePropertyChange("step", 0, scale.subSet(oldValue, value).size());
		} else {
			firePropertyChange("step", 0, -scale.subSet(value, oldValue).size());
		}
	}

	@Override
	public Iterator<Double> iterator() {
		return scale.iterator();
	}
	
	@Override
	public int size() {
		return scale.size();
	}

}
