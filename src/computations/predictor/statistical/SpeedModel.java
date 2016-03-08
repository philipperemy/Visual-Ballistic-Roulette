package computations.predictor.statistical;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/*
 * The diameter does not impact the measures. We set it to one.
 */
public class SpeedModel
{
	// ContainerId <-> speed.
	// A TreeMap is a Map key->values that enforces the natural ordering of the
	// keys.
	private Map<Integer, Double> speeds = new TreeMap<>();

	public void loadSpeeds(Map<Integer, LapTimesContainer> lapTimesModel)
	{
		for (Entry<Integer, LapTimesContainer> level : lapTimesModel.entrySet())
		{
			speeds.put(level.getKey(), 1 / (double) level.getValue().getAverage());
		}
	}
}