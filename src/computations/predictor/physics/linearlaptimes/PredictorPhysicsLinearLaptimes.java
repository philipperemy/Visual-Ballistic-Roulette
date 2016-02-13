package computations.predictor.physics.linearlaptimes;

import java.util.List;

import computations.Constants;
import computations.Wheel;
import computations.predictor.Phase;
import computations.predictor.Predictor;
import computations.utils.Helper;
import database.DatabaseAccessorInterface;
import utils.exception.PositiveValueExpectedException;
import utils.logger.Logger;

public class PredictorPhysicsLinearLaptimes implements Predictor
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

		List<Double> ballDiffTimes = Helper.computeDiff(ballCumsumTimes);
		List<Double> wheelDiffTimes = Helper.computeDiff(wheelCumsumTimes);

		List<Double> rangeBall = Helper.range(1, ballDiffTimes.size());
		LapTimeRegressionModel ballSpeedModel = new LapTimeRegressionModel(Helper.performRegression(rangeBall, ballDiffTimes));
		Logger.traceDEBUG("Ball Speed Model = " + ballSpeedModel);

		// relative to normalize. Add originTimeBall to have the same time as
		// the one in the video.
		double timeAtCutoffBall = HelperPhysics.estimateTimeForSpeed(cutoffSpeed, Constants.get_BALL_CIRCUMFERENCE(), ballSpeedModel);
		Logger.traceDEBUG("Cutoff time = " + Helper.printDigit(timeAtCutoffBall) + " s, relative to t_BALL(0) for speed = " + cutoffSpeed + " m/s");

		if (timeAtCutoffBall < lastTimeBallPassesInFrontOfRef + Constants.TIME_LEFT_FOR_PLACING_BETS_SECONDS)
		{
			throw new PositiveValueExpectedException();
		}

		Logger.traceDEBUG("Reference time of prediction = " + lastTimeBallPassesInFrontOfRef + " s");

		double lastWheelLapTimeInFrontOfRef = Helper.getLastTimeWheelIsInFrontOfRef(wheelCumsumTimes, lastTimeBallPassesInFrontOfRef);

		double constantWheelSpeed = Helper.getWheelSpeed(Helper.peek(wheelDiffTimes)); // trick
		double wheelSpeedInFrontOfMark = constantWheelSpeed;
		double lastKnownSpeedWheel = constantWheelSpeed;

		int initialPhase = Phase.findPhaseNumberBetweenBallAndWheel(lastTimeBallPassesInFrontOfRef, lastWheelLapTimeInFrontOfRef - diffOrigin,
				wheelSpeedInFrontOfMark, Constants.DEFAULT_WHEEL_WAY);

		int shiftPhaseBetweenInitialTimeAndCutOff = (int) (((timeAtCutoffBall - lastTimeBallPassesInFrontOfRef) / Helper.peek(wheelDiffTimes) % 1)
				* Wheel.NUMBERS.length);

		double distBall = HelperPhysics.estimateDistance(lastTimeBallPassesInFrontOfRef, timeAtCutoffBall, Constants.get_BALL_CIRCUMFERENCE(),
				ballSpeedModel) / Constants.get_BALL_CIRCUMFERENCE() % 1;
		int phaseAtCutOff = (int) (distBall * Wheel.NUMBERS.length);

		int numberBelowBallAtCutoff = Wheel.getNumberWithPhase(initialPhase, shiftPhaseBetweenInitialTimeAndCutOff + phaseAtCutOff,
				Constants.DEFAULT_WHEEL_WAY);

		int adjustedInitialPhase = (int) (Constants.DEFAULT_SHIFT_PHASE * lastKnownSpeedWheel);
		Logger.traceDEBUG("Number of pockets (computed from angle) = " + shiftPhaseBetweenInitialTimeAndCutOff);
		Logger.traceDEBUG("DEFAULT_SHIFT_PHASE = " + adjustedInitialPhase);

		int predictedNumber = Wheel.getNumberWithPhase(numberBelowBallAtCutoff, adjustedInitialPhase, Constants.DEFAULT_WHEEL_WAY);
		Logger.traceDEBUG("Predicted number is = " + predictedNumber);
		return predictedNumber;
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
