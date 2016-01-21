package computations.predictor.physics;

public class PositiveValueExpectedException extends RuntimeException
{
	private static final long serialVersionUID = 1L;
	
	public PositiveValueExpectedException()
	{
		super("positive value expected.");
	}
}