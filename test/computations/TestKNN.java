package computations;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import computations.ml.DataRecord;
import computations.ml.KNN;
import computations.outcome.Outcome;
import computations.outcome.OutcomeStatistics;

public class TestKNN {

	// 0, 32, 15, 19, 4, 21, 2, 25, 17, 34, 6, 27, 13, 36, 11, 30, 8, 23, 10, 5,
	// 24,
	// 16, 33, 1, 20, 14, 31, 9, 22, 18, 29, 7, 28, 12, 35, 3, 26

	@Test
	public void test_GetNeighbors_1() {
		List<DataRecord> lines = new ArrayList<>();
		DataRecord tst = new DataRecord();
		List<DataRecord> records = KNN.getNeighbors(tst, lines);
		Assert.assertTrue(records.isEmpty());
	}

	@Test
	public void test_OutcomeStatistics_3() {
		List<DataRecord> lines = new ArrayList<>();
		lines.add(new DataRecord(new Outcome(4, 0)));
		lines.add(new DataRecord(new Outcome(21, 0)));
		lines.add(new DataRecord(new Outcome(25, 0)));
		lines.add(new DataRecord(new Outcome(17, 0)));

		OutcomeStatistics os = KNN.getOutcomeStatistics(lines, null);
		Assert.assertEquals(2, os.meanNumber);

		List<DataRecord> lines2 = new ArrayList<>();

		lines2.add(new DataRecord(new Outcome(19, 0)));
		lines2.add(new DataRecord(new Outcome(4, 0)));
		lines2.add(new DataRecord(new Outcome(21, 0)));
		lines2.add(new DataRecord(new Outcome(25, 0)));
		lines2.add(new DataRecord(new Outcome(17, 0)));
		lines2.add(new DataRecord(new Outcome(34, 0)));

		OutcomeStatistics os2 = KNN.getOutcomeStatistics(lines2, null);
		Assert.assertEquals(2, os2.meanNumber);

		Assert.assertTrue(os.stdDeviation < os2.stdDeviation);
	}

	@Test
	public void test_OutcomeStatistics_2() {
		List<DataRecord> lines = new ArrayList<>();

		// 30, 11, 36
		lines.add(new DataRecord(new Outcome(36, 0)));
		lines.add(new DataRecord(new Outcome(36, 0)));
		lines.add(new DataRecord(new Outcome(36, 0)));

		OutcomeStatistics os = KNN.getOutcomeStatistics(lines, null);
		Assert.assertEquals(36, os.meanNumber);
		Assert.assertEquals(0, os.stdDeviation, 0.01); // Variance is 0.
	}

	@Test
	public void test_OutcomeStatistics_1() {
		List<DataRecord> lines = new ArrayList<>();

		// 30, 11, 36
		lines.add(new DataRecord(new Outcome(36, 0)));
		lines.add(new DataRecord(new Outcome(30, 0)));

		OutcomeStatistics os = KNN.getOutcomeStatistics(lines, null);
		Assert.assertEquals(11, os.meanNumber);
		Assert.assertEquals(Math.sqrt(2), os.stdDeviation, 0.01); // Variance is
																	// 2.
	}

}
