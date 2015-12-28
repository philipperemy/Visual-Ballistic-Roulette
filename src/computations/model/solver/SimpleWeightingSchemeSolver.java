package computations.model.solver;

import java.util.ArrayList;
import java.util.List;

import computations.Constants;
import computations.model.DataRecord;
import computations.model.OutcomeStatistics;
import computations.wheel.Wheel;
import logger.Logger;

public class SimpleWeightingSchemeSolver implements OutcomeSolver
{
	// Simple Weighting Scheme
	public int predictOutcome(DataRecord predict)
	{
		List<DataRecord> matchedRecordsList = DataRecord.matchCache(predict, Constants.NUMBER_OF_NEIGHBORS_KNN);
		List<Integer> outcomeNumbersList = new ArrayList<>();

		// Weight the outcome.
		for (int i = 0; i < matchedRecordsList.size(); i++)
		{
			DataRecord matched = matchedRecordsList.get(i);
			int predictedOutcome = Wheel.predictOutcomeWithShift(matched.phaseOfWheelWhenBallPassesInFrontOfMark, matched.outcome,
					predict.phaseOfWheelWhenBallPassesInFrontOfMark);

			for (int j = 0; j < matchedRecordsList.size() - i; j++)
			{
				outcomeNumbersList.add(predictedOutcome);
			}
		}

		OutcomeStatistics stat = OutcomeStatistics.create(outcomeNumbersList);
		Logger.traceINFO("Statistics : " + stat);
		return stat.meanNumber;
	}
}
