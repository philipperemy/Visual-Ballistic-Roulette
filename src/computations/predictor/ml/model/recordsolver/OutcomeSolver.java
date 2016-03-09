package computations.predictor.ml.model.recordsolver;

import java.util.ArrayList;
import java.util.List;

import computations.Constants;
import computations.Wheel;
import computations.predictor.ml.model.DataRecord;

/**
 * A data record corresponds to a <ball speed, wheel speed, phase> We retrieve
 * from the database all the similar games and we predict the outcome.
 *
 */
public interface OutcomeSolver
{
	public int predictOutcome(DataRecord predict);

	default List<Integer> outcomesFromKNNAlgorithm(DataRecord predict)
	{
		// Simple Scheme
		List<DataRecord> matchedRecordsList = DataRecord.matchCache(predict, Constants.NUMBER_OF_NEIGHBORS_KNN);
		List<Integer> outcomeNumbersList = new ArrayList<>();
		for (DataRecord matchedRecord : matchedRecordsList)
		{
			int predictedOutcome = Wheel.predictOutcomeWithShift(matchedRecord.phaseOfWheelWhenBallPassesInFrontOfMark, matchedRecord.outcome,
					predict.phaseOfWheelWhenBallPassesInFrontOfMark);
			outcomeNumbersList.add(predictedOutcome);
		}
		return outcomeNumbersList;
	}
}
