package computations;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class TestHelper
{

	@Test
	public void test_unserialize()
	{
		List<Integer> list = Helper.unserialize("[0, 4, 15, 19, 21, 26, 32]");
		Assert.assertEquals("[0, 4, 15, 19, 21, 26, 32]", list.toString());
	}

}
