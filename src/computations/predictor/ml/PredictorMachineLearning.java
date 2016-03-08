package computations.predictor.ml;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import computations.Constants;
import computations.predictor.Outcome;
import computations.predictor.Phase;
import computations.predictor.Predictor;
import computations.predictor.ml.model.DataRecord;
import computations.predictor.ml.solver.PredictorSolver;
import computations.predictor.physics.linearlaptimes.HelperLinearLapTimes;
import computations.utils.Helper;
import database.DatabaseAccessorInterface;
import utils.logger.Logger;

/**
 * This predictor uses machine learning algorithms to find the output of the game.
 * For now it uses the linear regression of the lap times (and not the constant deceleration).
 */
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

	public List<DataRecord> buildDataRecords(List<Double> ballCumsumTimes, List<Double> wheelCumsumTimes)
	{
		List<DataRecord> dataRecords = new ArrayList<>();
		if (ballCumsumTimes.isEmpty() || wheelCumsumTimes.isEmpty())
		{
			return dataRecords;
		}

		// We need at least three ball measures. To build the model (for one acceleration)
		if (ballCumsumTimes.size() < Constants.MIN_NUMBER_OF_BALL_TIMES_BEFORE_PREDICTION)
		{
			return dataRecords;
		}

		double originTimeBall = Helper.head(ballCumsumTimes);
		ballCumsumTimes = Helper.normalize(ballCumsumTimes, originTimeBall);

		double originTimeWheel = Helper.head(wheelCumsumTimes);
		wheelCumsumTimes = Helper.normalize(wheelCumsumTimes, originTimeWheel);

		double diffOrigin = originTimeBall - originTimeWheel;

		List<Double> ballDiffTimes = Helper.computeDiff(ballCumsumTimes);
		List<Double> rangeBall = Helper.range(1, ballDiffTimes.size());
		SimpleRegression ballSpeedModel = Helper.performRegression(rangeBall, ballDiffTimes);

		List<Double> wheelDiffTimes = Helper.computeDiff(wheelCumsumTimes);
		double constantWheelSpeed = Helper.getWheelSpeed(Helper.peek(wheelDiffTimes));

		for (int i = 0; i < ballCumsumTimes.size(); i++)
		{
			double correspondingBallLapTime = ballCumsumTimes.get(i);
			double wheelSpeedInFrontOfMark = constantWheelSpeed; // Simplification.

			Double lastWheelLapTimeInFrontOfRef = Helper.getLastTimeWheelIsInFrontOfRef(wheelCumsumTimes, correspondingBallLapTime + diffOrigin);

			// It means that the first record(s) occurred before the 1st wheel recorded measurement.
			if (lastWheelLapTimeInFrontOfRef == null)
			{
				Logger.traceDEBUG("Ball time = " + correspondingBallLapTime + " is ahead of the wheel. Skipping.");
				continue;
			}

			int phase = Phase.findPhaseNumberBetweenBallAndWheel(correspondingBallLapTime, lastWheelLapTimeInFrontOfRef, wheelSpeedInFrontOfMark,
					Constants.DEFAULT_WHEEL_WAY);

			DataRecord smr = new DataRecord();
			smr.ballSpeedInFrontOfMark = HelperLinearLapTimes.estimateSpeed(correspondingBallLapTime, Constants.get_BALL_CIRCUMFERENCE(), ballSpeedModel);
			smr.wheelSpeedInFrontOfMark = wheelSpeedInFrontOfMark;
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
