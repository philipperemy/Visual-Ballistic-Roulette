package framework.games;

import java.util.List;

import framework.TestClass;

//OTHER BALL
public class Game3 extends Game
{
	@Override
	public List<String> get_ballLaptimes()
	{
		return TestClass.create( //
				TestClass.getTime(0, 2, 7, 462), //
				TestClass.getTime(0, 2, 8, 807), //
				TestClass.getTime(0, 2, 10, 303), //
				TestClass.getTime(0, 2, 11, 977), //
				TestClass.getTime(0, 2, 13, 774), //
				TestClass.getTime(0, 2, 15, 700), //
				TestClass.getTime(0, 2, 17, 817), //
				TestClass.getTime(0, 2, 20, 204) //
			); //
	}

	@Override
	public List<String> get_wheelLaptimes()
	{
		return TestClass.create( //
				TestClass.getTime(0, 2, 11, 829), //
				TestClass.getTime(0, 2, 18, 577), //
				TestClass.getTime(0, 2, 26, 026)); //
	}

	@Override
	public Integer get_outcome()
	{
		return 13;
	}

}
