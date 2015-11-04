package framework.games;

import java.util.List;

import framework.TestClass;

public class Game8 extends Game
{
	@Override
	public List<String> get_ballLaptimes()
	{
		return TestClass.create( //
				TestClass.getTime(1, 29, 5, 865), //
				TestClass.getTime(1, 29, 6, 391), //
				TestClass.getTime(1, 29, 6, 827), //
				TestClass.getTime(1, 29, 7, 729), //
				TestClass.getTime(1, 29, 7, 856), //
				TestClass.getTime(1, 29, 8, 390), //
				TestClass.getTime(1, 29, 8, 932), //
				TestClass.getTime(1, 29, 9, 500), //
				TestClass.getTime(1, 29, 10, 115), //
				TestClass.getTime(1, 29, 10, 729), //
				TestClass.getTime(1, 29, 11, 338), //
				TestClass.getTime(1, 29, 12, 11), //
				TestClass.getTime(1, 29, 12, 723), //
				TestClass.getTime(1, 29, 13, 352), //
				TestClass.getTime(1, 29, 14, 127), //
				TestClass.getTime(1, 29, 14, 944), //
				TestClass.getTime(1, 29, 15, 787), //
				TestClass.getTime(1, 29, 16, 542), //
				TestClass.getTime(1, 29, 17, 621) //
		); //
	}

	@Override
	public List<String> get_wheelLaptimes()
	{
		return TestClass.create( //
				TestClass.getTime(1, 29, 6, 201), //
				TestClass.getTime(1, 29, 11, 708), //
				TestClass.getTime(1, 29, 17, 621) //
		); //
	}

	@Override
	public Integer get_outcome()
	{
		return 1;
	}

}
