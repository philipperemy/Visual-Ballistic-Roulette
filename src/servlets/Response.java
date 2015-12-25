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
import computations.model.DataRecord;
import computations.predictor.Predictor;
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
	private Predictor pr;

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
		this.pr = Predictor.getInstance();
		this.sm.init(da);
		this.pr.init(da);
	}

	public void forceDatasetReInit()
	{
		pr.init(da);
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
			Helper.notifyMissingFieldError(response, Parameters.SESSION_ID);
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
			Helper.notifyNotReadyYet(response, snre.getNumberOfRecordedTimesOfWheel());
		} catch (Exception e)
		{
			// Bad exception
			Logger.traceERROR(e);
			// -1 means EXCEPTION! So stop everything for every clients.
			Helper.notifyNotReadyYet(response, Constants.ERRORLEVEL_PROCESS_EXCEPTION_TAG);
		}
	}

	public List<Integer> predictMostProbableRegion(String sessionId) throws SessionNotReadyException
	{
		int mostProbableNumber = predictMostProbableNumber(sessionId);
		int[] numbers = Wheel.getNearbyNumbers(mostProbableNumber, Constants.REGION_HALF_SIZE);
		List<Integer> regionNumbersList = new ArrayList<>();
		for (int number : numbers)
		{
			regionNumbersList.add(number);
		}
		Collections.sort(regionNumbersList); // Sort it. The table is sorted.
		// http://www.casinogamespro.com/media/other/european_roulette_table_layout.png
		return regionNumbersList;
	}

	public List<Integer> predictMostProbableNumber_OnlyOne(String sessionId) throws SessionNotReadyException
	{
		int mostProbableNumber = predictMostProbableNumber(sessionId);
		List<Integer> list = new ArrayList<>();
		list.add(mostProbableNumber);
		return list;
	}

	public int predictMostProbableNumber(String sessionId) throws SessionNotReadyException
	{
		List<Double> wheelLapTimes = da.selectWheelLapTimes(sessionId);
		List<Double> ballLapTimes = da.selectBallLapTimes(sessionId);

		if (wheelLapTimes.isEmpty() || ballLapTimes.isEmpty())
		{
			throw new SessionNotReadyException(0);
		}

		int numberOfRecordedWheelTimes = wheelLapTimes.size(); // At least 2
		if (numberOfRecordedWheelTimes < Constants.MINIMUM_NUMBER_OF_WHEEL_TIMES_BEFORE_FORECASTING)
		{
			throw new SessionNotReadyException(numberOfRecordedWheelTimes);
		}

		List<Double> wheelLapTimesSeconds = computations.Helper.convertToSeconds(wheelLapTimes);
		List<Double> ballLapTimesSeconds = computations.Helper.convertToSeconds(ballLapTimes);

		int mostProbableNumber = pr.predict(ballLapTimesSeconds, wheelLapTimesSeconds, sessionId);
		return mostProbableNumber;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}

}
