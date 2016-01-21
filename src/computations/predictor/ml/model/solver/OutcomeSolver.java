package computations.predictor.ml.model.solver;

import computations.predictor.ml.model.DataRecord;

public interface OutcomeSolver
{
	public int predictOutcome(DataRecord predict);
}
