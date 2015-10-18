package computations.ml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import computations.outcome.Outcome;
import computations.predictor.ClockSpeed;
import computations.wheel.Wheel.WheelWay;

public class DataRecord implements Cloneable {

	@Override
	public String toString() {
		return "DataRecord [" + (ballSpeeds != null ? "ballSpeeds=" + ballSpeeds + ", " : "")
				+ (wheelSpeeds != null ? "wheelSpeeds=" + wheelSpeeds + ", " : "")
				+ (outcome != null ? "outcome=" + outcome + ", " : "") + (way != null ? "way=" + way + ", " : "")
				+ (phases != null ? "phases=" + phases : "") + "]";
	}

	/**
	 * Absolute time is taken when the wheel (its 0) is in front of the
	 * landmark.
	 */
	public List<ClockSpeed> ballSpeeds = new ArrayList<>();
	public List<ClockSpeed> wheelSpeeds = new ArrayList<>();
	public Outcome outcome;
	public WheelWay way = WheelWay.CLOCKWISE; // Only that implemented for now

	/**
	 * Phases of the ball when the zero of the ball is in front of a landmark.
	 * After it's just looking at the phase between each phase and what we have
	 * to shift the outcome.
	 */
	public List<Integer> phases = new ArrayList<>();
	
	public DataRecord() {
		
	}

	// For testing purposes.
	public DataRecord(Outcome outcome) {
		this.outcome = outcome;
	}

	// For testing purposes.
	@Override
	public DataRecord clone() {
		DataRecord dr = new DataRecord();
		Collections.copy(dr.ballSpeeds, ballSpeeds);
		Collections.copy(dr.wheelSpeeds, wheelSpeeds);
		Collections.copy(dr.phases, phases);
		dr.outcome = outcome.clone();
		dr.way = way;
		return dr;
	}

}
