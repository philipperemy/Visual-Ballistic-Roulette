package responsetime;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import framework.TestClass;

public class HumanResponseTime
{
	public Map<String, String> ballResponseTimes = new HashMap<>();

	@Test
	public void philippe()
	{
		ballResponseTimes.put(TestClass.getTime(0, 1, 47, 071), TestClass.getTime(0, 1, 47, 015));
		ballResponseTimes.put(TestClass.getTime(0, 1, 48, 276), TestClass.getTime(0, 1, 48, 268));
		ballResponseTimes.put(TestClass.getTime(0, 1, 49, 737), TestClass.getTime(0, 1, 49, 675));
		ballResponseTimes.put(TestClass.getTime(0, 1, 51, 404), TestClass.getTime(0, 1, 51, 441));
		ballResponseTimes.put(TestClass.getTime(0, 1, 53, 220), TestClass.getTime(0, 1, 53, 318));
		ballResponseTimes.put(TestClass.getTime(0, 1, 55, 308), TestClass.getTime(0, 1, 55, 310));

		ballResponseTimes.put(TestClass.getTime(0, 1, 23, 601), TestClass.getTime(0, 1, 23, 591));
		ballResponseTimes.put(TestClass.getTime(0, 1, 24, 992), TestClass.getTime(0, 1, 25, 109));
		ballResponseTimes.put(TestClass.getTime(0, 1, 26, 563), TestClass.getTime(0, 1, 26, 579));
		ballResponseTimes.put(TestClass.getTime(0, 1, 28, 394), TestClass.getTime(0, 1, 28, 472));
		ballResponseTimes.put(TestClass.getTime(0, 1, 30, 369), TestClass.getTime(0, 1, 30, 410));
		ballResponseTimes.put(TestClass.getTime(0, 1, 32, 731), TestClass.getTime(0, 1, 32, 757));

		ballResponseTimes.put(TestClass.getTime(0, 2, 7, 462), TestClass.getTime(0, 2, 7, 556));
		ballResponseTimes.put(TestClass.getTime(0, 2, 8, 807), TestClass.getTime(0, 2, 8, 803));
		ballResponseTimes.put(TestClass.getTime(0, 2, 10, 303), TestClass.getTime(0, 2, 10, 326));
		ballResponseTimes.put(TestClass.getTime(0, 2, 11, 977), TestClass.getTime(0, 2, 11, 985));
		ballResponseTimes.put(TestClass.getTime(0, 2, 13, 774), TestClass.getTime(0, 2, 13, 788));
		ballResponseTimes.put(TestClass.getTime(0, 2, 15, 700), TestClass.getTime(0, 2, 15, 763));
		ballResponseTimes.put(TestClass.getTime(0, 2, 17, 817), TestClass.getTime(0, 2, 17, 868));
		ballResponseTimes.put(TestClass.getTime(0, 2, 20, 204), TestClass.getTime(0, 2, 20, 153));

		ballResponseTimes.put(TestClass.getTime(0, 2, 26, 020), TestClass.getTime(0, 2, 26, 38));
		ballResponseTimes.put(TestClass.getTime(0, 2, 27, 508), TestClass.getTime(0, 2, 27, 547));
		ballResponseTimes.put(TestClass.getTime(0, 2, 29, 235), TestClass.getTime(0, 2, 29, 215));
		ballResponseTimes.put(TestClass.getTime(0, 2, 30, 974), TestClass.getTime(0, 2, 31, 12));
		ballResponseTimes.put(TestClass.getTime(0, 2, 32, 885), TestClass.getTime(0, 2, 32, 787));
		ballResponseTimes.put(TestClass.getTime(0, 2, 34, 957), TestClass.getTime(0, 2, 35, 13));
		ballResponseTimes.put(TestClass.getTime(0, 2, 37, 278), TestClass.getTime(0, 2, 37, 318));

		ballResponseTimes.put(TestClass.getTime(0, 7, 54, 525), TestClass.getTime(0, 7, 54, 591));
		ballResponseTimes.put(TestClass.getTime(0, 7, 55, 138), TestClass.getTime(0, 7, 55, 194));
		ballResponseTimes.put(TestClass.getTime(0, 7, 55, 883), TestClass.getTime(0, 7, 55, 830));
		ballResponseTimes.put(TestClass.getTime(0, 7, 56, 603), TestClass.getTime(0, 7, 56, 651));
		ballResponseTimes.put(TestClass.getTime(0, 7, 57, 374), TestClass.getTime(0, 7, 57, 424));
		ballResponseTimes.put(TestClass.getTime(0, 7, 58, 174), TestClass.getTime(0, 7, 58, 202));
		ballResponseTimes.put(TestClass.getTime(0, 7, 59, 002), TestClass.getTime(0, 7, 59, 022));
		ballResponseTimes.put(TestClass.getTime(0, 7, 59, 949), TestClass.getTime(0, 8, 0, 45));
		ballResponseTimes.put(TestClass.getTime(0, 8, 0, 923), TestClass.getTime(0, 8, 0, 905));
		ballResponseTimes.put(TestClass.getTime(0, 8, 2, 246), TestClass.getTime(0, 8, 2, 33));
		ballResponseTimes.put(TestClass.getTime(0, 8, 3, 540), TestClass.getTime(0, 8, 3, 540));
		ballResponseTimes.put(TestClass.getTime(0, 8, 5, 031), TestClass.getTime(0, 8, 5, 041));
		ballResponseTimes.put(TestClass.getTime(0, 8, 6, 805), TestClass.getTime(0, 8, 6, 806));
		ballResponseTimes.put(TestClass.getTime(0, 8, 8, 660), TestClass.getTime(0, 8, 8, 693));
		ballResponseTimes.put(TestClass.getTime(0, 8, 10, 716), TestClass.getTime(0, 8, 10, 760));

		analyze();
	}

	public void analyze()
	{
		for (Entry<String, String> entry : ballResponseTimes.entrySet())
		{
			System.out.print(Long.parseLong(entry.getValue()) - Long.parseLong(entry.getKey()) + ", ");
		}
	}

}
