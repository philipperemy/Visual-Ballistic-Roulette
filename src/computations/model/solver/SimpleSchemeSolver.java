package computations.model.solver;

import java.util.ArrayList;
import java.util.List;

import computations.Constants;
import computations.model.DataRecord;
import computations.model.OutcomeStatistics;
import computations.wheel.Wheel;
import logger.Logger;

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
		Logger.traceINFO("Statistics : " + stat);
		return stat.meanNumber;
	}
}
