package computations.predictor.solver;

import java.util.ArrayList;
import java.util.List;

import computations.Constants;
import computations.model.DataRecord;
import computations.model.OutcomeStatistics;
import computations.predictor.Predictor;
import logger.Logger;
import servlets.SessionNotReadyException;

public class FixedMostRecentMeasuresSolver implements PredictorSolver
{
	public int predict(Predictor predictor, List<Double> ballLapTimes, List<Double> wheelLapTimes, String sessionId) throws SessionNotReadyException
	{
		// Phase is filled. All lap times are used to build the model.
		List<DataRecord> predictRecords = predictor.buildDataRecords(ballLapTimes, wheelLapTimes, sessionId);

		if (predictRecords.isEmpty())
		{
			Logger.traceERROR("No records to predict.");
			throw new SessionNotReadyException(wheelLapTimes.size());
		}

		List<Integer> mostProbableNumberList = new ArrayList<>();
		for (int i = 0; i < predictRecords.size(); i++)
		{
			DataRecord predictRecord = predictRecords.get(i);
			Logger.traceINFO("(" + i + ") Record to predict : " + predictRecord);
			int mostProbableNumber = DataRecord.predictOutcome(predictRecord);
			Logger.traceINFO("(" + i + ") Most probable number : " + mostProbableNumber);
			mostProbableNumberList.add(mostProbableNumber);
		}

		int size = mostProbableNumberList.size();
		int par = Constants.RECORDS_COUNT_FOR_PREDICTION;
		if (size >= par)
		{
			mostProbableNumberList = mostProbableNumberList.subList(size - par, size);
		}

		OutcomeStatistics os = OutcomeStatistics.create(mostProbableNumberList);
		int mostProbableFinalNumber = os.meanNumber;
		Logger.traceINFO("(final) Most probable number : " + mostProbableFinalNumber);
		return os.meanNumber;
	}
}
