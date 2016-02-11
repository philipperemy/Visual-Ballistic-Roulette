package experiments.time;

public class GetTime
{
	public String getTime(int hour, int min, int sec, int millis)
	{
		return String.valueOf((hour * 3600 + min * 60 + sec) * 1000 + millis);
	}
}
