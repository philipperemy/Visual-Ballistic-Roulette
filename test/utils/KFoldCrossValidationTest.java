package utils;

import java.util.ArrayList;
import java.util.List;

import computations.Constants;
import computations.predictor.Predictor;
import computations.predictor.ml.PredictorMachineLearning;
import computations.predictor.physics.PredictorPhysics;
import computations.predictor.statistical.PredictorStatisticalAnalysis;
import computations.utils.Helper;
import database.DatabaseAccessorInterface;
import utils.logger.Logger;

public class KFoldCrossValidationTest extends TestClass
{
	private List<Game> games = new ArrayList<>();
	private int K = 2; // number of times you fold.

	public KFoldCrossValidationTest(List<Game> games, Predictor predictor, DatabaseAccessorInterface dbRef, int K)
	{
		super(predictor, dbRef);
		this.games = games;
		this.K = K;
	}

	public void run()
	{
		List<List<Game>> gameFolds = Helper.split(games, (int) Math.floor(games.size() / K));
		double error = 0.0;
		// Select one set as the validation set and all the others are gathered
		// in the training set.
		// Do that for every possible combination.
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

			// Specific call depending on the type of the predictor.
			if (predictor instanceof PredictorMachineLearning)
			{
				error += runCrossValidationMachineLearning(trainingSet, validationSet);
			} else if (predictor instanceof PredictorPhysics)
			{
				error += runCrossValidationPhysics(trainingSet, validationSet);
			} else if (predictor instanceof PredictorStatisticalAnalysis)
			{
				error += runCrossValidationStatistics(trainingSet, validationSet);
			} else
			{
				throw new RuntimeException("Invalid predictor.");
			}
		}
		error /= K;
		Logger.traceINFO("[validation] final error= " + error);
	}

	private double runCrossValidationStatistics(List<Game> trainingSet, List<Game> validationSet)
	{
		double bestTrainingError = Double.MAX_VALUE;
		Integer best_phase = null;
		for (int phase = 1; phase < 60; phase++)
		{
			Constants.DEFAULT_SHIFT_PHASE = phase;
			try
			{
				double trainingSetError = evaluate(trainingSet);
				if (trainingSetError < bestTrainingError)
				{
					bestTrainingError = trainingSetError;
					best_phase = phase;
					System.out.println("err=" + bestTrainingError + ", phase=" + best_phase);
				}
			} catch (Exception e)
			{
				// Do nothing.
			}
		}

		if (best_phase == null)
		{
			throw new RuntimeException("Error in the cross validation.");
		}

		double validationSetError = evaluate(validationSet);
		System.out.println("valid=" + validationSetError);
		return validationSetError;
	}

	public static List<String> getAllSessionIdsButOne(List<Game> gameSet, Game gameToExclude)
	{
		String sessionIdToExclude = gameToExclude.get_sessionId();
		List<String> sessionIds = new ArrayList<>();
		for (Game game : gameSet)
		{
			String currentSessionId = game.get_sessionId();
			if (!currentSessionId.equals(sessionIdToExclude))
			{
				sessionIds.add(game.get_sessionId());
			}
		}
		return sessionIds;
	}

	public double evaluate(List<Game> trainingSet)
	{
		double trainingError = 0.0;
		try
		{
			int trainingSampleSize = 0;
			for (Game trainingGame : trainingSet)
			{
				try
				{
					double curError = runTest(trainingGame, getAllSessionIdsButOne(trainingSet, trainingGame)).error();
					trainingError += curError;
					trainingSampleSize++;
					if (trainingSampleSize < trainingSet.size() * 0.7 || trainingError > 100)
					{
						throw new RuntimeException();
					}
				} catch (Exception e)
				{
				}
			}
			trainingError /= trainingSampleSize;
			System.out.println("trainingSampleSize=" + trainingSampleSize);
		} catch (Exception e)
		{
			throw new RuntimeException();
		}

		return trainingError;
	}

	private double runCrossValidationPhysics(List<Game> trainingSet, List<Game> validationSet)
	{
		System.out.println("__________________________");
		double bestTrainingError = Double.MAX_VALUE;
		Double best_cutoffSpeed = null;
		Integer best_phase = null;

		double cutoffSpeed = 1;
		Constants.CUTOFF_SPEED = cutoffSpeed;
		System.out.println("[training] set= " + trainingSet.toString());
		for (int phase = 1; phase < 60; phase++)
		{
			Constants.DEFAULT_SHIFT_PHASE = phase;
			try
			{
				double trainingSetError = evaluate(trainingSet);
				if (trainingSetError < bestTrainingError)
				{
					bestTrainingError = trainingSetError;
					best_cutoffSpeed = cutoffSpeed;
					best_phase = phase;
					System.out.println("[training] error= " + bestTrainingError + ", scatter phase= " + Helper.printDigit(best_phase)
							+ ", cutoff speed= " + Helper.printDigit(best_cutoffSpeed));
				}
			} catch (Exception e)
			{
				// Do nothing.
			}
		}

		if (best_cutoffSpeed == null || best_phase == null)
		{
			throw new RuntimeException("Error in the cross validation.");
		}

		double validationSetError = evaluate(validationSet);
		System.out.println("[validation] set= " + validationSet.toString());
		System.out.println("[validation] error= " + validationSetError);
		System.out.println("__________________________");
		return validationSetError;
	}

	private double runCrossValidationMachineLearning(List<Game> trainingSet, List<Game> validationSet)
	{
		double bestTrainingError = evaluate(trainingSet);
		double bestValidationError = evaluate(validationSet);
		return 0.5 * bestTrainingError + 0.5 * bestValidationError;
	}
}
