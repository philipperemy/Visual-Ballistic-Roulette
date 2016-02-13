package computations.predictor.physics;

import java.util.List;

import computations.Constants;
import computations.predictor.Predictor;
import computations.utils.Helper;

public abstract class PredictorPhysics implements Predictor
{
	double			cutoffSpeed;
	double			originTimeBall;

	List<Double>	ballCumsumTimes;
	List<Double>	wheelCumsumTimes;

	double			originTimeWheel;
	double			diffOrigin;
	Double			lastTimeBallPassesInFrontOfRef;

	List<Double>	ballDiffTimes;
	List<Double>	wheelDiffTimes;

	public void init(List<Double> ballCumsumTimes, List<Double> wheelCumsumTimes)
	{
		this.cutoffSpeed = Constants.CUTOFF_SPEED;

		this.originTimeBall = Helper.head(ballCumsumTimes);
		this.ballCumsumTimes = Helper.normalize(ballCumsumTimes, originTimeBall);

		this.originTimeWheel = Helper.head(wheelCumsumTimes);
		this.wheelCumsumTimes = Helper.normalize(wheelCumsumTimes, originTimeWheel);

		this.diffOrigin = originTimeBall - originTimeWheel;
		this.lastTimeBallPassesInFrontOfRef = Helper.peek(ballCumsumTimes);

		this.ballDiffTimes = Helper.computeDiff(ballCumsumTimes);
		this.wheelDiffTimes = Helper.computeDiff(wheelCumsumTimes);
	}
}
