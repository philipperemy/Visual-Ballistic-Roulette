package servlets;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.logger.Logger;

class Helper
{
	static void notifyMissingFieldError(HttpServletResponse response, String missingField)
	{
		notifyError(response, missingField + " missing");
	}

	static void notifyInvalidFieldError(HttpServletResponse response, String invalidField)
	{
		notifyError(response, invalidField + " invalid");
	}

	static void notifyError(HttpServletResponse response, String msg)
	{
		notifyClient(response, "ERROR: " + msg);
	}

	static void notifyClient(HttpServletResponse response, String msg)
	{
		try
		{
			response.getWriter().append(msg);
		} catch (IOException e)
		{
			Logger.traceERROR(e);
		}
	}

	static void enableAjax(HttpServletResponse response)
	{
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
		response.addHeader("Access-Control-Allow-Credentials", "true");
		response.addHeader("Access-Control-Allow-Headers", "Content-Type, Accept");
	}

	static String toString(HttpServletRequest request)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(request.getProtocol() + " ");
		sb.append(request.getContextPath());
		sb.append(" From HOST : " + request.getRemoteHost() + ":" + request.getRemotePort());
		return sb.toString();
	}
}
