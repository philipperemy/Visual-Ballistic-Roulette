package computations.predictor.physics;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import computations.utils.Helper;

public class PhysicsRegressionModel
{
	public double	slope;
	public double	intercept;

	protected PhysicsRegressionModel(SimpleRegression simpleRegression)
	{
		this.slope = simpleRegression.getSlope();
		this.intercept = simpleRegression.getIntercept();
	}

	protected PhysicsRegressionModel(double slope, double intercept)
	{
		this.slope = slope;
		this.intercept = intercept;
	}

	@Override
	public String toString()
	{
		return "[slope=" + Helper.printDigit(slope) + ", intercept=" + Helper.printDigit(intercept) + "]";
	}
}
