package timer;

import java.io.IOException;

public class Main
{
	// Possible to configure at the beginning!
	// Type type = Type.WHEEL;
	static Type type = Type.BALL;

	static VibratorInterface vibratorInterface = new VibratorInterface() {

		@Override
		public void vibrate()
		{
			System.out.println("VIBRATING THE PHONE TO GIVE FEEDBACK TO THE USER!!!!");
		}
	};

	static ButtonInterface buttonInterface = new ButtonInterface() {

		@Override
		public void onButtonPressed()
		{
			long currentMs = System.currentTimeMillis();
			String url = HttpInterface.forgeRequest(currentMs, type);
			try
			{
				HttpInterface.send(url);
			} catch (IOException e)
			{
				vibratorInterface.vibrate();
			}
		}
	};

	public static void main(String[] args) throws IOException, InterruptedException
	{
		while (true)
		{
			buttonInterface.onButtonPressed();
			Thread.sleep(10000);
		}
	}

}
