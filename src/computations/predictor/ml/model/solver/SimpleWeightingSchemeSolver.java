package computations.predictor.ml.model.solver;

import java.util.ArrayList;
import java.util.List;

import computations.Constants;
import computations.predictor.OutcomeStatistics;
import computations.predictor.ml.model.DataRecord;
import computations.wheel.Wheel;
import servlets.SessionNotReadyException;

public class SimpleWeightingSchemeSolver implements OutcomeSolver
{
	// Simple Weighting Scheme
	public int predictOutcome(DataRecord predict) throws SessionNotReadyException
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
		return stat.meanNumber;
	}
}
