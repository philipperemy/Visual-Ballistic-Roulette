package computations.predictor;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import computations.Constants;
import computations.Helper;
import computations.wheel.Type;
import log.Logger;

public class BallisticManager
{
	// Could interpolate with ML stuffs.
	static double getTimeForOneBallLoop(double ballSpeed)
	{
		return Constants.BALL_CIRCUMFERENCE / ballSpeed;
	}

	// Could interpolate with ML stuffs.
	static double getTimeForOneWheelLoop(double wheelSpeed)
	{
		return Constants.WHEEL_CIRCUMFERENCE / wheelSpeed;
	}

	// m/s. T1 and T2

	private static double getSpeed(double t1, double t2, Type type)
	{
		switch (type)
		{
		case BALL:
			return getBallSpeed(t1, t2);
		case WHEEL:
			return getWheelSpeed(t1, t2);
		default:
			throw new RuntimeException();
		}
	}

	public static double getBallSpeed(double t1, double t2)
	{
		return Constants.BALL_CIRCUMFERENCE / (t2 - t1);
	}

	// m/s
	public static double getWheelSpeed(double t1, double t2)
	{
		return Constants.WHEEL_CIRCUMFERENCE / (t2 - t1);
	}

	/*
	 * Each speed is the average of the measurements intervals
	 */
	private static List<ClockSpeed> computeInstantAverageSpeeds(List<Double> lapTimes, Type type)
	{
		List<ClockSpeed> speedMeasurements = new ArrayList<>(lapTimes.size() - 1);
		for (int i = 1; i < lapTimes.size(); i++)
		{
			double t1 = lapTimes.get(i - 1);
			double t2 = lapTimes.get(i);
			ClockSpeed speedMeasurement = new ClockSpeed();
			speedMeasurement.speed = getSpeed(t1, t2, type);
			speedMeasurement.time = 0.5 * t1 + 0.5 * t2;
			speedMeasurements.add(speedMeasurement);
		}
		Logger.traceDEBUG("computeInstantAverageSpeeds(" + type + ") - " + speedMeasurements.toString());
		return speedMeasurements;
	}

	/*
	 * Speed is inverted to have a linear problem.
	 */
	private static AccelerationModel performLinearRegression(List<ClockSpeed> speedMeasurements, Type type)
	{
		int n = speedMeasurements.size();
		double[] x = new double[n];
		double[] y = new double[n];
		for (int i = 0; i < n; i++)
		{
			x[i] = speedMeasurements.get(i).time;
			y[i] = Helper.inverseSpeed(speedMeasurements.get(i).speed);
		}

		SimpleRegression regression = new SimpleRegression();
		for (int i = 0; i < n; i++)
		{
			regression.addData(x[i], y[i]);
		}

		return new AccelerationModel(regression.getSlope(), regression.getIntercept(), type);
	}

	static AccelerationModel computeModel(List<Double> times, Type type)
	{
		List<ClockSpeed> speeds = BallisticManager.computeInstantAverageSpeeds(times, type);

		// Last known speed in the model.
		if (speeds.size() == 1)
		{
			Logger.traceINFO("Only one speed detected for type: " + type);
			return new AccelerationModelOneSpeed(type, speeds.get(0).speed);
		}

		return performLinearRegression(speeds, type);
	}
}
