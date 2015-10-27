package computations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import log.Logger;

public class Helper {

	static long lastQueryTimestamp = System.currentTimeMillis();
	
	public static List<Double> convertToSeconds(List<Double> listInMilliseconds) {
		List<Double> listInSeconds = new ArrayList<>();
		for (Double itemMsec : listInMilliseconds) {
			listInSeconds.add(itemMsec * 0.001);
		}
		return listInSeconds;
	}

	// [0, 4, 15, 19, 21, 26, 32]
	public static List<Integer> unserialize(String input) {
		String str = input.substring(1, input.length() - 1);
		List<Integer> list = new ArrayList<>();
		for (String chunk : str.split(",")) {
			list.add(Integer.parseInt(chunk.trim()));
		}
		return list;
	}

	public static <T> T pop(List<T> list) {
		return list.get(list.size() - 1);
	}

	private static String queryResponseServlet(boolean isTest, String urlString) throws InterruptedException, IOException  {
		String fullString = "";
		Logger.traceINFO("QUERY : " + urlString);
		if (System.currentTimeMillis() - lastQueryTimestamp < Constants.POLLING_INTERVAL_MS) {
			Thread.sleep(Constants.POLLING_INTERVAL_MS);
		}
		lastQueryTimestamp = System.currentTimeMillis();
		URL url = new URL(urlString);
		BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
		String line;
		while ((line = reader.readLine()) != null) {
			fullString += line;
		}
		reader.close();
		return fullString;
	}

	// Return: query and response.
	public static List<String> queryResponseServlet(String sessionId, boolean isTest) throws Exception {
		List<String> queryAndResponseList = new ArrayList<>();
		String query = isTest ? Constants.LOCALHOST_SERVER_RESPONSE_QUERY_URL + sessionId
				: Constants.SERVER_RESPONSE_QUERY_URL + sessionId;
		queryAndResponseList.add(query);
		queryAndResponseList.add(queryResponseServlet(isTest, query));
		return queryAndResponseList;
	}

	// Not the best but okay for the purpose.
	public static String predictNextSessionId(String currentSessionId) {
		int cur = Integer.valueOf(currentSessionId);
		String nextSessionId = String.valueOf(++cur);
		Logger.traceINFO("Predicting next session id : " + currentSessionId + " -> " + nextSessionId);
		return nextSessionId;
	}

	public static String printValueOrInfty(Double value) {
		if (value > Constants.VERY_HIGH_NUMBER) {
			return "+oo";
		}
		return String.valueOf(value);
	}

	public static String printDigit(double number) {
		return new DecimalFormat("###.####").format(number);
	}
	
	public static double inverseSpeed(final double speed) {
		return (double) 1.0 / speed;
	}
}
