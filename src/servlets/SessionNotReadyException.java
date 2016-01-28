package servlets;

public class SessionNotReadyException extends Exception
{
	private static final long serialVersionUID = 5655911156126439803L;

	public SessionNotReadyException()
	{
	}

	public SessionNotReadyException(String msg)
	{
		super(msg);
	}
}
