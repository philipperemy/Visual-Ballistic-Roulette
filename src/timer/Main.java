package timer;

import java.io.IOException;
import java.util.Scanner;

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
		run();
	}

	private static void run()
	{
		Scanner scanner = new Scanner(System.in);
		String readString = scanner.nextLine();
		while (readString != null)
		{
			System.out.println(readString);

			if (readString.isEmpty())
			{
				System.out.println("Read Enter Key.");
				buttonInterface.onButtonPressed();
			}

			if (scanner.hasNextLine())
			{
				readString = scanner.nextLine();
			} else
			{
				readString = null;
			}
		}
		scanner.close();
	}

}
