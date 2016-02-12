package computations.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import computations.Constants;
import computations.predictor.physics.linearlaptimes.LapTimeRegressionModel;
import utils.exception.CriticalException;

public class Helper
{
	public static final double VERY_HIGH_NUMBER = 1_000_000_000;

	public static List<Double> convertToSeconds(List<Double> listInMilliseconds)
	{
		List<Double> listInSeconds = new ArrayList<>();
		for (Double itemMsec : listInMilliseconds)
		{
			listInSeconds.add(itemMsec * 0.001);
		}
		return listInSeconds;
	}

	// [0, 4, 15, 19, 21, 26, 32]
	public static List<Integer> unserializeOutcomeNumbers(String input)
	{
		String str = input.substring(1, input.length() - 1);
		List<Integer> list = new ArrayList<>();
		for (String chunk : str.split(","))
		{
			list.add(Integer.parseInt(chunk.trim()));
		}
		return list;
	}

	public static <T> T peek(List<T> list)
	{
		return list.get(list.size() - 1);
	}

	public static <T> T head(List<T> list)
	{
		return list.get(0);
	}

	public static String printValueOrInfty(Double value)
	{
		if (value > VERY_HIGH_NUMBER)
		{
			return "+oo";
		}
		return String.valueOf(value);
	}

	public static String printDigit(double number)
	{
		return new DecimalFormat("###.####").format(number);
	}

	public static double inverseSpeed(final double speed)
	{
		return (double) 1.0 / speed;
	}

	public static Double getLastTimeWheelIsInFrontOfRef(List<Double> wheelLapTimes, double ballLapTimeInFrontOfRef)
	{
		Double res = null;
		for (Double wheelTimeInFrontOfRef : wheelLapTimes)
		{
			if (wheelTimeInFrontOfRef < ballLapTimeInFrontOfRef)
			{
				res = wheelTimeInFrontOfRef;
			}
		}
		return res;
	}

	public static List<Double> computeDiff(List<Double> lapTimes)
	{
		List<Double> diffs = new ArrayList<>(lapTimes.size() - 1);
		for (int i = 1; i < lapTimes.size(); i++)
		{
			double t1 = lapTimes.get(i - 1);
			double t2 = lapTimes.get(i);
			diffs.add(t2 - t1);
		}
		return diffs;
	}

	public static List<Double> normalize(List<Double> cumsumTimes, double origin)
	{
		List<Double> ret = new ArrayList<>();
		for (Double val : cumsumTimes)
		{
			ret.add(val - origin);
		}
		return ret;
	}

	public static double estimateDistanceConstantSpeed(double t1, double t2, double speed)
	{
		return speed * (t2 - t1);
	}

	// m/s
	public static double getBallSpeed(double t1, double t2)
	{
		return Constants.get_BALL_CIRCUMFERENCE() / (t2 - t1);
	}

	// m/s
	public static double getWheelSpeed(double t1, double t2)
	{
		return Constants.get_WHEEL_CIRCUMFERENCE() / (t2 - t1);
	}

	public static double getBallSpeed(double deltaT)
	{
		return getBallSpeed(0, deltaT);
	}

	public static double getWheelSpeed(double deltaT)
	{
		return getWheelSpeed(0, deltaT);
	}

	public static double getTimeForOneBallLoop(double ballSpeed)
	{
		return Constants.get_BALL_CIRCUMFERENCE() / ballSpeed;
	}

	// Could interpolate with ML stuffs.
	public static double getTimeForOneWheelLoop(double wheelSpeed)
	{
		return Constants.get_WHEEL_CIRCUMFERENCE() / wheelSpeed;
	}

	// m/s. T1 and T2
	public static double getSpeed(double t1, double t2, Constants.Type type)
	{
		switch (type)
		{
			case BALL:
				return Helper.getBallSpeed(t1, t2);
			case WHEEL:
				return Helper.getWheelSpeed(t1, t2);
			default:
				throw new CriticalException("Unknown type.");
		}
	}

	public static List<Double> cumsum(List<Double> in)
	{
		double total = 0.0;
		List<Double> out = new ArrayList<>();
		out.add(0.0);
		for (Double val : in)
		{
			total += val;
			out.add(total);
		}
		return out;
	}

	/**
	 * TODO: implement it as a filter. Sometimes we can miss a loop, especially
	 * on the ball. But usually we can have a decrease in the diff times due to
	 * errors on the measurements.
	 */
	public boolean measuresAreValid(List<Double> diffTimes)
	{
		return false;
	}

	public static <T> List<List<T>> split(List<T> list, final int L)
	{
		List<List<T>> parts = new ArrayList<List<T>>();
		final int N = list.size();
		for (int i = 0; i < N; i += L)
		{
			parts.add(new ArrayList<T>(list.subList(i, Math.min(N, i + L))));
		}
		return parts;
	}

	public static SimpleRegression performRegression(List<Double> _x, List<Double> _y)
	{
		int n = _x.size();
		SimpleRegression regression = new SimpleRegression();
		for (int i = 0; i < n; i++)
		{
			regression.addData(_x.get(i), _y.get(i));
		}
		return regression;
	}

	public static List<Double> range(int beg, int end)
	{
		List<Double> range = new ArrayList<>();
		for (int i = beg; i <= end; i++)
		{
			range.add((double) i);
		}
		return range;
	}

	// Random - hyper parameters introduced. So it's not very good.
	public static LapTimeRegressionModel performRANSAC(List<Double> diffs)
	{
		List<Double> res = RANSAC.perform(diffs, 2, 100, 1, 0.2);
		return new LapTimeRegressionModel(res.get(0), res.get(1));
	}
}
