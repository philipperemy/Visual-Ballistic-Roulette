package computations.predictor.solver;

import java.util.ArrayList;
import java.util.List;

import computations.model.DataRecord;
import computations.model.OutcomeStatistics;
import computations.predictor.Predictor;
import logger.Logger;
import servlets.SessionNotReadyException;

public class AllKnownMeasuresSolver implements PredictorSolver
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
