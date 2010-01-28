/**
 * 
 */
package be.spitech.expocalc.swing;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BoundedRangeModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import be.spitech.expocalc.Factor;

/**
 * @author pis
 *
 */
public class FactorBoundedRangeModel<T> implements BoundedRangeModel {

    /**
     * Only one <code>ChangeEvent</code> is needed per model instance since the
     * event's only (read-only) state is the source property.  The source
     * of events generated here is always "this".
     */
    protected transient ChangeEvent changeEvent = null;
    
    /**
     * The listeners observing model changes.
     */
    private final EventListenerList listenerList = new EventListenerList();
    
	private final Factor<T> factor;
	
    private boolean isAdjusting = false;
	
	public FactorBoundedRangeModel(Factor<T> factor) {
		super();
		this.factor = factor;
		factor.addPropertyChangeListener(new SubjectValueChangeHandler());
	}

	/* (non-Javadoc)
	 * @see javax.swing.BoundedRangeModel#getExtent()
	 */
	@Override
	public int getExtent() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see javax.swing.BoundedRangeModel#getMaximum()
	 */
	@Override
	public int getMaximum() {
		return factor.getScale().size() - 1;
	}

	/* (non-Javadoc)
	 * @see javax.swing.BoundedRangeModel#getMinimum()
	 */
	@Override
	public int getMinimum() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see javax.swing.BoundedRangeModel#getValue()
	 */
	@Override
	public int getValue() {
        int value = factor.getScale().indexOf(factor.getValue());
        return value < 0
            ? getMinimum()
            : value;
	}

	/* (non-Javadoc)
	 * @see javax.swing.BoundedRangeModel#getValueIsAdjusting()
	 */
	@Override
	public boolean getValueIsAdjusting() {
		return isAdjusting;
	}



	/* (non-Javadoc)
	 * @see javax.swing.BoundedRangeModel#setExtent(int)
	 */
	@Override
	public void setExtent(int newExtent) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see javax.swing.BoundedRangeModel#setMaximum(int)
	 */
	@Override
	public void setMaximum(int newMaximum) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see javax.swing.BoundedRangeModel#setMinimum(int)
	 */
	@Override
	public void setMinimum(int newMinimum) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see javax.swing.BoundedRangeModel#setRangeProperties(int, int, int, int, boolean)
	 */
	@Override
	public void setRangeProperties(int newValue, int newExtent, int newMin, int newMax,
			boolean adjusting) {
	        boolean isChange =
	            (newValue != getValue())
	                || (adjusting != isAdjusting);
	        if (isChange) {
	            factor.setValue(factor.getScale().get(newValue));
	            isAdjusting = adjusting;
	            fireStateChanged();
	        }

	}

	/* (non-Javadoc)
	 * @see javax.swing.BoundedRangeModel#setValue(int)
	 */
	@Override
	public void setValue(int n) {
        setRangeProperties(n, getExtent(), getMinimum(), getMaximum(), isAdjusting);
	}

	/* (non-Javadoc)
	 * @see javax.swing.BoundedRangeModel#setValueIsAdjusting(boolean)
	 */
	@Override
	public void setValueIsAdjusting(boolean b) {
		setRangeProperties(getValue(), getExtent(), getMinimum(), getMaximum(), b);
	}
	
	 /**
     * Adds a <code>ChangeListener</code>.  The change listeners are run each
     * time any one of the Bounded Range model properties changes.
     *
     * @param l the ChangeListener to add
     * @see #removeChangeListener
     * @see BoundedRangeModel#addChangeListener
     */
    public void addChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }
    

    /**
     * Removes a <code>ChangeListener</code>.
     *
     * @param l the <code>ChangeListener</code> to remove
     * @see #addChangeListener
     * @see BoundedRangeModel#removeChangeListener
     */
    public void removeChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }


    /**
     * Returns an array of all the change listeners
     * registered on this <code>DefaultBoundedRangeModel</code>.
     *
     * @return all of this model's <code>ChangeListener</code>s 
     *         or an empty
     *         array if no change listeners are currently registered
     *
     * @see #addChangeListener
     * @see #removeChangeListener
     *
     * @since 1.4
     */
    public ChangeListener[] getChangeListeners() {
        return (ChangeListener[])listenerList.getListeners(
                ChangeListener.class);
    }


    /** 
     * Runs each <code>ChangeListener</code>'s <code>stateChanged</code> method.
     * 
     * @see #setRangeProperties
     * @see EventListenerList
     */
    protected void fireStateChanged() 
    {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -=2 ) {
            if (listeners[i] == ChangeListener.class) {
                if (changeEvent == null) {
                    changeEvent = new ChangeEvent(this);
                }
                ((ChangeListener)listeners[i+1]).stateChanged(changeEvent);
            }          
        }
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
