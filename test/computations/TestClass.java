package computations;

import org.junit.BeforeClass;

import database.DatabaseAccessorInterface;
import database.DatabaseAccessorStub;
import servlets.Response;

public class TestClass {

	protected static DatabaseAccessorInterface dbRef;
	protected static Response response;

	@BeforeClass
	public static void setUp() {
		dbRef = new DatabaseAccessorStub();
		response = new Response(dbRef);
		response.da = dbRef;
	}

	public static String getTime(int hour, int min, int sec, int millis) {
		return String.valueOf((hour * 3600 + min * 60 + sec) * 1000 + millis);
	}

}
