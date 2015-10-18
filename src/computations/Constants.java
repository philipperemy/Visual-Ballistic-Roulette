package computations;

public class Constants {

	/**
	 * TODO: To be measured
	 */
	// METERS
	public static final double WHEEL_DIAMETER = 0.80;
	public static final double CASE_DIAMETER = 1.20;

	public static final double PI = Math.PI;

	public static final double WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER * PI;
	public static final double BALL_CIRCUMFERENCE = CASE_DIAMETER * PI;

	public static final long THRESHOLD_BEFORE_NEW_SESSION_IN_MS = 30 * 1000;

	public static final long TIME_BEFORE_FORECASTING = 9;
	public static final int NUMBER_OF_SPEEDS_IN_DATASET = 3;

	public static final double EPSILON_SPEED_FOR_KNN1 = 0.1;
	public static final double NUMBER_OF_NEIGHBORS_KNN = 5;

}