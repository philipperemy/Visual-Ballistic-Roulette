package database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import computations.Constants;
import computations.model.Outcome;

public class DatabaseAccessorStub implements DatabaseAccessorInterface
{
	private Map<String, List<Double>> _ballLapTimes = new HashMap<>();
	private Map<String, List<Double>> _wheelLapTimes = new HashMap<>();
	private Map<String, Integer> _outcomes = new HashMap<>();
	private Integer _sessionId = 1;
	private Map<String, Boolean> _clockwise = new HashMap<>();

	private void appendMap(Map<String, List<Double>> map, String key, String value)
	{
		List<Double> list = map.get(key);
		if (list == null)
		{
			list = new ArrayList<>();
		}
		list.add(Double.valueOf(value));
		map.put(key, list);
	}

	private List<Double> getFromMap(Map<String, List<Double>> map, String key)
	{
		List<Double> list = map.get(key);
		if (list == null)
		{
			list = new ArrayList<>();
		}
		return list;
	}

	@Override
	public void insertBallLapTimes(String sessionId, String lapTime)
	{
		appendMap(_ballLapTimes, sessionId, lapTime);
	}

	@Override
	public void insertWheelLapTimes(String sessionId, String lapTime)
	{
		appendMap(_wheelLapTimes, sessionId, lapTime);

	}

	@Override
	public String incrementAndGetSessionId()
	{
		_sessionId++;
		return getLastSessionId();
	}

	@Override
	public Outcome getOutcome(String sessionId)
	{
		Outcome outcome = new Outcome();
		Integer num = _outcomes.get(sessionId);
		if (num == null)
		{
			return null;
		}
		outcome.number = num;
		outcome.obstaclesHitCount = 0;
		return outcome;
	}

	@Override
	public List<String> getSessionIds()
	{
		List<String> ids = new ArrayList<>();
		for (int i = 1; i <= _sessionId; i++)
		{
			ids.add(String.valueOf(i));
		}
		return ids;
	}

	@Override
	public String getLastSessionId()
	{
		return String.valueOf(_sessionId);
	}

	@Override
	public List<Double> selectBallLapTimes(String sessionId)
	{
		return getFromMap(_ballLapTimes, sessionId);
	}

	@Override
	public List<Double> selectWheelLapTimes(String sessionId)
	{
		return getFromMap(_wheelLapTimes, sessionId);
	}

	@Override
	public void insertClockwiseFromPrevious(String sessionId)
	{
		Boolean val = _clockwise.get(getLastClockwise());
		_clockwise.put(sessionId, !val);
	}

	@Override
	public void insertClockwise(String sessionId, String clockwise)
	{
		_clockwise.put(sessionId, clockwise.equals(Constants.WHEEL_CLOCKWISE));
	}

	@Override
	public String selectClockwise(String sessionId)
	{
		Boolean clockwise = _clockwise.get(sessionId);
		if (clockwise == null)
		{
			return null;
		}
		return clockwise ? Constants.WHEEL_CLOCKWISE : Constants.WHEEL_ANTICLOCKWISE;
	}

	@Override
	public String getLastClockwise()
	{
		return selectClockwise(String.valueOf(_sessionId));
	}

	@Override
	public void insertOutcome(String sessionId, String number)
	{
		_outcomes.put(sessionId, Integer.parseInt(number));
	}

}
