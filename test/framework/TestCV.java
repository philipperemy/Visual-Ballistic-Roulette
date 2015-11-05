package framework;

import java.util.ArrayList;
import java.util.List;

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
	
	@Test
	public void test_cv()
	{
		List<Game> games = genGames();
		CrossValidationLeaveOneOut cv = new CrossValidationLeaveOneOut(games);
		double error = cv.runCrossValidation();
		Logger.traceINFO("error is : " + error);
	}
	
	@Test
	public void test_cv_mc()
	{
		double meanError = 0;
		//List<Double> errors = new ArrayList<>();
		for(int i = 0; i < 1000; i++)
		{
			setUp();
			addNoise();
			List<Game> games = genGames();
			CrossValidationLeaveOneOut cv = new CrossValidationLeaveOneOut(games);
			double error = cv.runCrossValidation();
			//errors.add(error);
			meanError += error;
		}
		meanError /= 1000;
		Logger.traceINFO("error is : " + meanError);
	}
	
	@Test
	@Deprecated
	public void simulate_game()
	{
		double meanError = 0;
		List<Integer> pnl = new ArrayList<>();
		for(int i = 0; i < 100; i++)
		{
			setUp();
			addNoise();
			List<Game> games = genGames();
			CrossValidationLeaveOneOut cv = new CrossValidationLeaveOneOut(games);
			double error = cv.runCrossValidation();
			meanError += error;
			pnl.add(-9);
			if(error <= 4) { // 36/37-1 or 35/37-36/37
				//WIN
				pnl.add(36);
			}
		}
		meanError /= 100;
		Logger.traceINFO("error is : " + meanError);
		Logger.traceINFO("PNL : " + pnl.toString());
	}
}
