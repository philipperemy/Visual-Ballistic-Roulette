package computations.predictor.physics.constantdeceleration;

import java.util.List;

import computations.Constants;
import computations.Wheel;
import computations.predictor.Phase;
import computations.predictor.Predictor;
import computations.utils.Helper;
import database.DatabaseAccessorInterface;
import utils.exception.PositiveValueExpectedException;
import utils.logger.Logger;

public class PredictorPhysicsConstantDeceleration implements Predictor
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

		double constantWheelSpeed = Helper.getWheelSpeed(Helper.peek(wheelDiffTimes));
		ConstantDecelerationModel ballModel = RegressionManager.computeModel(ballDiffTimes);

		double timeAtCutoffBall = lastTimeBallPassesInFrontOfRef + RegressionManager.estimateTime(ballModel, ballDiffTimes.size(), cutoffSpeed);

		if (timeAtCutoffBall < lastTimeBallPassesInFrontOfRef + Constants.TIME_LEFT_FOR_PLACING_BETS_SECONDS)
		{
			throw new PositiveValueExpectedException();
		}

		double lastWheelLapTimeInFrontOfRef = Helper.getLastTimeWheelIsInFrontOfRef(wheelCumsumTimes, lastTimeBallPassesInFrontOfRef);

		double wheelSpeedInFrontOfMark = constantWheelSpeed;
		double lastKnownSpeedWheel = constantWheelSpeed;

		int initialPhase = Phase.findPhaseNumberBetweenBallAndWheel(lastTimeBallPassesInFrontOfRef, lastWheelLapTimeInFrontOfRef - diffOrigin,
				wheelSpeedInFrontOfMark, Constants.DEFAULT_WHEEL_WAY);

		int shiftPhaseBetweenInitialTimeAndCutOff = (int) (((timeAtCutoffBall - lastTimeBallPassesInFrontOfRef) / Helper.peek(wheelDiffTimes) % 1)
				* Wheel.NUMBERS.length);

		double numberOfRevolutionsLeftBall = RegressionManager.estimateRevolutionCountLeft(ballModel, ballDiffTimes.size(), cutoffSpeed);
		int phaseAtCutOff = (int) (numberOfRevolutionsLeftBall * Wheel.NUMBERS.length);

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
