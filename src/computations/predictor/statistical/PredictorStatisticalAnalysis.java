package computations.predictor.statistical;

import java.util.List;

import computations.Constants;
import computations.Wheel;
import computations.predictor.Phase;
import computations.predictor.Predictor;
import computations.predictor.physics.linearlaptimes.HelperPhysics;
import computations.utils.Helper;
import database.DatabaseAccessorInterface;
import utils.logger.Logger;

public class PredictorStatisticalAnalysis implements Predictor
{
	private StatisticalLapTimesModel manager = new StatisticalLapTimesModel();

	@Override
	public void init(DatabaseAccessorInterface da)
	{
		init(da, da.getSessionIds());
	}

	public int predict(List<Double> ballCumsumTimes, List<Double> wheelCumsumTimes)
	{
		double originTimeBall = Helper.head(ballCumsumTimes);
		ballCumsumTimes = Helper.normalize(ballCumsumTimes, originTimeBall);

		double originTimeWheel = Helper.head(wheelCumsumTimes);
		wheelCumsumTimes = Helper.normalize(wheelCumsumTimes, originTimeWheel);

		List<Double> ballDiffTimes = Helper.computeDiff(ballCumsumTimes);
		List<Double> wheelDiffTimes = Helper.computeDiff(wheelCumsumTimes);

		double constantWheelSpeed = Helper.getWheelSpeed(Helper.peek(wheelDiffTimes)); // trick

		Double remainingTime = manager.remainingTime(ballDiffTimes);

		double lastTimeBallPassesInFrontOfRef = Helper.peek(ballCumsumTimes);
		double remainingDistance = Helper.estimateDistanceConstantSpeed(lastTimeBallPassesInFrontOfRef, remainingTime, constantWheelSpeed);
		double wheelSpeedInFrontOfMark = constantWheelSpeed;
		double lastKnownSpeedWheel = constantWheelSpeed;

		Logger.traceDEBUG("Remaining distance computed = " + Helper.printDigit(remainingDistance) + " m");

		double angleAtCutOffTime = HelperPhysics.estimatePhaseAngleDegrees(remainingDistance, Constants.get_WHEEL_CIRCUMFERENCE());
		Logger.traceDEBUG("Angle of the wheel at cutoff time = " + Helper.printDigit(angleAtCutOffTime) + " degrees.");

		int shiftPhaseBetweenInitialTimeAndCutOff = HelperPhysics.estimateShiftWithAngle(angleAtCutOffTime);

		/**
		 * Comparing this value with the true value can be used to optimize the
		 * algorithm.
		 */
		double lastWheelLapTimeInFrontOfRef = Helper.getLastTimeWheelIsInFrontOfRef(wheelCumsumTimes, lastTimeBallPassesInFrontOfRef);
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
	public void init(DatabaseAccessorInterface da, List<String> sessionIds)
	{
		for (String sessionId : sessionIds)
		{
			List<Double> ballCumsumTimes = computations.utils.Helper.convertToSeconds(da.selectBallLapTimes(sessionId));

			if (ballCumsumTimes.isEmpty())
			{
				Logger.traceERROR("Ball lap times are empty for session id = " + sessionId + ". Ignoring this game.");
				continue;
			}
			List<Double> ballDiffTimesSeconds = computations.utils.Helper.computeDiff(ballCumsumTimes);
			manager.enrichModel(ballDiffTimesSeconds);
		}
	}

	@Override
	public void clear()
	{
		manager.free();
	}

}
