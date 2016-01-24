package computations.predictor.physics;

import java.util.List;

import computations.Constants;
import computations.Helper;
import computations.predictor.Phase;
import computations.wheel.Wheel;
import logger.Logger;

public class PredictorPhysics
{

	private static volatile PredictorPhysics instance = null;

	public static PredictorPhysics getInstance()
	{
		if (instance == null)
		{
			instance = new PredictorPhysics();
		}
		return instance;
	}

	public int predict(List<Double> ballCumsumTimes, List<Double> wheelCumsumTimes) throws PositiveValueExpectedException
	{
		double cutOffSpeed = Constants.CUTOFF_SPEED;

		double originTimeBall = Helper.head(ballCumsumTimes);
		ballCumsumTimes = Helper.normalize(ballCumsumTimes, originTimeBall);

		double originTimeWheel = Helper.head(wheelCumsumTimes);
		wheelCumsumTimes = Helper.normalize(wheelCumsumTimes, originTimeWheel);

		List<Double> ballDiffTimes = Helper.computeDiff(ballCumsumTimes);
		List<Double> wheelDiffTimes = Helper.computeDiff(wheelCumsumTimes);
		
		LapTimeRegressionModel ballSpeedModel = LapTimeRegressionModel.performLinearRegressionTimes(ballDiffTimes);
		Logger.traceDEBUG("Ball Speed Model = " + ballSpeedModel);

		double timeAtCutoffBall = HelperPhysics.estimateTimeForSpeed(cutOffSpeed, Constants.get_BALL_CIRCUMFERENCE(), ballSpeedModel);
		Logger.traceDEBUG("Cutoff time = " + Helper.printDigit(timeAtCutoffBall) + " s, relative to t_BALL(0) for speed = " + cutOffSpeed + " m/s");

		double lastTimeBallPassesInFrontOfRef = Helper.peek(ballCumsumTimes);
		Logger.traceDEBUG("Reference time of prediction = " + lastTimeBallPassesInFrontOfRef + " s");

		double lastWheelLapTimeInFrontOfRef = Helper.getLastTimeWheelIsInFrontOfRef(wheelCumsumTimes, lastTimeBallPassesInFrontOfRef);

		LapTimeRegressionModel wheelSpeedModel = LapTimeRegressionModel.performLinearRegressionTimes(wheelDiffTimes);
		Logger.traceDEBUG("Wheel Speed Model = " + ballSpeedModel);
		
		double wheelSpeedInFrontOfMark = HelperPhysics.estimateSpeed(lastWheelLapTimeInFrontOfRef, Constants.get_WHEEL_CIRCUMFERENCE(), wheelSpeedModel); // approximation

		int initialPhase = Phase.findPhaseNumberBetweenBallAndWheel(lastTimeBallPassesInFrontOfRef, lastWheelLapTimeInFrontOfRef,
				wheelSpeedInFrontOfMark, Constants.DEFAULT_WHEEL_WAY);

		double remainingDistance = HelperPhysics.estimateDistance(lastTimeBallPassesInFrontOfRef, lastTimeBallPassesInFrontOfRef + timeAtCutoffBall,
				Constants.get_WHEEL_CIRCUMFERENCE(), wheelSpeedModel);
		Logger.traceDEBUG("Remaining distance computed = " + Helper.printDigit(remainingDistance) + " m");

		double angleAtCutOffTime = HelperPhysics.estimatePhaseAngleDegrees(remainingDistance, Constants.get_WHEEL_CIRCUMFERENCE());
		Logger.traceDEBUG("Angle of the wheel at cutoff time = " + Helper.printDigit(angleAtCutOffTime) + " degrees.");

		int shiftPhaseBetweenInitialTimeAndCutOff = HelperPhysics.estimateShiftWithAngle(angleAtCutOffTime);

		/**
		 * Make phase being different regarding to the speed of the wheel.
		 */
		double lastKnownSpeedWheel = HelperPhysics.estimateSpeed(lastTimeBallPassesInFrontOfRef + timeAtCutoffBall, Constants.get_WHEEL_CIRCUMFERENCE(), wheelSpeedModel); // approximation
		int adjustedInitialPhase = (int) (Constants.DEFAULT_SHIFT_PHASE * lastKnownSpeedWheel);
		int finalPredictedShift = shiftPhaseBetweenInitialTimeAndCutOff + adjustedInitialPhase;
		Logger.traceDEBUG("Number of pockets (computed from angle) = " + shiftPhaseBetweenInitialTimeAndCutOff);
		Logger.traceDEBUG("DEFAULT_SHIFT_PHASE = " + adjustedInitialPhase);

		int predictedNumber = Wheel.getNumberWithPhase(initialPhase, finalPredictedShift, Constants.DEFAULT_WHEEL_WAY);
		Logger.traceDEBUG(
				"Initial phase was = " + initialPhase + ", Total shift = " + finalPredictedShift + ", Predicted number is = " + predictedNumber);
		return predictedNumber;
	}
}
