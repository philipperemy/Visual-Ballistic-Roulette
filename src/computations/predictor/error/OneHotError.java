package computations.predictor.error;

/**
 * Returns 1 if what the algorithm output and the expected number match
 * Else returns 0.
 * Example: error(32,12) = 1 (32 is different from 12).
 * error(28,28) = 0
 */
public class OneHotError extends DistanceError
{
	@Override
	public int error()
	{
		return (actual.intValue() != expected.intValue()) ? 1 : 0;
	}
}