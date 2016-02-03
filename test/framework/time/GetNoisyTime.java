package framework.time;

import computations.utils.GaussianNoiseGenerator;

public class GetNoisyTime extends GetTime
{
	// mu, sigma
	static GaussianNoiseGenerator gng = new GaussianNoiseGenerator(22, 45);

	@Override
	public String getTime(int hour, int min, int sec, int millis)
	{
		String time = super.getTime(hour, min, sec, millis);
		return gng.addNoiseTimeMillisStr(time);
	}
}
