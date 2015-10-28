package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import computations.session.SessionManager;
import database.DatabaseAccessor;
import database.DatabaseAccessorInterface;

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

	// http://localhost:8080/RouletteServer/Request?time=121212&type=wheel
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

		long currentTimeMillis = System.currentTimeMillis();
		String sessionId = sm.callManager(currentTimeMillis);

		// Basically a clockwise is associated to a session. If we don't receive
		// it for other, deduce it.
		String clockwise = request.getParameter(Parameters.CLOCK_WISE);
		if (clockwise != null)
		{
			processClockwise(sessionId, clockwise);
		}

		String time = request.getParameter(Parameters.TIME);
		String type = request.getParameter(Parameters.TYPE);

		if (time == null)
		{
			Helper.notifyMissingFieldError(response, Parameters.TIME);
			return;
		}

		if (type == null)
		{
			Helper.notifyMissingFieldError(response, Parameters.TYPE);
			return;
		}

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

	private void processClockwise(String sessionId, String clockwise)
	{
		da.insertClockwise(sessionId, clockwise);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}

}
