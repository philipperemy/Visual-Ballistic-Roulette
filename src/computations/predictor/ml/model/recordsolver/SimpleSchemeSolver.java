package computations.predictor.ml.model.recordsolver;

import java.util.ArrayList;
import java.util.List;

import computations.Constants;
import computations.Wheel;
import computations.predictor.OutcomeStatistics;
import computations.predictor.ml.model.DataRecord;

public class SimpleSchemeSolver implements OutcomeSolver
{
	// Simple Scheme
	public int predictOutcome(DataRecord predict)
	{
		List<DataRecord> matchedRecordsList = DataRecord.matchCache(predict, Constants.NUMBER_OF_NEIGHBORS_KNN);
		List<Integer> outcomeNumbersList = new ArrayList<>();
		for (DataRecord matched : matchedRecordsList)
		{
			int predictedOutcome = Wheel.predictOutcomeWithShift(matched.phaseOfWheelWhenBallPassesInFrontOfMark, matched.outcome,
					predict.phaseOfWheelWhenBallPassesInFrontOfMark);
			outcomeNumbersList.add(predictedOutcome);
		}

		OutcomeStatistics stat = OutcomeStatistics.create(outcomeNumbersList);
		return stat.meanNumber;
	}
}
