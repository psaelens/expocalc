package be.spitech.expocalc;

import java.util.List;

import com.jgoodies.binding.beans.Observable;


public interface Factor<T> extends Observable {
	
	enum Mode {
		FULL, ONE_HALF, ONE_THIRD, ONE_QUARTER;
	}
	
	public T getValue();
	public void setValue(T value);
	public T getNextValue();
	public T getPreviousValue();
	public T getMinimum();
	public T getMaximum();
	public List<T> getScale();
	public int size();
}
