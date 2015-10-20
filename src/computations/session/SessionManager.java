package computations.session;

import computations.Constants;
import database.DatabaseAccessorInterface;

/**
 * A session corresponds to a game played or measured
 */
public class SessionManager {

	private static volatile SessionManager instance = null;
	private DatabaseAccessorInterface da;
	private long timestampOfLastQuery = 0;

	public void init(DatabaseAccessorInterface da) {
		this.da = da;
	}

	public static SessionManager getInstance() {
		if (instance == null) {
			instance = new SessionManager();
		}
		return instance;
	}

	private SessionManager() {
	}

	public String callManager(long queryTime) {
		String newSessionId = null;
		if (queryTime - timestampOfLastQuery > Constants.THRESHOLD_BEFORE_NEW_SESSION_IN_MS) {
			// Start new session
			newSessionId = da.incrementAndGetSessionId();
			da.insertClockwiseFromPrevious(newSessionId); // Start new clockwise
		} else {
			newSessionId = da.getLastSessionId();
		}
		// update time of last query
		this.timestampOfLastQuery = queryTime;
		return newSessionId;
	}
}
