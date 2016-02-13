package computations.predictor.physics.constantdeceleration;

import java.util.List;

import computations.Constants;
import computations.Wheel;
import computations.predictor.physics.PredictorPhysics;
import computations.utils.Helper;
import database.DatabaseAccessorInterface;
import utils.logger.Logger;

public class PredictorPhysicsConstantDeceleration extends PredictorPhysics
{
	public int predict(List<Double> ballCumsumTimes, List<Double> wheelCumsumTimes)
	{
		double cutoffSpeed = Constants.CUTOFF_SPEED;

		double originTimeBall = Helper.head(ballCumsumTimes);
		ballCumsumTimes = Helper.normalize(ballCumsumTimes, originTimeBall);

		double originTimeWheel = Helper.head(wheelCumsumTimes);
		wheelCumsumTimes = Helper.normalize(wheelCumsumTimes, originTimeWheel);

		double diffOrigin = originTimeBall - originTimeWheel;
		double lastTimeBallPassesInFrontOfRef = Helper.peek(ballCumsumTimes);
		Logger.traceDEBUG("Reference time of prediction = " + lastTimeBallPassesInFrontOfRef + " s");

		List<Double> ballDiffTimes = Helper.computeDiff(ballCumsumTimes);
		List<Double> wheelDiffTimes = Helper.computeDiff(wheelCumsumTimes);

		ConstantDecelerationModel ballModel = RegressionManager.computeModel(ballDiffTimes);

		double numberOfRevolutionsLeftBall = RegressionManager.estimateRevolutionCountLeft(ballModel, ballDiffTimes.size(), cutoffSpeed);
		int phaseAtCutOff = (int) ((numberOfRevolutionsLeftBall % 1) * Wheel.NUMBERS.length);

		double timeAtCutoffBall = lastTimeBallPassesInFrontOfRef + RegressionManager.estimateTime(ballModel, ballDiffTimes.size(), cutoffSpeed);

		return predict(wheelCumsumTimes, diffOrigin, lastTimeBallPassesInFrontOfRef, wheelDiffTimes, phaseAtCutOff, timeAtCutoffBall);
	}

	@Override
	public void init(DatabaseAccessorInterface da)
	{
	}

	@Override
	public void init(DatabaseAccessorInterface da, List<String> sessionIds)
	{
	}

	@Override
	public void clear()
	{
	}
}
