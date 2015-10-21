package database;

import java.util.List;

import computations.model.Outcome;

public interface DatabaseAccessorInterface {

	public void insertBallLapTimes(String sessionId, String lapTime);

	public void insertWheelLapTimes(String sessionId, String lapTime);

	public String incrementAndGetSessionId();

	public Outcome getOutcome(String sessionId);

	public List<String> getSessionIds();

	public String getLastSessionId();

	public List<Double> selectBallLapTimes(String sessionId);

	public List<Double> selectWheelLapTimes(String sessionId);

	public void insertClockwiseFromPrevious(String sessionId);

	public void insertClockwise(String sessionId, String clockwise);

	public void insertOutcome(String sessionId, String number);

	public String selectClockwise(String sessionId);

	public String getLastClockwise();
}