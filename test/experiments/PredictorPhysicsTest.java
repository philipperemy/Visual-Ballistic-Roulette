package experiments;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import computations.Constants;
import computations.predictor.Predictor;
import computations.predictor.PredictorInterface;
import computations.utils.Helper;
import utils.exception.PositiveValueExpectedException;
import utils.exception.SessionNotReadyException;
import utils.logger.Logger;

public class PredictorPhysicsTest
{
	@Test
	public void test3() throws PositiveValueExpectedException, SessionNotReadyException
	{
		Constants.CUTOFF_SPEED = 0.96;
		Constants.DEFAULT_SHIFT_PHASE = 25;
		// 9.494 meters between t = 12520 and 20179
		// aligned 22. Roughly 2 loops and 3 pockets more.

		Logger.setDebug(true);
		Predictor py = PredictorInterface.getInstance().physicsLinearLapTimes();

		List<Double> ballCumsumTimes = new ArrayList<>();
		ballCumsumTimes.add(53960.0);
		ballCumsumTimes.add(54920.0);
		ballCumsumTimes.add(56000.0);
		ballCumsumTimes.add(57200.0);
		ballCumsumTimes.add(58560.0);
		ballCumsumTimes.add(60000.0);
		ballCumsumTimes.add(61560.0);
		ballCumsumTimes.add(63240.0);
		ballCumsumTimes.add(65080.0);
		// ballCumsumTimes.add(67040.0);
		// ballCumsumTimes.add(69240.0);
		// ballCumsumTimes.add(18400.0);

		// falls at 69960

		// cutoff speed?
		// diamond1 - 68960
		// diamond2 - 69240
		// t = 2240. same exactly!

		List<Double> wheelCumsumTimes = new ArrayList<>();
		wheelCumsumTimes.add(57160.0);
		wheelCumsumTimes.add(60880.0);
		wheelCumsumTimes.add(64600.0);
		wheelCumsumTimes.add(68360.0);

		ballCumsumTimes = Helper.convertToSeconds(ballCumsumTimes);
		wheelCumsumTimes = Helper.convertToSeconds(wheelCumsumTimes);

		Assert.assertEquals(15, py.predict(ballCumsumTimes, wheelCumsumTimes));
	}

	@Test
	public void test() throws PositiveValueExpectedException, SessionNotReadyException
	{
		Constants.CUTOFF_SPEED = 0.96;
		Constants.DEFAULT_SHIFT_PHASE = 20;
		// 9.494 meters between t = 12520 and 20179
		// aligned 22. Roughly 2 loops and 3 pockets more.

		Logger.setDebug(true);
		Predictor py = PredictorInterface.getInstance().physicsLinearLapTimes();

		List<Double> ballCumsumTimes = new ArrayList<>();
		ballCumsumTimes.add(2871.0);
		ballCumsumTimes.add(3582.0);
		ballCumsumTimes.add(4420.0);
		ballCumsumTimes.add(5460.0);
		ballCumsumTimes.add(6633.0);
		ballCumsumTimes.add(7939.0);
		ballCumsumTimes.add(9369.0);
		ballCumsumTimes.add(10888.0);
		ballCumsumTimes.add(12520.0);
		// ballCumsumTimes.add(14320.0);
		// ballCumsumTimes.add(16280.0);
		// ballCumsumTimes.add(18400.0);

		// falls at 20160

		// cutoff speed?
		// diamond1 - 19200
		// diamond2 - 19480
		// t = 2240.

		List<Double> wheelCumsumTimes = new ArrayList<>();
		wheelCumsumTimes.add(2632.0);
		wheelCumsumTimes.add(6213.0);
		wheelCumsumTimes.add(9812.0);

		ballCumsumTimes = Helper.convertToSeconds(ballCumsumTimes);
		wheelCumsumTimes = Helper.convertToSeconds(wheelCumsumTimes);

		Assert.assertEquals(15, py.predict(ballCumsumTimes, wheelCumsumTimes));
	}

}
