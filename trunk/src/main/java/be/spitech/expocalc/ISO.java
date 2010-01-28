package be.spitech.expocalc;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import com.jgoodies.binding.beans.Model;

public class ISO extends Model implements Factor<Integer>, Iterable<Integer> {

	public final static ISO STANDARD = new ISO(100, 6400);
	
	private Integer value;
	private final TreeSet<Integer> scale;
	
	public ISO(Integer minimum, Integer maximum) {
		this(minimum, minimum, maximum);
	}
	
	public ISO(Integer value, Integer minimum, Integer maximum) {
		super();
		if (value == null) {
		    throw new IllegalArgumentException("value must be non-null");
		}
		if (!(((minimum == null) || (minimum.compareTo(value) <= 0)) && 
		      ((maximum == null) || (maximum.compareTo(value) >= 0)))) {
		    throw new IllegalArgumentException("(minimum <= value <= maximum) is false");
		}
		this.value = value;
		// initialise FULL scale
		this.scale = new TreeSet<Integer>();
		for(int i = minimum; i <= maximum; i*=2) {
			scale.add(new Integer(i));
		}
		System.out.println(scale);
	}
	
	@Override
	public Integer getMaximum() {
		return scale.last();
	}

	@Override
	public Integer getMinimum() {
		return scale.first();
	}

	@Override
	public Integer getNextValue() {
		return scale.higher(value);
	}

	@Override
	public Integer getPreviousValue() {
		return scale.lower(value);
	}

	@Override
	public List<Integer> getScale() {
		return Collections.unmodifiableList(new LinkedList<Integer>(scale));
	}

	@Override
	public Integer getValue() {
		return value;
	}

	@Override
	public void setValue(Integer value) {
		if (!scale.contains(value)) {
			throw new IllegalArgumentException("invalid value [" + value + "] - should be in " + scale);
		}
		Integer oldValue = getValue();
		this.value = value;
		firePropertyChange("value", oldValue, value);
		if (oldValue.compareTo(value)<=0) {
			firePropertyChange("step", 0, scale.subSet(oldValue, value).size());
		} else {
			firePropertyChange("step", 0, -scale.subSet(value, oldValue).size());
		}
	}

	@Override
	public int size() {
		return scale.size();
	}
	
	@Override
	public Iterator<Integer> iterator() {
		return scale.iterator();
	}

}
