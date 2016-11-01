package ch.uzh.ifi.climateapp.shared;

import java.util.ArrayList;

public class Filter {

	private int startYear;
	private int endYear;
	private double maxDeviation;
	private ArrayList<String> cities;
	private ArrayList<String> countries;
	
	public void addCity(String city) {
		cities.add(city);
	}
	
	public void addCountry(String country) {
		countries.add(country);
	}
	
	public void removeCity(String city) {
		cities.remove(city);
	}
	
	public void removeCountry(String country) {
		countries.remove(country);
	}
	
	//getter/setter
	public ArrayList<String> getCountries() {
		return countries;
	}
	
	public ArrayList<String> getCities() {
		return cities;
	}
	
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
	
}
