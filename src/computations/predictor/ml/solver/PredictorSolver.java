package computations.predictor.ml.solver;

import java.util.List;

import computations.predictor.ml.PredictorMachineLearning;
import computations.predictor.ml.model.DataRecord;
import utils.exception.CriticalException;

/**
 * A game has several data records (<ball speed, wheel speed, phase>). We
 * investigate several strategies. For example we can keep only the most recent
 * (up to now) data record we have of the game that is currently playing.
 */
public interface PredictorSolver
{
	public int predict(PredictorMachineLearning predictor, List<Double> ballLapTimes, List<Double> wheelLapTimes);

	/*
	 * Throws a SessionNotReadyException that can happen during the first games
	 * when the database is empty.
	 */
	public default List<DataRecord> getPredictedRecords(PredictorMachineLearning predictor, List<Double> ballLapTimes, List<Double> wheelLapTimes)
	{
		// Phase is filled. All lap times are used to build the model.
		List<DataRecord> predictRecords = predictor.buildDataRecords(ballLapTimes, wheelLapTimes);

		if (predictRecords.isEmpty())
		{
			throw new CriticalException("No records to predict. Database is empty.");
		}
		return predictRecords;
	}
}
