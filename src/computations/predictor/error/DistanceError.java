package computations.predictor.error;

import computations.Wheel;

public class DistanceError
{
	public Integer expected;
	public Integer actual;

	public int error()
	{
		return Wheel.distanceBetweenNumbers(expected, actual);
	}
}