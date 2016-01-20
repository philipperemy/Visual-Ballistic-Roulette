package computations.predictor.ml.solver;

import java.util.List;

import computations.Helper;
import computations.model.DataRecord;
import computations.predictor.ml.PredictorML;
import logger.Logger;
import servlets.SessionNotReadyException;

public class MostRecentMeasureSolver implements PredictorSolver
{
	public int predict(PredictorML predictor, List<Double> ballLapTimes, List<Double> wheelLapTimes, String sessionId) throws SessionNotReadyException
	{
		// Phase is filled. All lap times are used to build the model.
		List<DataRecord> predictRecords = predictor.buildDataRecords(ballLapTimes, wheelLapTimes, sessionId);

		if (predictRecords.isEmpty())
		{
			Logger.traceERROR("No records to predict.");
			throw new SessionNotReadyException(wheelLapTimes.size());
		}

		// Take the last one.
		// TODO: maybe we can improve it by taking all.
		DataRecord predictRecord = Helper.peek(predictRecords);
		Logger.traceINFO("Record to predict : " + predictRecord);

		int mostProbableNumber = DataRecord.predictOutcome(predictRecord);
		Logger.traceINFO("Most probable number : " + mostProbableNumber);
		return mostProbableNumber;
	}
}
