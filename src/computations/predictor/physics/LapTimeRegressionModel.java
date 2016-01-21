package computations.predictor.physics;

import java.util.List;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import computations.Helper;

public class LapTimeRegressionModel
{
	public double slope;
	public double intercept;

	public LapTimeRegressionModel(double slope, double intercept)
	{
		this.slope = slope;
		this.intercept = intercept;
	}

	public double predict(double revolutionCount)
	{
		return slope * revolutionCount + intercept;
	}

	@Override
	public String toString()
	{
		return "LapTimeRegressionModel [slope=" + Helper.printDigit(slope) + ", intercept=" + Helper.printDigit(intercept) + "]";
	}
	
	public static LapTimeRegressionModel performLinearRegressionTimes(List<Double> diffs)
	{
		int n = diffs.size();
		double[] x = new double[n];
		double[] y = new double[n];
		for (int i = 0; i < n; i++)
		{
			x[i] = i + 1;
			y[i] = diffs.get(i);
		}

		SimpleRegression regression = new SimpleRegression();
		for (int i = 0; i < n; i++)
		{
			regression.addData(x[i], y[i]);
		}

		return new LapTimeRegressionModel(regression.getSlope(), regression.getIntercept());
	}
}
