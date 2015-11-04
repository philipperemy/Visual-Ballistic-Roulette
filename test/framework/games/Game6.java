package framework.games;

import java.util.List;

import framework.TestClass;

public class Game6 extends Game
{
	@Override
	public List<String> get_ballLaptimes()
	{
		return TestClass.create( //
				TestClass.getTime(0, 18, 32, 015), //
				TestClass.getTime(0, 18, 32, 764), //
				TestClass.getTime(0, 18, 33, 317), //
				TestClass.getTime(0, 18, 33, 957), //
				TestClass.getTime(0, 18, 34, 732), //
				TestClass.getTime(0, 18, 35, 483), //
				TestClass.getTime(0, 18, 36, 232), //
				TestClass.getTime(0, 18, 37, 178), //
				TestClass.getTime(0, 18, 38, 054), //
				TestClass.getTime(0, 18, 39, 041), //
				TestClass.getTime(0, 18, 40, 152), //
				TestClass.getTime(0, 18, 41, 560), //
				TestClass.getTime(0, 18, 43, 207), //
				TestClass.getTime(0, 18, 44, 935) //
		); //
	}

	@Override
	public List<String> get_wheelLaptimes()
	{
		return TestClass.create( //
				TestClass.getTime(0, 18, 31, 590), //
				TestClass.getTime(0, 18, 37, 270), //
				TestClass.getTime(0, 18, 43, 557) //
		); //
	}

	@Override
	public Integer get_outcome()
	{
		return 32;
	}

}
