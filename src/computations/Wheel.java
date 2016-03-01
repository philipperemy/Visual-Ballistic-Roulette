package computations;

import utils.exception.CriticalException;

public class Wheel
{
	/**
	 * Roulette numbers represented as a 1D-vector. Each number is accessed with
	 * its index. Arithmetic operations are made on the indexes. For example.
	 * Index of 0 is 0. Index of 32 is 1. Distance(0,32) = 1 - 0 = 1
	 */
	public static final int[] NUMBERS = { 0, 32, 15, 19, 4, 21, 2, 25, 17, //
			34, 6, 27, 13, 36, 11, 30, 8, 23, 10, 5, 24, 16, 33, 1, 20, 14, //
			31, 9, 22, 18, 29, 7, 28, 12, 35, 3, 26 };

	public static enum WheelWay
	{
		CLOCKWISE, //
		ANTICLOCKWISE
	};

	/**
	 * Calculate a valid index from Z -> [0, 36] (length = 37 numbers)
	 * 
	 * @param any
	 *            integer
	 * @return valid index
	 */
	public static int getIndex(int index)
	{
		while (index < 0)
		{
			index += NUMBERS.length;
		}
		while (index >= NUMBERS.length)
		{
			index -= NUMBERS.length;
		}
		return index;
	}

	/**
	 * Calculate the region around a specific number. Example is:
	 * region(referenceNumber=32, halfSize = 2) = [26,0,32,15,19]
	 */
	public static int[] getNearbyNumbers(int referenceNumber, int halfSize)
	{
		int idxNumber = findIndexOfNumber(referenceNumber);
		int firstIxNumber = getIndex(idxNumber - halfSize);
		int numOfElements = halfSize * 2 + 1;
		int[] res = new int[numOfElements];
		int c = 0;
		int cur = firstIxNumber;
		while (c < numOfElements)
		{
			cur = getIndex(cur);
			res[c++] = NUMBERS[cur++];
		}
		return res;
	}

	/**
	 * Translates a number on the wheel by a specific value.
	 * 
	 * @param referenceNumber
	 *            The number on the wheel
	 * @param phaseCount
	 *            How many pockets should the reference number be translated
	 * @param way
	 *            Clockwise or Anticlockwise
	 * @return Example is translated(referenceNumber=32, phaseCount=2,
	 *         way=Anticlockwise) = 19 ATTENTION: This is a bit tricky because
	 *         when the wheel turns anticlockwise, we scan the numbers forward
	 *         and not backwards as we would imagine. Sketch something if you
	 *         aint sure.
	 */
	public static int getNumberWithPhase(int referenceNumber, int phaseCount, WheelWay way)
	{
		int idxReferenceNumber = findIndexOfNumber(referenceNumber);

		int newIdx = 0;
		switch (way)
		{
		case CLOCKWISE:
			newIdx = idxReferenceNumber - phaseCount;
			break;

		case ANTICLOCKWISE:
			newIdx = idxReferenceNumber + phaseCount;
			break;

		default:
			throw new CriticalException("Unknown type.");
		}

		return NUMBERS[getIndex(newIdx)];
	}

	/**
	 * Example is: Give me the index of the number 32. Answer is 1.
	 */
	public static int findIndexOfNumber(int number)
	{
		for (int i = 0; i < NUMBERS.length; i++)
		{
			if (number == NUMBERS[i])
			{
				return i;
			}
		}
		throw new CriticalException("Unknown number.");
	}

	/**
	 * Calculates the translation between (phase1, outcome1) and applies this
	 * translation to phase2. Example is: phase1 = 0, outcome1 = 32. phase2 =
	 * 19. translation(phase1, outcome1) = you add 1 forward. You take the
	 * number 19 and you add this translation. The result is 4.
	 */
	public static int predictOutcomeWithShift(int phase1, int outcome1, int phase2)
	{
		int idx_p1 = findIndexOfNumber(phase1);
		int idx_o1 = findIndexOfNumber(outcome1);
		int diffIdxBetweenPhaseAndOutcome1 = getIndex(idx_o1 - idx_p1);

		int id_p2 = findIndexOfNumber(phase2);
		int id_o2 = getIndex(id_p2 + diffIdxBetweenPhaseAndOutcome1);
		return NUMBERS[id_o2];
	}

	/**
	 * Here max distance is 37/2. Opposite of the wheel. Calculate the shortest
	 * distance between two numbers. Example: distance(0,32) = 1.
	 */
	public static int distanceBetweenNumbers(int number1, int number2)
	{
		int idx1 = findIndexOfNumber(number1);
		int idx2 = findIndexOfNumber(number2);
		int diff = Math.abs(idx1 - idx2);
		int diff_to_maxlen = NUMBERS.length - diff;
		return diff < diff_to_maxlen ? diff : diff_to_maxlen;
	}
}
