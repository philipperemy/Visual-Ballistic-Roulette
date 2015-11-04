package framework;

import computations.wheel.Wheel;

public class TestResult
{
	Integer expected;
	Integer actual;

	public int error()
	{
		if (expected != null && actual != null)
		{
			return Wheel.distanceBetweenNumbers(expected, actual);
		} else
		{
			throw new RuntimeException();
		}
	}

}