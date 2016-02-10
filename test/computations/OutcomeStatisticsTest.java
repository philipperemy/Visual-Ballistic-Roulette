package computations;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import computations.predictor.OutcomeStatistics;
import utils.exception.SessionNotReadyException;

public class OutcomeStatisticsTest
{
	@Test
	public void testOutcomeStatistics() throws SessionNotReadyException
	{
		List<Integer> outcomes = Arrays.asList(3, 26, 0, 32, 15);
		OutcomeStatistics os = OutcomeStatistics.create(outcomes);
		Assert.assertEquals(0.0, os.meanNumber, 0.01);
		Assert.assertEquals(Math.sqrt(10.0), os.stdDeviation, 0.01);
	}
}
