package computations.predictor;

import computations.predictor.ml.PredictorMachineLearning;
import computations.predictor.physics.PredictorPhysics;

public class PredictorInterface
{
	private volatile Predictor					predictorMachineLearning	= new PredictorMachineLearning();
	private volatile Predictor					predictorPhysics			= new PredictorPhysics();

	private static volatile PredictorInterface	instance					= PredictorInterface.getInstance();

	public static PredictorInterface getInstance()
	{
		if (instance == null)
		{
			instance = new PredictorInterface();
		}
		return instance;
	}

	public Predictor machineLearning()
	{
		return predictorMachineLearning;
	}

	public Predictor physics()
	{
		return predictorPhysics;
	}
}
