package computations.predictor;

import computations.predictor.ml.PredictorML;
import computations.predictor.physics.PredictorPhysics;

public class PredictorInterface
{

	private volatile PredictorML predictorMachineLearning = PredictorML.getInstance();
	private volatile PredictorPhysics predictorPhysics = PredictorPhysics.getInstance();

	private static volatile PredictorInterface instance = PredictorInterface.getInstance();

	public static PredictorInterface getInstance()
	{
		if (instance == null)
		{
			instance = new PredictorInterface();
		}
		return instance;
	}

	public PredictorML machineLearning()
	{
		return predictorMachineLearning;
	}

	public PredictorPhysics physics()
	{
		return predictorPhysics;
	}
}
