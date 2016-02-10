package computations.predictor.physics.constantdeceleration;

import computations.Constants;
import computations.utils.Helper;
import utils.exception.PositiveValueExpectedException;
import utils.logger.Logger;

/**
 * UNIBET case. The deceleration is constant, meaning that V(t) = At+b where
 * A<0. This is not the case of Roulette computers.com, it is true.
 */
public class ConstantDecelerationModel
{
	@Override
	public String toString()
	{
		return "AccelerationModel" + type + " [slope=" + Helper.printDigit(slope) + ", intercept=" + Helper.printDigit(intercept) + "]";
	}

	double			slope;
	double			intercept;
	Constants.Type	type;

	public double estimateSpeed(double revolutionCount)
	{
		return slope * revolutionCount + intercept;
	}

	// Use with the cutoff speed.
	public double estimateTime(int currentRevolution, double cutoffSpeed)
	{
		double revolutionCountLeft = (cutoffSpeed - intercept) / slope - currentRevolution;
		if (revolutionCountLeft < 0)
		{
			throw new PositiveValueExpectedException();
		}

		int revolutionCountInteger = (int) Math.floor(revolutionCountLeft);
		double remainingTime = 0.0;
		for (int i = 1; i <= revolutionCountInteger; i++)
		{
			remainingTime += Helper.getTimeForOneBallLoop(estimateSpeed(currentRevolution + i));
		}
		double revolutionFloatLeft = revolutionCountLeft - revolutionCountInteger;

		double avgSpeedLastRev = 0.5 * estimateSpeed(currentRevolution + revolutionCountInteger)
				+ 0.5 * estimateSpeed(currentRevolution + revolutionCountInteger + 1);
		remainingTime += revolutionFloatLeft * Helper.getTimeForOneBallLoop(avgSpeedLastRev);
		return remainingTime;
	}

	ConstantDecelerationModel(double slope, double intercept, Constants.Type type)
	{
		this.slope = slope;
		this.intercept = intercept;
		this.type = type;
		Logger.traceDEBUG(toString());
	}

}
