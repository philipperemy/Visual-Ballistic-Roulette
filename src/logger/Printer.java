package logger;

import java.io.PrintStream;

class Printer
{
	private static final PrintStream	OUT_CONSOLE	= System.out;

	static boolean						canLog		= true;

	public void println(String str)
	{
		if (canLog)
		{
			OUT_CONSOLE.println(str);
		}
	}

	public void print(String str)
	{
		if (canLog)
		{
			OUT_CONSOLE.print(str);
		}
	}
}