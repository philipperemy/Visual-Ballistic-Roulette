package utils;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import computations.Constants;
import computations.predictor.Predictor;
import computations.predictor.PredictorInterface;
import database.DatabaseAccessor;
import database.DatabaseAccessorInterface;
import utils.logger.Logger;

public class UnibetTest
{
	@Test
	public void test()
	{
		Logger.setDebug(true);
		DatabaseAccessorInterface dbRef = DatabaseAccessor.getInstance();
		List<Game> games = new ArrayList<>();
		for (int i = 19; i <= 80; i++)
		{
			games.add(new Game(String.valueOf(i), dbRef));
		}
		PredictorInterface predictorInterface = new PredictorInterface();
		Predictor predictor = predictorInterface.machineLearning();
		KFoldCrossValidationTest kfcv = new KFoldCrossValidationTest(games, predictor, dbRef, 3);
		kfcv.run();

		Constants.CUTOFF_SPEED = 0.95;
		Constants.DEFAULT_SHIFT_PHASE = 45;
		System.out.println("eval= " + kfcv.evaluate(games));
	}
}
