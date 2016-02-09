package utils;

import java.util.ArrayList;
import java.util.List;

import computations.Constants;
import computations.predictor.Predictor;
import computations.predictor.ml.PredictorMachineLearning;
import computations.predictor.physics.PredictorPhysics;
import computations.predictor.statistical.PredictorStatisticalAnalysis;
import computations.predictor.statistical.PredictorStatisticalAnalysis2;
import computations.utils.Helper;
import database.DatabaseAccessorInterface;

public class KFoldCrossValidationTest extends TestClass
{
	private List<Game>	games	= new ArrayList<>();
	private int			K		= 2;				// number of times you fold.

	public KFoldCrossValidationTest(List<Game> games, Predictor predictor, DatabaseAccessorInterface dbRef, int K)
	{
		super(predictor, dbRef);
		this.games = games;
		this.K = K;
	}

	public double getError()
	{
		List<List<Game>> gameFolds = Helper.split(games, (int) Math.floor(games.size() / K));
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

			if (predictor instanceof PredictorMachineLearning)
			{
				error += runCrossValidationMachineLearning(trainingSet, validationSet);
			} else if (predictor instanceof PredictorPhysics | predictor instanceof PredictorStatisticalAnalysis2)
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
		return error;
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
				double trainingSetError = evaluate(trainingSet, new FilterSessionIds());
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

		double validationSetError = evaluate(validationSet, new FilterSessionIds());
		System.out.println("valid=" + validationSetError);
		return validationSetError;
	}

	public interface FilterSessionIdsInterface
	{
		List<String> getAllSessionIdsButOne(List<Game> gameSet, Game gameToExclude);
	}

	public static class FilterSessionIds implements FilterSessionIdsInterface
	{
		public List<String> getAllSessionIdsButOne(List<Game> gameSet, Game gameToExclude)
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

	}

	public double evaluate(List<Game> trainingSet, FilterSessionIdsInterface fsii)
	{
		double trainingError = 0.0;
		try
		{
			int trainingSampleSize = 0;
			for (Game trainingGame : trainingSet)
			{
				try
				{
					double curError = runTest(trainingGame, fsii.getAllSessionIdsButOne(trainingSet, trainingGame)).error();
					System.out.println(curError);
					trainingError += curError;
					trainingSampleSize++;
					if (trainingSampleSize < trainingSet.size() * 0.7 || trainingError > 100)
					{
						throw new RuntimeException();
					}
				} catch (Exception e)
				{
					// System.err.println("cannot compute.");
				}
			}
			trainingError /= trainingSampleSize;
		} catch (Exception e)
		{
			throw new RuntimeException();
		}

		return trainingError;
	}

	private double runCrossValidationPhysics(List<Game> trainingSet, List<Game> validationSet)
	{
		double bestTrainingError = Double.MAX_VALUE;
		Double best_cutoffSpeed = null;
		Integer best_phase = null;
		long count = 0;

		for (int phase = 1; phase < 60; phase++)
		{
			Constants.DEFAULT_SHIFT_PHASE = phase;
			for (double cutoffSpeed = 1.0; cutoffSpeed < 2.0; cutoffSpeed += 0.01)
			{
				Constants.CUTOFF_SPEED = cutoffSpeed;
				try
				{
					double trainingSetError = evaluate(trainingSet, new FilterSessionIds());
					if (trainingSetError < bestTrainingError)
					{
						bestTrainingError = trainingSetError;
						best_cutoffSpeed = cutoffSpeed;
						best_phase = phase;
						System.out.println(
								"e=" + bestTrainingError + ", p=" + Helper.printDigit(best_phase) + ", cs=" + Helper.printDigit(best_cutoffSpeed));
					}
				} catch (Exception e)
				{
					// Do nothing.
				}
			}
		}

		if (best_cutoffSpeed == null || best_phase == null)
		{
			throw new RuntimeException("Error in the cross validation.");
		}

		double validationSetError = evaluate(validationSet, new FilterSessionIds());
		return validationSetError;
	}

	private double runCrossValidationMachineLearning(List<Game> trainingSet, List<Game> validationSet)
	{
		List<Game> allGames = new ArrayList<>(trainingSet);
		allGames.addAll(validationSet);
		double bestTrainingError = Double.MAX_VALUE;
		try
		{
			double trainingError = 0.0;
			int traininSampleSize = 0;
			for (Game trainingGame : allGames)
			{
				try
				{
					trainingError += runTest(trainingGame, new FilterSessionIds().getAllSessionIdsButOne(allGames, trainingGame)).error();
					traininSampleSize++;
				} catch (Exception e)
				{

				}
			}
			trainingError /= traininSampleSize;
			if (trainingError < bestTrainingError && traininSampleSize >= allGames.size() * 0.7)
			{
				bestTrainingError = trainingError;
				System.out.println("count=" + traininSampleSize + ", training error=" + trainingError);
			}
		} catch (Exception e)
		{
			throw new RuntimeException();
		}
		return bestTrainingError;
	}

}
