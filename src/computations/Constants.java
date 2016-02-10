package computations;

import computations.Wheel.WheelWay;
import computations.predictor.ml.model.recordsolver.ComplexWeightingSchemeSolver;
import computations.predictor.ml.model.recordsolver.OutcomeSolver;
import computations.predictor.ml.solver.FixedMostRecentMeasuresSolver;
import computations.predictor.ml.solver.PredictorSolver;

public class Constants
{
	/**
	 * TODO: To be measured. All is in m/s.
	 */
	// METERS
	public static double	WHEEL_DIAMETER	= 0.547;// = 0.60;
	public static double	CASE_DIAMETER	= 0.687;// = 0.80;

	public static double get_WHEEL_CIRCUMFERENCE()
	{
		return WHEEL_DIAMETER * Math.PI;
	}

	public static double get_BALL_CIRCUMFERENCE()
	{
		return CASE_DIAMETER * Math.PI;
	}

	public static final long		THRESHOLD_BEFORE_NEW_SESSION_IN_MS			= 30 * 1000;

	// Machine Learning
	public static int				NUMBER_OF_NEIGHBORS_KNN						= 5;
	public static int				RECORDS_COUNT_FOR_PREDICTION				= 5;
	public static PredictorSolver	PREDICTOR_SOLVER							= new FixedMostRecentMeasuresSolver();
	public static OutcomeSolver		DATARECORD_SOLVER							= new ComplexWeightingSchemeSolver();

	// Physics
	public static double			CUTOFF_SPEED								= 0.75;
	public static int				DEFAULT_SHIFT_PHASE							= 77;

	public static final int			MIN_NUMBER_OF_WHEEL_TIMES_BEFORE_PREDICTION	= 2;
	public static final int			MIN_NUMBER_OF_BALL_TIMES_BEFORE_PREDICTION	= 3;

	// There is only one way. Never change this.
	public static final WheelWay	DEFAULT_WHEEL_WAY							= WheelWay.ANTICLOCKWISE;

	public static final String		DATABASE_NAME								= "roulette_db";

	public static final double		TIME_LEFT_FOR_PLACING_BETS_SECONDS			= 5;

	public enum Type
	{
		BALL, //
		WHEEL
	}
}