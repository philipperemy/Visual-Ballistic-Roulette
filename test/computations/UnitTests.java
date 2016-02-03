package computations;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import computations.utils.Helper;

public class UnitTests
{
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

}
