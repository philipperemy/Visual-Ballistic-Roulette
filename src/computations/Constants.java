package computations;

import computations.wheel.Wheel.WheelWay;

public class Constants
{
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

	public static int NUMBER_OF_NEIGHBORS_KNN = 3;

	public static final int REGION_HALF_SIZE = 3; // Region is 3+1+3 = 7.

	public static final int MINIMUM_NUMBER_OF_WHEEL_TIMES_BEFORE_FORECASTING = 2;
	public static final int MINIMUM_NUMBER_OF_BALL_TIMES_BEFORE_FORECASTING = 3;
	public static final int RECORDS_COUNT_FOR_PREDICTION = 5;

	public static final String ERRORLEVEL_PROCESS_EXCEPTION_TAG = "-1";
	public static final String ERRORLEVEL_SESSION_NOT_READY_STRING = "SESSION_NOT_READY_WHEEL_COUNT_ACTUAL";

	public static final WheelWay DEFAULT_WHEEL_WAY = WheelWay.ANTICLOCKWISE;

}