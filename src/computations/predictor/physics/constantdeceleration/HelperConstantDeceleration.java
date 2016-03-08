package computations.predictor.physics.constantdeceleration;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import computations.utils.Helper;
import utils.exception.PositiveValueExpectedException;

class HelperConstantDeceleration
{
	// We should be able to average the deceleration factor. The intercept
	// should not change.
	static double estimateTime(SimpleRegression constantDecelerationModel, int currentRevolution, double cutoffSpeed)
	{
		double revolutionCountLeft = estimateRevolutionCountLeft(constantDecelerationModel, currentRevolution, cutoffSpeed);

		int revolutionCountFloor = (int) Math.floor(revolutionCountLeft);
		double remainingTime = 0.0;
		for (int i = 1; i <= revolutionCountFloor; i++)
		{
			remainingTime += Helper.getTimeForOneBallLoop(constantDecelerationModel.predict(currentRevolution + i));
		}
		double revolutionCountResidual = revolutionCountLeft - revolutionCountFloor;

		double avgSpeedLastRev = 0.5 * constantDecelerationModel.predict(currentRevolution + revolutionCountFloor)
				+ 0.5 * constantDecelerationModel.predict(currentRevolution + revolutionCountFloor + 1);
		remainingTime += revolutionCountResidual * Helper.getTimeForOneBallLoop(avgSpeedLastRev);
		return remainingTime;
	}

	static double estimateRevolutionCountLeft(SimpleRegression constantDecelerationModel, int currentRevolution, double cutoffSpeed)
	{
		double revolutionCountLeft = (cutoffSpeed - constantDecelerationModel.getIntercept()) / constantDecelerationModel.getSlope()
				- currentRevolution;
		if (revolutionCountLeft < 0)
		{
			throw new PositiveValueExpectedException();
		}
		return revolutionCountLeft;
	}

	static SimpleRegression computeModel(List<Double> diffTimes)
	{
		List<Double> speeds = new ArrayList<>();
		for (Double diffTime : diffTimes)
		{
			speeds.add(Helper.getBallSpeed(diffTime));
		}
		return Helper.performRegression(Helper.range(1, speeds.size()), speeds);
	}
}
