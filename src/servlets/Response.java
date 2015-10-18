package servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import computations.predictor.Predictor;
import computations.session.SessionManager;
import computations.wheel.Wheel;
import computations.wheel.Wheel.WheelWay;
import database.DatabaseAccessor;

@WebServlet("/Response")
public class Response extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private DatabaseAccessor da;
	private SessionManager sm;
	private Predictor pr;

	public Response() {
		da = DatabaseAccessor.getInstance();
		sm = SessionManager.getInstance();
		pr = Predictor.getInstance();
		sm.init(da);
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
			int mostProbableNumber = predictMostProbableNumber(sessionId);
			response.getWriter().append(String.valueOf(mostProbableNumber));
		} catch (Exception e) {
			Helper.notifyInvalidFieldError(response, Parameters.SESSION_ID);
		}
	}

	public int predictMostProbableNumber(String sessionId) {
		List<Double> wheelLapTimes = da.selectWheelLapTimes(sessionId);
		List<Double> ballLapTimes = da.selectBallLapTimes(sessionId);

		if (wheelLapTimes.isEmpty() || ballLapTimes.isEmpty()) {
			throw new RuntimeException("Invalid sessionId");
		}

		List<Double> wheelLapTimesSeconds = Helper.convertToSeconds(wheelLapTimes);
		List<Double> ballLapTimesSeconds = Helper.convertToSeconds(ballLapTimes);

		WheelWay wheelWay = Wheel.convert(da.selectClockwise(sessionId));
		int mostProbableNumber = pr.predict(ballLapTimesSeconds, wheelLapTimesSeconds, wheelWay);
		return mostProbableNumber;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
