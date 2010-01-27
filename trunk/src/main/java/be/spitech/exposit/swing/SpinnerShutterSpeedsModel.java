/**
 * 
 */
package be.spitech.exposit.swing;

import javax.swing.AbstractSpinnerModel;

import org.apache.commons.lang.math.Fraction;

import be.spitech.exposit.ShutterSpeeds;

/**
 * @author pis
 *
 */
public class SpinnerShutterSpeedsModel extends AbstractSpinnerModel {

	private final ShutterSpeeds shutterSpeeds;
	
	public SpinnerShutterSpeedsModel(Fraction value, Fraction minimum, Fraction maximum) {
		if (value == null) {
		    throw new IllegalArgumentException("value must be non-null");
		}
		if (!(((minimum == null) || (minimum.compareTo(value) <= 0)) && 
		      ((maximum == null) || (maximum.compareTo(value) >= 0)))) {
		    throw new IllegalArgumentException("(minimum <= value <= maximum) is false");
		}
		this.shutterSpeeds = new ShutterSpeeds(value, minimum, maximum);
	}
	
	
	public SpinnerShutterSpeedsModel(ShutterSpeeds shutterSpeeds) {
		super();
		this.shutterSpeeds = shutterSpeeds;
	}



	/* (non-Javadoc)
	 * @see javax.swing.SpinnerModel#getNextValue()
	 */
	@Override
	public Object getNextValue() {
		return shutterSpeeds.getNextValue();
	}

	/* (non-Javadoc)
	 * @see javax.swing.SpinnerModel#getPreviousValue()
	 */
	@Override
	public Object getPreviousValue() {
		return shutterSpeeds.getPreviousValue();
	}

	/* (non-Javadoc)
	 * @see javax.swing.SpinnerModel#getValue()
	 */
	@Override
	public Object getValue() {
		return shutterSpeeds.getValue();
	}
	
	public Fraction getMinimum() {
		return shutterSpeeds.getRange()[0];
	}
	
	public Fraction getMaximum() {
		return shutterSpeeds.getRange()[1];
	}

	/* (non-Javadoc)
	 * @see javax.swing.SpinnerModel#setValue(java.lang.Object)
	 */
	@Override
	public void setValue(Object value) {
		if ((value == null) || !(value instanceof Fraction)) {
			throw new IllegalArgumentException("illegal value");
		}
		if (!value.equals(this.shutterSpeeds.getValue())) {
			this.shutterSpeeds.setValue((Fraction) value);
			fireStateChanged();
		}
	}

}
