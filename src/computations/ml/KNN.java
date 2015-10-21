package computations.ml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import computations.Constants;
import computations.outcome.OutcomeStatistics;
import computations.predictor.ClockSpeed;
import computations.wheel.Wheel;
import computations.wheel.Wheel.WheelWay;

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
			// https://upload.wikimedia.org/wikipedia/commons/thumb/7/7d/European_roulette_wheel.svg/2000px-European_roulette_wheel.svg.png
			Integer number = Wheel.getNumberWithPhase(record.outcome.number, shift, WheelWay.ANTICLOCKWISE);
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
			var += Math.pow(dist, 2); // distance^2
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

	public static Map<DataRecord, Double> getNeighbors(DataRecord recordToPredict,
			Collection<DataRecord> datasetRecords) {
		Map<Double, DataRecord> recordsMap = new TreeMap<>(); // TreeMap is
																// sorted using
																// the natural
																// order of the
																// keys

		for (DataRecord datasetRecord : datasetRecords) {
			if(datasetRecord.way == recordToPredict.way) {
				recordsMap.put(computeDistance(recordToPredict, datasetRecord), datasetRecord);
			} else {
				//TODO: check
				//Usage of mirroring. x2 on the dataset.
				DataRecord mirror = DataRecord.createMirror(datasetRecord);
				boolean checkWheelWay = false;
				recordsMap.put(computeDistanceClockwiseCheck(recordToPredict, datasetRecord, checkWheelWay), mirror);
			}
		}

		Map<DataRecord, Double> neighbors = new HashMap<>();
		int i = 0;
		for (Entry<Double, DataRecord> dist : recordsMap.entrySet()) {
			Double distance = dist.getKey();
			DataRecord neighbor = dist.getValue();
			neighbors.put(neighbor, distance);
			if (++i > Constants.NUMBER_OF_NEIGHBORS_KNN) {
				break;
			}
		}
		return neighbors;
	}

	// MAE(balls) + MAE(wheels) = they add up.
	public static double computeDistanceClockwiseCheck(DataRecord record1, DataRecord record2, boolean checkClockwise) {
		
		if(checkClockwise) {
			if (record1.way != record2.way) {
				return Double.MAX_VALUE;
			}
		}
		
		double dist = compareDistanceFromSpeeds(record1.ballSpeeds, record2.ballSpeeds);

		if (!record1.ballSpeeds.isEmpty()) {
			dist /= record1.ballSpeeds.size(); // Should have the same size.
		}

		double dist2 = compareDistanceFromSpeeds(record1.wheelSpeeds, record2.wheelSpeeds);

		if (!record1.wheelSpeeds.isEmpty()) {
			dist2 /= record1.wheelSpeeds.size();
		}

		return dist + dist2;
	}
	
	public static double computeDistance(DataRecord record1, DataRecord record2) {
		return computeDistanceClockwiseCheck(record1, record2, true);
	}

	private static double compareDistanceFromSpeeds(List<ClockSpeed> list1, List<ClockSpeed> list2) {

		if (list1.size() != list2.size()) {
			return Double.MAX_VALUE;
		}

		if (list1.isEmpty() && list2.isEmpty()) {
			return 0;
		}

		if (list1.isEmpty() || list2.isEmpty()) {
			throw new RuntimeException();
		}

		double dist = 0;
		for (int i = 0; i < list1.size(); i++) {
			dist += computeDistanceFromSpeed(list1.get(i), list2.get(i));
		}
		return dist;
	}

	private static double computeDistanceFromSpeed(ClockSpeed cs1, ClockSpeed cs2) {
		return (Double.compare(cs1.time, cs2.time) == 0) ? Math.abs(cs1.speed - cs2.speed) : Double.MAX_VALUE;
	}
}
