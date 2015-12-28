package computations.predictor.solver;

import java.util.List;

import computations.predictor.Predictor;
import servlets.SessionNotReadyException;

public interface PredictorSolver
{
	public int predict(Predictor predictor, List<Double> ballLapTimes, List<Double> wheelLapTimes, String sessionId) throws SessionNotReadyException;
}
