package computations;

import org.apache.commons.math3.stat.regression.SimpleRegression;

public class Test_Regression {

	public static void main(String[] args) {
		SimpleRegression regression = new SimpleRegression();
		regression.addData(1d, 2d);
		// At this point, with only one observation,
		// all regression statistics will return NaN

		regression.addData(3d, 3d);
		// With only two observations,
		// slope and intercept can be computed
		// but inference statistics will return NaN

		regression.addData(3d, 3d);
		// Now all statistics are defined.

		System.out.println(regression.getIntercept());
		// displays intercept of regression line

		System.out.println(regression.getSlope());
		// displays slope of regression line

		System.out.println(regression.getSlopeStdErr());
		// displays slope standard error
	}

}
