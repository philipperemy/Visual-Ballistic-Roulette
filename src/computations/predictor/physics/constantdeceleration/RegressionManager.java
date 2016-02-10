package computations.predictor.physics.constantdeceleration;

import java.util.ArrayList;
import java.util.List;

import computations.utils.Helper;
import utils.exception.PositiveValueExpectedException;

public class RegressionManager
{
	// Use with the cutoff speed.
	public static double estimateTime(ConstantDecelerationModel constantDecelerationModel, int currentRevolution, double cutoffSpeed)
	{
		double revolutionCountLeft = (cutoffSpeed - constantDecelerationModel.intercept) / constantDecelerationModel.slope - currentRevolution;
		if (revolutionCountLeft < 0)
		{
			throw new PositiveValueExpectedException();
		}

		int revolutionCountInteger = (int) Math.floor(revolutionCountLeft);
		double remainingTime = 0.0;
		for (int i = 1; i <= revolutionCountInteger; i++)
		{
			remainingTime += Helper.getTimeForOneBallLoop(constantDecelerationModel.estimateSpeed(currentRevolution + i));
		}
		double revolutionFloatLeft = revolutionCountLeft - revolutionCountInteger;

		double avgSpeedLastRev = 0.5 * constantDecelerationModel.estimateSpeed(currentRevolution + revolutionCountInteger)
				+ 0.5 * constantDecelerationModel.estimateSpeed(currentRevolution + revolutionCountInteger + 1);
		remainingTime += revolutionFloatLeft * Helper.getTimeForOneBallLoop(avgSpeedLastRev);
		return remainingTime;
	}

	public static ConstantDecelerationModel computeModel(List<Double> diffTimes)
	{
		List<Double> speeds = new ArrayList<>();
		for (Double diffTime : diffTimes)
		{
			speeds.add(Helper.getBallSpeed(diffTime));
		}
		return new ConstantDecelerationModel(Helper.performRegression(Helper.range(1, speeds.size()), speeds));
	}
}
