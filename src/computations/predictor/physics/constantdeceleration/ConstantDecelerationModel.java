package computations.predictor.physics.constantdeceleration;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import computations.predictor.physics.PhysicsRegressionModel;

/**
 * UNIBET case. The deceleration is constant across the revolutions, meaning
 * that V(t) = At+b where A<0. This is not the case of Roulette computers.com.
 */
class ConstantDecelerationModel extends PhysicsRegressionModel
{
	double estimateSpeed(double revolutionCount)
	{
		return evaluate(revolutionCount);
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
