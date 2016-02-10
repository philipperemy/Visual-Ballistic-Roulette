package computations;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import computations.utils.GaussianNoiseGenerator;
import computations.utils.Helper;
import utils.logger.Logger;

public class TestHelper
{
	@Test
	public void test_unserialize()
	{
		List<Integer> list = Helper.unserializeOutcomeNumbers("[0, 4, 15, 19, 21, 26, 32]");
		Assert.assertEquals("[0, 4, 15, 19, 21, 26, 32]", list.toString());
	}

	@Test
	public void convertToSeconds()
	{
		List<Double> listInMilliseconds = Arrays.asList(1000.0, 1010.0, 1020.0);
		Assert.assertEquals("[1.0, 1.01, 1.02]", Helper.convertToSeconds(listInMilliseconds).toString());
	}

	@Test
	public void peek()
	{
		List<Integer> list = Arrays.asList(10, 11, 12);
		Assert.assertEquals(12, Helper.peek(list));
	}

	@Test
	public void head()
	{
		List<Integer> list = Arrays.asList(10, 11, 12);
		Assert.assertEquals(10, Helper.head(list));
	}

	@Test
	public void printValueOrInfty()
	{
		Assert.assertEquals("+oo", Helper.printValueOrInfty(3_000_000_000.0));
		Assert.assertEquals("1.0E9", Helper.printValueOrInfty(1_000_000_000.0));

	}

	@Test
	public void printDigit()
	{
		Assert.assertEquals("12.2341", Helper.printDigit(12.234111234));
	}

	@Test
	public void getLastTimeWheelIsInFrontOfRef()
	{
		List<Double> wheelLapTimes = Arrays.asList(10.0, 11.0, 12.0, 13.0);
		double ballLapTimeInFrontOfRef = 12.5;
		Assert.assertEquals(12.0, Helper.getLastTimeWheelIsInFrontOfRef(wheelLapTimes, ballLapTimeInFrontOfRef), 0.01);

	}

	@Test
	public void computeDiff()
	{
		List<Double> cumsumTimes = Arrays.asList(10.0, 11.0, 12.0, 13.0, 15.0);
		Assert.assertEquals("[1.0, 1.0, 1.0, 2.0]", Helper.computeDiff(cumsumTimes).toString());
	}

	@Test
	public void normalize()
	{
		List<Double> cumsumTimes = Arrays.asList(10.0, 11.0, 12.0, 13.0, 15.0);
		double first = Helper.head(cumsumTimes);
		Assert.assertEquals("[0.0, 1.0, 2.0, 3.0, 5.0]", Helper.normalize(cumsumTimes, first).toString());
	}

	@Test
	public void helper_test_1()
	{
		List<Double> list = Arrays.asList(1000.0, 2000.0, 3000.0);
		List<Double> res = Helper.convertToSeconds(list);
		Assert.assertEquals("[1.0, 2.0, 3.0]", res.toString());
	}

	@Test
	public void helper_test_2()
	{
		List<Double> list = Arrays.asList(1000.0, 2000.0, 3000.0);
		double res = Helper.getLastTimeWheelIsInFrontOfRef(list, 2500.0);
		Assert.assertEquals(2000.0, res, 0.01);
		Double res2 = Helper.getLastTimeWheelIsInFrontOfRef(list, 900.0);
		Assert.assertNull(res2);
	}

	@Test
	public void helper_test_3()
	{
		Assert.assertEquals(0.1, Helper.inverseSpeed(10), 0.0001);
		Assert.assertEquals(0.01, Helper.inverseSpeed(100), 0.0001);
		Assert.assertEquals(0.25, Helper.inverseSpeed(4), 0.0001);
	}

	@Test
	public void helper_test_4()
	{
		List<Double> list = Arrays.asList(1000.0, 2000.0, 3000.0);
		Double res = Helper.peek(list);
		Assert.assertNotNull(res);
		Assert.assertEquals(3000.0, res, 0.01);
		Assert.assertEquals(3, list.size());
	}

	@Test
	public void helper_test_5()
	{
		Assert.assertEquals("12.24", Helper.printDigit(12.24));
		Assert.assertEquals("12.245", Helper.printDigit(12.245));
		Assert.assertEquals("12.246", Helper.printDigit(12.24598594));
	}

	@Test
	public void helper_test_6()
	{
		Assert.assertEquals("+oo", Helper.printValueOrInfty(2_000_000_000.0));
		Assert.assertEquals("12.245", Helper.printValueOrInfty(12.245));
	}

	@Test
	public void helper_test_7()
	{
		List<Integer> list = Arrays.asList(1000, 2000, 3000);
		List<Integer> list2 = Helper.unserializeOutcomeNumbers(list.toString());
		Assert.assertEquals(list.toString(), list2.toString());
	}

	@Test
	public void test1()
	{
		GaussianNoiseGenerator gng = new GaussianNoiseGenerator(0, 25);
		Logger.traceINFO(gng.addNoiseTimeMillis(1000));

		String in = "3000";
		String out = gng.addNoiseTimeMillisStr(in);
		Assert.assertTrue(!out.equals(in)); // out != in
	}
}
