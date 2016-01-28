package computations.predictor.ml.solver;

import java.util.List;

import computations.predictor.ml.PredictorML;
import computations.predictor.ml.model.DataRecord;
import servlets.SessionNotReadyException;

public interface PredictorSolver
{
	public int predict(PredictorML predictor, List<Double> ballLapTimes, List<Double> wheelLapTimes, String sessionId)
			throws SessionNotReadyException;

	/*
	 * Throws a SessionNotReadyException that can happen during the first games
	 * when the database is empty.
	 */
	public default List<DataRecord> getPredictedRecords(PredictorML predictor, List<Double> ballLapTimes, List<Double> wheelLapTimes,
			String sessionId) throws SessionNotReadyException
	{
		// Phase is filled. All lap times are used to build the model.
		List<DataRecord> predictRecords = predictor.buildDataRecords(ballLapTimes, wheelLapTimes, sessionId);

		if (predictRecords.isEmpty())
		{
			throw new SessionNotReadyException("No records to predict.");
		}
		return predictRecords;
	}
}
