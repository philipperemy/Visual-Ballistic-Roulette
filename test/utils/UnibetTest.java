package utils;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import computations.predictor.Predictor;
import computations.predictor.PredictorInterface;
import database.DatabaseAccessor;
import database.DatabaseAccessorInterface;
import logger.Logger;
import utils.KFoldCrossValidationTest.FilterSessionIds;

public class UnibetTest
{
	@Test
	public void test()
	{
		Logger.setDebug(false);
		DatabaseAccessorInterface dbRef = DatabaseAccessor.getInstance();
		List<Game> games = new ArrayList<>();
		for (int i = 19; i <= 80; i++)
		{
			games.add(new Game(String.valueOf(i), dbRef));
		}
		PredictorInterface predictorInterface = new PredictorInterface();
		Predictor predictor = predictorInterface.statisticalAnalysis2();
		KFoldCrossValidationTest kfcv = new KFoldCrossValidationTest(games, predictor, dbRef, 2);
		System.out.println(kfcv.getError());

		System.out.println(kfcv.evaluate(games, new FilterSessionIds()));
	}

}
