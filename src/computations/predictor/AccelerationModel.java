package computations.predictor;

import computations.Helper;
import computations.wheel.Type;
import log.Logger;

/**
 * Assuming the deceleration is constant. (1/Y) = AX + B
 */
public class AccelerationModel {

	@Override
	public String toString() {
		return "AccelerationModel" + type + " [slope=" + Helper.printDigit(slope) + ", intercept=" + Helper.printDigit(intercept) + "]";
	}

	double slope;
	double intercept;
	Type type;

	// The model fitted is f(x) = 1/y, f linear.
	public double estimateSpeed(double time) {
		return BallisticManager.inverseSpeed(slope * time + intercept);
	}

	public AccelerationModel(double A, double B, Type type) {
		this.slope = A;
		this.intercept = B;
		this.type = type;
		Logger.traceINFO(toString());
	}

	public AccelerationModel() {
	}

}
