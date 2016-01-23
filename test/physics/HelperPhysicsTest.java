package physics;

import org.junit.Assert;
import org.junit.Test;

import computations.Constants;
import computations.predictor.physics.HelperPhysics;
import computations.predictor.physics.LapTimeRegressionModel;
import computations.predictor.physics.PositiveValueExpectedException;

public class HelperPhysicsTest
{

	private static final double EPSILON = 0.001;

	@Test
	public void testEstimatePhaseAngleDegrees()
	{
		Assert.assertEquals(0.0, HelperPhysics.estimatePhaseAngleDegrees(100, 10), EPSILON);
		Assert.assertEquals(180.0, HelperPhysics.estimatePhaseAngleDegrees(15, 10), EPSILON);
		Assert.assertEquals(324.0, HelperPhysics.estimatePhaseAngleDegrees(19, 10), EPSILON);
	}

	@Test
	public void testEstimateShiftWithAngle()
	{
		Assert.assertEquals(0, HelperPhysics.estimateShiftWithAngle(0));
		Assert.assertEquals(1, HelperPhysics.estimateShiftWithAngle(10));
		Assert.assertEquals(2, HelperPhysics.estimateShiftWithAngle(20));
	}

	@Test
	public void testEstimateSpeed() throws PositiveValueExpectedException
	{
		double a_b = 0.1362;
		double b_b = 0.5962;
		LapTimeRegressionModel lrv = new LapTimeRegressionModel(a_b, b_b);
		Assert.assertEquals(0.4775, HelperPhysics.estimateSpeed(100, Constants.BALL_CIRCUMFERENCE, lrv), EPSILON);
	}

	@Test
	public void testEstimateDistance() throws PositiveValueExpectedException
	{
		double a_b = 0.0266;
		double b_b = 3.554;
		LapTimeRegressionModel lrv = new LapTimeRegressionModel(a_b, b_b);
		Assert.assertEquals(1.3238, HelperPhysics.estimateDistance(15.554, 18.128, Constants.WHEEL_CIRCUMFERENCE, lrv), EPSILON);
	}

	@Test
	public void testEstimateTimeForSpeed() throws PositiveValueExpectedException
	{
		double a_b = 0.1362;
		double b_b = 0.5962;
		LapTimeRegressionModel lrv = new LapTimeRegressionModel(a_b, b_b);

		double time = 12.5;
		double speed = HelperPhysics.estimateSpeed(time, Constants.BALL_CIRCUMFERENCE, lrv);

		Assert.assertEquals(time, HelperPhysics.estimateTimeForSpeed(speed, Constants.BALL_CIRCUMFERENCE, lrv), EPSILON);
	}

}
