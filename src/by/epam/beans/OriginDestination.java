package by.epam.beans;

import java.sql.Date;

public class OriginDestination {
	private String departureCity;
	private String arrivalCity;
	private Date departureDate;
	private Date arrivalDate;
	
	public OriginDestination() {
		
	}
	
	public OriginDestination(String departureCity, String arrivalCity, Date departureDate, Date arrivalDate) {
		this.departureCity = departureCity;
		this.arrivalCity = arrivalCity;
		this.departureDate = departureDate;
		this.arrivalDate = arrivalDate;
	}

	public String getDepartureCity() {
		return departureCity;
	}

	public String getArrivalCity() {
		return arrivalCity;
	}

	public Date getDepartureDate() {
		return departureDate;
	}

	public Date getArrivalDate() {
		return arrivalDate;
	}

	public void setDepartureCity(String departureCity) {
		this.departureCity = departureCity;
	}

	public void setArrivalCity(String arrivalCity) {
		this.arrivalCity = arrivalCity;
	}

	public void setDepartureDate(Date departureDate) {
		this.departureDate = departureDate;
	}

	public void setArrivalDate(Date arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public void setDepartureDate(String strDepartureDate) {
		this.departureDate = Date.valueOf(strDepartureDate);
	}

	public void setArrivalDate(String strArrivalDate) {
		this.arrivalDate = Date.valueOf(strArrivalDate);
	}
	
	@Override
	public String toString() {
		return "departure=" + departureCity + " " + departureDate + " destination=" + arrivalCity + " " + arrivalDate;
	}
}
