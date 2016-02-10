package computations.predictor.ml.solver;

import java.util.ArrayList;
import java.util.List;

import computations.predictor.OutcomeStatistics;
import computations.predictor.ml.PredictorMachineLearning;
import computations.predictor.ml.model.DataRecord;
import utils.logger.Logger;

public class AllKnownMeasuresSolver implements PredictorSolver
{
	public int predict(PredictorMachineLearning predictor, List<Double> ballLapTimes, List<Double> wheelLapTimes)
	{
		List<DataRecord> predictRecords = getPredictedRecords(predictor, ballLapTimes, wheelLapTimes);

		List<Integer> mostProbableNumberList = new ArrayList<>();
		int id = 1;
		for (DataRecord predictRecord : predictRecords)
		{
			Logger.traceDEBUG("(" + id + ") Record to predict : " + predictRecord);
			int mostProbableNumber = DataRecord.predictOutcome(predictRecord);
			Logger.traceDEBUG("(" + id + ") Most probable number : " + mostProbableNumber);
			mostProbableNumberList.add(mostProbableNumber);
			id++;
		}

		OutcomeStatistics os = OutcomeStatistics.create(mostProbableNumberList);
		int mostProbableFinalNumber = os.meanNumber;
		Logger.traceDEBUG("(final) Most probable number : " + mostProbableFinalNumber);
		return os.meanNumber;
	}
}
