package exceptions;

public class SommetNonExisteException extends Exception {
	public SommetNonExisteException(String msg)	{
		super(msg);
	}

	public SommetNonExisteException() {
		super();
	}
}
