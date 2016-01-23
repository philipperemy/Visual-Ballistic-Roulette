package computations.predictor.ml.solver;

import java.util.List;

import computations.predictor.ml.PredictorML;
import servlets.SessionNotReadyException;

public interface PredictorSolver
{
	public int predict(PredictorML predictor, List<Double> ballLapTimes, List<Double> wheelLapTimes, String sessionId)
			throws SessionNotReadyException;
}
