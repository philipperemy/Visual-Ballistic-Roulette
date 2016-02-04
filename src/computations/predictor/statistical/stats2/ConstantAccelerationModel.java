package computations.predictor.statistical.stats2;

import computations.Constants;
import computations.utils.Helper;
import logger.Logger;

/**
 * UNIBET case. The deceleration is constant, meaning that V(t) = At+b where
 * A<0. This is not the case of Roulette computers.com, it is true.
 */
public class ConstantAccelerationModel
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
	public double estimateTime(int currentRevolution, double speed)
	{
		double revolutionCountLeft = (speed - intercept) / slope;
		int revolutionCountInteger = (int) Math.floor(revolutionCountLeft);
		double remainingTime = 0.0;
		for (int i = currentRevolution + 1; i <= revolutionCountInteger; i++)
		{
			remainingTime += Helper.getTimeForOneBallLoop(estimateSpeed(revolutionCountInteger));
		}
		double revolutionFloatLeft = revolutionCountLeft - revolutionCountInteger;

		double avgSpeedLastRev = 0.5 * estimateSpeed(revolutionCountInteger) + 0.5 * estimateSpeed(revolutionCountInteger + 1);
		remainingTime += revolutionFloatLeft * Helper.getTimeForOneBallLoop(avgSpeedLastRev);
		return remainingTime;
	}

	ConstantAccelerationModel(double slope, double intercept, Constants.Type type)
	{
		this.slope = slope;
		this.intercept = intercept;
		this.type = type;
		Logger.traceDEBUG(toString());
	}

}
