package utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import computations.predictor.Predictor;
import computations.predictor.error.DistanceError;
import database.DatabaseAccessorInterface;

public abstract class TestClass
{
	public DatabaseAccessorInterface dbRef;
	protected Map<Integer, Game> games = new HashMap<>();
	protected Predictor predictor;

	public TestClass(Predictor predictor, DatabaseAccessorInterface dbRef)
	{
		this.dbRef = dbRef;
		this.predictor = predictor;
		this.predictor.clear();
	}

	public DistanceError runTest(Game predict, List<String> sessionIdsToPutInDatabase)
	{
		Integer actualOutcome = null;
		try
		{
			predictor.clear();
			predictor.init(dbRef, sessionIdsToPutInDatabase);
			actualOutcome = predictor.predict(predict.get_ballLaptimes(), predict.get_wheelLaptimes());
		} catch (Exception e)
		{
			throw new RuntimeException(e);
		} finally
		{
			predictor.clear();
		}

		DistanceError testResult = new DistanceError();
		testResult.expected = predict.get_outcome();
		testResult.actual = actualOutcome;
		return testResult;
	}
}
