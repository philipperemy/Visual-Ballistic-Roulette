package computations.predictor.ml;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import computations.Constants;
import computations.utils.Helper;
import utils.logger.Logger;

class BallisticManager
{
	private static class ClockSpeed
	{
		private double	time;
		private double	speed;

		@Override
		public String toString()
		{
			return "ClockSpeed [time=" + time + ", speed=" + speed + "]";
		}
	}

	/*
	 * Each speed is the average of the measurements intervals
	 */
	private static List<ClockSpeed> computeInstantAverageSpeeds(List<Double> lapTimes, Constants.Type type)
	{
		List<ClockSpeed> speedMeasurements = new ArrayList<>(lapTimes.size() - 1);
		for (int i = 1; i < lapTimes.size(); i++)
		{
			double t1 = lapTimes.get(i - 1);
			double t2 = lapTimes.get(i);
			ClockSpeed speedMeasurement = new ClockSpeed();
			speedMeasurement.speed = Helper.getSpeed(t1, t2, type);
			speedMeasurement.time = 0.5 * t1 + 0.5 * t2;
			speedMeasurements.add(speedMeasurement);
		}
		Logger.traceDEBUG("computeInstantAverageSpeeds(" + type + ") - " + speedMeasurements.toString());
		return speedMeasurements;
	}

	/*
	 * Speed is inverted to have a linear problem.
	 */
	private static AccelerationModel performLinearRegression(List<ClockSpeed> speedMeasurements, Constants.Type type)
	{
		List<Double> xAxis = new ArrayList<>();
		List<Double> yAxis = new ArrayList<>();
		for (ClockSpeed cSpeed : speedMeasurements)
		{
			xAxis.add(cSpeed.time);
			yAxis.add(Helper.inverseSpeed(cSpeed.speed));
		}

		SimpleRegression regression = Helper.performRegression(xAxis, yAxis);
		return new AccelerationModel(regression.getSlope(), regression.getIntercept());
	}

	static AccelerationModel computeModel(List<Double> times, Constants.Type type)
	{
		List<ClockSpeed> speeds = BallisticManager.computeInstantAverageSpeeds(times, type);

		// Last known speed in the model.
		if (speeds.size() == 1)
		{
			Logger.traceDEBUG("Only one speed detected for type: " + type);
			return new AccelerationModelOneSpeed(speeds.get(0).speed);
		}

		return performLinearRegression(speeds, type);
	}
}
