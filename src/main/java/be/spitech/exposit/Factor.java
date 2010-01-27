package be.spitech.exposit;

public interface Factor<T> {
	
	/** TODO HALF */
	enum Mode {
		ONE, /*HALF,*/ THIRD;
	}
	
	public T getValue();
}
