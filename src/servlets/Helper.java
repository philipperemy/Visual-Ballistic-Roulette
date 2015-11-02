package servlets;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import log.Logger;

public class Helper
{

	public static void notifyError(HttpServletResponse response, String msg)
	{
		try
		{
			response.getWriter().append("ERROR: " + msg);
		} catch (IOException e)
		{
			Logger.traceERROR(e);
		}
	}

	public static void notifyMissingFieldError(HttpServletResponse response, String missingField)
	{
		notifyError(response, missingField + " missing");
	}

	public static void notifyInvalidFieldError(HttpServletResponse response, String invalidField)
	{
		notifyError(response, invalidField + " invalid");
	}

	public static void notifyNotReadyYet(HttpServletResponse response, String numberOfRecordedWheelTimes)
	{
		try
		{
			response.getWriter().append("SESSION_NOT_READY:" + numberOfRecordedWheelTimes);
		} catch (IOException e)
		{
			Logger.traceERROR(e);
		}
	}

	public static void enableAjax(HttpServletResponse response)
	{
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
		response.addHeader("Access-Control-Allow-Credentials", "true");
		response.addHeader("Access-Control-Allow-Headers", "Content-Type, Accept");
	}

	public static String toString(HttpServletRequest request)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(request.getProtocol() + " ");
		sb.append(request.getContextPath());
		sb.append(" From HOST : " + request.getRemoteHost() + ":" + request.getRemotePort());
		return sb.toString();
	}

}
