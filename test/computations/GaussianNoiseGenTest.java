package computations;

import org.junit.Assert;
import org.junit.Test;

import computations.utils.GaussianNoiseGenerator;
import logger.Logger;

public class GaussianNoiseGenTest
{
	@Test
	public void test1()
	{
		GaussianNoiseGenerator gng = new GaussianNoiseGenerator(0, 25);
		Logger.traceINFO(gng.addNoiseTimeMillis(1000));

		String in = "3000";
		String out = gng.addNoiseTimeMillisStr(in);
		Assert.assertTrue(!out.equals(in)); // out != in
	}
}
