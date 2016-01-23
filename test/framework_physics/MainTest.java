package framework_physics;

import org.junit.Test;

public class MainTest extends TestClass
{

	@Test
	public void testPhysics()
	{
		Game game6 = new Game("6", dbRef);
		Game game7 = new Game("7", dbRef);
		Game game8 = new Game("8", dbRef);

		runTest(game6);
		runTest(game7);
		runTest(game8);

	}

}
