package framework.games;

import java.util.ArrayList;
import java.util.List;

import computations.Constants;
import framework.TestClass;

public abstract class Game
{
	public List<String> _ballLaptimes = new ArrayList<>();
	public List<String> _wheelLaptimes = new ArrayList<>();
	public Integer _outcome = null;

	public Game()
	{
		_ballLaptimes = get_ballLaptimes();
		_wheelLaptimes = get_wheelLaptimes();
		_outcome = get_outcome();
	}

	public abstract List<String> get_ballLaptimes();

	public abstract List<String> get_wheelLaptimes();

	public abstract Integer get_outcome();

	public void run(String sessionId)
	{
		TestClass.dbRef.insertClockwise(sessionId, Constants.WHEEL_ANTICLOCKWISE);

		for (String time : _ballLaptimes)
		{
			TestClass.dbRef.insertBallLapTimes(sessionId, time);
		}

		for (String time : _wheelLaptimes)
		{
			TestClass.dbRef.insertWheelLapTimes(sessionId, time);
		}

		if (_outcome != null)
		{
			TestClass.dbRef.insertOutcome(sessionId, String.valueOf(_outcome));
		}
	}

}
