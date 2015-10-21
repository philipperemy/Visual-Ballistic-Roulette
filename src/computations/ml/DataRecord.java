package computations.ml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import computations.outcome.Outcome;
import computations.predictor.ClockSpeed;
import computations.wheel.Wheel;
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
	 * Mirroring. Deep copy constructor.
	 */
	public static DataRecord createMirror(DataRecord dr) {
		DataRecord dr2 = new DataRecord();
		dr2.ballSpeeds = new ArrayList<>(dr.ballSpeeds);
		dr2.wheelSpeeds = new ArrayList<>(dr.wheelSpeeds);
		dr2.outcome = new Outcome(Wheel.getMirrorNumber(dr.outcome.number), dr.outcome.obstaclesHitCount);
		dr2.way = dr.way == WheelWay.CLOCKWISE ? WheelWay.ANTICLOCKWISE : WheelWay.CLOCKWISE;
		
		for(Integer phase : dr.phases) {
			dr2.phases.add(Wheel.getMirrorNumber(phase));
		}
		
		return dr2;
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


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ballSpeeds == null) ? 0 : ballSpeeds.hashCode());
		result = prime * result + ((outcome == null) ? 0 : outcome.hashCode());
		result = prime * result + ((phases == null) ? 0 : phases.hashCode());
		result = prime * result + ((way == null) ? 0 : way.hashCode());
		result = prime * result + ((wheelSpeeds == null) ? 0 : wheelSpeeds.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DataRecord other = (DataRecord) obj;
		if (ballSpeeds == null) {
			if (other.ballSpeeds != null)
				return false;
		} else if (!ballSpeeds.equals(other.ballSpeeds))
			return false;
		if (outcome == null) {
			if (other.outcome != null)
				return false;
		} else if (!outcome.equals(other.outcome))
			return false;
		if (phases == null) {
			if (other.phases != null)
				return false;
		} else if (!phases.equals(other.phases))
			return false;
		if (way != other.way)
			return false;
		if (wheelSpeeds == null) {
			if (other.wheelSpeeds != null)
				return false;
		} else if (!wheelSpeeds.equals(other.wheelSpeeds))
			return false;
		return true;
	}

}
