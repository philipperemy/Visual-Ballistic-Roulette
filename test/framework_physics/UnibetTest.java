package framework_physics;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import computations.Constants;
import computations.utils.Helper;
import framework_physics.cv.KFoldCrossValidation;
import utils.logger.Logger;

public class UnibetTest extends TestClass
{

	// @Test
	public void testPhysics4()
	{
		Logger.setDebug(false);
		List<Game> games = new ArrayList<>();
		for (int i = 19; i <= 80; i++)
		{
			games.add(new Game(String.valueOf(i), dbRef));
		}

		List<List<Double>> list = new ArrayList<>();
		for (Game g : games)
		{
			try
			{
				list.add(g.get_ballLaptimes());
			} catch (Exception e)
			{

			}
		}

		// should add diff times.
		// py.init(list);

		KFoldCrossValidation kfcv = new KFoldCrossValidation(games, 2);
		System.out.println(kfcv.getError());
	}

	@Test
	public void testPhysics2()
	{
		double best_error = Double.MAX_VALUE;
		Logger.setDebug(false); // No DEBUG.
		for (int phase = 1; phase < 100; phase++) // 30
		{
			Constants.DEFAULT_SHIFT_PHASE = phase;
			for (double speed = 1.0; speed < 3.0; speed += 0.001) // 100
			{
				Constants.CUTOFF_SPEED = speed;
				double error = 0.0;
				try
				{
					int errorCount = 0;
					for (int i = 0; i < games.size(); i++)
					{
						try
						{
							error += runTest(games.get(i)).error();
							errorCount++;
						} catch (NullPointerException npe)
						{

						}

					}

					error /= errorCount;
					if (error < best_error)
					{
						best_error = error;
						System.out.println(Helper.printDigit(phase) + ";" + Helper.printDigit(speed) + ";" + Helper.printDigit(error));
					}

				} catch (Exception e)
				{
				}
			}
		}
	}
}
