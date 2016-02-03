package computations.predictor.ml.solver;

import java.util.List;

import computations.predictor.ml.PredictorML;
import computations.predictor.ml.model.DataRecord;
import computations.utils.Helper;
import exceptions.SessionNotReadyException;
import logger.Logger;

public class MostRecentMeasureSolver implements PredictorSolver
{
	public int predict(PredictorML predictor, List<Double> ballLapTimes, List<Double> wheelLapTimes, String sessionId) throws SessionNotReadyException
	{
		List<DataRecord> predictRecords = getPredictedRecords(predictor, ballLapTimes, wheelLapTimes, sessionId);

		DataRecord predictRecord = Helper.peek(predictRecords);
		Logger.traceINFO("Record to predict : " + predictRecord);

		int mostProbableNumber = DataRecord.predictOutcome(predictRecord);
		Logger.traceINFO("Most probable number : " + mostProbableNumber);
		return mostProbableNumber;
	}
}
