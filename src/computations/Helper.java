package computations;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

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

}
