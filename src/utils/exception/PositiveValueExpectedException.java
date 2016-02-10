package utils.exception;

public class PositiveValueExpectedException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public PositiveValueExpectedException()
	{
		super("Positive value expected.");
	}
}