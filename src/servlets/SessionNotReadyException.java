package servlets;

public class SessionNotReadyException extends Exception
{

	private static final long serialVersionUID = 5655911156126439803L;

	private int numberOfRecordedWheelTimes = 0;

	public SessionNotReadyException(int numberOfRecordedWheelTimes)
	{
		this.numberOfRecordedWheelTimes = numberOfRecordedWheelTimes;
	}

	public String getNumberOfRecordedTimesOfWheel()
	{
		return String.valueOf(numberOfRecordedWheelTimes);
	}

}
