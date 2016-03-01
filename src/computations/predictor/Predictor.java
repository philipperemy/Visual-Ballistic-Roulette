package computations.predictor;

import java.util.List;

import database.DatabaseAccessorInterface;

public interface Predictor
{
	// By default, we are interested in all the possible games (one game is one
	// session) stored in the database.
	public void init(DatabaseAccessorInterface da);

	// Used for testing.
	public void init(DatabaseAccessorInterface da, List<String> sessionIds);

	public int predict(List<Double> ballLapTimes, List<Double> wheelLapTimes);

	public void clear();
}
