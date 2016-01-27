package framework_physics;

import org.junit.Test;

import computations.Constants;
import computations.Helper;
import logger.Logger;

public class MainTest extends TestClass
{
	static Game game1 = new Game("1", dbRef);
	static Game game2 = new Game("2", dbRef);
	static Game game3 = new Game("3", dbRef);
	static Game game4 = new Game("4", dbRef);
	static Game game5 = new Game("5", dbRef);
	static Game game8 = new Game("8", dbRef);
	static Game game9 = new Game("9", dbRef);
	static Game game12 = new Game("12", dbRef);
	static Game game13 = new Game("13", dbRef);

	@Test
	public void testPhysics3()
	{
		Constants.DEFAULT_SHIFT_PHASE = 5;
		Constants.CUTOFF_SPEED = 1.5;

		double error = 0.0;

		error += runTest(game1).error();
		error += runTest(game2).error();
		error += runTest(game3).error();
		error += runTest(game4).error();
		error += runTest(game5).error();
		error += runTest(game8).error();
		error += runTest(game9).error();
		error += runTest(game12).error();
		error += runTest(game13).error();

		error /= 9;

		System.out.println(error /= 9);
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
					error += runTest(game1).error();
					error += runTest(game2).error();
					error += runTest(game3).error();
					error += runTest(game4).error();
					error += runTest(game5).error();
					error += runTest(game8).error();
					error += runTest(game9).error();
					error += runTest(game12).error();
					error += runTest(game13).error();

					error /= 9;

					if (error < best_error)
					{
						best_error = error;
						System.out.println(Helper.printDigit(phase) + ";" + Helper.printDigit(speed) + ";" + Helper.printDigit(error));
					}

					// System.out.println(error /= 9);
				} catch (Exception e)
				{
					// System.out.println("E");
				}
			}
		}
	}

	@Test
	public void testPhysics()
	{
		Logger.setDebug(false); // No DEBUG.
		double best_error = Double.MAX_VALUE;
		for (double diam_wheel = 0.4; diam_wheel < 0.7; diam_wheel += 0.1) // 4
		{
			System.out.println("diam_wheel = " + diam_wheel);
			Constants.WHEEL_DIAMETER = diam_wheel;
			for (double diam_ball = diam_wheel + 0.1; diam_ball < 0.9; diam_ball += 0.1) // 3
			{
				System.out.println("diam_ball = " + diam_ball);
				Constants.CASE_DIAMETER = diam_ball;
				for (int phase = 1; phase < 30; phase++) // 30
				{
					Constants.DEFAULT_SHIFT_PHASE = phase;
					for (double speed = 1.0; speed < 2.0; speed += 0.01) // 100
					{
						Constants.CUTOFF_SPEED = speed;
						double error = 0.0;
						try
						{
							error += runTest(game1).error();
							error += runTest(game2).error();
							error += runTest(game3).error();
							error += runTest(game4).error();
							error += runTest(game5).error();
							error += runTest(game8).error();
							error += runTest(game9).error();
							error += runTest(game12).error();
							error += runTest(game13).error();

							error /= 9;

							if (error < best_error)
							{
								best_error = error;
								System.out.println(Helper.printDigit(diam_wheel) + ";" + Helper.printDigit(diam_ball) + ";" + Helper.printDigit(phase)
										+ ";" + Helper.printDigit(speed) + ";" + Helper.printDigit(error) + ";" + Helper.printDigit(best_error));
							}

							// System.out.println(error /= 9);
						} catch (Exception e)
						{
							// System.out.println("E");
						}
					}
				}
			}
		}
	}

}
