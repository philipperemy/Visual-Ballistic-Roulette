package framework_physics;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;

import computations.predictor.ml.model.DataRecord;
import computations.predictor.statistical.PredictorStatisticalAnalysis;
import database.DatabaseAccessor;
import database.DatabaseAccessorInterface;
import utils.TestResult;

public abstract class TestClass
{
	public static DatabaseAccessorInterface	dbRef;
	protected Map<Integer, Game>			games	= new HashMap<>();

	@BeforeClass
	public static void setUp()
	{
		dbRef = DatabaseAccessor.getInstance();
		DataRecord.clearCache();
	}

	@Before
	public void beforeTest()
	{
		setUp();
	}

	// static PredictorPhysics py = PredictorPhysics.getInstance();
	static PredictorStatisticalAnalysis py = new PredictorStatisticalAnalysis();

	public static TestResult runTest(Game predict)
	{
		Integer actualOutcome = null;
		try
		{
			actualOutcome = py.predict(predict.get_ballLaptimes(), predict.get_wheelLaptimes());
		} catch (Exception e)
		{
			throw new RuntimeException(e);
		}

		TestResult testResult = new TestResult();
		testResult.expected = predict.get_outcome();
		testResult.actual = actualOutcome;

		if (testResult.expected == null)
		{
			throw new NullPointerException();
		}

		return testResult;
	}
}
