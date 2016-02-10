package framework.test;

import java.util.List;

import computations.Constants;
import framework.CrossValidationLeaveOneOut;
import framework.TestClass;
import framework.games.Game;
import utils.logger.Logger;

public class OptimizeParameters extends TestClass
{

	public void test()
	{
		try
		{
			Logger.stopLogging();
			double best_error = Double.MAX_VALUE;
			for (int i = 1; i < 10; i++)
			{
				for (int j = 1; j < 10; j++)
				{
					Constants.NUMBER_OF_NEIGHBORS_KNN = i;
					Constants.RECORDS_COUNT_FOR_PREDICTION = j;
					double actual_error = run_error();
					if (actual_error < best_error)
					{
						best_error = actual_error;
					}
					System.out.println("NUMBER_OF_NEIGHBORS_KNN = " + i + ", RECORDS_COUNT_FOR_PREDICTION = " + j + ", best error = " + best_error
							+ ", actual error = " + actual_error);
				}
			}
		} catch (Exception e)
		{
			throw e;
		} finally
		{
			Logger.enableLogging();
		}
	}

	// @Test
	public void test2()
	{
		Logger.stopLogging();
		double best_error = Double.MAX_VALUE;
		for (int phase = 13; phase < 30; phase++)
		{
			for (double cutoffSpeed = 1.00; cutoffSpeed < 2.50; cutoffSpeed += 0.01)
			{
				try
				{
					Constants.CUTOFF_SPEED = cutoffSpeed;
					Constants.DEFAULT_SHIFT_PHASE = phase;
					double actual_error = run_error();
					if (actual_error < best_error)
					{
						best_error = actual_error;
					}
					System.out.println("actual= " + actual_error + ", best= " + best_error + ", cutoff=" + cutoffSpeed + ", phase=" + phase);
				} catch (Exception e)
				{
				}
			}
		}

		Logger.enableLogging();

		// Constants.CUTOFF_SPEED = 1.55;
		// System.out.println("error = " + run_error());
	}

	public double run_error()
	{
		setUp();
		addNoise();
		List<Game> games = genGames();
		CrossValidationLeaveOneOut cv = new CrossValidationLeaveOneOut(games);
		return cv.runCrossValidation();
	}
}
