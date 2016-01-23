package framework.test;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import framework.CrossValidationLeaveOneOut;
import framework.TestClass;
import framework.games.Game;
import logger.Logger;

public class TestCV extends TestClass
{
	@Test
	public void test_cv()
	{
		List<Game> games = genGames();
		CrossValidationLeaveOneOut cv = new CrossValidationLeaveOneOut(games);
		double error = cv.runCrossValidation();
		Logger.traceINFO("error is : " + error);
	}

	// @Test // 1.46-1.70 error. Lowest is when par = 1000. We take all what
	// have.
	public void test_cv_mc()
	{
		try
		{
			double meanError = 0;
			Logger.traceINFO("Cross validation running...");
			Logger.stopLogging();
			int MC_STEPS = 100;
			for (int i = 0; i < MC_STEPS; i++)
			{
				System.out.println(i);
				setUp();
				addNoise();
				List<Game> games = genGames();
				CrossValidationLeaveOneOut cv = new CrossValidationLeaveOneOut(games);
				double error = cv.runCrossValidation();
				meanError += error;
			}
			meanError /= MC_STEPS;
			Logger.enableLogging();
			Logger.traceINFO("error is : " + meanError);
			Assert.assertTrue(meanError < 2);
		} catch (Exception e)
		{
			throw new RuntimeException(e);
		} finally
		{
			Logger.enableLogging();
		}
	}
}
