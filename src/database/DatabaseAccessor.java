package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;

import computations.Constants;
import computations.model.Outcome;
import log.Logger;

public class DatabaseAccessor implements DatabaseAccessorInterface
{

	private static final String WHEEL_LAP_TIMES_TABLE_NAME = "wheel_lap_times";
	private static final String BALL_LAP_TIMES_TABLE_NAME = "ball_lap_times";
	private static volatile DatabaseAccessor instance;

	public static DatabaseAccessor getInstance()
	{
		if (instance == null)
		{
			instance = new DatabaseAccessor();
		}
		return instance;
	}

	private Connection connect = null;

	private DatabaseAccessor()
	{
		readDataBase();
	}

	private void readDataBase()
	{
		try
		{

			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection("jdbc:mysql://localhost/roulette_db?" + "user=root&password=");

		} catch (CommunicationsException ce)
		{
			Logger.traceERROR(ce);
			System.exit(0);
		} catch (Exception e)
		{
			Logger.traceERROR(e);
		}
	}

	public void insertBallLapTimes(String sessionId, String lapTime)
	{
		Logger.traceINFO("insertBallLapTimes, sessionId = " + sessionId + ", laptime = " + lapTime);
		insert(BALL_LAP_TIMES_TABLE_NAME, sessionId, lapTime);
	}

	public void insertWheelLapTimes(String sessionId, String lapTime)
	{
		Logger.traceINFO("insertWheelLapTimes, sessionId = " + sessionId + ", laptime = " + lapTime);
		insert(WHEEL_LAP_TIMES_TABLE_NAME, sessionId, lapTime);
	}

	private void insert(String tableName, String sessionId, String lapTime)
	{
		String query = "INSERT INTO `roulette_db`.`" + tableName + "` (`ID`, `SESSION_ID`, `TIME`) VALUES (NULL, '" + sessionId + "', '" + lapTime
				+ "');";
		try
		{
			connect.createStatement().execute(query);
		} catch (SQLException e)
		{
			Logger.traceERROR(e);
		}
	}

	public String incrementAndGetSessionId()
	{
		String query = "INSERT INTO `roulette_db`.`session` (`ID`) VALUES (NULL);";
		try
		{
			connect.createStatement().execute(query);
		} catch (SQLException e)
		{
			Logger.traceERROR(e);
		}
		return getLastSessionId();
	}

	public Outcome getOutcome(String sessionId)
	{
		String query = "SELECT * FROM `outcomes` WHERE SESSION_ID = " + sessionId + ";";
		try
		{
			ResultSet resultSet = connect.createStatement().executeQuery(query);
			Outcome outcome = new Outcome();
			if (resultSet.next())
			{
				outcome.number = Integer.parseInt(resultSet.getString("NUMBER"));
				outcome.obstaclesHitCount = Integer.parseInt(resultSet.getString("OBSTACLES"));
				return outcome;
			}
		} catch (SQLException e)
		{
			Logger.traceERROR(e);
		}
		return null;
	}

	public List<String> getSessionIds()
	{
		String query = "SELECT ID from session";
		List<String> ids = new ArrayList<>();
		try
		{
			ResultSet resultSet = connect.createStatement().executeQuery(query);
			while (resultSet.next())
			{
				ids.add(resultSet.getString("ID"));
			}

		} catch (SQLException e)
		{
			Logger.traceERROR(e);
		}
		return ids;
	}

	public String getLastSessionId()
	{
		String query = "SELECT ID from session ORDER BY id DESC LIMIT 1";
		try
		{
			ResultSet resultSet = connect.createStatement().executeQuery(query);
			while (resultSet.next())
			{
				return resultSet.getString("ID");
			}

		} catch (SQLException e)
		{
			Logger.traceERROR(e);
		}
		return null;
	}

	public List<Double> selectBallLapTimes(String sessionId)
	{
		return selectLapTimes(BALL_LAP_TIMES_TABLE_NAME, sessionId);
	}

	public List<Double> selectWheelLapTimes(String sessionId)
	{
		return selectLapTimes(WHEEL_LAP_TIMES_TABLE_NAME, sessionId);
	}

	private List<Double> selectLapTimes(String tableName, String sessionId)
	{
		String sqlQuery = "SELECT TIME FROM `" + tableName + "` WHERE SESSION_ID = " + sessionId + ";";
		List<Double> list = new ArrayList<>();
		try
		{
			ResultSet resultSet = connect.createStatement().executeQuery(sqlQuery);
			while (resultSet.next())
			{
				list.add(Double.valueOf(resultSet.getString("TIME")));
			}

		} catch (SQLException e)
		{
			Logger.traceERROR(e);
		}
		return list;
	}

	private String select(String SQLquery, String field)
	{
		try
		{
			ResultSet resultSet = connect.createStatement().executeQuery(SQLquery);
			while (resultSet.next())
			{
				return resultSet.getString(field);
			}

		} catch (SQLException e)
		{
			Logger.traceERROR(e);
		}
		return null;
	}

	public void close()
	{
		try
		{
			if (connect != null)
			{
				connect.close();
			}
		} catch (Exception e)
		{
			Logger.traceERROR(e);
		}
	}

	public void insertClockwiseFromPrevious(String sessionId)
	{
		// Compute new one
		String clockwise = getLastClockwise().equals(Constants.WHEEL_ANTICLOCKWISE) ? Constants.WHEEL_CLOCKWISE : Constants.WHEEL_ANTICLOCKWISE;
		insertClockwise(sessionId, clockwise);
	}

	// Should check if exist and update accordingly. The user should be able to
	// overwrite the default values.
	public void insertClockwise(String sessionId, String clockwise)
	{
		String query = "INSERT INTO `roulette_db`.`clockwise` (`ID`, `CLOCKWISE`, `SESSION_ID`) VALUES (NULL, '" + clockwise + "', '" + sessionId
				+ "');";
		try
		{
			connect.createStatement().execute(query);
		} catch (SQLException e)
		{
			Logger.traceERROR(e);
		}
	}

	public String selectClockwise(String sessionId)
	{
		String query = "SELECT * FROM `clockwise` WHERE SESSION_ID = " + sessionId;
		return select(query, "CLOCKWISE");
	}

	public String getLastClockwise()
	{
		// too dangerous to choose the last session.
		String query = "SELECT * FROM `clockwise` WHERE ID = (SELECT MAX(ID) FROM `clockwise`)";
		try
		{
			ResultSet resultSet = connect.createStatement().executeQuery(query);
			while (resultSet.next())
			{
				return resultSet.getString("CLOCKWISE");
			}

		} catch (SQLException e)
		{
			Logger.traceERROR(e);
		}
		return null;
	}

	// TODO: to test
	@Override
	public void insertOutcome(String sessionId, String number)
	{
		String query = "INSERT INTO `roulette_db`.`outcomes` (`ID`, `SESSION_ID`, `NUMBER`, `OBSTACLES`) VALUES (NULL, '" + sessionId + "', '"
				+ number + "', '0');";
		try
		{
			connect.createStatement().execute(query);
		} catch (SQLException e)
		{
			Logger.traceERROR(e);
		}
	}

}