package computations.wheel;

public class Wheel {

	public static final int[] NUMBERS = { 0, 32, 15, 19, 4, 21, 2, 25, 17, 34, 6, 27, 13, 36, 11, 30, 8, 23, 10, 5, 24,
			16, 33, 1, 20, 14, 31, 9, 22, 18, 29, 7, 28, 12, 35, 3, 26 };

	public static enum WheelWay {
		CLOCKWISE, ANTICLOCKWISE
	};

	public static WheelWay convert(String clockwise) {
		switch (clockwise) {
		case "1":
			return WheelWay.CLOCKWISE;
		case "0":
			return WheelWay.ANTICLOCKWISE;
		default:
			throw new RuntimeException("Invalid clockwise : " + clockwise);
		}
	}

	public static int getIndex(int index) {
		if (index < 0) {
			index += NUMBERS.length;
		}
		if (index >= NUMBERS.length) {
			index -= NUMBERS.length;
		}
		return index;
	}

	public static int[] getNearbyNumbers(int referenceNumber, int size) {
		int idxNumber = findIndexOfNumber(referenceNumber);
		int firstIxNumber = getIndex(idxNumber - size);
		int numOfElements = size * 2 + 1;
		int[] res = new int[numOfElements];
		int c = 0;
		int cur = firstIxNumber;
		while (c < numOfElements) {
			cur = getIndex(cur);
			res[c++] = NUMBERS[cur++];
		}
		return res;
	}

	public static int getNumberWithPhase(int referenceNumber, int phaseCount, WheelWay way) {
		int idxReferenceNumber = findIndexOfNumber(referenceNumber);

		int newIdx = 0;
		switch (way) {

		case CLOCKWISE:
			newIdx = idxReferenceNumber - phaseCount;
			break;

		case ANTICLOCKWISE:
			newIdx = idxReferenceNumber + phaseCount;
			break;

		default:
			throw new RuntimeException();
		}

		return NUMBERS[getIndex(newIdx)];
	}

	//Test it. Force the test by using Deprecated.
	@Deprecated 
	public static int getNextNumberClockwise(int number) {
		int idx = findIndexOfNumber(number);
		idx++;
		return NUMBERS[getIndex(idx)];
	}
	
	public static int findIndexOfNumber(int number) {
		for (int i = 0; i < NUMBERS.length; i++) {
			if (number == NUMBERS[i]) {
				return i;
			}
		}
		throw new RuntimeException();
	}

	//TODO: test it. Number1 - Number2. Rename it! Always positive.
	public static int signedDistanceBetweenNumbers(int number1, int number2) {
		int idx1 = findIndexOfNumber(number1);
		int idx2 = findIndexOfNumber(number2);
		return getIndex(idx1 - idx2);
	}
	
	public static int distanceBetweenNumbers(int number1, int number2) {
		int idx1 = findIndexOfNumber(number1);
		int idx2 = findIndexOfNumber(number2);
		int diff = Math.abs(idx1 - idx2);
		int diff_to_maxlen = NUMBERS.length - diff;
		return diff < diff_to_maxlen ? diff : diff_to_maxlen;
	}

	public static int getMirrorNumber(int number) {
		int idx = findIndexOfNumber(number);
		int newIdx = getIndex(-idx + NUMBERS.length);
		return NUMBERS[newIdx];
	}
}
