package computations;

import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Assert;
import org.junit.Test;

import computations.predictor.physics.linearlaptimes.HelperLinearLapTimes;
import utils.TestConstants;
import utils.exception.PositiveValueExpectedException;

public class HelperPhysicsTest
{
	static
	{
		Constants.WHEEL_DIAMETER = 0.60;
		Constants.CASE_DIAMETER = 0.80;
	}

	@Test
	public void testEstimatePhaseAngleDegrees()
	{
		Assert.assertEquals(0.0, HelperLinearLapTimes.estimatePhaseAngleDegrees(100, 10), TestConstants.EPSILON);
		Assert.assertEquals(180.0, HelperLinearLapTimes.estimatePhaseAngleDegrees(15, 10), TestConstants.EPSILON);
		Assert.assertEquals(324.0, HelperLinearLapTimes.estimatePhaseAngleDegrees(19, 10), TestConstants.EPSILON);
	}

	@Test
	public void testEstimateShiftWithAngle()
	{
		Assert.assertEquals(0, HelperLinearLapTimes.estimateShiftWithAngle(0));
		Assert.assertEquals(1, HelperLinearLapTimes.estimateShiftWithAngle(10));
		Assert.assertEquals(2, HelperLinearLapTimes.estimateShiftWithAngle(20));
	}

	@Test
	public void testEstimateSpeed() throws PositiveValueExpectedException
	{
		double a_b = 0.1362;
		double b_b = 0.5962;
		SimpleRegression lrv = generateLinearModel(a_b, b_b);
		Assert.assertEquals(0.4775, HelperLinearLapTimes.estimateSpeed(100, Constants.get_BALL_CIRCUMFERENCE(), lrv), TestConstants.EPSILON);
	}

	@Test
	public void testEstimateDistance() throws PositiveValueExpectedException
	{
		double a_b = 0.0266;
		double b_b = 3.554;
		SimpleRegression lrv = generateLinearModel(a_b, b_b);
		Assert.assertEquals(1.3238, HelperLinearLapTimes.estimateDistance(15.554, 18.128, Constants.get_WHEEL_CIRCUMFERENCE(), lrv),
				TestConstants.EPSILON);
	}

	private SimpleRegression generateLinearModel(double a_b, double b_b)
	{
		SimpleRegression lrv = new SimpleRegression();
		lrv.addData(0, b_b);
		lrv.addData(1, a_b + b_b);
		return lrv;
	}

	@Test
	public void testEstimateTimeForSpeed() throws PositiveValueExpectedException
	{
		double a_b = 0.1362;
		double b_b = 0.5962;
		SimpleRegression lrv = generateLinearModel(a_b, b_b);

		double time = 12.5;
		double speed = HelperLinearLapTimes.estimateSpeed(time, Constants.get_BALL_CIRCUMFERENCE(), lrv);

		Assert.assertEquals(time, HelperLinearLapTimes.estimateTimeForSpeed(speed, Constants.get_BALL_CIRCUMFERENCE(), lrv), TestConstants.EPSILON);
	}

}
