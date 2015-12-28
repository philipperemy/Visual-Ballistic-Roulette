package computations.predictor;

import computations.Helper;
import computations.wheel.Type;
import logger.Logger;

/**
 * Assuming the deceleration is constant. (1/Y) = AX + B. According to Roulette
 * computers.com, it is true.
 */
class AccelerationModel
{
	@Override
	public String toString()
	{
		return "AccelerationModel" + type + " [slope=" + Helper.printDigit(slope) + ", intercept=" + Helper.printDigit(intercept) + "]";
	}

	double slope;
	double intercept;
	Type type;

	// The model fitted is f(x) = 1/y, f linear.
	public double estimateSpeed(double time)
	{
		return Helper.inverseSpeed(slope * time + intercept);
	}

	AccelerationModel(double slope, double intercept, Type type)
	{
		this.slope = slope;
		this.intercept = intercept;
		this.type = type;
		Logger.traceDEBUG(toString());
	}

}
