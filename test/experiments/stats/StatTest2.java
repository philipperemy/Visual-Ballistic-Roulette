package experiments.stats;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import computations.Constants;
import computations.predictor.statistical.PredictorStatisticalAnalysis2;
import computations.utils.Helper;
import exceptions.SessionNotReadyException;
import logger.Logger;

public class StatTest2
{
	static List<Double>	a1		= Arrays.asList(1454133022532.0, 1454133023212.0, 1454133023846.0, 1454133024549.0, 1454133025255.0, 1454133026055.0,
			1454133026850.0, 1454133027751.0, 1454133028760.0, 1454133029956.0, 1454133031774.0, 1454133033720.0, 1454133035721.0, 1454133037997.0);

	static List<Double>	b1		= Arrays.asList(1454133119042.0, 1454133119517.0, 1454133120037.0, 1454133120586.0, 1454133121159.0, 1454133121670.0,
			1454133122260.0, 1454133122849.0, 1454133123461.0, 1454133124214.0, 1454133124877.0, 1454133125588.0, 1454133126467.0, 1454133127311.0,
			1454133128257.0, 1454133129316.0, 1454133130770.0, 1454133132587.0, 1454133134513.0, 1454133136680.0, 1454133139024.0);

	static List<Double>	c1_2	= Arrays.asList(1454133524280.0, 1454133525248.0, 1454133526091.0, 1454133526960.0, 1454133528073.0, 1454133529517.0,
			1454133531443.0, 1454133533514.0);

	static List<Double>	c2_2	= Arrays.asList(1454133570298.0, 1454133570846.0, 1454133571479.0, 1454133572074.0, 1454133572723.0, 1454133573445.0,
			1454133574104.0, 1454133574825.0, 1454133575619.0, 1454133576520.0, 1454133577479.0, 1454133578925.0, 1454133580695.0, 1454133582714.0,
			1454133584886.0);

	static
	{
		a1 = Helper.convertToSeconds(a1);
		b1 = Helper.convertToSeconds(b1);
		c1_2 = Helper.convertToSeconds(c1_2);
		c2_2 = Helper.convertToSeconds(c2_2);
	}

	@Test
	public void real_test() throws SessionNotReadyException
	{
		Constants.CASE_DIAMETER = 0.65;

		PredictorStatisticalAnalysis2 predictor = new PredictorStatisticalAnalysis2();

		List<Double> wheelCumsumTimes = Arrays.asList(1.0, 4.0);
		a1 = a1.subList(0, a1.size() - 3);

		Logger.setDebug(true);
		System.out.println(predictor.predict(a1, wheelCumsumTimes));
	}

}
