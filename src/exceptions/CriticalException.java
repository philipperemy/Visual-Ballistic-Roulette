package exceptions;

public class CriticalException extends RuntimeException
{
	private static final long serialVersionUID = 8854198925276962129L;

	public CriticalException(String msg)
	{
		super(msg);
	}
}
