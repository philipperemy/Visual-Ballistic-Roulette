package utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import computations.predictor.Predictor;
import computations.predictor.ml.model.DataRecord;
import database.DatabaseAccessorInterface;

public abstract class TestClass
{
	public DatabaseAccessorInterface	dbRef;
	protected Map<Integer, Game>		games	= new HashMap<>();
	protected Predictor					predictor;

	public TestClass(Predictor predictor, DatabaseAccessorInterface dbRef)
	{
		this.dbRef = dbRef;
		DataRecord.clearCache();
		this.predictor = predictor;
	}

	public TestResult runTest(Game predict, List<String> sessionIdsToPutInDatabase)
	{
		Integer actualOutcome = null;
		try
		{
			DataRecord.clearCache();
			predictor.init(dbRef, sessionIdsToPutInDatabase);
			actualOutcome = predictor.predict(predict.get_ballLaptimes(), predict.get_wheelLaptimes());
		} catch (Exception e)
		{
			throw new RuntimeException(e);
		} finally
		{
			DataRecord.clearCache();
		}

		TestResult testResult = new TestResult2();
		testResult.expected = predict.get_outcome();
		testResult.actual = actualOutcome;
		return testResult;
	}
}
