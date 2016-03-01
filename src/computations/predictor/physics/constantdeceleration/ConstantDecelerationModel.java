package computations.predictor.physics.constantdeceleration;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import computations.predictor.physics.PhysicsRegressionModel;

/**
 * UNIBET case. The deceleration is constant, meaning that V(t) = At+b where
 * A<0. This is not the case of Roulette computers.com, it is true.
 */
class ConstantDecelerationModel extends PhysicsRegressionModel
{
	double estimateSpeed(double revolutionCount)
	{
		return slope * revolutionCount + intercept;
	}

	ConstantDecelerationModel(SimpleRegression simpleRegression)
	{
		super(simpleRegression);
	}

	@Override
	public String toString()
	{
		return "ConstantDecelerationModel " + super.toString();
	}
}
