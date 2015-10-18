package computations.outcome;

import java.util.Map;

public class OutcomeStatistics {

	@Override
	public String toString() {
		return "OutcomeStatistics [meanNumber=" + meanNumber + ", stdDeviation=" + stdDeviation + ", "
				+ (frequency != null ? "frequency=" + frequency + ", " : "") + "mostProbablyNumber="
				+ mostProbablyNumber + "]";
	}

	public int meanNumber;

	public double stdDeviation;

	public Map<Integer, Integer> frequency;

	public int mostProbablyNumber;

}
