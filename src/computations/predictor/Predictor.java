package computations.predictor;

import java.util.ArrayList;
import java.util.List;

import computations.Constants;
import computations.Helper;
import computations.model.DataRecord;
import computations.model.Outcome;
import computations.model.OutcomeStatistics;
import computations.wheel.Type;
import database.DatabaseAccessorInterface;
import logger.Logger;
import servlets.SessionNotReadyException;

public class Predictor
{
	private static volatile Predictor instance = null;
	private DatabaseAccessorInterface da;

	public void init(DatabaseAccessorInterface da)
	{
		this.da = da;
		preLoadDataSet();
	}

	private void preLoadDataSet()
	{
		List<String> sessionIds = da.getSessionIds();
		for (String sessionId : sessionIds)
		{
			// SessionID = GameID
			List<Double> ballLapTimes = da.selectBallLapTimes(sessionId);
			List<Double> wheelLapTimes = da.selectWheelLapTimes(sessionId);

			List<Double> wheelLapTimesSeconds = computations.Helper.convertToSeconds(wheelLapTimes);
			List<Double> ballLapTimesSeconds = computations.Helper.convertToSeconds(ballLapTimes);

			if (wheelLapTimes.isEmpty())
			{
				Logger.traceERROR("Wheel lap times are empty for session id = " + sessionId + ". Ignoring this game.");
				continue;
			}

			if (ballLapTimes.isEmpty())
			{
				Logger.traceERROR("Ball lap times are empty for session id = " + sessionId + ". Ignoring this game.");
				continue;
			}

			try
			{
				List<DataRecord> records = buildDataRecords(ballLapTimesSeconds, wheelLapTimesSeconds, sessionId);
				for (DataRecord record : records)
				{
					Outcome outcome = da.getOutcome(sessionId);
					if (outcome != null)
					{
						record.outcome = outcome.number;
					} else
					{
						Logger.traceERROR("No outcome for session id = " + sessionId);
					}
					record.cacheIt();
				}
			} catch (Exception e)
			{
				Logger.traceERROR(e);
			}

		}
	}

	private List<DataRecord> buildDataRecords(List<Double> ballLapTimes, List<Double> wheelLapTimes, String sessionId)
	{
		List<DataRecord> dataRecords = new ArrayList<>();
		if (ballLapTimes.isEmpty() || wheelLapTimes.isEmpty())
		{
			return dataRecords;
		}

		// We need at least three ball measures. To build the model (for one
		// acceleration)
		if (ballLapTimes.size() < Constants.MINIMUM_NUMBER_OF_BALL_TIMES_BEFORE_FORECASTING)
		{
			return dataRecords;
		}

		AccelerationModel ballAccModel = BallisticManager.computeModel(ballLapTimes, Type.BALL);
		AccelerationModel wheelAccModel = BallisticManager.computeModel(wheelLapTimes, Type.WHEEL);

		for (int i = 0; i < ballLapTimes.size(); i++)
		{
			double correspondingBallLapTime = ballLapTimes.get(i);
			double wheelSpeedInFrontOfMark = wheelAccModel.estimateSpeed(correspondingBallLapTime);

			Double lastWheelLapTimeInFrontOfRef = Helper.getLastTimeWheelIsInFrontOfRef(wheelLapTimes, correspondingBallLapTime);

			// It means that the first record is a ball lap times.
			if (lastWheelLapTimeInFrontOfRef == null)
			{
				continue;
			}

			int phase = Phase.findPhaseNumberBetweenBallAndWheel(correspondingBallLapTime, lastWheelLapTimeInFrontOfRef, wheelSpeedInFrontOfMark,
					Constants.DEFAULT_WHEEL_WAY);

			DataRecord smr = new DataRecord();
			// Done because the speed is measured between t1 and t2 and reflects
			// the average speed, 0.5*t1+0.5*t2. At t2, the speed is less than
			// this average.
			smr.ballSpeedInFrontOfMark = ballAccModel.estimateSpeed(correspondingBallLapTime);
			smr.wheelSpeedInFrontOfMark = wheelSpeedInFrontOfMark;
			smr.sessionId = sessionId;
			smr.phaseOfWheelWhenBallPassesInFrontOfMark = phase;
			dataRecords.add(smr);
		}

		return dataRecords;
	}

	public static Predictor getInstance()
	{
		if (instance == null)
		{
			instance = new Predictor();
		}
		return instance;
	}

	private Predictor()
	{
	}

	public int predict_old(List<Double> ballLapTimes, List<Double> wheelLapTimes, String sessionId) throws SessionNotReadyException
	{
		// Phase is filled. All lap times are used to build the model.
		List<DataRecord> predictRecords = buildDataRecords(ballLapTimes, wheelLapTimes, sessionId);

		if (predictRecords.isEmpty())
		{
			Logger.traceERROR("No records to predict.");
			throw new SessionNotReadyException(wheelLapTimes.size());
		}

		// Take the last one.
		// TODO: maybe we can improve it by taking all.
		DataRecord predictRecord = Helper.peek(predictRecords);
		Logger.traceINFO("Record to predict : " + predictRecord);

		int mostProbableNumber = DataRecord.predictOutcome(predictRecord);
		Logger.traceINFO("Most probable number : " + mostProbableNumber);
		return mostProbableNumber;
	}

	public int predict(List<Double> ballLapTimes, List<Double> wheelLapTimes, String sessionId) throws SessionNotReadyException
	{
		// Phase is filled. All lap times are used to build the model.
		List<DataRecord> predictRecords = buildDataRecords(ballLapTimes, wheelLapTimes, sessionId);

		if (predictRecords.isEmpty())
		{
			Logger.traceERROR("No records to predict.");
			throw new SessionNotReadyException(wheelLapTimes.size());
		}

		List<Integer> mostProbableNumberList = new ArrayList<>();
		for (int i = 0; i < predictRecords.size(); i++)
		{
			DataRecord predictRecord = predictRecords.get(i);
			Logger.traceINFO("(" + i + ") Record to predict : " + predictRecord);
			int mostProbableNumber = DataRecord.predictOutcome(predictRecord);
			Logger.traceINFO("(" + i + ") Most probable number : " + mostProbableNumber);
			mostProbableNumberList.add(mostProbableNumber);
		}

		int size = mostProbableNumberList.size();
		int par = Constants.RECORDS_COUNT_FOR_PREDICTION;
		if (size >= par)
		{
			mostProbableNumberList = mostProbableNumberList.subList(size - par, size);
		}

		OutcomeStatistics os = OutcomeStatistics.create(mostProbableNumberList);
		int mostProbableFinalNumber = os.meanNumber;
		Logger.traceINFO("(final) Most probable number : " + mostProbableFinalNumber);
		return os.meanNumber;
	}

	public int predict_3(List<Double> ballLapTimes, List<Double> wheelLapTimes, String sessionId) throws SessionNotReadyException
	{
		// Phase is filled. All lap times are used to build the model.
		List<DataRecord> predictRecords = buildDataRecords(ballLapTimes, wheelLapTimes, sessionId);

		if (predictRecords.isEmpty())
		{
			Logger.traceERROR("No records to predict.");
			throw new SessionNotReadyException(wheelLapTimes.size());
		}

		List<Integer> mostProbableNumberList = new ArrayList<>();
		int id = 1;
		for (DataRecord predictRecord : predictRecords)
		{
			Logger.traceINFO("(" + id + ") Record to predict : " + predictRecord);
			int mostProbableNumber = DataRecord.predictOutcome(predictRecord);
			Logger.traceINFO("(" + id + ") Most probable number : " + mostProbableNumber);
			mostProbableNumberList.add(mostProbableNumber);
			id++;
		}

		OutcomeStatistics os = OutcomeStatistics.create(mostProbableNumberList);
		int mostProbableFinalNumber = os.meanNumber;
		Logger.traceINFO("(final) Most probable number : " + mostProbableFinalNumber);
		return os.meanNumber;
	}

}
