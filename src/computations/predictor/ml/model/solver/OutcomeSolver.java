package computations.predictor.ml.model.solver;

import computations.predictor.ml.model.DataRecord;
import servlets.SessionNotReadyException;

public interface OutcomeSolver
{
	public int predictOutcome(DataRecord predict) throws SessionNotReadyException;
}
