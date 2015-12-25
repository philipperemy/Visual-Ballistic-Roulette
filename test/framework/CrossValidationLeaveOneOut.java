package framework;

import java.util.ArrayList;
import java.util.List;

import database.DatabaseAccessorStub;
import framework.games.Game;
import logger.Logger;

public class CrossValidationLeaveOneOut
{
	List<Game> games;

	public CrossValidationLeaveOneOut(List<Game> games)
	{
		this.games = games;
	}

	public double runCrossValidation()
	{
		List<TestResult> trs = new ArrayList<>();
		for (int i = 0; i < games.size(); i++)
		{
			((DatabaseAccessorStub) TestClass.dbRef).reset();
			Game predict = games.get(i);
			List<Game> trainingSet = new ArrayList<>();
			for (int j = 0; j < games.size(); j++)
			{
				if (j != i)
				{
					trainingSet.add(games.get(j));
				}
			}
			TestResult res = TestClass.runTest(trainingSet, predict);
			trs.add(res);
		}

		double mean_error = 0.0;
		for (TestResult tr : trs)
		{
			int cur_error = tr.error();
			Logger.traceINFO("Error : " + cur_error);
			mean_error += cur_error;
		}
		mean_error /= trs.size();
		return mean_error;
	}
}
