package framework.games;

import java.util.List;

import framework.TestClass;

public class Game5 extends Game
{
	@Override
	public List<String> get_ballLaptimes()
	{
		return TestClass.create( //
				TestClass.getTime(0, 7, 54, 525), //
				TestClass.getTime(0, 7, 55, 138), //
				TestClass.getTime(0, 7, 55, 883), //
				TestClass.getTime(0, 7, 56, 603), //
				TestClass.getTime(0, 7, 57, 374), //
				TestClass.getTime(0, 7, 58, 174), //
				TestClass.getTime(0, 7, 59, 002), //
				TestClass.getTime(0, 7, 59, 949), //
				TestClass.getTime(0, 8, 0, 923), //
				TestClass.getTime(0, 8, 2, 246), //
				TestClass.getTime(0, 8, 3, 508), //
				TestClass.getTime(0, 8, 5, 031), //
				TestClass.getTime(0, 8, 6, 769), //
				TestClass.getTime(0, 8, 8, 660), //
				TestClass.getTime(0, 8, 10, 716) //
		); //
	}

	@Override
	public List<String> get_wheelLaptimes()
	{
		return TestClass.create( //
				TestClass.getTime(0, 7, 54, 814), //
				TestClass.getTime(0, 8, 0, 853), //
				TestClass.getTime(0, 8, 7, 171)); //
	}

	@Override
	public Integer get_outcome()
	{
		return 34;
	}

}
