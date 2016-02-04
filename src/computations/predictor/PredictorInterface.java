package computations.predictor;

import computations.predictor.ml.PredictorMachineLearning;
import computations.predictor.physics.PredictorPhysics;
import computations.predictor.statistical.PredictorStatisticalAnalysis;

public class PredictorInterface
{
	private volatile Predictor					predictorMachineLearning		= new PredictorMachineLearning();
	private volatile Predictor					predictorPhysics				= new PredictorPhysics();
	private volatile Predictor					predictorStatisticalAnalysis	= new PredictorStatisticalAnalysis();

	private static volatile PredictorInterface	instance						= PredictorInterface.getInstance();

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

	public Predictor statisticalAnalysis()
	{
		return predictorStatisticalAnalysis;
	}
}
