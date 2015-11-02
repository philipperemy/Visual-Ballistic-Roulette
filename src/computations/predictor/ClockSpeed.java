package computations.predictor;

public class ClockSpeed
{
	@Override
	public String toString()
	{
		return "ClockSpeed [time=" + time + ", speed=" + speed + "]";
	}

	public double time;
	public double speed;

	public ClockSpeed(double time, double speed)
	{
		this.time = time;
		this.speed = speed;
	}

	public ClockSpeed()
	{
	}
}