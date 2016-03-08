package computations.predictor.physics.linearlaptimes;

import java.util.List;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import computations.Constants;
import computations.Wheel;
import computations.predictor.physics.PredictorPhysics;
import computations.utils.Helper;
import database.DatabaseAccessorInterface;
import utils.logger.Logger;

public class PredictorPhysicsLinearLaptimes extends PredictorPhysics
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

		List<Double> rangeBall = Helper.range(1, ballDiffTimes.size());
		SimpleRegression ballSpeedModel = Helper.performRegression(rangeBall, ballDiffTimes);
		Logger.traceDEBUG("Ball Speed Model = " + ballSpeedModel);

		// relative to normalize. Add originTimeBall to have the same time as
		// the one in the video.
		double timeAtCutoffBall = HelperLinearLapTimes.estimateTimeForSpeed(cutoffSpeed, Constants.get_BALL_CIRCUMFERENCE(), ballSpeedModel);
		Logger.traceDEBUG("Cutoff time = " + Helper.printDigit(timeAtCutoffBall) + " s, relative to t_BALL(0) for speed = " + cutoffSpeed + " m/s");

		double distBall = HelperLinearLapTimes.estimateDistance(lastTimeBallPassesInFrontOfRef, timeAtCutoffBall, Constants.get_BALL_CIRCUMFERENCE(),
				ballSpeedModel) / Constants.get_BALL_CIRCUMFERENCE() % 1;
		int phaseAtCutOff = (int) (distBall * Wheel.NUMBERS.length);

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

/*
 * else if (wheelDiffTimesSize > 1) { List<Double> rangeWheel = Helper.range(1,
 * wheelDiffTimes.size()); // ConstantDecelerationModel(regression.getSlope(),
 * // regression.getIntercept(), type); LapTimeRegressionModel wheelSpeedModel =
 * new LapTimeRegressionModel(Helper.performRegression(rangeWheel,
 * wheelDiffTimes)); Logger.traceDEBUG("Wheel Speed Model = " + ballSpeedModel);
 * wheelSpeedInFrontOfMark =
 * HelperPhysics.estimateSpeed(lastWheelLapTimeInFrontOfRef - diffOrigin,
 * Constants.get_WHEEL_CIRCUMFERENCE(), wheelSpeedModel); // approximation
 * lastKnownSpeedWheel = HelperPhysics.estimateSpeed(timeAtCutoffBall -
 * diffOrigin, Constants.get_WHEEL_CIRCUMFERENCE(), wheelSpeedModel); //
 * approximation }
 */
