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

		/**
		 * Parameter that should be optimized. How many should we take.
		 */
		ballCumsumTimes = ballCumsumTimes.subList(0, ballCumsumTimes.size() - 4);
		wheelCumsumTimes = wheelCumsumTimes.subList(0, wheelCumsumTimes.size());

		double originTimeBall = Helper.head(ballCumsumTimes);
		ballCumsumTimes = Helper.normalize(ballCumsumTimes, originTimeBall);

		double originTimeWheel = Helper.head(wheelCumsumTimes);
		wheelCumsumTimes = Helper.normalize(wheelCumsumTimes, originTimeWheel);

		List<Double> ballDiffTimes = Helper.computeDiff(ballCumsumTimes);
		List<Double> wheelDiffTimes = Helper.computeDiff(wheelCumsumTimes);

		// LapTimeRegressionModel ballSpeedModel =
		// LapTimeRegressionModel.performLinearRegressionTimes(ballDiffTimes);
		LapTimeRegressionModel ballSpeedModel = LapTimeRegressionModel.performLinearRegressionTimes(ballDiffTimes);
		Logger.traceDEBUG("Ball Speed Model = " + ballSpeedModel);

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
			LapTimeRegressionModel wheelSpeedModel = LapTimeRegressionModel.performLinearRegressionTimes(wheelDiffTimes);
			Logger.traceDEBUG("Wheel Speed Model = " + ballSpeedModel);

			remainingDistance = HelperPhysics.estimateDistance(lastTimeBallPassesInFrontOfRef, lastTimeBallPassesInFrontOfRef + timeAtCutoffBall,
					Constants.get_WHEEL_CIRCUMFERENCE(), wheelSpeedModel);

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
		int initialPhase = Phase.findPhaseNumberBetweenBallAndWheel(lastTimeBallPassesInFrontOfRef, lastWheelLapTimeInFrontOfRef,
				wheelSpeedInFrontOfMark, Constants.DEFAULT_WHEEL_WAY);
		/**
		 * Shift depends on the speed of the wheel. High speed means more travel
		 * on average.
		 */
		int adjustedInitialPhase = (int) (Constants.DEFAULT_SHIFT_PHASE * lastKnownSpeedWheel);
		int finalPredictedShift = shiftPhaseBetweenInitialTimeAndCutOff + adjustedInitialPhase;
		Logger.traceDEBUG("Number of pockets (computed from angle) = " + shiftPhaseBetweenInitialTimeAndCutOff);
		Logger.traceDEBUG("DEFAULT_SHIFT_PHASE = " + adjustedInitialPhase);

		int predictedNumber = Wheel.getNumberWithPhase(initialPhase, finalPredictedShift, Constants.DEFAULT_WHEEL_WAY);
		Logger.traceDEBUG(
				"Initial phase was = " + initialPhase + ", Total shift = " + finalPredictedShift + ", Predicted number is = " + predictedNumber);
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
