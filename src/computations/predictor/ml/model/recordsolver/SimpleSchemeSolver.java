package computations.predictor.ml.model.recordsolver;

import java.util.List;

import computations.predictor.OutcomeStatistics;
import computations.predictor.ml.model.DataRecord;

/**
 * For one particular data record (ball speed, wheel speed, phase), we select
 * from the database the records that are the closest. Each one is EQUALLY
 * weighted. The final result is just a linear combination of the selected
 * games.
 */
public class SimpleSchemeSolver implements OutcomeSolver
{
	public int predictOutcome(DataRecord predict)
	{
		List<Integer> outcomeNumbersList = outcomesFromKNNAlgorithm(predict);
		OutcomeStatistics stat = OutcomeStatistics.create(outcomeNumbersList);
		return stat.meanNumber;
	}
}
