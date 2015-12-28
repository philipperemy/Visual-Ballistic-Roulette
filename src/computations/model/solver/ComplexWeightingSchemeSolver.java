package computations.model.solver;

import java.util.ArrayList;
import java.util.List;

import computations.Constants;
import computations.model.DataRecord;
import computations.model.OutcomeStatistics;
import computations.wheel.Wheel;
import logger.Logger;

public class ComplexWeightingSchemeSolver implements OutcomeSolver
{
	// Complex Weighting Aggregation Scheme
	public int predictOutcome(DataRecord predict)
	{
		List<DataRecord> matchedRecordsList = DataRecord.matchCache(predict, Constants.NUMBER_OF_NEIGHBORS_KNN);
		List<Integer> outcomeNumbersList = new ArrayList<>();
		for (DataRecord matchedRecord : matchedRecordsList)
		{
			int predictedOutcome = Wheel.predictOutcomeWithShift(matchedRecord.phaseOfWheelWhenBallPassesInFrontOfMark, matchedRecord.outcome,
					predict.phaseOfWheelWhenBallPassesInFrontOfMark);
			outcomeNumbersList.add(predictedOutcome);
		}

		// Do that in order to have a stable KNN. What is the behavior of my
		// result if I change some parameters to the model?
		List<Integer> outcomeMeanNumbersList = new ArrayList<>();
		for (int knnNumber = 1; knnNumber <= Constants.NUMBER_OF_NEIGHBORS_KNN; knnNumber++)
		{
			OutcomeStatistics stat = OutcomeStatistics.create(outcomeNumbersList.subList(0, knnNumber));
			Logger.traceINFO("Statistics : " + stat);
			outcomeMeanNumbersList.add(stat.meanNumber);
		}

		// Give more credit to the KNN(2), KNN(3)... as they appear more.
		OutcomeStatistics outcomeStatistics = OutcomeStatistics.create(outcomeMeanNumbersList);
		return outcomeStatistics.meanNumber;
	}
}
