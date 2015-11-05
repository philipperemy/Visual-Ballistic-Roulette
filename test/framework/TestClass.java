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
import framework.time.GetNoisyTime;
import framework.time.GetTime;
import servlets.Response;
import servlets.SessionNotReadyException;

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
		} catch (SessionNotReadyException snre)
		{
			throw new RuntimeException(snre);
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

}
