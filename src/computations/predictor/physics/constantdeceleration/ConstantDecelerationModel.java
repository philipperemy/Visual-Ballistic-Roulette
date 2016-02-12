package computations.predictor.physics.constantdeceleration;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import computations.utils.Helper;

/**
 * UNIBET case. The deceleration is constant, meaning that V(t) = At+b where
 * A<0. This is not the case of Roulette computers.com, it is true.
 */
class ConstantDecelerationModel
{
	double	slope;
	double	intercept;

	double estimateSpeed(double revolutionCount)
	{
		return slope * revolutionCount + intercept;
	}

	ConstantDecelerationModel(SimpleRegression simpleRegression)
	{
		this.slope = simpleRegression.getSlope();
		this.intercept = simpleRegression.getIntercept();
	}

	@Override
	public String toString()
	{
		return "AccelerationModel [slope=" + Helper.printDigit(slope) + ", intercept=" + Helper.printDigit(intercept) + "]";
	}

}
