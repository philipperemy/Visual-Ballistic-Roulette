package computations.predictor.error;

import computations.Wheel;

/**
 * Computes the euclidian distance between the expected number and the number
 * the algorithm outputs.
 */
public class DistanceError
{
	public Integer expected;
	public Integer actual;

	public int error()
	{
		return Wheel.distanceBetweenNumbers(expected, actual);
	}
}