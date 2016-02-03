package computations.utils;

import org.apache.commons.math3.distribution.NormalDistribution;

import logger.Logger;

public class GaussianNoiseGenerator
{
	private NormalDistribution nd;

	public GaussianNoiseGenerator(double mu, double sigma)
	{
		nd = new NormalDistribution(mu, sigma);
	}

	public long addNoiseTimeMillis(long time)
	{
		long noise = time + (long) nd.sample();
		Logger.traceDEBUG("Adding noise: before [" + time + "], after [" + noise + "]");
		return noise;
	}

	public String addNoiseTimeMillisStr(String timeStr)
	{
		long time = Long.valueOf(timeStr);
		long noise = addNoiseTimeMillis(time);
		return String.valueOf(noise);
	}
}
