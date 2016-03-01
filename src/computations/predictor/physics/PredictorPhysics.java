package computations.predictor.physics;

import java.util.List;

import computations.Constants;
import computations.Wheel;
import computations.predictor.Phase;
import computations.predictor.Predictor;
import computations.utils.Helper;
import utils.exception.PositiveValueExpectedException;
import utils.logger.Logger;

public abstract class PredictorPhysics implements Predictor
{
	// Used as an auxiliary function.
	protected static int predict(List<Double> wheelCumsumTimes, double diffOrigin, double lastTimeBallPassesInFrontOfRef, List<Double> wheelDiffTimes,
			int phaseAtCutOff, double timeAtCutoffBall)
	{
		if (timeAtCutoffBall < lastTimeBallPassesInFrontOfRef + Constants.TIME_LEFT_FOR_PLACING_BETS_SECONDS)
		{
			throw new PositiveValueExpectedException();
		}

		double lastWheelLapTimeInFrontOfRef = Helper.getLastTimeWheelIsInFrontOfRef(wheelCumsumTimes, lastTimeBallPassesInFrontOfRef);

		double constantWheelSpeed = Helper.getWheelSpeed(Helper.peek(wheelDiffTimes));
		double wheelSpeedInFrontOfMark = constantWheelSpeed;
		double lastKnownSpeedWheel = constantWheelSpeed;

		int initialPhase = Phase.findPhaseNumberBetweenBallAndWheel(lastTimeBallPassesInFrontOfRef, lastWheelLapTimeInFrontOfRef - diffOrigin,
				wheelSpeedInFrontOfMark, Constants.DEFAULT_WHEEL_WAY);

		int shiftPhaseBetweenInitialTimeAndCutOff = (int) (((timeAtCutoffBall - lastTimeBallPassesInFrontOfRef) / Helper.peek(wheelDiffTimes) % 1)
				* Wheel.NUMBERS.length);

		int numberBelowBallAtCutoff = Wheel.getNumberWithPhase(initialPhase, shiftPhaseBetweenInitialTimeAndCutOff + phaseAtCutOff,
				Constants.DEFAULT_WHEEL_WAY);

		int adjustedInitialPhase = (int) (Constants.DEFAULT_SHIFT_PHASE * lastKnownSpeedWheel);
		Logger.traceDEBUG("Number of pockets (computed from angle) = " + shiftPhaseBetweenInitialTimeAndCutOff);
		Logger.traceDEBUG("DEFAULT_SHIFT_PHASE = " + adjustedInitialPhase);

		int predictedNumber = Wheel.getNumberWithPhase(numberBelowBallAtCutoff, adjustedInitialPhase, Constants.DEFAULT_WHEEL_WAY);
		Logger.traceDEBUG("Predicted number is = " + predictedNumber);
		return predictedNumber;
	}

}
