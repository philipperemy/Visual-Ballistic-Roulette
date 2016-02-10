package servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import computations.Constants;
import computations.predictor.PredictorInterface;
import computations.predictor.ml.model.DataRecord;
import database.DatabaseAccessor;
import database.DatabaseAccessorInterface;
import utils.exception.PositiveValueExpectedException;
import utils.exception.SessionNotReadyException;
import utils.logger.Logger;

//http://localhost:8080/RouletteServer/Response
@WebServlet("/Response")
public class Response extends HttpServlet
{
	private static final long			serialVersionUID	= 1L;

	public DatabaseAccessorInterface	da;
	private SessionManager				sm;
	private PredictorInterface			pr;

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
			Helper.notifyError(response, "Problem occurred. SessionId should not be empty.");
			return;
		}

		// Insert outcome workflow.
		String outcome = request.getParameter(Parameters.OUTCOME);
		if (outcome != null && !outcome.isEmpty())
		{
			da.insertOutcome(sessionId, outcome);
			Logger.traceINFO("New outcome inserted. Session id = " + sessionId + ", outcome = " + outcome);
			return; // Workflow finished.
		}

		// Predict outcome workflow.
		try
		{
			int predictedNumber = predictMostProbableNumber(sessionId);
			response.getWriter().append(String.valueOf(predictedNumber));
			Logger.traceINFO("Prediction=" + predictedNumber + ", sessionId=" + sessionId);
		} catch (SessionNotReadyException e)
		{
			Helper.notifyClient(response, "Session not ready.");
		} catch (PositiveValueExpectedException e)
		{
			Helper.notifyClient(response, "Arithmetic exception has occurred. Skip the game.");
		} catch (Exception e)
		{
			Logger.traceERROR(e);
			Helper.notifyError(response, "Critical error.");
		}
	}

	public int predictMostProbableNumber(String sessionId) throws SessionNotReadyException, PositiveValueExpectedException
	{
		List<Double> wheelCumsumTimes = da.selectWheelLapTimes(sessionId);
		List<Double> ballCumsumTimes = da.selectBallLapTimes(sessionId);

		int numberOfRecordedWheelTimes = wheelCumsumTimes.size();
		if (numberOfRecordedWheelTimes < Constants.MIN_NUMBER_OF_WHEEL_TIMES_BEFORE_PREDICTION
				|| ballCumsumTimes.size() < Constants.MIN_NUMBER_OF_BALL_TIMES_BEFORE_PREDICTION)
		{
			throw new SessionNotReadyException();
		}

		List<Double> wheelCumsumTimesSeconds = computations.utils.Helper.convertToSeconds(wheelCumsumTimes);
		List<Double> ballCumsumTimesSeconds = computations.utils.Helper.convertToSeconds(ballCumsumTimes);

		// use a predictor big interface here.
		int mostProbableNumberML = pr.machineLearning().predict(ballCumsumTimesSeconds, wheelCumsumTimesSeconds);
		return mostProbableNumberML;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}
}
