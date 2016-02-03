package computations.predictor.ml.model.recordsolver;

import computations.predictor.ml.model.DataRecord;
import exceptions.SessionNotReadyException;

public interface OutcomeSolver
{
	public int predictOutcome(DataRecord predict) throws SessionNotReadyException;
}
