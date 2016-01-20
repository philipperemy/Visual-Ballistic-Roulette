package computations.predictor;

import java.util.List;

import computations.model.DataRecord;
import database.DatabaseAccessorInterface;
import servlets.SessionNotReadyException;

public interface Predictor {

	public void init(DatabaseAccessorInterface da);

	public List<DataRecord> buildDataRecords(List<Double> ballLapTimes, List<Double> wheelLapTimes, String sessionId);
	
	public int predict(List<Double> ballLapTimes, List<Double> wheelLapTimes, String sessionId) throws SessionNotReadyException;	
}
