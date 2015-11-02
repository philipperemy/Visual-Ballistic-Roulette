package earpad;

import java.util.List;

import computations.Constants;
import computations.Helper;
import log.Logger;

//For one game only.
class GameTask implements Runnable
{
	private String sessionId;
	private boolean isTest = Constants.IS_TEST_ENABLED;
	private CallBack callback;
	private long count = 0;

	void init(String sessionId, CallBack callback)
	{
		this.sessionId = sessionId;
		this.callback = callback;
	}

	@Override
	public void run()
	{
		try
		{
			Logger.traceINFO("Thread is started...");
			String lastResponse = "";

			// Avoiding too many calls to the callback. Make the different
			// between technical calls to refresh and business ones.
			while (true)
			{
				List<String> responses = Helper.queryResponseServlet(sessionId, isTest);
				// Response should never be null. So throw an exception in this
				// case.predictNextSessionId.
				String query = responses.get(0);
				String response = responses.get(1);

				if (response.equals(Constants.ERRORLEVEL_PROCESS_EXCEPTION_TAG))
				{
					Exception e = new Exception("Received exception from the server. Will stop.");
					callback.onError(e);
					return;
				}

				if (!lastResponse.equals(response))
				{
					Logger.traceINFO("Query: " + query + ", Response: " + response);
					if (callback.onResponse(response))
					{
						return;
					}
					lastResponse = response;
				}

				if (++count % 1000 == 0)
				{
					Logger.traceINFO("Polling session every [" + Constants.POLLING_INTERVAL_MS + "] : " + sessionId);
				}
				Thread.sleep(Constants.POLLING_INTERVAL_MS);
			}
		} catch (Exception e)
		{
			Logger.traceERROR(e);
			callback.onError(e);
		}
	}

}
