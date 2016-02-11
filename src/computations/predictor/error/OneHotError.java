package computations.predictor.error;

public class OneHotError extends DistanceError
{
	@Override
	public int error()
	{
		return (actual.intValue() != expected.intValue()) ? 1 : 0;
	}
}