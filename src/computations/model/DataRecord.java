package computations.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import computations.Constants;
import computations.Helper;
import computations.wheel.Wheel;
import computations.wheel.Wheel.WheelWay;
import log.Logger;

public class DataRecord {

	// Principle:
	// BallSpeed known in front of mark
	// Wheel speed known in front of mark
	// Phase between both (wheel number in front of mark when ball passes in
	// front of the mark)
	// => possible to predict the outcome.

	public double ballSpeedInFrontOfMark;
	public double wheelSpeedInFrontOfMark;

	public String sessionId;

	/**
	 * Phases of the ball when the zero of the ball is in front of a landmark.
	 * After it's just looking at the phase between each phase and what we have
	 * to shift the outcome.
	 */
	public int phaseOfWheelWhenBallPassesInFrontOfMark;
	public Integer outcome = null; // outcome of the game.
	public WheelWay way;

	public static List<DataRecord> cacheSMR = new ArrayList<>();

	public void cacheIt() {
		Logger.traceINFO("[Cache] Record added : " + toString());
		cacheSMR.add(this);
	}

	public double mae(DataRecord smr) {
		return Math.abs(smr.ballSpeedInFrontOfMark - this.ballSpeedInFrontOfMark)
				+ Math.abs(smr.wheelSpeedInFrontOfMark - this.wheelSpeedInFrontOfMark);
	}

	public static List<DataRecord> matchCache(DataRecord smr) {
		Map<Double, DataRecord> smrMap = new TreeMap<>();
		for (DataRecord cacheSmr : cacheSMR) {
			double dist = cacheSmr.mae(smr);
			if (smr.way == cacheSmr.way) { // For now discarding if the way is
											// different.
				smrMap.put(dist, cacheSmr);
				Logger.traceINFO("Dist : " + Helper.printDigit(dist) + ", " + cacheSmr);
			} else {
				Logger.traceINFO("Dist : DISCARDED (WAY) " + Helper.printDigit(dist) + ", " + cacheSmr);
			}

		}

		int i = 0;
		List<DataRecord> knnList = new ArrayList<>();
		for (Entry<Double, DataRecord> orderedCache : smrMap.entrySet()) {
			if (i++ < Constants.NUMBER_OF_NEIGHBORS_KNN) {
				knnList.add(orderedCache.getValue());
				Logger.traceINFO("Selected : {" + orderedCache + "}");
			}
		}
		return knnList;
	}

	public static int predictOutcome(DataRecord smr) {
		List<DataRecord> matchedRecordsList = matchCache(smr);
		List<Integer> outcomeNumbersList = new ArrayList<>();
		for (DataRecord matched : matchedRecordsList) {
			int predictedOutcome = Wheel.predictOutcomeWithShift(matched.phaseOfWheelWhenBallPassesInFrontOfMark, matched.outcome, smr.phaseOfWheelWhenBallPassesInFrontOfMark);
			outcomeNumbersList.add(predictedOutcome);
		}

		OutcomeStatistics stat = OutcomeStatistics.create(outcomeNumbersList);
		Logger.traceINFO("Statistics : " + stat);
		return stat.meanNumber;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(ballSpeedInFrontOfMark);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + phaseOfWheelWhenBallPassesInFrontOfMark;
		temp = Double.doubleToLongBits(wheelSpeedInFrontOfMark);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DataRecord other = (DataRecord) obj;
		if (Double.doubleToLongBits(ballSpeedInFrontOfMark) != Double.doubleToLongBits(other.ballSpeedInFrontOfMark))
			return false;
		if (phaseOfWheelWhenBallPassesInFrontOfMark != other.phaseOfWheelWhenBallPassesInFrontOfMark)
			return false;
		if (Double.doubleToLongBits(wheelSpeedInFrontOfMark) != Double.doubleToLongBits(other.wheelSpeedInFrontOfMark))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DataRecord [BS=" + Helper.printDigit(ballSpeedInFrontOfMark) + ", WS="
				+ Helper.printDigit(wheelSpeedInFrontOfMark) + ", "
				+ (sessionId != null ? "SessionId=" + sessionId + ", " : "") + "Phase="
				+ phaseOfWheelWhenBallPassesInFrontOfMark + ", Outcome=" + outcome + ", "
				+ (way != null ? "Way=" + way : "") + "]";
	}

}
