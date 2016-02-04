package utils;

import java.util.ArrayList;
import java.util.List;

import computations.predictor.Predictor;
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
			error += runCrossValidationMachineLearning(trainingSet, validationSet);
		}
		error /= K;
		return error;
	}

	private List<String> getAllSessionIdsButOne(List<Game> gameSet, Game gameToExclude)
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

	private double runCrossValidationPhysics(List<Game> trainingSet, List<Game> validationSet)
	{
		return 0;
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
					trainingError += runTest(trainingGame, getAllSessionIdsButOne(allGames, trainingGame)).error();
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
