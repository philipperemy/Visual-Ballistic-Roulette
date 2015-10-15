package computations.outcome;

public class Outcome implements Cloneable {

	public int number;
	public int obstaclesHitCount;

	@Override
	public Outcome clone() {
		Outcome outcome = new Outcome();
		outcome.number = number;
		outcome.obstaclesHitCount = obstaclesHitCount;
		return outcome;
	}

	public Outcome(int number, int obstaclesHitCount) {
		this.number = number;
		this.obstaclesHitCount = obstaclesHitCount;
	}

	public Outcome() {
	}

}
