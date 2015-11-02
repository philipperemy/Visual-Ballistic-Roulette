package computations.model;

public class Outcome implements Cloneable
{
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + number;
		result = prime * result + obstaclesHitCount;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Outcome other = (Outcome) obj;
		if (number != other.number)
			return false;
		if (obstaclesHitCount != other.obstaclesHitCount)
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "Outcome [number=" + number + ", obstaclesHitCount=" + obstaclesHitCount + "]";
	}

	public int number;
	public int obstaclesHitCount;

	public Outcome()
	{
	}

}
