package computations.predictor.ml.solver;

import java.util.List;

import computations.predictor.ml.PredictorMachineLearning;
import computations.predictor.ml.model.DataRecord;
import exceptions.CriticalException;

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
