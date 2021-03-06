package computations.predictor.ml.model.recordsolver;

import java.util.ArrayList;
import java.util.List;

import computations.Constants;
import computations.Wheel;
import computations.predictor.OutcomeStatistics;
import computations.predictor.ml.model.DataRecord;

/**
 * For one particular data record (ball speed, wheel speed, phase), we select
 * from the database the records that are the closest. Each one is weighted
 * according to its distance. A lower distance means a stronger weight. The
 * final result is just a linear combination of the selected weighted games.
 */
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
		return stat.meanNumber;
	}
}
