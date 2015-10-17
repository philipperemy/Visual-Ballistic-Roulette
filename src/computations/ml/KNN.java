package computations.ml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import computations.Constants;
import computations.outcome.OutcomeStatistics;
import computations.predictor.ClockSpeed;
import computations.wheel.Wheel;

public class KNN {

	public static OutcomeStatistics getOutcomeStatistics(List<DataRecord> records, List<Integer> shifts) {
		List<Integer> outcomeNumbers = new ArrayList<>();
		Map<Integer, Integer> frequencyPerNumber = new HashMap<>(); // Number
																	// <->
																	// Frequency
		for (int i = 0; i < records.size(); i++) {
			DataRecord record = records.get(i);

			Integer shift = 0;
			if (shifts != null) {
				shift = shifts.get(i);
			}
			Integer number = record.outcome.number + shift;
			outcomeNumbers.add(number);

			Integer freq = frequencyPerNumber.get(number);
			if (freq == null || freq.intValue() == 0) {
				frequencyPerNumber.put(number, Integer.valueOf(1));
			} else {
				frequencyPerNumber.put(number, ++freq);
			}
		}

		double meanIndex = 0;
		for (int i = 0; i < outcomeNumbers.size(); i++) {
			int currentIndex = Wheel.findIndexOfNumber(outcomeNumbers.get(i));
			meanIndex += currentIndex;
		}
		meanIndex /= outcomeNumbers.size();
		int meanNumber = Wheel.NUMBERS[(int) meanIndex];

		double var = 0;
		for (int i = 0; i < outcomeNumbers.size(); i++) {
			int dist = Wheel.distanceBetweenNumbers(outcomeNumbers.get(i), meanNumber);
			var += Math.pow(dist, 2); // dist^2
		}

		int maxFreq = 0;
		Integer mostProbableNumber = null;
		for (Entry<Integer, Integer> frequencyEntry : frequencyPerNumber.entrySet()) {
			if (frequencyEntry.getValue() > maxFreq) {
				mostProbableNumber = Integer.valueOf(frequencyEntry.getKey());
			}
		}

		if (mostProbableNumber == null) {
			throw new RuntimeException();
		}

		OutcomeStatistics outcomeStatistics = new OutcomeStatistics();
		outcomeStatistics.meanNumber = meanNumber;
		outcomeStatistics.stdDeviation = Math.sqrt(var);
		outcomeStatistics.frequency = frequencyPerNumber;
		outcomeStatistics.mostProbablyNumber = mostProbableNumber.intValue();
		return outcomeStatistics;
	}

	public static List<DataRecord> getNeighbors(DataRecord recordToPredict, List<DataRecord> datasetRecords) {
		Map<Double, DataRecord> dists = new TreeMap<>(); // TreeMap is sorted
															// using the natural
															// order of the keys
		for (DataRecord datasetRecord : datasetRecords) {
			dists.put(computeMaeDist(recordToPredict, datasetRecord), datasetRecord);
		}

		List<DataRecord> neighbors = new ArrayList<>();
		int i = 0;
		for (Entry<Double, DataRecord> dist : dists.entrySet()) {
			neighbors.add(dist.getValue());
			if (++i > Constants.NUMBER_OF_NEIGHBORS_KNN) {
				break;
			}
		}
		return neighbors;
	}

	private static double computeMaeDist(DataRecord line1, DataRecord line2) {
		if (line1.way != line2.way) {
			return Double.MAX_VALUE;
		}

		double dist = compareDistanceFromSpeeds(line1.ballSpeeds, line2.ballSpeeds);
		dist /= line1.ballSpeeds.size(); // Should have the same size.

		double dist2 = compareDistanceFromSpeeds(line1.wheelSpeeds, line2.wheelSpeeds);
		dist2 /= line1.wheelSpeeds.size();

		return dist + dist2;
	}

	private static double compareDistanceFromSpeeds(List<ClockSpeed> list1, List<ClockSpeed> list2) {

		if (list1.size() != list2.size()) {
			return Double.MAX_VALUE;
		}

		if (list1.size() == 0) {
			throw new RuntimeException();
		}

		double dist = 0;
		for (int i = 0; i < list1.size(); i++) {
			dist += computeDistanceFromSpeed(list1.get(i), list2.get(i));
		}
		return dist;
	}

	private static double computeDistanceFromSpeed(ClockSpeed co1, ClockSpeed co2) {
		return co1.time == co2.time ? Double.MAX_VALUE : Math.abs(co1.speed - co2.speed);
	}
}
