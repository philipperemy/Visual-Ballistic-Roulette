package computations.predictor.ml;

import java.util.ArrayList;
import java.util.List;

import computations.Constants;
import computations.predictor.Outcome;
import computations.predictor.Phase;
import computations.predictor.Predictor;
import computations.predictor.ml.model.DataRecord;
import computations.predictor.ml.solver.PredictorSolver;
import computations.utils.Helper;
import database.DatabaseAccessorInterface;
import utils.logger.Logger;

public class PredictorMachineLearning implements Predictor
{
	private DatabaseAccessorInterface	da;
	private PredictorSolver				solver	= Constants.PREDICTOR_SOLVER;

	@Override
	public void init(DatabaseAccessorInterface da)
	{
		init(da, da.getSessionIds());
	}

	@Override
	public void init(DatabaseAccessorInterface da, List<String> sessionIds)
	{
		this.da = da;
		load(sessionIds);
	}

	public int predict(List<Double> ballLapTimes, List<Double> wheelLapTimes)
	{
		return solver.predict(this, ballLapTimes, wheelLapTimes);
	}

	private void load(List<String> sessionIds)
	{
		for (String sessionId : sessionIds)
		{
			List<Double> ballCumsumTimes = computations.utils.Helper.convertToSeconds(da.selectBallLapTimes(sessionId));
			List<Double> wheelCumsumTimes = computations.utils.Helper.convertToSeconds(da.selectWheelLapTimes(sessionId));

			if (wheelCumsumTimes.isEmpty())
			{
				Logger.traceERROR("Wheel cumsum times are empty for session id = " + sessionId + ". Ignoring this game.");
				continue;
			}

			if (ballCumsumTimes.isEmpty())
			{
				Logger.traceERROR("Ball cumsum times are empty for session id = " + sessionId + ". Ignoring this game.");
				continue;
			}

			try
			{
				List<DataRecord> records = buildDataRecords(ballCumsumTimes, wheelCumsumTimes);
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

	public List<DataRecord> buildDataRecords(List<Double> ballLapTimes, List<Double> wheelLapTimes)
	{
		List<DataRecord> dataRecords = new ArrayList<>();
		if (ballLapTimes.isEmpty() || wheelLapTimes.isEmpty())
		{
			return dataRecords;
		}

		// We need at least three ball measures. To build the model (for one
		// acceleration)
		if (ballLapTimes.size() < Constants.MIN_NUMBER_OF_BALL_TIMES_BEFORE_PREDICTION)
		{
			return dataRecords;
		}

		AccelerationModel ballAccModel = BallisticManager.computeModel(ballLapTimes, Constants.Type.BALL);
		AccelerationModel wheelAccModel = BallisticManager.computeModel(wheelLapTimes, Constants.Type.WHEEL);

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
			smr.sessionId = null;
			smr.phaseOfWheelWhenBallPassesInFrontOfMark = phase;
			dataRecords.add(smr);
		}

		return dataRecords;
	}

	@Override
	public void clear()
	{
		DataRecord.clearCache();
	}
}
