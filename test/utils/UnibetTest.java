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
	public void testPhysics()
	{
		Logger.setDebug(false);
		DatabaseAccessorInterface dbRef = DatabaseAccessor.getInstance();
		List<Game> games = new ArrayList<>();
		for (int i = 19; i <= 130; i++)
		{
			games.add(new Game(String.valueOf(i), dbRef));
		}
		PredictorInterface predictorInterface = new PredictorInterface();
		Predictor predictor = predictorInterface.physicsConstantDeceleration();
		KFoldCrossValidationTest kfcv = new KFoldCrossValidationTest(games, predictor, dbRef, 3);
		// kfcv.run();

		Constants.CUTOFF_SPEED = 1.05;
		Constants.DEFAULT_SHIFT_PHASE = 20;
		List<Double> errors = new ArrayList<>();
		for(int i = 2; i <= 8; i++) {
			TestConstants.NUMBER_OF_BALL_LAP_TIMES_TO_DISCARD = i;
			errors.add(kfcv.evaluate(games));
		}
		System.out.println("error (2, 3, 4, 5, 6, 7, 8) = " + errors.toString());
	}

	//@Test
	public void testMachineLearning()
	{
		Logger.setDebug(false);
		DatabaseAccessorInterface dbRef = DatabaseAccessor.getInstance();
		List<Game> games = new ArrayList<>();
		for (int i = 19; i <= 130; i++)
		{
			games.add(new Game(String.valueOf(i), dbRef));
		}
		PredictorInterface predictorInterface = new PredictorInterface();
		Predictor predictor = predictorInterface.machineLearning();
		KFoldCrossValidationTest kfcv = new KFoldCrossValidationTest(games, predictor, dbRef, 2);
		kfcv.run();
	}
}
