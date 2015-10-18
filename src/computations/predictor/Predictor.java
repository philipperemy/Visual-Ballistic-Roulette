package computations.predictor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import computations.Constants;
import computations.ml.DataRecord;
import computations.ml.KNN;
import computations.outcome.OutcomeStatistics;
import computations.wheel.Type;
import computations.wheel.Wheel;
import computations.wheel.Wheel.WheelWay;
import database.DatabaseAccessor;
import log.Logger;
import servlets.Helper;

public class Predictor {

	private static volatile Predictor instance = null;
	private DatabaseAccessor da;
	private List<DataRecord> cache = new ArrayList<>();

	public void init(DatabaseAccessor da) {
		this.da = da;
		preLoadDataSet();
	}

	private void preLoadDataSet() {
		List<String> sessionIds = da.getSessionIds();
		for (String sessionId : sessionIds) {

			List<Double> ballLapTimes = da.selectBallLapTimes(sessionId);
			List<Double> wheelLapTimes = da.selectWheelLapTimes(sessionId);

			List<Double> wheelLapTimesSeconds = Helper.convertToSeconds(wheelLapTimes);
			List<Double> ballLapTimesSeconds = Helper.convertToSeconds(ballLapTimes);

			String clockwise = da.selectClockwise(sessionId);
			if (clockwise == null) {
				Logger.traceERROR("No Clockwise for session id = " + sessionId);
				continue;
			}

			WheelWay wheelWay = Wheel.convert(clockwise);
			DataRecord record = buildRecord(ballLapTimesSeconds, wheelLapTimesSeconds, wheelWay);
			if (record != null) {
				record.outcome = da.getOutcome(sessionId);
				cache.add(record);
				Logger.traceINFO("[Cache] Record added : " + record.toString());

			}
		}
	}

	private DataRecord buildRecord(List<Double> ballLapTimes, List<Double> wheelLapTimes, WheelWay wheelWay) {

		if (ballLapTimes.isEmpty() || wheelLapTimes.isEmpty()) {
			return null;
		}

		Logger.traceINFO("____________");
		Logger.traceINFO("buildRecord()");
		Logger.traceINFO("Ball lap times : " + ballLapTimes.toString());
		Logger.traceINFO("Wheel lap times : " + wheelLapTimes.toString());
		Logger.traceINFO("Wheel way : " + wheelWay.toString());

		DataRecord record = new DataRecord();
		AccelerationModel ballAccModel = BallisticManager.computeModel(ballLapTimes, Type.BALL);
		AccelerationModel wheelAccModel = BallisticManager.computeModel(wheelLapTimes, Type.WHEEL);

		double refTime = wheelLapTimes.get(0);
		Logger.traceINFO("Reference time for measurements : " + refTime);
		double timeInterval = Constants.TIME_BEFORE_FORECASTING / Constants.NUMBER_OF_SPEEDS_IN_DATASET;
		Logger.traceINFO("Time interval is : " + timeInterval);
		for (int i = 1; i <= Constants.NUMBER_OF_SPEEDS_IN_DATASET; i++) {
			double timeForDataset = refTime + i * timeInterval;
			record.ballSpeeds.add(new ClockSpeed(timeForDataset, ballAccModel.estimateSpeed(timeForDataset)));
			record.wheelSpeeds.add(new ClockSpeed(timeForDataset, wheelAccModel.estimateSpeed(timeForDataset)));
		}

		// maybe upgrade a little more the model to make it more understandable.

		for (int i = 1; i <= Constants.NUMBER_OF_SPEEDS_IN_DATASET; i++) {
			double wheelLapTimeInFrontOfRef = wheelLapTimes.get(i);
			double previousWheelLapTimeInFrontOfRef = wheelLapTimes.get(i - 1);

			// TODO: improved
			// Before it was Phase.getNextTimeBallIsInFrontOfRef(ballLapTimes,
			// wheelLapTimeInFrontOfRef);
			double correspondingBallLapTime = Phase.getNextTimeBallIsInFrontOfRef(ballLapTimes,
					previousWheelLapTimeInFrontOfRef);
			double lastWheelSpeed = BallisticManager.getWheelSpeed(previousWheelLapTimeInFrontOfRef,
					wheelLapTimeInFrontOfRef);

			record.phases.add(Phase.findPhaseNumberBetweenBallAndWheel(correspondingBallLapTime,
					wheelLapTimeInFrontOfRef, lastWheelSpeed, wheelWay));
		}
		return record;
	}

	public static Predictor getInstance() {
		if (instance == null) {
			instance = new Predictor();
		}
		return instance;
	}

	private Predictor() {
	}

	// We turn wheel artificially to align the phases. Return the best shift.
	// Shift DR1 on DR2.
	public static int getShiftFromPhaseNumbers(DataRecord dr1, DataRecord dr2) {
		if (dr1.phases.size() != dr2.phases.size()) {
			return -1; // Cannot compare them
		}

		double shift = 0;
		for (int i = 0; i < dr1.phases.size(); i++) {
			double positiveShift = Wheel.signedDistanceBetweenNumbers(dr2.phases.get(i), dr1.phases.get(i));
			double negativeShift = Wheel.signedDistanceBetweenNumbers(dr1.phases.get(i), dr2.phases.get(i));
			//Take smallest of all.
			if(positiveShift < negativeShift) {
				shift += positiveShift;
			} else {
				shift -= negativeShift;
			}
		}
		shift /= dr1.phases.size();
		return (int) Math.round(shift); //0.6667 => 1, 0.3332 => 0.
	}
	
	

	public int predict(List<Double> ballLapTimes, List<Double> wheelLapTimes, WheelWay wheelWay) {
		DataRecord recordToPredict = buildRecord(ballLapTimes, wheelLapTimes, wheelWay); // phase
																							// is
																							// filled.
		Logger.traceINFO("Record to predict : " + recordToPredict);

		Map<DataRecord, Double> neighbors = KNN.getNeighbors(recordToPredict, cache);
		List<Integer> shifts = new ArrayList<>();
		for (Entry<DataRecord, Double> entry : neighbors.entrySet()) {
			DataRecord neighbor = entry.getKey();
			Double distance = entry.getValue();
			int alignedPhase = getShiftFromPhaseNumbers(neighbor, recordToPredict);
			Logger.traceINFO(
					"Dist = " + distance + ", phase = " + alignedPhase + ", neighbour = " + neighbor.toString());
			shifts.add(alignedPhase);
		}

		OutcomeStatistics outcomeStatistics = KNN.getOutcomeStatistics(new ArrayList<>(neighbors.keySet()), shifts);
		Logger.traceINFO(outcomeStatistics);
		return outcomeStatistics.mostProbablyNumber;
	}

}
