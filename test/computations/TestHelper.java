package computations;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import computations.utils.Helper;

public class TestHelper
{

	@Test
	public void test_unserialize()
	{
		List<Integer> list = Helper.unserializeOutcomeNumbers("[0, 4, 15, 19, 21, 26, 32]");
		Assert.assertEquals("[0, 4, 15, 19, 21, 26, 32]", list.toString());
	}

}
