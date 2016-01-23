package computations;

import org.junit.Test;

import framework.TestClass;

public class TestEuropeanRouletteWheel extends TestClass
{

	// From video. European Roulette Wheel.mp4
	@Test
	public void test_1() throws Exception
	{
		String sessionId = dbRef.getLastSessionId();
		game_1(sessionId, true);
		// sessionId = dbRef.incrementAndGetSessionId();
		// game_2(sessionId, true);
		response.forceDatasetReInit();

		sessionId = dbRef.incrementAndGetSessionId();
		game_3(sessionId, false);
		System.out.println(response.predictMostProbableNumber(sessionId));
	}

	public void game_3(String sessionId, boolean train)
	{
		dbRef.insertBallLapTimes(sessionId, getTime(0, 0, 53, 994));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 0, 54, 929));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 0, 56, 29));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 0, 57, 240));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 0, 58, 554));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 0, 59, 995));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 1, 01, 569));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 1, 3, 254));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 1, 5, 91));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 1, 7, 62));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 1, 9, 244));

		dbRef.insertWheelLapTimes(sessionId, getTime(0, 0, 53, 516));
		dbRef.insertWheelLapTimes(sessionId, getTime(0, 0, 57, 191));
		dbRef.insertWheelLapTimes(sessionId, getTime(0, 1, 00, 908));
		dbRef.insertWheelLapTimes(sessionId, getTime(0, 1, 4, 616));
		dbRef.insertWheelLapTimes(sessionId, getTime(0, 1, 8, 382));

		if (train)
		{
			dbRef.insertOutcome(sessionId, "33");
		}
	}

	public void game_2(String sessionId, boolean train)
	{
		dbRef.insertBallLapTimes(sessionId, getTime(0, 0, 28, 170));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 0, 28, 713));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 0, 29, 304));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 0, 29, 982));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 0, 30, 784));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 0, 31, 725));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 0, 32, 820));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 0, 34, 016));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 0, 35, 326));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 0, 36, 736));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 0, 38, 268));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 0, 39, 926));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 0, 41, 696));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 0, 43, 632));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 0, 45, 737));

		dbRef.insertWheelLapTimes(sessionId, getTime(0, 0, 27, 934));
		dbRef.insertWheelLapTimes(sessionId, getTime(0, 0, 31, 822));
		dbRef.insertWheelLapTimes(sessionId, getTime(0, 0, 35, 767));
		dbRef.insertWheelLapTimes(sessionId, getTime(0, 0, 39, 735));
		dbRef.insertWheelLapTimes(sessionId, getTime(0, 0, 43, 743));
		dbRef.insertWheelLapTimes(sessionId, getTime(0, 0, 47, 769));

		if (train)
		{
			dbRef.insertOutcome(sessionId, "13");
		}
	}

	public void game_1(String sessionId, boolean train)
	{
		dbRef.insertBallLapTimes(sessionId, getTime(0, 0, 2, 871));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 0, 3, 582));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 0, 4, 420));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 0, 5, 460));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 0, 6, 633));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 0, 7, 939));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 0, 9, 369));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 0, 10, 888));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 0, 12, 547));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 0, 14, 339));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 0, 16, 311));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 0, 18, 425));

		dbRef.insertWheelLapTimes(sessionId, getTime(0, 0, 2, 632));
		dbRef.insertWheelLapTimes(sessionId, getTime(0, 0, 6, 213));
		dbRef.insertWheelLapTimes(sessionId, getTime(0, 0, 9, 812));
		dbRef.insertWheelLapTimes(sessionId, getTime(0, 0, 13, 461));
		dbRef.insertWheelLapTimes(sessionId, getTime(0, 0, 17, 114));

		if (train)
		{
			dbRef.insertOutcome(sessionId, "15");
		}
	}

}
