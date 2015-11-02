package earpad;

import computations.Constants;
import computations.Helper;
import database.DatabaseAccessor;
import database.DatabaseAccessorInterface;
import log.Logger;

public class Main
{

	/**
	 * to be updated if needed.
	 */
	
	private DatabaseAccessorInterface da = DatabaseAccessor.getInstance();
	private String firstSessionId = da.getLastSessionId();

	@Deprecated
	public Main()
	{
		da.getLastSessionId();
	}

	@Deprecated
	public void run() throws InterruptedException
	{
		/**
		 * TODO: change it. Absolutely.
		 */
		Main main = new Main();
		main.firstSessionId = Constants.WHEEL_CLOCKWISE;
		Logger.traceINFO("Starting the program with session id = " + main.firstSessionId);

		CallBack callback = new CallBack() {

			@Override
			public boolean onResponse(String response)
			{
				Logger.traceINFO("[Callback] Response is : " + response);
				switch (response)
				{
				case Constants.ERRORLEVEL_PROCESS_EXCEPTION_TAG:
					return false;
				case "0":
				case "1":
				case "2":
					Player.playSound_NumberOfRecordedWheelTimes(response);
					return false;
				default: // Response of the numbers where to bet.
					Player.playSound_Numbers(response);
					return true;
				}
			}

			@Override
			public void onError(Exception e)
			{
				Logger.traceERROR(e);
			}
		};

		String currentSessionId = main.firstSessionId;
		while (true)
		{
			GameTask pollerTask = new GameTask();
			pollerTask.init(currentSessionId, callback);
			Thread thread = new Thread(pollerTask);
			thread.start();
			Logger.traceINFO("Waiting for thread to finish...");
			thread.join();
			// If we arrive here, it means all went fine or exception raised.
			currentSessionId = Helper.predictNextSessionId(currentSessionId);
		}

	}

	public static void main(String[] args) throws Exception
	{
		Main main = new Main();
		main.run();
		// Logger.traceINFO("Should never see this message except in the case
		// where an exception is thrown...");
	}

}
