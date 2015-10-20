package computations;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import computations.ml.DataRecord;
import computations.predictor.Predictor;

public class TestPredictor {

	// { 0, 32, 15, 19, 4, 21, 2, 25, 17, 34, 6, 27, 13, 36, 11, 30, 8, 23, 10,
	// 5, 24,
	// 16, 33, 1, 20, 14, 31, 9, 22, 18, 29, 7, 28, 12, 35, 3, 26 }

	@Test
	public void test_GetShiftFromPhaseNumbers() {
		DataRecord dr1 = new DataRecord();
		dr1.phases = Arrays.asList(14, 19, 26);

		DataRecord dr2 = new DataRecord();
		dr2.phases = Arrays.asList(9, 21, 32); // +2,+2,+2

		int bestShift = Predictor.getShiftFromPhaseNumbers(dr1, dr2);
		Assert.assertEquals(2, bestShift);
	}

	@Test
	public void test_GetShiftFromPhaseNumbers2() {
		DataRecord dr1 = new DataRecord();
		dr1.phases = Arrays.asList(14, 31, 9);

		DataRecord dr2 = new DataRecord();
		dr2.phases = Arrays.asList(9, 9, 29); // +2,+1,+3

		int bestShift = Predictor.getShiftFromPhaseNumbers(dr1, dr2);
		Assert.assertEquals(2, bestShift);
	}

	@Test
	public void test_GetShiftFromPhaseNumbers3() {
		DataRecord dr1 = new DataRecord();
		dr1.phases = Arrays.asList(9, 21, 32);

		DataRecord dr2 = new DataRecord();
		dr2.phases = Arrays.asList(14, 19, 26); // -2,-2,-2

		int bestShift = Predictor.getShiftFromPhaseNumbers(dr1, dr2);
		Assert.assertEquals(-2, bestShift);
	}

	@Test
	public void test_GetShiftFromPhaseNumbers4() {
		DataRecord dr1 = new DataRecord();
		dr1.phases = Arrays.asList(9, 9, 29);

		DataRecord dr2 = new DataRecord();
		dr2.phases = Arrays.asList(14, 31, 9); // -2,-1,-3

		int bestShift = Predictor.getShiftFromPhaseNumbers(dr1, dr2);
		Assert.assertEquals(-2, bestShift);
	}

	@Test
	public void test_GetShiftFromPhaseNumbers5() {
		DataRecord dr1 = new DataRecord();
		dr1.phases = Arrays.asList(14, 21, 26);

		DataRecord dr2 = new DataRecord();
		dr2.phases = Arrays.asList(9, 19, 32); // +2,-2,+2

		int bestShift = Predictor.getShiftFromPhaseNumbers(dr1, dr2);
		Assert.assertEquals(1, bestShift);
	}

}
