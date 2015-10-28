package computations.predictor;

import java.util.ArrayList;
import java.util.List;

import computations.Constants;
import computations.Helper;
import computations.model.DataRecord;
import computations.model.Outcome;
import computations.wheel.Type;
import computations.wheel.Wheel;
import computations.wheel.Wheel.WheelWay;
import database.DatabaseAccessorInterface;
import log.Logger;

public class Predictor {

	private static volatile Predictor instance = null;
	private DatabaseAccessorInterface da;

	public void init(DatabaseAccessorInterface da) {
		this.da = da;
		preLoadDataSet();
	}

	private void preLoadDataSet() {
		List<String> sessionIds = da.getSessionIds();
		for (String sessionId : sessionIds) { // SessionID = GameID

			List<Double> ballLapTimes = da.selectBallLapTimes(sessionId);
			List<Double> wheelLapTimes = da.selectWheelLapTimes(sessionId);

			List<Double> wheelLapTimesSeconds = computations.Helper.convertToSeconds(wheelLapTimes);
			List<Double> ballLapTimesSeconds = computations.Helper.convertToSeconds(ballLapTimes);

			if (wheelLapTimes.isEmpty() || ballLapTimes.isEmpty()) {
				Logger.traceERROR("[preLoadDataSet] ball or wheel speeds are empty for session id = " + sessionId
						+ ". Ignoring this game.");
				continue;
			}

			String clockwise = da.selectClockwise(sessionId);
			if (clockwise == null) {
				Logger.traceERROR(
						"[preLoadDataSet] No Clockwise for session id = " + sessionId + ". Ignoring this game.");
				continue;
			}

			try {
				WheelWay wheelWay = Wheel.convert(clockwise);
				List<DataRecord> records = buildRecord2(ballLapTimesSeconds, wheelLapTimesSeconds, wheelWay, sessionId);
				for (DataRecord record : records) {
					Outcome outcome = da.getOutcome(sessionId);
					if (outcome != null) {
						record.outcome = outcome.number;
					} else {
						Logger.traceERROR("No outcome for session = " + sessionId);
					}
					record.cacheIt();
				}
			} catch (Exception e) {
				Logger.traceERROR(e);
			}

		}
	}

	private List<DataRecord> buildRecord2(List<Double> ballLapTimes, List<Double> wheelLapTimes, WheelWay wheelWay,
			String sessionId) {

		if (ballLapTimes.isEmpty() || wheelLapTimes.isEmpty()) {
			return null;
		}

		AccelerationModel ballAccModel = BallisticManager.computeModel(ballLapTimes, Type.BALL);
		AccelerationModel wheelAccModel = BallisticManager.computeModel(wheelLapTimes, Type.WHEEL);

		List<DataRecord> smallDataRecords = new ArrayList<>();
		for (int i = 0; i < ballLapTimes.size(); i++) {

			double correspondingBallLapTime = ballLapTimes.get(i);
			double wheelSpeedInFrontOfMark = wheelAccModel.estimateSpeed(correspondingBallLapTime);

			Double lastWheelLapTimeInFrontOfRef = Helper.getLastTimeWheelIsInFrontOfRef(wheelLapTimes,
					correspondingBallLapTime);

			//It means that the first record is a ball lap times.
			if(lastWheelLapTimeInFrontOfRef == null) {
				continue;
			}
			
			int phase = Phase.findPhaseNumberBetweenBallAndWheel(correspondingBallLapTime, lastWheelLapTimeInFrontOfRef,
					wheelSpeedInFrontOfMark, wheelWay);

			DataRecord smr = new DataRecord();
			smr.ballSpeedInFrontOfMark = ballAccModel.estimateSpeed(correspondingBallLapTime);
			smr.wheelSpeedInFrontOfMark = wheelSpeedInFrontOfMark;
			smr.way = wheelWay;
			smr.sessionId = sessionId;
			smr.phaseOfWheelWhenBallPassesInFrontOfMark = phase;

			smallDataRecords.add(smr);
		}

		return smallDataRecords;
	}

	@SuppressWarnings("unused")
	private List<DataRecord> buildRecord(List<Double> ballLapTimes, List<Double> wheelLapTimes, WheelWay wheelWay,
			String sessionId) {

		if (ballLapTimes.isEmpty() || wheelLapTimes.isEmpty()) {
			return null;
		}

		Logger.traceDEBUG("____________");
		Logger.traceDEBUG("buildRecord()");
		Logger.traceDEBUG("Ball lap times : " + ballLapTimes.toString());
		Logger.traceDEBUG("Wheel lap times : " + wheelLapTimes.toString());
		Logger.traceDEBUG("Wheel way : " + wheelWay.toString());

		AccelerationModel ballAccModel = BallisticManager.computeModel(ballLapTimes, Type.BALL);
		AccelerationModel wheelAccModel = BallisticManager.computeModel(wheelLapTimes, Type.WHEEL);

		List<DataRecord> smallDataRecords = new ArrayList<>();
		for (int i = 1; i < wheelLapTimes.size(); i++) {
			double wheelLapTimeInFrontOfRef = wheelLapTimes.get(i);
			double previousWheelLapTimeInFrontOfRef = wheelLapTimes.get(i - 1);

			double correspondingBallLapTime = Helper.getNextTimeBallIsInFrontOfRef(ballLapTimes,
					previousWheelLapTimeInFrontOfRef);

			double wheelSpeedInFrontOfMark = wheelAccModel.estimateSpeed(correspondingBallLapTime);

			int phase = Phase.findPhaseNumberBetweenBallAndWheel(correspondingBallLapTime, wheelLapTimeInFrontOfRef,
					wheelSpeedInFrontOfMark, wheelWay);

			DataRecord smr = new DataRecord();
			smr.ballSpeedInFrontOfMark = ballAccModel.estimateSpeed(correspondingBallLapTime);
			smr.wheelSpeedInFrontOfMark = wheelSpeedInFrontOfMark;
			smr.way = wheelWay;
			smr.sessionId = sessionId;
			smr.phaseOfWheelWhenBallPassesInFrontOfMark = phase;

			smallDataRecords.add(smr);
		}

		return smallDataRecords;
	}

	public static Predictor getInstance() {
		if (instance == null) {
			instance = new Predictor();
		}
		return instance;
	}

	private Predictor() {
	}

	public int predict(List<Double> ballLapTimes, List<Double> wheelLapTimes, WheelWay wheelWay, String sessionId) {
		// Phase is filled. All lap times are used to build the model.
		List<DataRecord> recordToPredicts = buildRecord2(ballLapTimes, wheelLapTimes, wheelWay, sessionId);

		if (recordToPredicts.isEmpty()) {
			String errMsg = "No records to predict.";
			Logger.traceERROR(errMsg);
			throw new RuntimeException(errMsg);
		}

		// Take the last one.
		DataRecord sdr = Helper.pop(recordToPredicts);
		Logger.traceINFO("Record to predict : " + sdr);

		int mostProbableNumber = DataRecord.predictOutcome(sdr);
		Logger.traceINFO("Most probable number : " + mostProbableNumber);
		return mostProbableNumber;
	}

}
