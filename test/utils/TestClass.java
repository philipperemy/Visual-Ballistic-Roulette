package utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import computations.predictor.Predictor;
import database.DatabaseAccessorInterface;
import servlets.Response;

public abstract class TestClass
{
	public DatabaseAccessorInterface	dbRef;
	protected Response					response;
	protected Map<Integer, Game>		games	= new HashMap<>();
	protected Predictor					predictor;

	public TestClass(Predictor predictor, DatabaseAccessorInterface dbRef)
	{
		this.dbRef = dbRef;
		response = new Response(dbRef);
		response.da = dbRef;
		response.clearCache();
		this.predictor = predictor;
	}

	public TestResult runTest(Game predict, List<String> sessionIdsToPutInDatabase)
	{
		Integer actualOutcome = null;
		try
		{
			response.clearCache();
			predictor.init(dbRef, sessionIdsToPutInDatabase);
			actualOutcome = predictor.predict(predict.get_ballLaptimes(), predict.get_wheelLaptimes());
		} catch (Exception e)
		{
			throw new RuntimeException(e);
		} finally
		{
			response.clearCache();
		}

		TestResult testResult = new TestResult();
		testResult.expected = predict.get_outcome();
		testResult.actual = actualOutcome;
		return testResult;
	}
}
