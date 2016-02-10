package utils.exception;

public class CriticalException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public CriticalException(String msg)
	{
		super(msg);
	}
}
