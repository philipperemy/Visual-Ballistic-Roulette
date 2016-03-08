package computations.predictor.error;

/**
 * Returns 1 if what the algorithm output and the expected number match
 * Else returns 0.
 */
public class OneHotError extends DistanceError
{
	@Override
	public int error()
	{
		return (actual.intValue() != expected.intValue()) ? 1 : 0;
	}
}