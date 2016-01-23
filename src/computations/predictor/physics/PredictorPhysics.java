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
		LapTimeRegressionModel ballSpeedModel = LapTimeRegressionModel.performLinearRegressionTimes(ballDiffTimes);
		Logger.traceINFO("Speed Model = " + ballSpeedModel);

		double timeAtCutoffBall = HelperPhysics.estimateTimeForSpeed(cutOffSpeed, Constants.BALL_CIRCUMFERENCE, ballSpeedModel);
		Logger.traceINFO("Cutoff time = " + Helper.printDigit(timeAtCutoffBall) + " s, relative to t_BALL(0) for speed = " + cutOffSpeed + " m/s");

		double lastTimeBallPassesInFrontOfRef = Helper.peek(ballCumsumTimes);
		Logger.traceINFO("Reference time of prediction = " + lastTimeBallPassesInFrontOfRef + " s");

		double lastWheelLapTimeInFrontOfRef = Helper.getLastTimeWheelIsInFrontOfRef(wheelCumsumTimes, lastTimeBallPassesInFrontOfRef);

		LapTimeRegressionModel wheelSpeedModel = LapTimeRegressionModel.performLinearRegressionTimes(wheelCumsumTimes);
		double wheelSpeedInFrontOfMark = HelperPhysics.estimateSpeed(lastWheelLapTimeInFrontOfRef, Constants.WHEEL_CIRCUMFERENCE, wheelSpeedModel); // approximation

		int initialPhase = Phase.findPhaseNumberBetweenBallAndWheel(lastTimeBallPassesInFrontOfRef, lastWheelLapTimeInFrontOfRef,
				wheelSpeedInFrontOfMark, Constants.DEFAULT_WHEEL_WAY);

		double remainingDistance = HelperPhysics.estimateDistance(lastTimeBallPassesInFrontOfRef, lastTimeBallPassesInFrontOfRef + timeAtCutoffBall,
				Constants.WHEEL_CIRCUMFERENCE, wheelSpeedModel);
		Logger.traceINFO("Remaining distance computed = " + Helper.printDigit(remainingDistance) + " m");

		double angleAtCutOffTime = HelperPhysics.estimatePhaseAngleDegrees(remainingDistance, Constants.WHEEL_CIRCUMFERENCE);
		Logger.traceINFO("Angle of the wheel at cutoff time = " + Helper.printDigit(angleAtCutOffTime) + " degrees.");

		int shiftPhaseBetweenInitialTimeAndCutOff = HelperPhysics.estimateShiftWithAngle(angleAtCutOffTime);

		int finalPredictedShift = shiftPhaseBetweenInitialTimeAndCutOff + Constants.DEFAULT_SHIFT_PHASE;
		Logger.traceINFO("Number of pockets (computed from angle) = " + shiftPhaseBetweenInitialTimeAndCutOff);
		Logger.traceINFO("DEFAULT_SHIFT_PHASE = " + Constants.DEFAULT_SHIFT_PHASE);

		int predictedNumber = Wheel.getNumberWithPhase(initialPhase, finalPredictedShift, Constants.DEFAULT_WHEEL_WAY);
		Logger.traceINFO(
				"Initial phase was = " + initialPhase + ", Total shift = " + finalPredictedShift + ", Predicted number is = " + predictedNumber);
		return predictedNumber;
	}
}
