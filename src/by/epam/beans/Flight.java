package by.epam.beans;

public class Flight {
	private int number;
	private OriginDestination originDestination;
	private Aircraft aircraft;
	private int gate;
	
	public Flight() {
		
	}
	
	public Flight(int number, OriginDestination originDestination, Aircraft aircraft, int gate) {
		this.number = number;
		this.originDestination = originDestination;
		this.aircraft = aircraft;
		this.gate = gate;
	}

	public int getNumber() {
		return number;
	}

	public OriginDestination getOriginDestination() {
		return originDestination;
	}

	public Aircraft getAircraft() {
		return aircraft;
	}

	public int getGate() {
		return gate;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public void setOriginDestination(OriginDestination originDestination) {
		this.originDestination = originDestination;
	}

	public void setAircraft(Aircraft aircraft) {
		this.aircraft = aircraft;
	}

	public void setGate(int gate) {
		this.gate = gate;
	}
	
	@Override
	public String toString() {
		return "flight=" + number + " " + originDestination + " gate=" + gate + " " + aircraft;
	}
}
