package physics;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import computations.Helper;
import computations.predictor.PredictorInterface;
import computations.predictor.physics.PositiveValueExpectedException;
import computations.predictor.physics.PredictorPhysics;

public class PredictorPhysicsTest
{

	@Test
	public void test() throws PositiveValueExpectedException
	{
		PredictorPhysics py = PredictorInterface.getInstance().physics();

		List<Double> ballCumsumTimes = new ArrayList<>();
		ballCumsumTimes.add(2871.0);
		ballCumsumTimes.add(3582.0);
		ballCumsumTimes.add(4420.0);
		ballCumsumTimes.add(5460.0);
		ballCumsumTimes.add(6633.0);
		ballCumsumTimes.add(7939.0);
		ballCumsumTimes.add(9369.0);
		ballCumsumTimes.add(10888.0);

		List<Double> wheelCumsumTimes = new ArrayList<>();
		wheelCumsumTimes.add(2632.0);
		wheelCumsumTimes.add(6213.0);
		wheelCumsumTimes.add(9812.0);

		ballCumsumTimes = Helper.convertToSeconds(ballCumsumTimes);
		wheelCumsumTimes = Helper.convertToSeconds(wheelCumsumTimes);

		Assert.assertEquals(15, py.predict(ballCumsumTimes, wheelCumsumTimes));
	}

}
