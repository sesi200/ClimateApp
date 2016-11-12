package ch.uzh.ifi.climateapp.shared;

import java.io.Serializable;

public class Filter implements Serializable{

	private int startYear;
	private int endYear;
	private double maxDeviation;
	private String city;
	private String country;
	
	public Filter() {}
	
	public int getStartYear() {
		return startYear;
	}
	public void setStartYear(int startYear) {
		this.startYear = startYear;
	}
	public int getEndYear() {
		return endYear;
	}
	public void setEndYear(int endYear) {
		this.endYear = endYear;
	}
	public double getMaxDeviation() {
		return maxDeviation;
	}
	public void setMaxDeviation(double maxDeviation) {
		this.maxDeviation = maxDeviation;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
	
	
}
