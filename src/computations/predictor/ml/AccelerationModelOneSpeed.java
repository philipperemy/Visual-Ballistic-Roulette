package computations.predictor.ml;

//We cannot constructor an acceleration speed model with just one speed.
//So we create an object to emulate this model, which returns the last wheel speed.
class AccelerationModelOneSpeed extends AccelerationModel
{
	private double lastSpeed = 0;

	AccelerationModelOneSpeed(double lastSpeed)
	{
		super(0, 0);
		this.lastSpeed = lastSpeed;
	}

	@Override
	public String toString()
	{
		return "AccelerationModelOneSpeed [lastSpeed=" + lastSpeed + "]";
	}

	@Override
	public double estimateSpeed(double time)
	{
		return lastSpeed;
	}

}
