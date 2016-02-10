package computations.predictor.statistical;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/*
 * Lets consider the diameter is one.
 */
public class SpeedModel
{
	/**
	 * ContainerId <-> speed.
	 */
	private Map<Integer, Double> speeds = new TreeMap<>();

	public void loadSpeeds(Map<Integer, LapTimesContainer> lapTimesModel)
	{
		for (Entry<Integer, LapTimesContainer> level : lapTimesModel.entrySet())
		{
			speeds.put(level.getKey(), 1 / (double) level.getValue().getAverage());
		}
	}

}
