package computations.wheel;

import servlets.CriticalException;

public class Wheel
{
	public static final int[] NUMBERS = { 0, 32, 15, 19, 4, 21, 2, 25, 17, 34, 6, 27, 13, 36, 11, 30, 8, 23, 10, 5, 24, 16, 33, 1, 20, 14, 31, 9, 22,
			18, 29, 7, 28, 12, 35, 3, 26 };

	public static enum WheelWay
	{
		CLOCKWISE, ANTICLOCKWISE
	};

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

	public static int[] getNearbyNumbers(int referenceNumber, int size)
	{
		int idxNumber = findIndexOfNumber(referenceNumber);
		int firstIxNumber = getIndex(idxNumber - size);
		int numOfElements = size * 2 + 1;
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

	public static int predictOutcomeWithShift(int phase1, int outcome1, int phase2)
	{
		int idx_p1 = findIndexOfNumber(phase1);
		int idx_o1 = findIndexOfNumber(outcome1);
		int diffIdxBetweenPhaseAndOutcome1 = getIndex(idx_o1 - idx_p1);

		int id_p2 = findIndexOfNumber(phase2);
		int id_o2 = getIndex(id_p2 + diffIdxBetweenPhaseAndOutcome1);
		return NUMBERS[id_o2];
	}

	// Here max distance is 37/2. Opposite of the wheel.
	public static int distanceBetweenNumbers(int number1, int number2)
	{
		int idx1 = findIndexOfNumber(number1);
		int idx2 = findIndexOfNumber(number2);
		int diff = Math.abs(idx1 - idx2);
		int diff_to_maxlen = NUMBERS.length - diff;
		return diff < diff_to_maxlen ? diff : diff_to_maxlen;
	}

	// Do not use it. We do not know the mirroring because we do not know at
	// time 0.
	@Deprecated
	public static int getMirrorNumber(int number)
	{
		int idx = findIndexOfNumber(number);
		// int newIdx = getIndex(idx + NUMBERS.length/2);
		int newIdx = getIndex(-idx + NUMBERS.length);
		return NUMBERS[newIdx];
	}
}
