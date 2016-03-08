package computations.predictor.physics.linearlaptimes;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import computations.Wheel;
import utils.exception.PositiveValueExpectedException;

public class HelperLinearLapTimes
{
	public static double estimatePhaseAngleDegrees(double remainingDistance, double circumference)
	{
		double revolutions_left = remainingDistance / circumference;
		double angle = (revolutions_left - Math.floor(revolutions_left)) * 360;
		angle = angle % 360;
		return angle;
	}

	public static int estimateShiftWithAngle(double angleAtCutOffTime)
	{
		return (int) Math.round(Wheel.NUMBERS.length * angleAtCutOffTime / 360);
	}

	// speed_function_shifted_b = @(x)
	// 2*ball_track_circumference/sqrt(8*a*x+(2*b+a)^2);
	public static double estimateSpeed(double x, double circumference, SimpleRegression lrv) throws PositiveValueExpectedException
	{
		double a = lrv.getSlope();
		double b = lrv.getIntercept();
		double speed = 2 * circumference / Math.sqrt(8 * a * x + Math.pow(2 * b + a, 2));
		if (speed <= 0)
		{
			throw new PositiveValueExpectedException();
		}
		return speed;
	}

	public static double estimateDistance(double t1, double t2, double circumference, SimpleRegression lrv)
			throws PositiveValueExpectedException
	{
		double a = lrv.getSlope();
		double b = lrv.getIntercept();
		double dist = primitiveSpeed(t2, circumference, a, b) - primitiveSpeed(t1, circumference, a, b);
		if (dist <= 0)
		{
			throw new PositiveValueExpectedException();
		}
		return dist;
	}

	// Inverse function for speed and not shifted_speed!
	// inv_speed_function = @(x)
	// (4*ball_track_circumference^2-x^2*(2*b+a)^2)/(8*a*x^2)+b;
	public static double estimateTimeForSpeed(double x, double circumference, SimpleRegression lrv) throws PositiveValueExpectedException
	{
		return estimateTimeForSpeedShiftB(x, circumference, lrv, true);
	}

	private static double estimateTimeForSpeedShiftB(double x, double circumference, SimpleRegression lrv, boolean shiftB)
			throws PositiveValueExpectedException
	{
		double a = lrv.getSlope();
		double b = lrv.getIntercept();
		double time = (4 * Math.pow(circumference, 2) - Math.pow(x * (2 * b + a), 2)) / (8 * a * Math.pow(x, 2)) + b;

		if (shiftB)
		{
			time -= b;
		}

		if (time <= 0)
		{
			throw new PositiveValueExpectedException();
		}
		return time;
	}

	// @(x)
	// ball_track_circumference*0.5/a*(sqrt((a+2*b)^2-4*a*(2*b-2*x))-(a+2*b));
	private static double primitiveSpeed(double x, double circumference, double a, double b)
	{
		return circumference * 0.5 / a * (Math.sqrt(Math.pow(a + 2 * b, 2) - 4 * a * (2 * b - 2 * x)) - (a + 2 * b));
	}
}
