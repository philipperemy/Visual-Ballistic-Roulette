package framework_physics;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;

import database.DatabaseAccessor;
import database.DatabaseAccessorInterface;
import servlets.Response;
import servlets.SessionNotReadyException;
import utils.TestResult;

public abstract class TestClass
{
	public static DatabaseAccessorInterface dbRef;
	protected static Response response;
	protected Map<Integer, Game> games = new HashMap<>();

	@BeforeClass
	public static void setUp()
	{
		dbRef = DatabaseAccessor.getInstance();
		response = new Response(dbRef);
		response.da = dbRef;
		response.clearCache();
	}

	@Before
	public void beforeTest()
	{
		setUp();
	}

	public static TestResult runTest(Game predict)
	{
		Integer actualOutcome = null;
		try
		{
			String sessionId = predict.get_sessionId();
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
}
