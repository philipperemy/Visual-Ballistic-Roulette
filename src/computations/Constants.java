package computations;

public class Constants
{

	/**
	 * TODO: To be measured
	 */
	// METERS
	public static final double WHEEL_DIAMETER = 0.80;
	public static final double CASE_DIAMETER = 1.20;

	public static final boolean IS_TEST_ENABLED = true;

	public static final double PI = Math.PI;

	public static final double WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER * PI;
	public static final double BALL_CIRCUMFERENCE = CASE_DIAMETER * PI;

	public static final long THRESHOLD_BEFORE_NEW_SESSION_IN_MS = 30 * 1000;

	public static final long TIME_BEFORE_FORECASTING = 9;

	public static int NUMBER_OF_NEIGHBORS_KNN = 6;

	public static final int REGION_HALF_SIZE = 3; // Region is 3+1+3 = 7.

	public static final String IP_ADDRESS = "89.87.50.128";
	public static final String IP_LOCALHOST = "localhost";

	public static final String SERVER_RESPONSE_QUERY_URL = "http://" + IP_ADDRESS + ":8080/RouletteServer/Response?sessionid=";

	public static final String LOCALHOST_SERVER_RESPONSE_QUERY_URL = "http://" + IP_LOCALHOST + ":8080/RouletteServer/Response?sessionid=";

	public static final long POLLING_INTERVAL_MS = 1000;

	public static final int MINIMUM_NUMBER_OF_WHEEL_TIMES_BEFORE_FORECASTING = 2;
	public static final int MINIMUM_NUMBER_OF_BALL_TIMES_BEFORE_FORECASTING = 3;

	public static final double VERY_HIGH_NUMBER = 1_000_000_000;

	public static final String WHEEL_CLOCKWISE = "1";
	public static final String WHEEL_ANTICLOCKWISE = "0";
}