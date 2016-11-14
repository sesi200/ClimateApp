package ch.uzh.ifi.climateapp.shared;

import java.io.Serializable;

public class Filter implements Serializable{

	private int startYear = -1;
	private int endYear = -1;
	private double maxDeviation = -1;
	private double minDeviation = 0;
	private String city = null;
	private String country = null;
	
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
	public void setMinDeviation(double minDeviation) {
		this.minDeviation = minDeviation;
	}
	public double getMinDeviation() {
		return minDeviation;
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
