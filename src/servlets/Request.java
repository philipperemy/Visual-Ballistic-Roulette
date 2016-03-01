package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.DatabaseAccessor;
import database.DatabaseAccessorInterface;
import utils.logger.Logger;

//http://localhost:8080/RouletteServer/Request?ts=121212&type=wheel
@WebServlet("/Request")
public class Request extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	public DatabaseAccessorInterface da;
	private SessionManager sm;

	public Request(DatabaseAccessorInterface dai)
	{
		init(dai);
	}

	public Request()
	{
		init(DatabaseAccessor.getInstance());
	}

	private void init(DatabaseAccessorInterface da)
	{
		this.da = da;
		this.sm = SessionManager.getInstance();
		this.sm.init(da);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		Logger.traceINFO(Helper.toString(request));

		String time = request.getParameter(Parameters.TIME);
		if (time == null)
		{
			Helper.notifyMissingFieldError(response, Parameters.TIME);
			return;
		}

		String type = request.getParameter(Parameters.TYPE);
		if (type == null)
		{
			Helper.notifyMissingFieldError(response, Parameters.TYPE);
			return;
		}

		String sessionId = sm.callManager(System.currentTimeMillis());
		switch (type)
		{
		case Parameters.TYPE_BALL:
			da.insertBallLapTimes(sessionId, time);
			break;

		case Parameters.TYPE_WHEEL:
			da.insertWheelLapTimes(sessionId, time);
			break;

		default:
			Helper.notifyInvalidFieldError(response, Parameters.TYPE);
			return;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}
}
