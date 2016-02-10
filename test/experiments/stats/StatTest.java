package experiments.stats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import computations.predictor.statistical.PredictorStatisticalAnalysis;
import computations.predictor.statistical.StatisticalLapTimesModel;
import computations.utils.Helper;
import utils.exception.SessionNotReadyException;
import utils.logger.Logger;

public class StatTest
{
	/**
	 * That would be cool to click when the ball hits the diamond region.
	 */
	StatisticalLapTimesModel	manager	= new StatisticalLapTimesModel();

	static List<Double>			a1		= Arrays.asList(1454133022532.0, 1454133023212.0, 1454133023846.0, 1454133024549.0, 1454133025255.0,
			1454133026055.0, 1454133026850.0, 1454133027751.0, 1454133028760.0, 1454133029956.0, 1454133031774.0, 1454133033720.0, 1454133035721.0,
			1454133037997.0);

	static List<Double>			b1		= Arrays.asList(1454133119042.0, 1454133119517.0, 1454133120037.0, 1454133120586.0, 1454133121159.0,
			1454133121670.0, 1454133122260.0, 1454133122849.0, 1454133123461.0, 1454133124214.0, 1454133124877.0, 1454133125588.0, 1454133126467.0,
			1454133127311.0, 1454133128257.0, 1454133129316.0, 1454133130770.0, 1454133132587.0, 1454133134513.0, 1454133136680.0, 1454133139024.0);

	static List<Double>			c1_2	= Arrays.asList(1454133524280.0, 1454133525248.0, 1454133526091.0, 1454133526960.0, 1454133528073.0,
			1454133529517.0, 1454133531443.0, 1454133533514.0);

	static List<Double>			c2_2	= Arrays.asList(1454133570298.0, 1454133570846.0, 1454133571479.0, 1454133572074.0, 1454133572723.0,
			1454133573445.0, 1454133574104.0, 1454133574825.0, 1454133575619.0, 1454133576520.0, 1454133577479.0, 1454133578925.0, 1454133580695.0,
			1454133582714.0, 1454133584886.0);

	static
	{
		a1 = Helper.computeDiff(a1);
		b1 = Helper.computeDiff(b1);
		c1_2 = Helper.computeDiff(c1_2);
		c2_2 = Helper.computeDiff(c2_2);

		a1 = Helper.convertToSeconds(a1);
		b1 = Helper.convertToSeconds(b1);
		c1_2 = Helper.convertToSeconds(c1_2);
		c2_2 = Helper.convertToSeconds(c2_2);

	}

	@Test
	public void test2()
	{
		// size = 11
		List<Double> l1 = Arrays.asList(0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0);
		List<Double> l2 = Arrays.asList(5.1, 6.1, 7.1);

		// 7
		System.out.println(manager.identifyLastContainerId(l1, l2));

		// 8.0+9.0+10.0 = 27
		System.out.println(manager.remainingTimeAux(l1, l2));

	}

	// @Test
	public void real_test() throws SessionNotReadyException
	{
		PredictorStatisticalAnalysis predictor = new PredictorStatisticalAnalysis();
		List<List<Double>> list = new ArrayList<>();
		list.add(a1);
		list.add(b1);
		list.add(c1_2);
		// list.add(c2_2);
		// predictor.init(list);

		List<Double> wheelCumsumTimes = Arrays.asList(1.0, 4.0);

		List<Double> pred_c2_2 = Arrays.asList(1454133570298.0, 1454133570846.0, 1454133571479.0, 1454133572074.0, 1454133572723.0, 1454133573445.0,
				1454133574104.0, 1454133574825.0, 1454133575619.0, 1454133576520.0, 1454133577479.0, 1454133578925.0, 1454133580695.0,
				1454133582714.0, 1454133584886.0);

		pred_c2_2 = Helper.convertToSeconds(pred_c2_2);

		pred_c2_2 = pred_c2_2.subList(0, pred_c2_2.size() - 10);

		Logger.setDebug(true);
		System.out.println(predictor.predict(pred_c2_2, wheelCumsumTimes));
	}

	@Test
	public void test()
	{
		manager.enrichModel(a1);
		manager.enrichModel(b1);
		manager.enrichModel(c1_2);
		manager.enrichModel(c2_2);

		System.out.println(manager.printAverageValues());

	}
}
