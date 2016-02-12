package computations.predictor.ml;

import computations.Constants;
import computations.utils.Helper;

/**
 * Assuming the deceleration is constant. (1/Y) = AX + B. According to
 * Roulettecomputers.com, it is true.
 */
// TODO: integrate the physics model.
class AccelerationModel
{
	@Override
	public String toString()
	{
		return "AccelerationModel" + type + " [slope=" + Helper.printDigit(slope) + ", intercept=" + Helper.printDigit(intercept) + "]";
	}

	double			slope;
	double			intercept;
	Constants.Type	type;

	// The model fitted is f(x) = 1/y, f linear.
	public double estimateSpeed(double time)
	{
		return Helper.inverseSpeed(slope * time + intercept);
	}

	AccelerationModel(double slope, double intercept, Constants.Type type)
	{
		this.slope = slope;
		this.intercept = intercept;
		this.type = type;
	}
}
