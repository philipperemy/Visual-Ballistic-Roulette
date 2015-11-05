package framework.games;

import java.util.List;

import framework.TestClass;

public class Game2 extends Game
{
	@Override
	public List<String> get_ballLaptimes()
	{
		return TestClass.create( //
				TestClass.getTime(0, 1, 23, 601), //
				TestClass.getTime(0, 1, 24, 992), //
				TestClass.getTime(0, 1, 26, 563), //
				TestClass.getTime(0, 1, 28, 394), //
				TestClass.getTime(0, 1, 30, 369), //
				TestClass.getTime(0, 1, 32, 731) //
		); //
	}

	@Override
	public List<String> get_wheelLaptimes()
	{
		return TestClass.create( //
				TestClass.getTime(0, 1, 23, 416), //
				TestClass.getTime(0, 1, 29, 723), //
				TestClass.getTime(0, 1, 36, 510)); //
	}

	@Override
	public Integer get_outcome()
	{
		return 4;
	}

}
