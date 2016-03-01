package computations.predictor.statistical;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import utils.exception.CriticalException;
import utils.logger.Logger;

public class StatisticalLapTimesModel
{
	private static final int FIRST_BACKWARD_IDENTIFIER = 100;

	// should begin at FIRST_BACKWARD_IDENTIFIER and decreases.
	// We know when the games ends
	// because it is always the same
	// speed that the ball falls in the
	// track.
	private Map<Integer, LapTimesContainer> model = new TreeMap<>();

	void free()
	{
		model = new TreeMap<>();
	}

	private void addLapTime(int containerId, double lapTime)
	{
		LapTimesContainer container = model.get(containerId);
		if (container == null)
		{
			container = new LapTimesContainer();
			model.put(containerId, container);
		}
		container.revolutionTimes.add(lapTime);
	}

	public List<LapTimesContainer> enrichModel(List<Double> ballLapTimes)
	{
		List<LapTimesContainer> containers = new ArrayList<>();

		// trick to iterate backward
		int containerId = FIRST_BACKWARD_IDENTIFIER;
		for (int b = ballLapTimes.size() - 1; b >= 0; b--)
		{
			double lapTime = ballLapTimes.get(b);
			addLapTime(containerId, lapTime);
			containerId--;
		}

		return containers;
	}

	/**
	 * Given a list of ballLapTimes, we want to detect where are we in the game.
	 */
	private List<Double> getAverageValues()
	{
		if (model.isEmpty())
		{
			throw new CriticalException("Initialization required before usage");
		}

		List<Double> avgs = new ArrayList<>();
		for (Entry<Integer, LapTimesContainer> level : model.entrySet())
		{
			avgs.add(level.getValue().getAverage());
		}

		return avgs;
	}

	double remainingTime(List<Double> lapTimes)
	{
		List<Double> avgs = getAverageValues();
		return remainingTimeAux(avgs, lapTimes);
	}

	public double remainingTimeAux(List<Double> avgs, List<Double> lapTimes)
	{
		int idx = identifyLastContainerId(avgs, lapTimes);
		double remTime = 0.0;
		for (int i = idx + 1; i < avgs.size(); i++)
		{
			remTime += avgs.get(i);
		}
		return remTime;
	}

	public int identifyLastContainerId(List<Double> avgs, List<Double> lapTimes)
	{
		Logger.traceDEBUG("matcher=" + lapTimes.toString());
		Logger.traceDEBUG("avgs=" + avgs.toString());
		Integer result = null;
		double bestError = Double.MAX_VALUE;
		for (int i = 0; i <= avgs.size() - lapTimes.size(); i++)
		{
			double error = 0.0;
			for (int j = 0; j < lapTimes.size(); j++)
			{
				double val1 = lapTimes.get(j);
				double val2 = avgs.get(j + i);
				error += Math.pow((1 / val1) - (1 / val2), 2);
			}
			error = Math.sqrt(error);

			if (error < bestError)
			{
				bestError = error;
				result = i;
			}
			Logger.traceDEBUG("error=" + error + ", index=" + result);
		}
		Logger.traceDEBUG("best error=" + bestError);
		Logger.traceDEBUG("idx matching start=" + result);
		result += lapTimes.size() - 1; // index of last game.
		Logger.traceDEBUG("idx matching end=" + result);
		return result;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for (Entry<Integer, LapTimesContainer> level : model.entrySet())
		{
			LapTimesContainer container = level.getValue();
			sb.append("_______________\n");
			sb.append("key = " + level.getKey() + ", average = " + container.getAverage() + "\n");
			for (double time : container.revolutionTimes)
			{
				sb.append(time + "\n");
			}
		}
		return sb.toString();
	}

	public String printAverageValues()
	{
		StringBuilder sb = new StringBuilder();
		for (Double val : getAverageValues())
		{
			sb.append(val + "\n");
		}
		return sb.toString();
	}
}
