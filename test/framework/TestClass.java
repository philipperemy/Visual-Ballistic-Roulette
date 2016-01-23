package framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;

import database.DatabaseAccessorInterface;
import database.DatabaseAccessorStub;
import framework.games.Game;
import framework.games.Game1;
import framework.games.Game2;
import framework.games.Game3;
import framework.games.Game4;
import framework.games.Game5;
import framework.games.Game6;
import framework.games.Game7;
import framework.games.Game8;
import framework.time.GetNoisyTime;
import framework.time.GetTime;
import servlets.Response;
import utils.TestResult;

public abstract class TestClass
{
	public static DatabaseAccessorInterface dbRef;
	protected static Response response;
	protected Map<Integer, Game> games = new HashMap<>();
	public static GetTime timerGetter = new GetTime();

	@BeforeClass
	public static void setUp()
	{
		dbRef = new DatabaseAccessorStub();
		response = new Response(dbRef);
		response.da = dbRef;
		response.clearCache();
		timerGetter = new GetTime();
	}

	@Before
	public void beforeTest()
	{
		setUp();
	}

	public static String getTime(int hour, int min, int sec, int millis)
	{
		return timerGetter.getTime(hour, min, sec, millis);
	}

	public static void addNoise()
	{
		timerGetter = new GetNoisyTime();
	}

	public static TestResult runTest(List<Game> games, Game predict)
	{
		String sessionId = dbRef.getLastSessionId();

		for (int i = 0; i < games.size(); i++)
		{
			Game game = games.get(i);
			game.run(sessionId);
			sessionId = dbRef.incrementAndGetSessionId();
		}

		response.forceDatasetReInit();

		predict.run(sessionId);

		Integer actualOutcome = null;
		try
		{
			actualOutcome = response.predictMostProbableNumber(sessionId);
		} catch (Exception e)
		{
			throw new RuntimeException(e);
		}

		TestResult testResult = new TestResult();
		testResult.expected = predict.get_outcome();
		testResult.actual = actualOutcome;
		return testResult;
	}

	public static List<String> create(String... strings)
	{
		List<String> list = new ArrayList<>();
		for (String str : strings)
		{
			list.add(str);
		}
		return list;
	}

	protected List<Game> genGames()
	{
		List<Game> games = new ArrayList<>();
		games.add(new Game1());
		games.add(new Game2());
		games.add(new Game3());
		games.add(new Game4());
		games.add(new Game5());
		games.add(new Game6());
		games.add(new Game7());
		games.add(new Game8());
		return games;
	}

}
