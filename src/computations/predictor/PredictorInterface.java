package computations.predictor;

import computations.predictor.ml.PredictorMachineLearning;
import computations.predictor.physics.constantdeceleration.PredictorPhysicsConstantDeceleration;
import computations.predictor.physics.linearlaptimes.PredictorPhysicsLinearLaptimes;
import computations.predictor.statistical.PredictorStatisticalAnalysis;

public class PredictorInterface
{
	private volatile Predictor predictorMachineLearning = new PredictorMachineLearning();
	private volatile Predictor physicsLinearLapTimes = new PredictorPhysicsLinearLaptimes();
	private volatile Predictor predictorStatisticalAnalysis = new PredictorStatisticalAnalysis();
	private volatile Predictor predictorPhysicsConstantDeceleration = new PredictorPhysicsConstantDeceleration();

	private static PredictorInterface instance = PredictorInterface.getInstance();

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

	public Predictor physicsLinearLapTimes()
	{
		return physicsLinearLapTimes;
	}

	public Predictor statisticalAnalysis()
	{
		return predictorStatisticalAnalysis;
	}

	public Predictor physicsConstantDeceleration()
	{
		return predictorPhysicsConstantDeceleration;
	}
}
