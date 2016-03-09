package computations.predictor.ml.model.recordsolver;

import java.util.ArrayList;
import java.util.List;

import computations.Constants;
import computations.predictor.OutcomeStatistics;
import computations.predictor.ml.model.DataRecord;
import utils.logger.Logger;

/**
 * For one particular data record (ball speed, wheel speed, phase), we select
 * from the database the records that are the closest. Each one is equally
 * weighted. Then we investigate the impact of the number of records we have
 * selected previously. We average the simple scheme for different KNN numbers.
 */
public class ComplexWeightingSchemeSolver implements OutcomeSolver
{
	public int predictOutcome(DataRecord predict)
	{
		// Simple Scheme
		List<Integer> outcomeNumbersList = outcomesFromKNNAlgorithm(predict);

		// Do that in order to have a stable KNN. What is the behavior of my
		// result if I change some parameters to the model?
		List<Integer> outcomeMeanNumbersList = new ArrayList<>();
		for (int knnNumber = 1; knnNumber <= Constants.NUMBER_OF_NEIGHBORS_KNN; knnNumber++)
		{
			OutcomeStatistics stat = OutcomeStatistics.create(outcomeNumbersList.subList(0, knnNumber));
			Logger.traceDEBUG("Statistics : " + stat);
			outcomeMeanNumbersList.add(stat.meanNumber);
		}

		OutcomeStatistics outcomeStatistics = OutcomeStatistics.create(outcomeMeanNumbersList);
		return outcomeStatistics.meanNumber;
	}
}
