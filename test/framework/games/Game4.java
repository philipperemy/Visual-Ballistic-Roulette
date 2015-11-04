package framework.games;

import java.util.List;

import framework.TestClass;

public class Game4 extends Game
{
	@Override
	public List<String> get_ballLaptimes()
	{
		return TestClass.create( //
				TestClass.getTime(0, 2, 26, 020), //
				TestClass.getTime(0, 2, 27, 508), //
				TestClass.getTime(0, 2, 29, 235), //
				TestClass.getTime(0, 2, 30, 974), //
				TestClass.getTime(0, 2, 32, 885), //
				TestClass.getTime(0, 2, 34, 957), //
				TestClass.getTime(0, 2, 37, 278) //
		); //
	}

	@Override
	public List<String> get_wheelLaptimes()
	{
		return TestClass.create( //
				TestClass.getTime(0, 2, 25, 944), //
				TestClass.getTime(0, 2, 34, 386), //
				TestClass.getTime(0, 2, 43, 995)); //
	}

	@Override
	public Integer get_outcome()
	{
		return 35;
	}

}
