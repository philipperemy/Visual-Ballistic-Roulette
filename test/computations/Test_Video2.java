package computations;

import org.junit.Test;

import framework.TestClass;
import servlets.SessionNotReadyException;

public class Test_Video2 extends TestClass
{
	private void fill_1_23_601(String sessionId, boolean addOutcome)
	{

		dbRef.insertBallLapTimes(sessionId, getTime(0, 1, 23, 601));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 1, 24, 992));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 1, 26, 563));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 1, 28, 394));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 1, 30, 369));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 1, 32, 731));

		dbRef.insertWheelLapTimes(sessionId, getTime(0, 1, 23, 416));
		dbRef.insertWheelLapTimes(sessionId, getTime(0, 1, 29, 723));
		dbRef.insertWheelLapTimes(sessionId, getTime(0, 1, 36, 510));

		if (addOutcome)
		{
			dbRef.insertOutcome(sessionId, "4");
			// response.forceDatasetReInit();
		}
	}

	private void fill_1_47_071(String sessionId, boolean addOutcome)
	{

		dbRef.insertBallLapTimes(sessionId, getTime(0, 1, 47, 071));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 1, 48, 276));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 1, 49, 737));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 1, 51, 404));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 1, 53, 220));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 1, 55, 308));

		dbRef.insertWheelLapTimes(sessionId, getTime(0, 1, 48, 867));
		dbRef.insertWheelLapTimes(sessionId, getTime(0, 1, 54, 153));
		dbRef.insertWheelLapTimes(sessionId, getTime(0, 1, 59, 717));

		if (addOutcome)
		{
			dbRef.insertOutcome(sessionId, "28");
			// response.forceDatasetReInit();
		}
	}

	// OTHER BALL!
	private void fill_2_07_462(String sessionId, boolean addOutcome)
	{

		dbRef.insertBallLapTimes(sessionId, getTime(0, 2, 7, 462));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 2, 8, 807));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 2, 10, 303));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 2, 11, 977));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 2, 13, 774));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 2, 15, 700));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 2, 17, 817));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 2, 20, 204));

		dbRef.insertWheelLapTimes(sessionId, getTime(0, 2, 11, 829));
		dbRef.insertWheelLapTimes(sessionId, getTime(0, 2, 18, 577));
		dbRef.insertWheelLapTimes(sessionId, getTime(0, 2, 26, 026));

		if (addOutcome)
		{
			dbRef.insertOutcome(sessionId, "13");
			// response.forceDatasetReInit();
		}
	}

	private void fill_2_25_944(String sessionId, boolean addOutcome)
	{

		dbRef.insertBallLapTimes(sessionId, getTime(0, 2, 26, 020));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 2, 27, 508));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 2, 29, 235));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 2, 30, 974));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 2, 32, 885));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 2, 34, 957));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 2, 37, 278));

		dbRef.insertWheelLapTimes(sessionId, getTime(0, 2, 25, 944));
		dbRef.insertWheelLapTimes(sessionId, getTime(0, 2, 34, 386));
		dbRef.insertWheelLapTimes(sessionId, getTime(0, 2, 43, 995));

		if (addOutcome)
		{
			dbRef.insertOutcome(sessionId, "35");
			// response.forceDatasetReInit();
		}
	}

	private void fill_7_54_507(String sessionId, boolean addOutcome)
	{

		dbRef.insertBallLapTimes(sessionId, getTime(0, 7, 54, 525));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 7, 55, 138));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 7, 55, 883));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 7, 56, 603));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 7, 57, 374));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 7, 58, 174));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 7, 59, 002));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 7, 59, 949));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 8, 0, 923));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 8, 2, 246));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 8, 3, 508));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 8, 5, 031));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 8, 6, 769));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 8, 8, 660));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 8, 10, 716));

		dbRef.insertWheelLapTimes(sessionId, getTime(0, 7, 54, 814));
		dbRef.insertWheelLapTimes(sessionId, getTime(0, 8, 0, 853));
		dbRef.insertWheelLapTimes(sessionId, getTime(0, 8, 7, 171));

		if (addOutcome)
		{
			dbRef.insertOutcome(sessionId, "34");
			// response.forceDatasetReInit();
		}
	}

	private void fill_18_27_336(String sessionId, boolean addOutcome)
	{

		dbRef.insertBallLapTimes(sessionId, getTime(0, 18, 32, 015));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 18, 32, 764));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 18, 33, 317));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 18, 33, 957));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 18, 34, 732));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 18, 35, 483));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 18, 36, 232));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 18, 37, 178));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 18, 38, 054));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 18, 39, 041));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 18, 40, 152));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 18, 41, 560));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 18, 43, 207));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 18, 44, 935));

		dbRef.insertWheelLapTimes(sessionId, getTime(0, 18, 31, 590));
		dbRef.insertWheelLapTimes(sessionId, getTime(0, 18, 37, 270));
		dbRef.insertWheelLapTimes(sessionId, getTime(0, 18, 43, 557));

		if (addOutcome)
		{
			dbRef.insertOutcome(sessionId, "32");
			// response.forceDatasetReInit();
		}
	}

	@Test
	public void test_2() throws SessionNotReadyException
	{
		String sessionId = dbRef.getLastSessionId();
		fill_1_23_601(sessionId, true); // 1
		sessionId = dbRef.incrementAndGetSessionId();
		fill_1_47_071(sessionId, true); // 2
		sessionId = dbRef.incrementAndGetSessionId();
		fill_2_07_462(sessionId, true); // 3
		sessionId = dbRef.incrementAndGetSessionId();
		fill_2_25_944(sessionId, true); // 4

		response.forceDatasetReInit();

		sessionId = dbRef.incrementAndGetSessionId();
		fill_7_54_507(sessionId, false); // 5

		response.predictMostProbableNumber(sessionId);

		// 13.
	}

	@Test
	public void test_3() throws SessionNotReadyException
	{
		String sessionId = dbRef.getLastSessionId();
		fill_1_23_601(sessionId, true); // 1
		sessionId = dbRef.incrementAndGetSessionId();
		fill_1_47_071(sessionId, true); // 2
		sessionId = dbRef.incrementAndGetSessionId();
		fill_2_07_462(sessionId, true); // 3
		sessionId = dbRef.incrementAndGetSessionId();
		fill_2_25_944(sessionId, true); // 4

		response.forceDatasetReInit();

		sessionId = dbRef.incrementAndGetSessionId();

		dbRef.insertBallLapTimes(sessionId, getTime(0, 7, 54, 525));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 7, 55, 138));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 7, 55, 883));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 7, 56, 603));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 7, 57, 374));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 7, 58, 174));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 7, 59, 002));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 7, 59, 949));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 8, 0, 923));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 8, 2, 246));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 8, 3, 508));
		// dbRef.insertBallLapTimes(sessionId, getTime(0, 8, 5, 031));
		// dbRef.insertBallLapTimes(sessionId, getTime(0, 8, 6, 769));
		// dbRef.insertBallLapTimes(sessionId, getTime(0, 8, 8, 660));
		// dbRef.insertBallLapTimes(sessionId, getTime(0, 8, 10, 716));

		dbRef.insertWheelLapTimes(sessionId, getTime(0, 7, 54, 814));
		dbRef.insertWheelLapTimes(sessionId, getTime(0, 8, 0, 853));
		// dbRef.insertWheelLapTimes(sessionId, getTime(0, 8, 7, 171));

		response.predictMostProbableNumber(sessionId);

		// 13.
	}

	@Test(expected = SessionNotReadyException.class)
	public void test_4() throws SessionNotReadyException
	{
		String sessionId = dbRef.getLastSessionId();
		fill_1_23_601(sessionId, true); // 1
		sessionId = dbRef.incrementAndGetSessionId();
		fill_1_47_071(sessionId, true); // 2
		sessionId = dbRef.incrementAndGetSessionId();
		fill_2_07_462(sessionId, true); // 3
		sessionId = dbRef.incrementAndGetSessionId();
		fill_2_25_944(sessionId, true); // 4

		response.forceDatasetReInit();

		sessionId = dbRef.incrementAndGetSessionId();

		dbRef.insertBallLapTimes(sessionId, getTime(0, 7, 54, 525));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 7, 55, 138));
		// dbRef.insertBallLapTimes(sessionId, getTime(0, 7, 55, 883));
		// dbRef.insertBallLapTimes(sessionId, getTime(0, 7, 56, 603));
		// dbRef.insertBallLapTimes(sessionId, getTime(0, 7, 57, 374));
		// dbRef.insertBallLapTimes(sessionId, getTime(0, 7, 58, 174));
		// dbRef.insertBallLapTimes(sessionId, getTime(0, 7, 59, 002));
		// dbRef.insertBallLapTimes(sessionId, getTime(0, 7, 59, 949));
		// dbRef.insertBallLapTimes(sessionId, getTime(0, 8, 0, 923));
		// dbRef.insertBallLapTimes(sessionId, getTime(0, 8, 2, 246));
		// dbRef.insertBallLapTimes(sessionId, getTime(0, 8, 3, 508));
		// dbRef.insertBallLapTimes(sessionId, getTime(0, 8, 5, 031));
		// dbRef.insertBallLapTimes(sessionId, getTime(0, 8, 6, 769));
		// dbRef.insertBallLapTimes(sessionId, getTime(0, 8, 8, 660));
		// dbRef.insertBallLapTimes(sessionId, getTime(0, 8, 10, 716));

		dbRef.insertWheelLapTimes(sessionId, getTime(0, 7, 54, 814));
		dbRef.insertWheelLapTimes(sessionId, getTime(0, 8, 0, 853));
		// dbRef.insertWheelLapTimes(sessionId, getTime(0, 8, 7, 171));

		response.predictMostProbableNumber(sessionId);

		// 13.
	}

	@Test
	public void test_5() throws SessionNotReadyException
	{
		String sessionId = dbRef.getLastSessionId();
		fill_1_23_601(sessionId, true); // 1
		sessionId = dbRef.incrementAndGetSessionId();
		fill_1_47_071(sessionId, true); // 2
		sessionId = dbRef.incrementAndGetSessionId();
		fill_2_07_462(sessionId, true); // 3
		sessionId = dbRef.incrementAndGetSessionId();
		fill_2_25_944(sessionId, true); // 4

		response.forceDatasetReInit();

		sessionId = dbRef.incrementAndGetSessionId();

		dbRef.insertBallLapTimes(sessionId, getTime(0, 7, 54, 525));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 7, 55, 138));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 7, 55, 883));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 7, 56, 603));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 7, 57, 374));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 7, 58, 174));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 7, 59, 002));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 7, 59, 949));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 8, 0, 923));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 8, 2, 246));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 8, 3, 508));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 8, 5, 031));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 8, 6, 769));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 8, 8, 660));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 8, 10, 716));

		dbRef.insertWheelLapTimes(sessionId, getTime(0, 7, 54, 814));
		dbRef.insertWheelLapTimes(sessionId, getTime(0, 8, 0, 853));
		dbRef.insertWheelLapTimes(sessionId, getTime(0, 8, 7, 171));

		response.predictMostProbableNumber(sessionId);

		// 13.
	}

}