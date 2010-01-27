package be.spitech.expocalc;


public interface Factor<T> {
	
	/** TODO HALF */
	enum Mode {
		ONE, /*HALF,*/ THIRD;
	}
	
	public T getValue();
	public T getNextValue();
	public T getPreviousValue();
}
