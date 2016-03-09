package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import computations.Constants;
import utils.exception.CriticalException;
import utils.logger.Logger;

public final class DatabaseAccessor implements DatabaseAccessorInterface
{
	private static final String WHEEL_LAP_TIMES_TABLE_NAME = "wheel_lap_times";
	private static final String BALL_LAP_TIMES_TABLE_NAME = "ball_lap_times";
	private static final String DATABASE_NAME = Constants.DATABASE_NAME;

	private static DatabaseAccessor instance;

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
			connect = DriverManager.getConnection("jdbc:mysql://localhost/" + DATABASE_NAME + "?" + "user=root&password=");

		} catch (Exception e)
		{
			Logger.traceERROR(e);
			throw new CriticalException("Cannot connected to the database.");
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
		String query = "INSERT INTO `" + tableName + "` (`ID`, `SESSION_ID`, `TIME`) VALUES (NULL, '" + sessionId + "', '" + lapTime + "');";
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
		String query = "INSERT INTO `session` (`ID`) VALUES (NULL);";
		try
		{
			connect.createStatement().execute(query);
		} catch (SQLException e)
		{
			Logger.traceERROR(e);
		}
		return getLastSessionId();
	}

	public Integer getOutcome(String sessionId)
	{
		String query = "SELECT * FROM `outcomes` WHERE SESSION_ID = " + sessionId + ";";
		try
		{
			ResultSet resultSet = connect.createStatement().executeQuery(query);
			if (resultSet.next())
			{
				return Integer.parseInt(resultSet.getString("NUMBER"));
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

	@Override
	public void insertOutcome(String sessionId, String number)
	{
		String query = "INSERT INTO `outcomes` (`ID`, `SESSION_ID`, `NUMBER`) VALUES (NULL, '" + sessionId + "', '" + number + "');";
		try
		{
			connect.createStatement().execute(query);
		} catch (SQLException e)
		{
			Logger.traceERROR(e);
		}
	}
}