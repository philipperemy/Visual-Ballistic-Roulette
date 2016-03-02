package computations.predictor.ml.model.recordsolver;

import computations.predictor.ml.model.DataRecord;

/**
 * A data record corresponds to a <ball speed, wheel speed, phase> We retrieve
 * from the database all the similar games and we predict the outcome.
 *
 */
public interface OutcomeSolver
{
	public int predictOutcome(DataRecord predict);
}
