package earpad;

import java.util.ArrayList;
import java.util.List;

import computations.Helper;
import log.Logger;

public class Player
{

	private static final String SOUND_NAME_0_RECORDS_FILENAME = "record_0.wav";
	private static final String SOUND_NAME_1_RECORDS_FILENAME = "record_1.wav";
	private static final String SOUND_NAME_2_RECORDS_FILENAME = "record_2.wav";
	private static final String REPEAT_FILENAME = "repeat.wav";
	private static final String TERMINATED_FILENAME = "terminated.wav";

	public static void playSound_NumberOfRecordedWheelTimes(String ref)
	{
		switch (ref)
		{
		case "0":
			audioPlay(SOUND_NAME_0_RECORDS_FILENAME);
			break;
		case "1": // Happens where there is at least ONE record of the wheel and
					// at least ONE record of the ball.
			audioPlay(SOUND_NAME_1_RECORDS_FILENAME);
			break;
		case "2": // Happens where there is at least TWO records of the wheel
					// and at least ONE record of the ball.
			audioPlay(SOUND_NAME_2_RECORDS_FILENAME);
			break;
		default:
			throw new RuntimeException("Invalid NumberOfRecordedWheelTimes : " + ref);
		}
	}

	public static void playSound_Numbers(String response)
	{
		List<Integer> numbers = Helper.unserialize(response);
		List<String> filenames = new ArrayList<>();
		for (Integer num : numbers)
		{
			String filename = getFilename(num);
			audioPlay(filename);
			filenames.add(filename);
		}
		audioPlay(REPEAT_FILENAME); // Say it is going to repeat.

		for (String filename : filenames)
		{
			audioPlay(filename);
		}
		audioPlay(TERMINATED_FILENAME); // Say it is going to repeat.
	}

	private static String getFilename(Integer num)
	{
		String numStr = String.valueOf(num);
		return "number_" + numStr + ".wav";
	}

	private static void audioPlay(String soundNameRecordsFilename)
	{
		// TODO: implement it. Dependent on the software.
		Logger.traceERROR("Play : " + soundNameRecordsFilename);
	}

}
