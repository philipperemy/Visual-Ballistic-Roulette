package framework;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import framework.games.Game;
import framework.games.Game1;
import framework.games.Game2;
import framework.games.Game3;
import framework.games.Game4;
import framework.games.Game5;
import framework.games.Game6;
import framework.games.Game7;
import framework.games.Game8;
import log.Logger;

public class TestCV extends TestClass
{

	private List<Game> genGames()
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

	// @Test
	public void test_cv()
	{
		List<Game> games = genGames();
		CrossValidationLeaveOneOut cv = new CrossValidationLeaveOneOut(games);
		double error = cv.runCrossValidation();
		Logger.traceINFO("error is : " + error);
	}

	@Test // 1.46-1.70 error. Lowest is when par = 1000. We take all what have.
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
