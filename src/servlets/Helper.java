package servlets;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import log.Logger;

public class Helper {

	public static void notifyError(HttpServletResponse response, String msg) {
		try {
			response.getWriter().append("ERROR: " + msg);
		} catch (IOException e) {
			Logger.traceERROR(e);
		}
	}

	public static void notifyMissingFieldError(HttpServletResponse response, String missingField) {
		notifyError(response, missingField + " missing");
	}
	
	public static void notifyInvalidFieldError(HttpServletResponse response, String invalidField) {
		notifyError(response, invalidField + " invalid");
	}

}
