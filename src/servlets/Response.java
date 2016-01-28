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
import computations.predictor.PredictorInterface;
import computations.predictor.ml.model.DataRecord;
import computations.predictor.physics.PositiveValueExpectedException;
import computations.session.SessionManager;
import computations.wheel.Wheel;
import database.DatabaseAccessor;
import database.DatabaseAccessorInterface;
import logger.Logger;

//http://localhost:8080/RouletteServer/Response
@WebServlet("/Response")
public class Response extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	public DatabaseAccessorInterface da;
	private SessionManager sm;
	private PredictorInterface pr;

	public Response(DatabaseAccessorInterface da)
	{
		init(da);
	}

	public Response()
	{
		init(DatabaseAccessor.getInstance());
	}

	private void init(DatabaseAccessorInterface da)
	{
		this.da = da;
		this.sm = SessionManager.getInstance();
		this.pr = PredictorInterface.getInstance();
		this.sm.init(da);
		this.pr.machineLearning().init(da);
	}

	public void forceDatasetReInit()
	{
		pr.machineLearning().init(da);
	}

	public void clearCache()
	{
		DataRecord.clearCache();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		Logger.traceINFO(Helper.toString(request));
		Helper.enableAjax(response);

		String sessionId = request.getParameter(Parameters.SESSION_ID);
		if (sessionId == null)
		{
			sessionId = da.getLastSessionId();
			Logger.traceINFO("No session specified. Selecting the last known session id = " + sessionId);
		}

		if (sessionId == null || sessionId.isEmpty())
		{
			Helper.notifyError(response, "Problem occurred: sessionId should not be empty.");
			return;
		}

		// Insert outcome workflow.
		String outcome = request.getParameter(Parameters.OUTCOME);
		if (outcome != null && !outcome.isEmpty())
		{
			da.insertOutcome(sessionId, outcome);
			Logger.traceINFO("New outcome inserted. Session id = " + sessionId + ", outcome = " + outcome);
			return;
		}

		// Predict outcome workflow.
		try
		{
			List<Integer> region = predictMostProbableNumber_OnlyOne(sessionId);
			response.getWriter().append(region.toString());
		} catch (SessionNotReadyException snre)
		{
			// Good exception
			Helper.notifyNotReadyYet(response, "Session not ready.");

		} catch (PositiveValueExpectedException e)
		{
			// Good exception
			Helper.notifyNotReadyYet(response, "Arithmetic exception has occurred. Skip the game.");
		} catch (CriticalException e)
		{
			// Bad exception
			Logger.traceERROR(e);
			// -1 means EXCEPTION! So stop everything for every clients.
			Helper.notifyNotReadyYet(response, Constants.ERRORLEVEL_PROCESS_EXCEPTION_TAG);
		}
	}

	public List<Integer> predictMostProbableRegion(String sessionId) throws SessionNotReadyException, PositiveValueExpectedException
	{
		int mostProbableNumber = predictMostProbableNumber(sessionId);
		int[] nearbyNumbers = Wheel.getNearbyNumbers(mostProbableNumber, Constants.REGION_HALF_SIZE);
		List<Integer> regionNumbersList = new ArrayList<>();
		for (int number : nearbyNumbers)
		{
			regionNumbersList.add(number);
		}
		Collections.sort(regionNumbersList); // Sort it. The table is sorted.
		// http://www.casinogamespro.com/media/other/european_roulette_table_layout.png
		return regionNumbersList;
	}

	private List<Integer> predictMostProbableNumber_OnlyOne(String sessionId) throws SessionNotReadyException, PositiveValueExpectedException
	{
		int mostProbableNumber = predictMostProbableNumber(sessionId);
		List<Integer> list = new ArrayList<>();
		list.add(mostProbableNumber);
		return list;
	}

	public int predictMostProbableNumber(String sessionId) throws SessionNotReadyException, PositiveValueExpectedException
	{
		List<Double> wheelCumsumTimes = da.selectWheelLapTimes(sessionId);
		List<Double> ballCumsumTimes = da.selectBallLapTimes(sessionId);

		int numberOfRecordedWheelTimes = wheelCumsumTimes.size(); // At least 2
		if (numberOfRecordedWheelTimes < Constants.MIN_NUMBER_OF_WHEEL_TIMES_BEFORE_PREDICTION
				|| ballCumsumTimes.size() < Constants.MIN_NUMBER_OF_BALL_TIMES_BEFORE_PREDICTION)
		{
			throw new SessionNotReadyException();
		}

		List<Double> wheelCumsumTimesSeconds = computations.Helper.convertToSeconds(wheelCumsumTimes);
		List<Double> ballCumsumTimesSeconds = computations.Helper.convertToSeconds(ballCumsumTimes);

		// int mostProbableNumberML =
		// pr.machineLearning().predict(ballLapTimesSeconds,
		// wheelLapTimesSeconds, sessionId);
		int mostProbableNumberPH = pr.physics().predict(ballCumsumTimesSeconds, wheelCumsumTimesSeconds);
		return mostProbableNumberPH;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}

}
