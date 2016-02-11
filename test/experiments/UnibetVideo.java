package experiments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import computations.Wheel;
import computations.predictor.Predictor;
import computations.predictor.PredictorInterface;
import computations.predictor.physics.constantdeceleration.PredictorPhysicsConstantDeceleration;
import database.DatabaseAccessor;
import database.DatabaseAccessorInterface;
import utils.Game;
import utils.KFoldCrossValidationTest;
import utils.logger.Logger;

public class UnibetVideo
{
	@Test
	public void test2()
	{
		Logger.setDebug(false);
		DatabaseAccessorInterface dbRef = DatabaseAccessor.getInstance();
		List<Game> games = new ArrayList<>();
		for (int i = 19; i <= 80; i++)
		{
			games.add(new Game(String.valueOf(i), dbRef));
		}
		PredictorInterface predictorInterface = new PredictorInterface();
		Predictor predictor = predictorInterface.statisticalAnalysis();
		KFoldCrossValidationTest kfcv = new KFoldCrossValidationTest(games, predictor, dbRef, 2);
		System.out.println(kfcv.evaluate(games));

	}

	public void test()
	{
		List<Double> ballCumsumTimes = Arrays.asList(11.799, 12.350, 13.050, 13.716, 14.399, 15.166, 15.933, 16.816, 17.850, 19.250, 20.899, 22.816,
				24.833, 27.050);
		List<Double> wheelCumsumTimes = Arrays.asList(11.999, 16.616, 21.299, 26.050);

		PredictorPhysicsConstantDeceleration py = new PredictorPhysicsConstantDeceleration();

		int pred = py.predict(ballCumsumTimes, wheelCumsumTimes);
		System.out.println(pred);

		// line of diamonds hit at 27.599.
		// phase is 16 just before hitting the diamond (type is vertical).

		// difference is 27.599-27.050=0.549

		// outcome is 3, near the 0.
		System.out.println(Wheel.distanceBetweenNumbers(0, pred));
	}
}
