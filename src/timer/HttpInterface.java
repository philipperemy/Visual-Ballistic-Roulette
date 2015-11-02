package timer;

import java.io.IOException;
import java.net.URL;

/**
 * When database and server is up. Sending query :
 * http://localhost:8080/RouletteServer/Request?time=1446170517356&type=ball
 * Sending query :
 * http://localhost:8080/RouletteServer/Request?time=1446170527679&type=ball
 * 
 * When database and server is down. Unable to send query :
 * http://localhost:8080/RouletteServer/Request?time=1446170787256&type=ball
 * VIBRATING THE PHONE TO GIVE FEEDBACK TO THE USER!!!!
 */

public class HttpInterface
{
	public static String forgeRequest(long timestamp, Type type)
	{
		return "http://localhost:8080/RouletteServer/Request?time=" + String.valueOf(timestamp) + "&type=" + type.toString().toLowerCase();
	}

	public static void send(String urlString) throws IOException
	{
		try
		{
			new URL(urlString).openStream();
		} catch (IOException ioe)
		{
			System.out.println("Unable to send query : " + urlString);
			throw ioe;
		}
		System.out.println("Sending query : " + urlString);
	}

}
