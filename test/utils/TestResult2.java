package utils;

public class TestResult2 extends TestResult
{
	@Override
	public int error()
	{
		if (expected != null && actual != null)
		{
			if (actual.intValue() == expected.intValue())
			{
				return 0;
			} else
			{
				return 1;
			}
		} else
		{
			throw new RuntimeException();
		}
	}
}