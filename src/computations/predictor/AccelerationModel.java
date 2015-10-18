package computations.predictor;

import log.Logger;

/**
 * Assuming the deceleration is constant. (1/Y) = AX + B
 */
public class AccelerationModel {

	@Override
	public String toString() {
		return "AccelerationModel [slope=" + slope + ", intercept=" + intercept + "]";
	}

	double slope;
	double intercept;

	// The model fitted is f(x) = 1/y, f linear.
	public double estimateSpeed(double time) {
		return BallisticManager.inverseSpeed(slope * time + intercept);
	}

	public AccelerationModel(double A, double B) {
		this.slope = A;
		this.intercept = B;
		Logger.traceINFO(toString());
	}

	public AccelerationModel() {
	}

}
