package framework.games;

import java.util.List;

import framework.TestClass;

public class Game7 extends Game
{
	@Override
	public List<String> get_ballLaptimes()
	{
		return TestClass.create( //
				TestClass.getTime(1, 28, 25, 491), //
				TestClass.getTime(1, 28, 25, 974), //
				TestClass.getTime(1, 28, 26, 391), //
				TestClass.getTime(1, 28, 26, 906), //
				TestClass.getTime(1, 28, 27, 596), //
				TestClass.getTime(1, 28, 28, 25), //
				TestClass.getTime(1, 28, 28, 566), //
				TestClass.getTime(1, 28, 29, 94), //
				TestClass.getTime(1, 28, 29, 794), //
				TestClass.getTime(1, 28, 30, 363), //
				TestClass.getTime(1, 28, 30, 976), //
				TestClass.getTime(1, 28, 31, 713), //
				TestClass.getTime(1, 28, 32, 406), //
				TestClass.getTime(1, 28, 33, 6), //
				TestClass.getTime(1, 28, 33, 901), //
				TestClass.getTime(1, 28, 34, 603), //
				TestClass.getTime(1, 28, 35, 444), //
				TestClass.getTime(1, 28, 36, 274) //
		); //
	}

	@Override
	public List<String> get_wheelLaptimes()
	{
		return TestClass.create( //
				TestClass.getTime(1, 28, 25, 778), //
				TestClass.getTime(1, 28, 30, 806), //
				TestClass.getTime(1, 28, 35, 886) //
		); //
	}

	@Override
	public Integer get_outcome()
	{
		return 4;
	}

}
