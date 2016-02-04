package computations;

import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Assert;
import org.junit.Test;

public class RegressionTest
{
	@Test
	public void testRegression()
	{
		SimpleRegression regression = new SimpleRegression();
		regression.addData(1d, 1d);
		// At this point, with only one observation,
		// all regression statistics will return NaN

		regression.addData(3d, 3d);
		// With only two observations,
		// slope and intercept can be computed
		// but inference statistics will return NaN

		regression.addData(4d, 4d);
		// Now all statistics are defined.

		Assert.assertEquals(0, regression.getIntercept(), 0.01);
		// displays intercept of regression line

		Assert.assertEquals(1, regression.getSlope(), 0.01);
		// displays slope of regression line

		System.out.println(regression.getSlopeStdErr());
		// displays slope standard error
	}

}
