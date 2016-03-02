package computations.predictor.physics.linearlaptimes;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import computations.predictor.physics.PhysicsRegressionModel;

public class LapTimeRegressionModel extends PhysicsRegressionModel
{
	public LapTimeRegressionModel(SimpleRegression simpleRegression)
	{
		super(simpleRegression);
	}

	public LapTimeRegressionModel(double slope, double intercept)
	{
		super(slope, intercept);
	}

	@Override
	public String toString()
	{
		return "LapTimeRegressionModel " + super.toString();
	}
}
