package framework_physics.cv;

import java.util.ArrayList;
import java.util.List;

import computations.Constants;
import computations.utils.Helper;
import framework_physics.Game;
import framework_physics.TestClass;

public class KFoldCrossValidation
{
	private List<Game>	games;
	private int			K	= 3;	// number of times you fold.

	public KFoldCrossValidation(List<Game> games, int K)
	{
		if (K < 2)
		{
			throw new IllegalArgumentException();
		}

		this.games = games;
		this.K = K;
	}

	public double getError()
	{
		List<List<Game>> gameFolds = split(games, (int) Math.floor(games.size() / K));
		double error = 0.0;
		for (int foldId = 0; foldId < K; foldId++)
		{
			List<Game> validationSet = gameFolds.get(foldId);
			List<Game> trainingSet = new ArrayList<>();
			for (int i = 0; i < K; i++)
			{
				if (i != foldId)
				{
					trainingSet.addAll(gameFolds.get(i));
				}
			}
			error += runCrossValidation(trainingSet, validationSet);
		}
		error /= K;
		return error;
	}

	private double runCrossValidation(List<Game> trainingSet, List<Game> validationSet)
	{
		double bestTrainingError = Double.MAX_VALUE;
		int bestPhase = -1;
		double bestSpeed = -1;
		for (int phase = 1; phase < 60; phase++) // 30
		{
			Constants.DEFAULT_SHIFT_PHASE = phase;
			// for (double speed = 1.15; speed < 2.0; speed += 0.001) // 100
			// {
			// Constants.CUTOFF_SPEED = speed;
			try
			{
				double trainingError = 0.0;
				int traininSampleSize = 0;
				for (Game trainingGame : trainingSet)
				{
					try
					{
						trainingError += TestClass.runTest(trainingGame).error();
						traininSampleSize++;
					} catch (Exception e)
					{

					}
				}
				trainingError /= traininSampleSize;
				if (trainingError < bestTrainingError && traininSampleSize >= trainingSet.size() * 0.7)
				{
					bestTrainingError = trainingError;
					bestPhase = phase;
					System.out.println("count=" + traininSampleSize + ", training error = " + trainingError + ", phase = " + phase);
				}
			} catch (Exception e)
			{
				System.out.println("E");
			}
		}

		Constants.DEFAULT_SHIFT_PHASE = bestPhase;
		Constants.CUTOFF_SPEED = bestSpeed;
		System.out.println("best phase =" + Helper.printDigit(bestPhase) + "; best speed = " + Helper.printDigit(bestSpeed));
		// Evaluation on the validation set

		double validationError = 0.0;
		int totalOfValidations = 0;
		for (Game validationGame : validationSet)

		{
			try
			{
				validationError += TestClass.runTest(validationGame).error();
				totalOfValidations++;
			} catch (Exception e)
			{
			}
		}
		validationError /= totalOfValidations;
		System.out.println("valid error = " + Helper.printDigit(validationError) + ", total validations = " + totalOfValidations);
		return validationError;

	}

	static <T> List<List<T>> split(List<T> list, final int L)
	{
		List<List<T>> parts = new ArrayList<List<T>>();
		final int N = list.size();
		for (int i = 0; i < N; i += L)
		{
			parts.add(new ArrayList<T>(list.subList(i, Math.min(N, i + L))));
		}
		return parts;
	}
}
