package computations.predictor.physics.linearlaptimes;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import computations.utils.Helper;

public class LapTimeRegressionModel
{
	public double	slope;
	public double	intercept;

	public LapTimeRegressionModel(double slope, double intercept)
	{
		this.slope = slope;
		this.intercept = intercept;
	}

	public LapTimeRegressionModel(SimpleRegression simpleRegression)
	{
		this.slope = simpleRegression.getSlope();
		this.intercept = simpleRegression.getIntercept();
	}

	@Override
	public String toString()
	{
		return "LapTimeRegressionModel [slope=" + Helper.printDigit(slope) + ", intercept=" + Helper.printDigit(intercept) + "]";
	}
}
