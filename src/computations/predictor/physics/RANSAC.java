package computations.predictor.physics;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.Test;

import computations.Helper;

public class RANSAC
{
	static Random random = new Random();

	// RANDPERM(N,K) returns a vector of K unique values. This is sometimes
	// referred to as a K-permutation of 1:N or as sampling without replacement.
	public static Set<Integer> randPerm(int N, int K)
	{
		Set<Integer> res = new LinkedHashSet<>(); // unsorted set.
		while (res.size() < K)
		{
			res.add(random.nextInt(N)); // [0, number-1]
		}
		return res;
	}

	public static double norm(List<Double> vec)
	{
		return Math.sqrt(Math.pow(vec.get(0), 2) + Math.pow(vec.get(1), 2));
	}

	public static List<Integer> findLessThan(List<Double> distance, double threshDist)
	{
		List<Integer> res = new ArrayList<>();
		for (int i = 0; i < distance.size(); i++)
		{
			double dist = distance.get(i);
			if (Math.abs(dist) <= threshDist)
			{
				res.add(i);
			}
		}
		return res;
	}

	public static List<Double> perform(List<Double> data_Y, int num, int iter, double threshDist, double inlierRatio)
	{
		int number = data_Y.size();
		List<Integer> data_X = new ArrayList<>();
		for (int i = 0; i < number; i++)
		{
			data_X.add(i + 1);
		}

		double bestInNum = 0;
		double bestParameter1 = 0, bestParameter2 = 0;

		for (int i = 0; i < iter; i++)
		{
			Set<Integer> idx = randPerm(number, num);

			List<Integer> sample_X = new ArrayList<>();
			List<Double> sample_Y = new ArrayList<>();
			for (Integer idxVal : idx)
			{
				sample_X.add(data_X.get(idxVal));
				sample_Y.add(data_Y.get(idxVal));
			}

			List<Double> kLine = new ArrayList<>();
			kLine.add((double) (sample_X.get(1) - sample_X.get(0)));
			kLine.add(sample_Y.get(1) - sample_Y.get(0));

			List<Double> kLineNorm = new ArrayList<>();
			double norm = norm(kLine);
			kLineNorm.add(kLine.get(0) / norm);
			kLineNorm.add(kLine.get(1) / norm);

			List<Double> normVector = new ArrayList<>();
			normVector.add(-kLineNorm.get(1));
			normVector.add(kLineNorm.get(0));

			List<Double> distance = new ArrayList<>();
			for (int j = 0; j < number; j++)
			{
				double distTmp = normVector.get(0) * (data_X.get(j) - sample_X.get(0));
				distTmp += normVector.get(1) * (data_Y.get(j) - sample_Y.get(0));
				distance.add(distTmp);
			}

			List<Integer> inlierIdx = findLessThan(distance, threshDist);
			int inlierNum = inlierIdx.size();

			double parameter1 = 0;
			double parameter2 = 0;

			if ((inlierNum >= Math.round(inlierRatio * number)) && (inlierNum > bestInNum))
			{
				bestInNum = inlierNum;
				parameter1 = (sample_Y.get(1) - sample_Y.get(0)) / (sample_X.get(1) - sample_X.get(0));
				parameter2 = sample_Y.get(0) - parameter1 * sample_X.get(0);
				bestParameter1 = parameter1;
				bestParameter2 = parameter2;
			}
		}

		List<Double> res = new ArrayList<>();
		res.add(bestParameter1);
		res.add(bestParameter2);
		return res;
	}

	@Test
	public void test_ransac()
	{
		// 5 8 11 14 17 20 23 26
		List<Double> data = new ArrayList<>();
		data.add(5.0);
		data.add(8.0);
		data.add(11.0);
		data.add(22.0);
		data.add(17.0);
		data.add(20.0);
		data.add(23.0);
		data.add(500.0);

		System.out.println(RANSAC.perform(data, 2, 100, 1, 0.2));
	}

	@Test
	public void test_ransac_2()
	{
		// 2858 3591 4421 5456 6625 7950 9355 10887 12539 14336 16302 18387
		List<Double> data = new ArrayList<>();
		data.add(2858.0);
		data.add(3591.0);
		data.add(4421.0);
		data.add(5456.0);
		data.add(6625.0);
		data.add(7950.0);
		data.add(9355.0);
		data.add(10887.0);
		data.add(12539.0);
		data.add(14336.0);
		data.add(16302.0);
		data.add(18387.0);

		List<Double> diffs = Helper.computeDiff(data);
		diffs = Helper.convertToSeconds(diffs);

		System.out.println(RANSAC.perform(diffs, 2, 100, 1, 0.2));

	}
}
