package computations.predictor.physics.constantdeceleration;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import computations.Constants;
import computations.utils.Helper;

public class RegressionManager
{
	// TODO: wrap that in a generic function. Input should be x[] and y[]
	private static ConstantDecelerationModel performLinearRegression(List<Double> speeds, Constants.Type type)
	{
		int n = speeds.size();
		double[] x = new double[n];
		double[] y = new double[n];
		for (int i = 0; i < n; i++)
		{
			x[i] = (i + 1);
			y[i] = speeds.get(i); // big difference is that
									// here we dont inverse the
									// speed.
		}

		SimpleRegression regression = new SimpleRegression();
		for (int i = 0; i < n; i++)
		{
			regression.addData(x[i], y[i]);
		}

		return new ConstantDecelerationModel(regression.getSlope(), regression.getIntercept(), type);
	}

	// Only for the BALL now.
	public static ConstantDecelerationModel computeModel(List<Double> times, Constants.Type type)
	{
		List<Double> speeds = new ArrayList<>();
		for (Double ţime : times)
		{
			speeds.add(Helper.getBallSpeed(0, ţime));
		}
		return performLinearRegression(speeds, type);
	}
}
