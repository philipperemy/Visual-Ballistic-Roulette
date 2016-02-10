package utils.exception;

public class InitializationRequiredException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public InitializationRequiredException()
	{
		super("Initialization required before usage.");
	}
}
