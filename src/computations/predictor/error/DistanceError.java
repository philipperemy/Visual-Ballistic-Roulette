package computations.predictor.error;

import computations.Wheel;

/**
 * Computes the euclidian distance between the expected number and the number
 * the algorithm outputs.
 * Example: error(32,32) = 0
 * error(32,0) = 1 (distance of 0 to 32 is 1 on the wheel).
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