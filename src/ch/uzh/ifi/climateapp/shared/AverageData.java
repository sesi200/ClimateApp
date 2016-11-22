package ch.uzh.ifi.climateapp.shared;

import java.io.Serializable;

public class AverageData implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8388654543381139460L;
	private String country;
	private float avgTemp;
	private int year;
	
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public float getAvgTemp() {
		return avgTemp;
	}
	public void setAvgTemp(float avgTemp) {
		this.avgTemp = avgTemp;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}

}
