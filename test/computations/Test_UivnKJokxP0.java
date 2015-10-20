package computations;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import database.DatabaseAccessorInterface;
import database.DatabaseAccessorStub;
import servlets.Response;
import servlets.SessionNotReadyException;

public class Test_UivnKJokxP0 {

	public static String getTime(int hour, int min, int sec, int millis) {
		return String.valueOf((hour * 3600 + min * 60 + sec) * 1000 + millis);
	}

	private static DatabaseAccessorInterface dbRef;
	private static Response response;

	@BeforeClass
	public static void setUp() {
		dbRef = new DatabaseAccessorStub();
		response = new Response(dbRef);
		response.da = dbRef;
	}

	private void fill_1_23_601(String sessionId) {
		dbRef.insertClockwise(sessionId, "0");

		dbRef.insertBallLapTimes(sessionId, getTime(0, 1, 23, 601));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 1, 24, 962));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 1, 26, 578));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 1, 28, 394));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 1, 30, 369));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 1, 32, 731));

		dbRef.insertWheelLapTimes(sessionId, getTime(0, 1, 23, 416));
		dbRef.insertWheelLapTimes(sessionId, getTime(0, 1, 29, 723));
		dbRef.insertWheelLapTimes(sessionId, getTime(0, 1, 36, 510));

		dbRef.insertOutcome(sessionId, "4");

		response.forceDatasetReInit();
	}

	private void fill_1_47_071(String sessionId) {
		dbRef.insertClockwise(sessionId, "1");

		dbRef.insertBallLapTimes(sessionId, getTime(0, 1, 47, 071));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 1, 48, 276));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 1, 49, 737));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 1, 51, 404));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 1, 53, 220));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 1, 55, 308));

		dbRef.insertWheelLapTimes(sessionId, getTime(0, 1, 48, 867));
		dbRef.insertWheelLapTimes(sessionId, getTime(0, 1, 54, 153));
		dbRef.insertWheelLapTimes(sessionId, getTime(0, 1, 59, 717));

		dbRef.insertOutcome(sessionId, "28");

		response.forceDatasetReInit();
	}
	
	private void fill_2_07_462(String sessionId, boolean addOutcome) {
		dbRef.insertClockwise(sessionId, "0");

		dbRef.insertBallLapTimes(sessionId, getTime(0, 2, 7, 462));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 2, 8, 807));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 2, 10, 303));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 2, 11, 977));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 2, 13, 774));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 2, 15, 700));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 2, 17, 817));
		dbRef.insertBallLapTimes(sessionId, getTime(0, 2, 20, 204));
		
		dbRef.insertWheelLapTimes(sessionId, getTime(0, 2, 11, 829));
		dbRef.insertWheelLapTimes(sessionId, getTime(0, 2, 18, 577));
		dbRef.insertWheelLapTimes(sessionId, getTime(0, 2, 26, 026));
		
		if(addOutcome) {
			dbRef.insertOutcome(sessionId, "13");
			response.forceDatasetReInit();
		}
		
	}
	
	@Test 
	public void test_2() throws SessionNotReadyException { 
		String sessionId = dbRef.getLastSessionId();
		fill_1_23_601(sessionId); //1
		sessionId = dbRef.incrementAndGetSessionId();
		fill_1_47_071(sessionId); //2
		sessionId = dbRef.incrementAndGetSessionId();
		fill_2_07_462(sessionId, false); //3
		
		response.predictMostProbableNumber(sessionId);
		
	  // 13.
	}
	

}
