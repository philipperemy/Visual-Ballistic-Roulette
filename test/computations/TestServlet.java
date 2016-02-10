package computations;

import org.junit.Assert;

import servlets.Response;
import utils.exception.SessionNotReadyException;

public class TestServlet
{

	/*
	 * Make sure the database is started
	 */

	/*
	 * First part of the video. BALLS = 2858 3591 4421 5456 6625 7950 9355 10887
	 * 12539 14336 16302 18387 WHEELS = 2600 6168 9810 13427 17154 20810
	 * Associated session is 1. Obstacles hit is 0. Outcome number is 15. Way is
	 * ANTICLOCKWISE.
	 */

	// KNN = 1, For testing only.
	public void test() throws SessionNotReadyException
	{
		int old_val_knn = Constants.NUMBER_OF_NEIGHBORS_KNN;
		try
		{
			Response response = new Response();
			String sessionId = "1";
			Constants.NUMBER_OF_NEIGHBORS_KNN = 1;
			Assert.assertEquals(15, response.predictMostProbableNumber(sessionId));
		} catch (Exception e)
		{
			throw new RuntimeException(e);
		} finally
		{
			Constants.NUMBER_OF_NEIGHBORS_KNN = old_val_knn;
		}
	}
}
