package computations;

import org.junit.Test;

import logger.Logger;

public class GaussianNoiseGenTest
{
	@Test
	public void test1()
	{
		GaussianNoiseGenerator gng = new GaussianNoiseGenerator(0, 25);
		Logger.traceINFO(gng.addNoiseTimeMillis(1000));
	}
}
