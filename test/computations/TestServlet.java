package computations;

import org.junit.Assert;
import org.junit.Test;

import servlets.Response;
import servlets.SessionNotReadyException;

public class TestServlet {

	/*
	 * Make sure the database is started
	 */

	/*
	 * First part of the video. BALLS = 2858 3591 4421 5456 6625 7950 9355 10887
	 * 12539 14336 16302 18387 WHEELS = 2600 6168 9810 13427 17154 20810
	 * Associated session is 1. Obstacles hit is 0. Outcome number is 15. Way is
	 * ANTICLOCKWISE.
	 */
	@Test
	public void test() throws SessionNotReadyException {
		Response response = new Response();
		Assert.assertEquals(15, response.predictMostProbableNumber(Constants.WHEEL_CLOCKWISE));
		Assert.assertEquals("[0, 4, 15, 19, 21, 26, 32]",
				response.predictMostProbableRegion(Constants.WHEEL_CLOCKWISE).toString()); // sorted
	}
}
