package computations.predictor.ml.solver;

import java.util.List;

import computations.predictor.ml.PredictorMachineLearning;
import computations.predictor.ml.model.DataRecord;
import computations.utils.Helper;
import logger.Logger;

public class MostRecentMeasureSolver implements PredictorSolver
{
	public int predict(PredictorMachineLearning predictor, List<Double> ballLapTimes, List<Double> wheelLapTimes)
	{
		List<DataRecord> predictRecords = getPredictedRecords(predictor, ballLapTimes, wheelLapTimes);

		DataRecord predictRecord = Helper.peek(predictRecords);
		Logger.traceINFO("Record to predict : " + predictRecord);

		int mostProbableNumber = DataRecord.predictOutcome(predictRecord);
		Logger.traceINFO("Most probable number : " + mostProbableNumber);
		return mostProbableNumber;
	}
}
