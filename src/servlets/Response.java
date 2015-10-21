package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import computations.Constants;
import computations.predictor.Predictor;
import computations.session.SessionManager;
import computations.wheel.Wheel;
import computations.wheel.Wheel.WheelWay;
import database.DatabaseAccessor;
import database.DatabaseAccessorInterface;
import log.Logger;

//http://localhost:8080/RouletteServer/Response
@WebServlet("/Response")
public class Response extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public DatabaseAccessorInterface da;
	private SessionManager sm;
	private Predictor pr;

	public Response(DatabaseAccessorInterface dai) {
		init(dai);
	}

	public Response() {
		init(DatabaseAccessor.getInstance());
	}

	private void init(DatabaseAccessorInterface da) {
		this.da = da;
		this.sm = SessionManager.getInstance();
		this.pr = Predictor.getInstance();
		this.sm.init(da);
		this.pr.init(da); // TODO: change it.
	}

	public void forceDatasetReInit() {
		pr.init(da);
	}

	/**
	 * To be implemented Basically, 4 measures of wheel loop.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String sessionId = request.getParameter(Parameters.SESSION_ID);
		if (sessionId == null) {
			Helper.notifyMissingFieldError(response, Parameters.SESSION_ID);
			return;
		}

		try {
			List<Integer> region = predictMostProbableRegion(sessionId);
			response.getWriter().append(region.toString());
		} catch (SessionNotReadyException snre) { // Good exception
			Helper.notifyNotReadyYet(response, snre.getNumberOfRecordedTimesOfWheel());
		} catch (Exception e) { // Bad exception
			Logger.traceERROR(e);
			Helper.notifyNotReadyYet(response, "E"); // E means EXCEPTION! So
														// stop everything for
														// every client.
		}
	}

	public List<Integer> predictMostProbableRegion(String sessionId) throws SessionNotReadyException {
		int mostProbableNumber = predictMostProbableNumber(sessionId);
		int[] numbers = Wheel.getNearbyNumbers(mostProbableNumber, Constants.REGION_HALF_SIZE);
		List<Integer> regionNumbersList = new ArrayList<>();
		for (int number : numbers) {
			regionNumbersList.add(number);
		}
		Collections.sort(regionNumbersList); // Sort it. The table is sorted.
		// http://www.casinogamespro.com/media/other/european_roulette_table_layout.png
		return regionNumbersList;
	}

	public int predictMostProbableNumber(String sessionId) throws SessionNotReadyException {
		List<Double> wheelLapTimes = da.selectWheelLapTimes(sessionId);
		List<Double> ballLapTimes = da.selectBallLapTimes(sessionId);

		if (wheelLapTimes.isEmpty() || ballLapTimes.isEmpty()) {
			throw new SessionNotReadyException(0);
		}

		int numberOfRecordedWheelTimes = wheelLapTimes.size();
		if (wheelLapTimes.size() < Constants.MINIMUM_NUMBER_OF_WHEEL_TIMES_BEFORE_FORECASTING) {
			throw new SessionNotReadyException(numberOfRecordedWheelTimes);
		}

		List<Double> wheelLapTimesSeconds = computations.Helper.convertToSeconds(wheelLapTimes);
		List<Double> ballLapTimesSeconds = computations.Helper.convertToSeconds(ballLapTimes);

		WheelWay wheelWay = Wheel.convert(da.selectClockwise(sessionId));
		int mostProbableNumber = pr.predict(ballLapTimesSeconds, wheelLapTimesSeconds, wheelWay);
		return mostProbableNumber;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
