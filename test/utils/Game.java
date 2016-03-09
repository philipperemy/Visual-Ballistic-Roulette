package utils;

import java.util.ArrayList;
import java.util.List;

import database.DatabaseAccessorInterface;
import utils.logger.Logger;

public class Game
{
	private List<Double> _ballLaptimes = new ArrayList<>();
	private List<Double> _wheelLaptimes = new ArrayList<>();
	private Integer _outcome = null;
	private String _sessionId = null;

	public Game(String sessionId, DatabaseAccessorInterface dbRef)
	{
		_sessionId = sessionId;
		_ballLaptimes = computations.utils.Helper.convertToSeconds(dbRef.selectBallLapTimes(_sessionId));
		_wheelLaptimes = computations.utils.Helper.convertToSeconds(dbRef.selectWheelLapTimes(_sessionId));
		Integer out = dbRef.getOutcome(_sessionId);
		if (out != null)
		{
			_outcome = dbRef.getOutcome(_sessionId);
		} else
		{
			Logger.traceERROR("Outcome is null for sessionId = " + sessionId);
		}
	}

	public String get_sessionId()
	{
		return _sessionId;
	}

	public List<Double> get_ballLaptimes()
	{
		return _ballLaptimes.subList(0, _ballLaptimes.size() - Constants.NUMBER_OF_BALL_LAP_TIMES_TO_DISCARD);
	}

	public List<Double> get_wheelLaptimes()
	{
		return _wheelLaptimes;
	}

	public Integer get_outcome()
	{
		return _outcome;
	}

	@Override
	public String toString()
	{
		return "Game [_sessionId=" + _sessionId + "]";
	}

}
