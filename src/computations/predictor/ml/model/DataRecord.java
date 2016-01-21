package computations.predictor.ml.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import computations.Constants;
import computations.Helper;
import computations.predictor.ml.model.solver.OutcomeSolver;
import computations.wheel.Wheel.WheelWay;
import logger.Logger;

public class DataRecord
{
	// Principle:
	// - BallSpeed known in front of mark
	// - Wheel speed known in front of mark
	// - Phase between both (wheel number in front of mark when ball passes in
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
	private final WheelWay way = Constants.DEFAULT_WHEEL_WAY;

	private static List<DataRecord> cache = new ArrayList<>();

	private static OutcomeSolver solver = Constants.DATARECORD_SOLVER;

	public static void clearCache()
	{
		Logger.traceINFO("[Cache] Clearing all cache.");
		cache = new ArrayList<>();
	}

	public void cacheIt()
	{
		Logger.traceINFO("[Cache] Record added : " + toString());
		cache.add(this);
	}

	private double mae(DataRecord smr)
	{
		return Math.abs(smr.ballSpeedInFrontOfMark - this.ballSpeedInFrontOfMark)
				+ Math.abs(smr.wheelSpeedInFrontOfMark - this.wheelSpeedInFrontOfMark);
	}

	public static List<DataRecord> matchCache(DataRecord predictRecord, int knnNumber)
	{
		Map<Double, DataRecord> distRecordsMap = new TreeMap<>();
		for (DataRecord cacheRecord : cache)
		{
			double dist = cacheRecord.mae(predictRecord);
			if (predictRecord.way == cacheRecord.way)
			{
				// For now discarding if the way is different.
				distRecordsMap.put(dist, cacheRecord);
			} else
			{
				Logger.traceINFO("Dist : DISCARDED (WHEEL WAY) " + Helper.printDigit(dist) + ", " + cacheRecord);
			}
		}

		// print them all.
		for (Entry<Double, DataRecord> entry : distRecordsMap.entrySet())
		{
			double dist = entry.getKey();
			DataRecord cacheSmr = entry.getValue();
			Logger.traceINFO("Dist : " + Helper.printDigit(dist) + ", " + cacheSmr);
		}

		int i = 0;
		List<DataRecord> knnList = new ArrayList<>();
		for (Entry<Double, DataRecord> orderedCacheRecord : distRecordsMap.entrySet())
		{
			if (i++ < knnNumber)
			{
				knnList.add(orderedCacheRecord.getValue());
				Logger.traceINFO("Selected : {" + orderedCacheRecord + "}");
			}
		}
		return knnList;
	}

	@Override
	public String toString()
	{
		return "DataRecord [BS=" + Helper.printDigit(ballSpeedInFrontOfMark) + ", WS=" + Helper.printDigit(wheelSpeedInFrontOfMark) + ", "
				+ (sessionId != null ? "SessionId=" + sessionId + ", " : "") + "Phase=" + phaseOfWheelWhenBallPassesInFrontOfMark + ", Outcome="
				+ outcome + ", " + (way != null ? "Way=" + way : "") + "]";
	}

	public static int predictOutcome(DataRecord predictRecord)
	{
		return solver.predictOutcome(predictRecord);
	}

}
