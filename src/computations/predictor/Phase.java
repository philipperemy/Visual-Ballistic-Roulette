package computations.predictor;

import java.util.List;

import computations.wheel.Wheel;
import computations.wheel.Wheel.WheelWay;

public class Phase {

	/**
	 * Should find out if ball considered before is better or not but stay
	 * coherent across the data set.
	 */
	public static double getNextTimeBallIsInFrontOfRef(List<Double> ballLapTimes, double wheelLapTimeInFrontOfRef) {
		for (Double ballTimeInFrontOfRef : ballLapTimes) {
			if (ballTimeInFrontOfRef > wheelLapTimeInFrontOfRef) {
				return ballTimeInFrontOfRef;
			}
		}
		throw new RuntimeException("getNextTimeBallIsInFrontOfRef()");
	}

	@Deprecated
	public static int findNumberInFrontOfRefWhenBallIsLaunched(double timeOfLastPassageOfZeroInFrontOfRef,
			double timeWhenBallWasLaunched, double lastWheelSpeed, WheelWay way) {
		double diffTime = timeWhenBallWasLaunched - timeOfLastPassageOfZeroInFrontOfRef;
		double timeForOneWheelLoop = BallisticManager.getTimeForOneBallLoop(lastWheelSpeed);
		int numbersCount = Wheel.NUMBERS.length;
		int idxPhase = (int) (diffTime / timeForOneWheelLoop * numbersCount);
		int idxZero = Wheel.findIndexOfNumber(0); // Should be always 0
		int idx = 0;
		if (way == WheelWay.CLOCKWISE) {
			idx = idxZero - idxPhase;
		} else if (way == WheelWay.ANTICLOCKWISE) {
			idx = idxZero + idxPhase;
		} else {
			throw new RuntimeException();
		}
		return Wheel.NUMBERS[Wheel.getIndex(idx)];
	}

	// We want to find the number of the wheel where the ball passes in front of
	// the mark.
	public static int findPhaseNumberBetweenBallAndWheel(double timeOfBallInFrontOfMark,
			double timeOfWheelInFrontOfMark, double lastWheelSpeed, WheelWay way) {

		double diffTime = Math.abs(timeOfBallInFrontOfMark - timeOfWheelInFrontOfMark);
		double timeForOneWheelLoop = BallisticManager.getTimeForOneWheelLoop(lastWheelSpeed);
		int numbersCount = Wheel.NUMBERS.length;
		int idxPhase = (int) (diffTime / timeForOneWheelLoop * numbersCount);
		int idxZero = Wheel.findIndexOfNumber(0); // Should be always 0
		int idx = 0;

		if (timeOfBallInFrontOfMark > timeOfWheelInFrontOfMark) {
			// t(Wheel) < t(Ball) Wheel is ahead of phase
			// The question is: what is the value of the wheel when the ball
			// is in front of the mark?

			if (way == WheelWay.CLOCKWISE) {
				idx = idxZero - idxPhase;
			} else if (way == WheelWay.ANTICLOCKWISE) {
				idx = idxZero + idxPhase;
			} else {
				throw new RuntimeException();
			}

		} else {
			// t(Wheel) > t(Ball) Ball is ahead of phase
			// The question is what is the value of the ball when the wheel
			// is in front of the wheel?

			if (way == WheelWay.CLOCKWISE) {
				idx = idxZero + idxPhase;
			} else if (way == WheelWay.ANTICLOCKWISE) {
				idx = idxZero - idxPhase;
			} else {
				throw new RuntimeException();
			}
		}
		return Wheel.NUMBERS[Wheel.getIndex(idx)];
	}
}
