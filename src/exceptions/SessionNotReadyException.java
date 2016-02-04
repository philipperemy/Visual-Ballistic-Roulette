package exceptions;

public class SessionNotReadyException extends Exception
{
	private static final long serialVersionUID = 1L;

	public SessionNotReadyException()
	{
	}

	public SessionNotReadyException(String msg)
	{
		super(msg);
	}
}
