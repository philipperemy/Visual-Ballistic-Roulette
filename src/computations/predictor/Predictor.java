package computations.predictor;

import java.util.List;

import database.DatabaseAccessorInterface;

public interface Predictor
{
	public void init(DatabaseAccessorInterface da);

	public void init(DatabaseAccessorInterface da, List<String> sessionIds);

	public int predict(List<Double> ballLapTimes, List<Double> wheelLapTimes);
}
