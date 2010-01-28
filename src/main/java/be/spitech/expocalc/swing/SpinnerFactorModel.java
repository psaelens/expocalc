/**
 * 
 */
package be.spitech.expocalc.swing;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractSpinnerModel;
import javax.swing.SpinnerModel;

import be.spitech.expocalc.Factor;

/**
 * @author pis
 *
 */
public class SpinnerFactorModel<T> extends AbstractSpinnerModel implements SpinnerModel {

	private final Factor<T> factor;
	
	public SpinnerFactorModel(Factor<T> factor) {
		super();
		this.factor = factor;
		factor.addPropertyChangeListener(new SubjectValueChangeHandler());
	}



	/* (non-Javadoc)
	 * @see javax.swing.SpinnerModel#getNextValue()
	 */
	@Override
	public Object getNextValue() {
		return factor.getNextValue();
	}

	/* (non-Javadoc)
	 * @see javax.swing.SpinnerModel#getPreviousValue()
	 */
	@Override
	public Object getPreviousValue() {
		return factor.getPreviousValue();
	}

	/* (non-Javadoc)
	 * @see javax.swing.SpinnerModel#getValue()
	 */
	@Override
	public Object getValue() {
		return factor.getValue();
	}
	
	public T getMinimum() {
		return factor.getMinimum();
	}
	
	public T getMaximum() {
		return factor.getMaximum();
	}

	/* (non-Javadoc)
	 * @see javax.swing.SpinnerModel#setValue(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setValue(Object value) {
		if ((value == null) /*|| !(value instanceof T)*/) {
			throw new IllegalArgumentException("illegal value");
		}
//		if (!value.equals(this.shutterSpeeds.getValue())) {
			this.factor.setValue((T) value);
			fireStateChanged();
//		}
	}
	
    /**
     * Handles changes in the subject's value.
     */
    private final class SubjectValueChangeHandler implements PropertyChangeListener {

        /**
         * The subect's value has changed. Fires a state change so
         * all registered listeners will be notified about the change.
         *
         * @param evt the property change event fired by the subject
         */
        public void propertyChange(PropertyChangeEvent evt) {
            fireStateChanged();
        }

    }

}
