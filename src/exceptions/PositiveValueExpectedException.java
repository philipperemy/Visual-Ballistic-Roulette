package exceptions;

public class PositiveValueExpectedException extends Exception
{
	private static final long serialVersionUID = 1L;

	public PositiveValueExpectedException()
	{
		super("positive value expected.");
	}
}