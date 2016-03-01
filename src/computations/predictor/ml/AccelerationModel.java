package computations.predictor.ml;

import computations.predictor.physics.PhysicsRegressionModel;
import computations.utils.Helper;

/**
 * Assuming the deceleration is constant. (1/Y) = AX + B. According to
 * Roulettecomputers.com, it is true.
 */
// TODO: integrate the physics model.
class AccelerationModel extends PhysicsRegressionModel
{
	// The model fitted is f(x) = 1/y, f linear.
	public double estimateSpeed(double time)
	{
		return Helper.inverseSpeed(evaluate(time));
	}

	AccelerationModel(double slope, double intercept)
	{
		super(slope, intercept);
	}

	@Override
	public String toString()
	{
		return "AccelerationModel" + super.toString();
	}

}
