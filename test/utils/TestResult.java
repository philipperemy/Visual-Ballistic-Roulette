package utils;

import computations.Wheel;

public class TestResult
{
	public Integer	expected;
	public Integer	actual;

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