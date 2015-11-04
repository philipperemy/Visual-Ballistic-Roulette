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

public class Test1 extends TestClass
{
	@Test
	public void t1()
	{
		List<Game> games = new ArrayList<>();
		games.add(new Game1());
		games.add(new Game2());
		CrossValidationLeaveOneOut cv = new CrossValidationLeaveOneOut(games);
		double error = cv.runCrossValidation();
		Logger.traceINFO("error is : " + error);
	}

	@Test
	public void t2()
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
		CrossValidationLeaveOneOut cv = new CrossValidationLeaveOneOut(games);
		double error = cv.runCrossValidation();
		Logger.traceINFO("error is : " + error);
	}
}
