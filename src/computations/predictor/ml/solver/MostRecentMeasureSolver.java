package computations.predictor.ml.solver;

import java.util.List;

import computations.predictor.ml.PredictorMachineLearning;
import computations.predictor.ml.model.DataRecord;
import computations.utils.Helper;
import utils.logger.Logger;

public class MostRecentMeasureSolver implements PredictorSolver
{
	public int predict(PredictorMachineLearning predictor, List<Double> ballLapTimes, List<Double> wheelLapTimes)
	{
		List<DataRecord> predictRecords = getPredictedRecords(predictor, ballLapTimes, wheelLapTimes);

		// Consider only the most recent record we know up to now.
		DataRecord predictRecord = Helper.peek(predictRecords);
		Logger.traceDEBUG("Record to predict : " + predictRecord);

		int mostProbableNumber = DataRecord.predictOutcome(predictRecord);
		Logger.traceDEBUG("Most probable number : " + mostProbableNumber);
		return mostProbableNumber;
	}
}
