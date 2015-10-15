package computations.predictor;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import computations.Constants;
import computations.wheel.Type;

public class BallisticManager {

	/**
	 * Assuming the deceleration is constant. Y = AX + B
	 */
	public static class AccelerationModel {
		@Override
		public String toString() {
			return "AccelerationModel [A=" + A + ", B=" + B + "]";
		}

		double A;
		double B;

		public double estimateSpeed(double time) {
			return A * time + B;
		}

		public AccelerationModel(double A, double B) {
			this.A = A;
			this.B = B;
		}

		public AccelerationModel() {
		}
	}

	// Could interpolate with ML stuffs.
	public static double getTimeForOneBallLoop(double ballSpeed) {
		return Constants.BALL_CIRCUMFERENCE / (ballSpeed * 100);
	}

	// Could interpolate with ML stuffs.
	public static double getTimeForOneWheelLoop(double wheelSpeed) {
		return Constants.WHEEL_CIRCUMFERENCE / wheelSpeed;
	}

	// m/s. T1 and T2

	private static double getSpeed(double t1, double t2, Type type) {
		switch (type) {
		case BALL:
			return getBallSpeed(t1, t2);
		case WHEEL:
			return getWheelSpeed(t1, t2);
		default:
			throw new RuntimeException();
		}
	}

	public static double getBallSpeed(double t1, double t2) {
		return Constants.BALL_CIRCUMFERENCE / (t2 - t1);
	}

	// m/s
	public static double getWheelSpeed(double t1, double t2) {
		return Constants.WHEEL_CIRCUMFERENCE / (t2 - t1);
	}

	/**
	 * Each speed is the average of the measurements intervals
	 */
	public static List<ClockSpeed> computeInstantAverageSpeeds(List<Double> lapTimes, Type type) {
		List<ClockSpeed> SpeedMeasurements = new ArrayList<>(lapTimes.size()-1);
		for (int i = 1; i < lapTimes.size(); i++) {
			double t1 = lapTimes.get(i - 1);
			double t2 = lapTimes.get(i);
			ClockSpeed SpeedMeasurement = new ClockSpeed();
			SpeedMeasurement.speed = getSpeed(t1, t2, type);
			SpeedMeasurement.time = 0.5 * t1 + 0.5 * t2;
			SpeedMeasurements.add(SpeedMeasurement);
		}
		return SpeedMeasurements;
	}

	@SuppressWarnings("unused")
	private static AccelerationModel performLinearRegression(List<ClockSpeed> speedMeasurements) {
		int n = speedMeasurements.size();
		double[] x = new double[n];
		double[] y = new double[n];
		for (int i = 0; i < n; i++) {
			x[i] = speedMeasurements.get(i).time;
			y[i] = speedMeasurements.get(i).speed;
		}
		
		SimpleRegression regression = new SimpleRegression();
		for(int i = 0; i <n; i++) {
			regression.addData(x[i], y[i]);			
		}
		
		return new AccelerationModel(regression.getSlope(), regression.getIntercept());
	}

	/*
	 * TODO: do not perform linear regressions on speed but on times. Speeds are not linear.
	 */
	// Should not be used
	private static AccelerationModel performSimpleLinearRegression(List<ClockSpeed> speedMeasurements) {
		ClockSpeed start = speedMeasurements.get(0);
		ClockSpeed end = speedMeasurements.get(speedMeasurements.size() - 1);
		AccelerationModel ac = new AccelerationModel();
		ac.A = (end.speed - start.speed) / (end.time - start.time);
		ac.B = end.speed - ac.A * end.time;
		return ac;
	}

	public static AccelerationModel computeModel(List<Double> times, Type type) {
		List<ClockSpeed> speeds = BallisticManager.computeInstantAverageSpeeds(times, type);
		return BallisticManager.performSimpleLinearRegression(speeds); //TODO: use the other when it works
	}

}
