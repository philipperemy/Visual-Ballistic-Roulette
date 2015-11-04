package framework.games;

import java.util.List;

import framework.TestClass;

public class Game1 extends Game
{
	@Override
	public List<String> get_ballLaptimes()
	{
		return TestClass.create( //
				TestClass.getTime(0, 1, 47, 071), //
				TestClass.getTime(0, 1, 48, 276), //
				TestClass.getTime(0, 1, 49, 737), //
				TestClass.getTime(0, 1, 51, 404), //
				TestClass.getTime(0, 1, 53, 220), //
				TestClass.getTime(0, 1, 55, 308)); //
	}

	@Override
	public List<String> get_wheelLaptimes()
	{
		return TestClass.create( //
				TestClass.getTime(0, 1, 48, 867), //
				TestClass.getTime(0, 1, 54, 153), //
				TestClass.getTime(0, 1, 59, 717) //
		); //
	}

	@Override
	public Integer get_outcome()
	{
		return 28;
	}

}
