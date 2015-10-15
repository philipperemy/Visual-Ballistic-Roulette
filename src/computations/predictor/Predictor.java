package computations.predictor;

import java.util.ArrayList;
import java.util.List;

import computations.Constants;
import computations.ml.DataRecord;
import computations.ml.KNN;
import computations.outcome.OutcomeStatistics;
import computations.predictor.BallisticManager.AccelerationModel;
import computations.wheel.Type;
import computations.wheel.Wheel;
import computations.wheel.Wheel.WheelWay;
import database.DatabaseAccessor;

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

			DataRecord record = buildRecord(ballLapTimes, wheelLapTimes);
			if(record != null) {
				record.outcome = da.getOutcome(sessionId);
				cache.add(record);
			}
		}
	}

	private DataRecord buildRecord(List<Double> ballLapTimes, List<Double> wheelLapTimes) {
		
		if(ballLapTimes.isEmpty() || wheelLapTimes.isEmpty()) {
			return null;
		}
		
		DataRecord record = new DataRecord();
		AccelerationModel ballAccModel = BallisticManager.computeModel(ballLapTimes, Type.BALL);
		AccelerationModel wheelAccModel = BallisticManager.computeModel(wheelLapTimes, Type.WHEEL);

		double refTime = wheelLapTimes.get(0);
		double timeInterval = Constants.TIME_BEFORE_FORECASTING / Constants.NUMBER_OF_SPEEDS_IN_DATASET;
		for (int i = 1; i <= Constants.NUMBER_OF_SPEEDS_IN_DATASET; i++) {
			double timeForDataset = refTime + i * timeInterval;
			record.ballSpeeds.add(new ClockSpeed(timeForDataset, ballAccModel.estimateSpeed(timeForDataset)));
			record.wheelSpeeds.add(new ClockSpeed(timeForDataset, wheelAccModel.estimateSpeed(timeForDataset)));
		}

		for (int i = 1; i <= Constants.NUMBER_OF_SPEEDS_IN_DATASET; i++) {
			double wheelLapTime = wheelLapTimes.get(i);
			double previousWheelLapTime = wheelLapTimes.get(i - 1);

			double correspondingBallLapTime = Phase.getNextTimeBallIsInFrontOfRef(ballLapTimes, wheelLapTime);
			double lastWheelSpeed = BallisticManager.getWheelSpeed(previousWheelLapTime, wheelLapTime);

			/**
			 * TODO: implement in the database the CLOCKWISE/ANTICLOCKWISE way
			 * column in a table. Change WheelWay.ANTICLOCKWISE
			 */
			record.phases.add(Phase.findPhaseBetweenBallAndWheel(correspondingBallLapTime, wheelLapTime, lastWheelSpeed,
					WheelWay.ANTICLOCKWISE));
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
	private int alignThePhase(DataRecord dr1, DataRecord dr2) {
		if (dr1.phases.size() != dr2.phases.size()) {
			return -1; // Cannot compare them
		}

		double minDist = Double.MAX_VALUE;
		int maxShift = Wheel.NUMBERS.length;
		int selectedShift = -1;
		for (int shift = -maxShift; shift <= maxShift; shift++) {

			List<Integer> dr1Copy = new ArrayList<Integer>(dr1.phases);

			for (int i = 0; i < dr1.phases.size(); i++) {
				dr1Copy.set(i, dr1.phases.get(i) + shift);
			}

			double dist = 0;
			for (int i = 0; i < dr1Copy.size(); i++) {
				dist += Math.abs(dr1Copy.get(i) - dr2.phases.get(i));
			}

			if (dist < minDist) {
				minDist = dist;
				selectedShift = shift;
			}
		}

		return selectedShift;
	}

	public int predict(List<Double> wheelLapTimes, List<Double> ballLapTimes) {
		DataRecord recordToPredict = buildRecord(ballLapTimes, wheelLapTimes); // phase
																				// is
																				// filled.

		List<DataRecord> neighbors = KNN.getNeighbors(recordToPredict, cache);
		List<Integer> shifts = new ArrayList<>();
		for (DataRecord neighbor : neighbors) {
			int alignedPhase = alignThePhase(neighbor, recordToPredict);
			shifts.add(alignedPhase);
		}

		OutcomeStatistics outcomeStatistics = KNN.getOutcomeStatistics(neighbors, shifts);
		return outcomeStatistics.mostProbablyNumber;
	}

}
