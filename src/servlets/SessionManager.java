package servlets;

import computations.Constants;
import database.DatabaseAccessorInterface;
import utils.logger.Logger;

/**
 * A session corresponds to a game played or measured
 */
public final class SessionManager
{
	private static SessionManager instance = null;
	private DatabaseAccessorInterface da;
	private long timestampOfLastQuery = 0;

	void init(DatabaseAccessorInterface da)
	{
		this.da = da;
	}

	static SessionManager getInstance()
	{
		if (instance == null)
		{
			instance = new SessionManager();
		}
		return instance;
	}

	private SessionManager()
	{
	}

	/**
	 * Everytime a new lap time (ball or wheel) is submitted, we chech if 30
	 * seconds have elapsed. If this is the case, it means that this is a new
	 * game and we should increment the session ID. If not, we just add it
	 * normally.
	 */
	String callManager(long queryTime)
	{
		String newSessionId = null;
		if (queryTime - timestampOfLastQuery > Constants.THRESHOLD_BEFORE_NEW_SESSION_IN_MS)
		{
			// Start new session
			newSessionId = da.incrementAndGetSessionId();
			Logger.traceINFO("Starting new session with id = " + newSessionId);
		} else
		{
			newSessionId = da.getLastSessionId();
		}
		// update time of last query
		this.timestampOfLastQuery = queryTime;
		return newSessionId;
	}
}
