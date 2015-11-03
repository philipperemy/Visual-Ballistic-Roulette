package log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger
{
	private static final String INFO_KEY = "INFO";
	private static final String ERROR_KEY = "ERROR";
	private static final String DEBUG_KEY = "DEBUG";

	private static boolean isDebug = false;

	private static final Printer out = new Printer();

	private static boolean isDebugEnabled()
	{
		return isDebug;
	}

	public static void setDebug(boolean debug)
	{
		isDebug = debug;
	}

	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss_SSS");

	private static String getCallingClass()
	{
		final Throwable t = new Throwable();
		final StackTraceElement methodCaller = t.getStackTrace()[3];
		String filename = methodCaller.getFileName();
		return filename.substring(0, filename.lastIndexOf('.'));
	}

	private static String baseLogMessage(String key, String msg)
	{
		long threadId = Thread.currentThread().getId();
		StringBuilder sb = new StringBuilder();
		sb.append(sdf.format(new Date()));
		sb.append(" ");
		sb.append("[" + threadId + "]");
		sb.append(" ");
		sb.append("[" + key + "]");
		sb.append(" ");
		sb.append("[" + getCallingClass() + "]");
		sb.append(" ");
		sb.append(msg);
		return sb.toString();
	}

	public static void traceINFO(Object obj)
	{
		out.println(baseLogMessage(INFO_KEY, obj.toString()));
	}

	public static void traceINFO(String msg)
	{
		out.println(baseLogMessage(INFO_KEY, msg));
	}

	public static void traceDEBUG(String msg)
	{
		if (isDebugEnabled())
		{
			out.println(baseLogMessage(DEBUG_KEY, msg));
		}
	}

	public static void traceINFO_NoNewLine(String msg)
	{
		out.print(baseLogMessage(INFO_KEY, msg));
	}

	public static void traceINFO_NoBaseLine(String msg)
	{
		out.println(msg);
	}

	public static void traceERROR(String msg)
	{
		out.println(baseLogMessage(ERROR_KEY, msg));
	}

	public static void traceINFO(String... msgs)
	{
		StringBuilder sBuilder = new StringBuilder();
		for (int i = 0; i < msgs.length; i++)
		{
			if (i == msgs.length - 1)
			{

				sBuilder.append(msgs[i]);
			} else
			{
				sBuilder.append(msgs[i] + ", ");
			}
		}
		out.println(baseLogMessage(INFO_KEY, sBuilder.toString()));
	}

	public static void traceERROR(Exception e)
	{
		if (e != null)
		{
			out.println(baseLogMessage(ERROR_KEY, e.getMessage()));
			e.printStackTrace(System.out);
		}
	}

}