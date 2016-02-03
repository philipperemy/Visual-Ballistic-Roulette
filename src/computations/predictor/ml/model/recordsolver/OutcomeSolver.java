package computations.predictor.ml.model.recordsolver;

import computations.predictor.ml.model.DataRecord;

public interface OutcomeSolver
{
	public int predictOutcome(DataRecord predict);
}
