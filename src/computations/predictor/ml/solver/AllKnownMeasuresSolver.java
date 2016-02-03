package computations.predictor.ml.solver;

import java.util.ArrayList;
import java.util.List;

import computations.predictor.OutcomeStatistics;
import computations.predictor.ml.PredictorML;
import computations.predictor.ml.model.DataRecord;
import exceptions.SessionNotReadyException;
import logger.Logger;

public class AllKnownMeasuresSolver implements PredictorSolver
{
	public int predict(PredictorML predictor, List<Double> ballLapTimes, List<Double> wheelLapTimes, String sessionId) throws SessionNotReadyException
	{
		List<DataRecord> predictRecords = getPredictedRecords(predictor, ballLapTimes, wheelLapTimes, sessionId);

		List<Integer> mostProbableNumberList = new ArrayList<>();
		int id = 1;
		for (DataRecord predictRecord : predictRecords)
		{
			Logger.traceINFO("(" + id + ") Record to predict : " + predictRecord);
			int mostProbableNumber = DataRecord.predictOutcome(predictRecord);
			Logger.traceINFO("(" + id + ") Most probable number : " + mostProbableNumber);
			mostProbableNumberList.add(mostProbableNumber);
			id++;
		}

		OutcomeStatistics os = OutcomeStatistics.create(mostProbableNumberList);
		int mostProbableFinalNumber = os.meanNumber;
		Logger.traceINFO("(final) Most probable number : " + mostProbableFinalNumber);
		return os.meanNumber;
	}
}
