package computations.predictor.physics.linearlaptimes;

import java.util.List;

import computations.Constants;
import computations.Wheel;
import computations.predictor.Phase;
import computations.predictor.Predictor;
import computations.utils.Helper;
import database.DatabaseAccessorInterface;
import utils.exception.CriticalException;
import utils.exception.PositiveValueExpectedException;
import utils.logger.Logger;

public class PredictorPhysicsLinearLaptimes implements Predictor
{
	public int predict(List<Double> ballCumsumTimes, List<Double> wheelCumsumTimes)
	{
		double cutOffSpeed = Constants.CUTOFF_SPEED;

		double originTimeBall = Helper.head(ballCumsumTimes);
		ballCumsumTimes = Helper.normalize(ballCumsumTimes, originTimeBall);

		double originTimeWheel = Helper.head(wheelCumsumTimes);
		wheelCumsumTimes = Helper.normalize(wheelCumsumTimes, originTimeWheel);

		List<Double> ballDiffTimes = Helper.computeDiff(ballCumsumTimes);
		List<Double> wheelDiffTimes = Helper.computeDiff(wheelCumsumTimes);

		List<Double> rangeBall = Helper.range(1, ballDiffTimes.size());
		LapTimeRegressionModel ballSpeedModel = new LapTimeRegressionModel(Helper.performRegression(rangeBall, ballDiffTimes));
		Logger.traceDEBUG("Ball Speed Model = " + ballSpeedModel);

		// relative to normalize. Add originTimeBall to have the same time as
		// the one in the video.
		double timeAtCutoffBall = HelperPhysics.estimateTimeForSpeed(cutOffSpeed, Constants.get_BALL_CIRCUMFERENCE(), ballSpeedModel);
		Logger.traceDEBUG("Cutoff time = " + Helper.printDigit(timeAtCutoffBall) + " s, relative to t_BALL(0) for speed = " + cutOffSpeed + " m/s");

		double lastTimeBallPassesInFrontOfRef = Helper.peek(ballCumsumTimes);
		if (timeAtCutoffBall < lastTimeBallPassesInFrontOfRef + Constants.TIME_LEFT_FOR_PLACING_BETS_SECONDS)
		{
			throw new PositiveValueExpectedException();
		}

		Logger.traceDEBUG("Reference time of prediction = " + lastTimeBallPassesInFrontOfRef + " s");

		double lastWheelLapTimeInFrontOfRef = Helper.getLastTimeWheelIsInFrontOfRef(wheelCumsumTimes, lastTimeBallPassesInFrontOfRef);

		Double remainingDistance = null;
		Double wheelSpeedInFrontOfMark = null;
		Double lastKnownSpeedWheel = null;
		int wheelDiffTimesSize = wheelDiffTimes.size();
		if (wheelDiffTimesSize == 1)
		{
			double constantWheelSpeed = Helper.getWheelSpeed(0, wheelDiffTimes.get(0)); // trick
			remainingDistance = Helper.estimateDistanceConstantSpeed(lastTimeBallPassesInFrontOfRef,
					lastTimeBallPassesInFrontOfRef + timeAtCutoffBall, constantWheelSpeed);
			wheelSpeedInFrontOfMark = constantWheelSpeed;
			lastKnownSpeedWheel = constantWheelSpeed;

		} else if (wheelDiffTimesSize > 1)
		{
			List<Double> rangeWheel = Helper.range(1, wheelDiffTimes.size());
			// ConstantDecelerationModel(regression.getSlope(),
			// regression.getIntercept(), type);
			LapTimeRegressionModel wheelSpeedModel = new LapTimeRegressionModel(Helper.performRegression(rangeWheel, wheelDiffTimes));
			Logger.traceDEBUG("Wheel Speed Model = " + ballSpeedModel);

			// maybe partEnt() - remainingPhase.
			remainingDistance = HelperPhysics.estimateDistance(lastTimeBallPassesInFrontOfRef, timeAtCutoffBall, Constants.get_WHEEL_CIRCUMFERENCE(),
					wheelSpeedModel);

			wheelSpeedInFrontOfMark = HelperPhysics.estimateSpeed(lastWheelLapTimeInFrontOfRef, Constants.get_WHEEL_CIRCUMFERENCE(), wheelSpeedModel); // approximation

			lastKnownSpeedWheel = HelperPhysics.estimateSpeed(lastTimeBallPassesInFrontOfRef + timeAtCutoffBall, Constants.get_WHEEL_CIRCUMFERENCE(),
					wheelSpeedModel); // approximation
		} else
		{
			throw new CriticalException("Invalid number of wheelDiffTimes.");
		}

		Logger.traceDEBUG("Remaining distance computed = " + Helper.printDigit(remainingDistance) + " m");

		double angleAtCutOffTime = HelperPhysics.estimatePhaseAngleDegrees(remainingDistance, Constants.get_WHEEL_CIRCUMFERENCE());
		Logger.traceDEBUG("Angle of the wheel at cutoff time = " + Helper.printDigit(angleAtCutOffTime) + " degrees.");

		int shiftPhaseBetweenInitialTimeAndCutOff = HelperPhysics.estimateShiftWithAngle(angleAtCutOffTime);

		/**
		 * Comparing this value with the true value can be used to optimize the
		 * algorithm.
		 */
		double diffOrigin = originTimeBall - originTimeWheel;
		int initialPhase = Phase.findPhaseNumberBetweenBallAndWheel(lastTimeBallPassesInFrontOfRef, lastWheelLapTimeInFrontOfRef - diffOrigin,
				wheelSpeedInFrontOfMark, Constants.DEFAULT_WHEEL_WAY);
		/**
		 * Shift depends on the speed of the wheel. High speed means more travel
		 * on average.
		 */
		int numberAtCutoff = Wheel.getNumberWithPhase(initialPhase, shiftPhaseBetweenInitialTimeAndCutOff, computations.Wheel.WheelWay.ANTICLOCKWISE);

		// Maybe change a bit Constants.get_BALL_CIRCUMFERENCE().
		// HelperPhysics.estimateDistance(lastTimeBallPassesInFrontOfRef,
		// timeAtCutoffBall, Constants.get_BALL_CIRCUMFERENCE(),
		// ballSpeedModel)/Constants.get_BALL_CIRCUMFERENCE()
		//
		// double distBall = Constants.get_BALL_CIRCUMFERENCE()
		// - Constants.get_BALL_CIRCUMFERENCE() *
		// (HelperPhysics.estimateDistance(lastTimeBallPassesInFrontOfRef,
		// timeAtCutoffBall,
		// Constants.get_BALL_CIRCUMFERENCE(), ballSpeedModel) /
		// Constants.get_BALL_CIRCUMFERENCE() % 1);
		// int phaseAtCutOff = (int) (distBall /
		// Constants.get_WHEEL_CIRCUMFERENCE() * Wheel.NUMBERS.length);

		// int numberAtCutoff = Wheel.getNumberWithPhase(numberAtCutoff,
		// phaseAtCutOff, computations.Wheel.WheelWay.CLOCKWISE);

		int adjustedInitialPhase = (int) (Constants.DEFAULT_SHIFT_PHASE * lastKnownSpeedWheel);
		Logger.traceDEBUG("Number of pockets (computed from angle) = " + shiftPhaseBetweenInitialTimeAndCutOff);
		Logger.traceDEBUG("DEFAULT_SHIFT_PHASE = " + adjustedInitialPhase);

		int predictedNumber = Wheel.getNumberWithPhase(numberAtCutoff, adjustedInitialPhase, Constants.DEFAULT_WHEEL_WAY);
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

}
