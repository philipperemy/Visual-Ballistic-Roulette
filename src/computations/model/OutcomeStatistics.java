package computations.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import computations.Helper;
import computations.wheel.Wheel;

public class OutcomeStatistics
{
	@Override
	public String toString()
	{
		return "OutcomeStatistics [meanNumber=" + meanNumber + ", stdDeviation=" + Helper.printDigit(stdDeviation) + ", "
				+ (frequency != null ? "frequency=" + frequency + ", " : "") + "]";
	}

	public int meanNumber;
	private double stdDeviation;
	private Map<Integer, Integer> frequency;

	public static OutcomeStatistics create(List<Integer> outcomeNumbers)
	{
		Map<Integer, Integer> frequencyPerNumber = new HashMap<>(); // Number
																	// <->
																	// Frequency
		for (Integer number : outcomeNumbers)
		{

			Integer freq = frequencyPerNumber.get(number);
			if (freq == null || freq.intValue() == 0)
			{
				frequencyPerNumber.put(number, Integer.valueOf(1));
			} else
			{
				frequencyPerNumber.put(number, ++freq);
			}
		}

		// Reduce the Residuals Sum of Squares (RSS).
		int meanNumber = 0;
		double minRss = Double.MAX_VALUE;
		for (int idxMean = 0; idxMean < Wheel.NUMBERS.length; idxMean++)
		{

			double rss = 0;
			for (Integer outcomeNumber : outcomeNumbers)
			{
				rss += Math.pow(Wheel.distanceBetweenNumbers(outcomeNumber, Wheel.NUMBERS[idxMean]), 2);
			}

			if (rss < minRss)
			{
				meanNumber = Wheel.NUMBERS[idxMean];
				minRss = rss;
			}
		}

		double var = 0;
		for (int i = 0; i < outcomeNumbers.size(); i++)
		{
			int dist = Wheel.distanceBetweenNumbers(outcomeNumbers.get(i), meanNumber);
			var += Math.pow(dist, 2); // distance^2
		}

		int maxFreq = 0;
		Integer mostProbableNumber = null;
		for (Entry<Integer, Integer> frequencyEntry : frequencyPerNumber.entrySet())
		{
			if (frequencyEntry.getValue() > maxFreq)
			{
				mostProbableNumber = Integer.valueOf(frequencyEntry.getKey());
			}
		}

		if (mostProbableNumber == null)
		{
			throw new RuntimeException();
		}

		OutcomeStatistics outcomeStatistics = new OutcomeStatistics();
		outcomeStatistics.meanNumber = meanNumber;
		outcomeStatistics.stdDeviation = Math.sqrt(var);
		outcomeStatistics.frequency = frequencyPerNumber;
		return outcomeStatistics;
	}

}
