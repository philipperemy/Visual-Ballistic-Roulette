package computations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;

import database.DatabaseAccessorInterface;
import database.DatabaseAccessorStub;
import servlets.Response;
import servlets.SessionNotReadyException;

public class TestClass
{

	protected static DatabaseAccessorInterface dbRef;
	protected static Response response;
	protected Map<Integer, Game> games = new HashMap<>();

	@BeforeClass
	public static void setUp()
	{
		dbRef = new DatabaseAccessorStub();
		response = new Response(dbRef);
		response.da = dbRef;
	}

	protected Game game1() {
		int outcome = 4;
		return new Game(outcome, create(
					getTime(0, 1, 23, 601),
					getTime(0, 1, 24, 992),
					getTime(0, 1, 26, 563),
					getTime(0, 1, 28, 394),
					getTime(0, 1, 28, 394),
					getTime(0, 1, 30, 369),
					getTime(0, 1, 32, 731)
				),
			 create(
					getTime(0, 1, 23, 416),
					getTime(0, 1, 29, 723),
					getTime(0, 1, 36, 510))
				);
	}
	
	protected Game game2() {
		int outcome = 28;
		return new Game(outcome, create(
						getTime(0, 1, 47, 071),
						getTime(0, 1, 48, 276),
						getTime(0, 1, 49, 737),
						getTime(0, 1, 51, 404),
						getTime(0, 1, 53, 220),
						getTime(0, 1, 55, 308)
				), 
				create(
						getTime(0, 1, 48, 867),
						getTime(0, 1, 54, 153),
						getTime(0, 1, 59, 717)
						)
				);
				
	}
	

	public static String getTime(int hour, int min, int sec, int millis)
	{
		return String.valueOf((hour * 3600 + min * 60 + sec) * 1000 + millis);
	}

	public static void runTest(List<Game> games, Game predict, int expectedOutcome) throws SessionNotReadyException
	{
		String sessionId = dbRef.getLastSessionId();

		for (Game game : games)
		{
			game.run(sessionId);
			sessionId = dbRef.incrementAndGetSessionId();
		}

		response.forceDatasetReInit();

		predict.run(sessionId);

		int actualOutcome = response.predictMostProbableNumber(sessionId);
		Assert.assertEquals(expectedOutcome, actualOutcome);
	}

	public class Game
	{
		public List<String> ballLaptimes = new ArrayList<>();
		public List<String> wheelLaptimes = new ArrayList<>();
		public Integer outcome = null;

		public Game()
		{
		}

		public Game(Integer outcome, List<String> ballLaptimes, List<String> wheelLaptimes)
		{
			this.outcome = outcome;
			this.ballLaptimes = ballLaptimes;
			this.wheelLaptimes = wheelLaptimes;
		}

		public void run(String sessionId)
		{
			dbRef.insertClockwise(sessionId, Constants.WHEEL_ANTICLOCKWISE);

			for (String time : ballLaptimes)
			{
				dbRef.insertBallLapTimes(sessionId, time);
			}

			for (String time : wheelLaptimes)
			{
				dbRef.insertWheelLapTimes(sessionId, time);
			}

			if (outcome != null)
			{
				dbRef.insertOutcome(sessionId, String.valueOf(outcome));
			}

		}

		private TestClass getOuterType()
		{
			return TestClass.this;
		}

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((ballLaptimes == null) ? 0 : ballLaptimes.hashCode());
			result = prime * result + ((outcome == null) ? 0 : outcome.hashCode());
			result = prime * result + ((wheelLaptimes == null) ? 0 : wheelLaptimes.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Game other = (Game) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (ballLaptimes == null)
			{
				if (other.ballLaptimes != null)
					return false;
			} else if (!ballLaptimes.equals(other.ballLaptimes))
				return false;
			if (outcome == null)
			{
				if (other.outcome != null)
					return false;
			} else if (!outcome.equals(other.outcome))
				return false;
			if (wheelLaptimes == null)
			{
				if (other.wheelLaptimes != null)
					return false;
			} else if (!wheelLaptimes.equals(other.wheelLaptimes))
				return false;
			return true;
		}

	}

	public List<String> create(String... strings)
	{
		List<String> list = new ArrayList<>();
		for (String str : strings)
		{
			list.add(str);
		}
		return list;
	}

}
