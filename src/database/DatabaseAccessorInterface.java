package database;

import java.util.List;

public interface DatabaseAccessorInterface
{
	public void insertBallLapTimes(String sessionId, String lapTime);

	public void insertWheelLapTimes(String sessionId, String lapTime);

	public String incrementAndGetSessionId();

	public Outcome getOutcome(String sessionId);

	public List<String> getSessionIds();

	public String getLastSessionId();

	public List<Double> selectBallLapTimes(String sessionId);

	public List<Double> selectWheelLapTimes(String sessionId);

	public void insertOutcome(String sessionId, String number);
}