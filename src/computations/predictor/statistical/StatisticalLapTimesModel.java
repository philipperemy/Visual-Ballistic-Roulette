package computations.predictor.statistical;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import utils.exception.CriticalException;
import utils.logger.Logger;

// It does not work now because we don't model what happens after the last measure.
public class StatisticalLapTimesModel
{
	private static final int				FIRST_BACKWARD_IDENTIFIER	= 100;

	// should begin at FIRST_BACKWARD_IDENTIFIER and decreases. We know when the
	// games ends because we assume that ball falls off the track at the same
	// speed.

	// The model is a list of all the recorded laptimes indexed by their
	// absolute positions in the game.
	// Example: Let's consider the lap times of the ball for two different
	// games. The ball falls off the track just after.
	// Game1 [1, 2, 3, 4, 5] - 5 revolutions
	// Game2 [2.5, 3.5, 4.5] - 3 revolutions
	// The objective is to derive an average game with all the average lap times
	// for each index.
	// We understand that we cannot add the two games because the lengths are
	// different (3 <> 5). In this case, we pad the game 2 with ?. This symbol ?
	// means that mean(1, ?) = 1. We don't count this value in the computation
	// of the mean. Otherwise, if we set it to 0, we would have mean(1,0) = 0.5
	// and this is clearly a false assertion.
	// Game1 [1, 2, 3, 4, 5] - 5 revolutions
	// Game2 [?, ?, 2.5, 3.5, 4.5] - 3 revolutions
	// Once the padding is done, we can average the game to have an expectation.
	// GameE [1, 2, (3+2.5)/2,...] = [1, 2, 2.75, 3.75, 4.75]
	// It means that on average, we expect the last revolution to be 4.75
	// seconds, the before last 3.75 and so on.

	// Index_100 -> list of the LAST revolution lap times
	// Index_99 -> list of the BEFORE LAST revolution lap times
	// Index_100 is taken arbitrary and large enough to ensure that we don't
	// have more
	// than 100 revolutions within a game.
	// We proceed backwards as we know when the game ends but due to the initial
	// conditions, we can't match two games forward.
	private Map<Integer, LapTimesContainer>	model						= new TreeMap<>();

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

	// Add the laptimes going backwards.
	// Last lap time is added to the index 100. Before last to 99. Until all are
	// processed.
	public List<LapTimesContainer> enrichModel(List<Double> ballLapTimes)
	{
		List<LapTimesContainer> containers = new ArrayList<>();

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
	 * Returns a list of each average value per level (100, 99,...).
	 */
	private List<Double> getAverageValues()
	{
		if (model.isEmpty())
		{
			throw new CriticalException("Initialization required before usage.");
		}

		List<Double> avgs = new ArrayList<>();
		for (Entry<Integer, LapTimesContainer> level : model.entrySet())
		{
			avgs.add(level.getValue().getAverage());
		}

		return avgs;
	}

	/**
	 * For a given list of lap times, we want to find how many revolutions are
	 * left. From that we sum all the average revolution times.
	 */
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

	/**
	 * For a given list of lap times, we want to find how many revolutions are
	 * left. If we have, 97 -> 3sec, 98 -> 5sec, 99 -> 6sec, 100 -> 8 sec and if
	 * we find that there is 2 revolutions left, we calculate that we have
	 * 6+8=14sec left.
	 */
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
				error += Math.pow((1 / val1) - (1 / val2), 2); // Compare the
																// inverse of
																// the lap
																// times.
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
