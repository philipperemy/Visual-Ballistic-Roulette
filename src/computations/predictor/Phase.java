package computations.predictor;

import computations.utils.Helper;
import computations.wheel.Wheel;
import computations.wheel.Wheel.WheelWay;
import exceptions.CriticalException;

public class Phase
{
	// We want to find the number of the wheel where the ball passes in front of
	// the mark.
	public static int findPhaseNumberBetweenBallAndWheel(double timeOfBallInFrontOfMark, double timeOfWheelInFrontOfMark, double lastWheelSpeed,
			WheelWay way)
	{
		double diffTime = Math.abs(timeOfBallInFrontOfMark - timeOfWheelInFrontOfMark);
		double timeForOneWheelLoop = Helper.getTimeForOneWheelLoop(lastWheelSpeed);
		int numbersCount = Wheel.NUMBERS.length;
		int idxPhase = (int) (diffTime / timeForOneWheelLoop * numbersCount);
		int idxZero = Wheel.findIndexOfNumber(0); // Should be always 0
		int idx = 0;

		if (timeOfBallInFrontOfMark > timeOfWheelInFrontOfMark)
		{
			// t(Wheel) < t(Ball) Wheel is ahead of phase
			// The question is: what is the value of the wheel when the ball
			// is in front of the mark?
			if (way == WheelWay.CLOCKWISE)
			{
				idx = idxZero - idxPhase;
			} else if (way == WheelWay.ANTICLOCKWISE)
			{
				idx = idxZero + idxPhase;
			} else
			{
				throw new CriticalException("Unknown type.");
			}

		} else
		{
			// t(Wheel) > t(Ball) Ball is ahead of phase
			// The question is what is the value of the ball when the wheel
			// is in front of the wheel?
			if (way == WheelWay.CLOCKWISE)
			{
				idx = idxZero + idxPhase;
			} else if (way == WheelWay.ANTICLOCKWISE)
			{
				idx = idxZero - idxPhase;
			} else
			{
				throw new CriticalException("Unknown type.");
			}
		}
		return Wheel.NUMBERS[Wheel.getIndex(idx)];
	}
}
