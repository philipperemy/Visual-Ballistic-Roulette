package computations;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

//TODO: merge it with the BallisticManager
//Or maybe split the java functions with the business functions.
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

	public static String getSessionNotReadyErrorMessage(String numberOfRecordedWheelTimes)
	{
		return Constants.ERRORLEVEL_SESSION_NOT_READY_STRING + "," + numberOfRecordedWheelTimes;
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

	/**
	 * Should find out if ball considered before is better or not but stay
	 * coherent across the data set.
	 */
	@Deprecated
	public static double getNextTimeBallIsInFrontOfRef(List<Double> ballLapTimes, double wheelLapTimeInFrontOfRef)
	{
		for (Double ballTimeInFrontOfRef : ballLapTimes)
		{
			if (ballTimeInFrontOfRef > wheelLapTimeInFrontOfRef)
			{
				return ballTimeInFrontOfRef;
			}
		}
		throw new RuntimeException("getNextTimeBallIsInFrontOfRef()");
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

	// Here we put all the calculus
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

	public double getLastSpeedWheel(List<Double> wheelDiffTimes)
	{
		double lastTimeRev = Helper.peek(wheelDiffTimes);
		return Constants.get_WHEEL_CIRCUMFERENCE() / lastTimeRev;
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

}
