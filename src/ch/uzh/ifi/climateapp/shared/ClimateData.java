package ch.uzh.ifi.climateapp.shared;

import java.io.Serializable;
import java.util.Date;

import com.google.appengine.labs.repackaged.com.google.common.primitives.Ints;

/**
 * The ClimateDataBean is a JavaBean object since the library which is used 
 * to retrieve the data from .csv file reads the lines from csv into bean object.
 * Which means that it has only fields, getters / setters for them, and public default constructor.
 * This class also complies with DTO standard.
 * 
 * The fields must be named as a header of the .csv file to be able 
 * to use the CsvBeanReader from the super-csv-2.4.0 library
 * 
 * @author lada
 *
 */
@SuppressWarnings("deprecation") // using deprecated Date#getYear - it's ok in our case
public class ClimateData implements Serializable, Comparable<ClimateData> {
	
	private static final long serialVersionUID = 966748726465050125L;
	
	public ClimateData() {}
	
	
	
	public ClimateData(Date dt, double averageTemperature, double averageTemperatureUncertainty, String city,
			String country, String longitude, String latitude) {
		this.dt = dt;
		AverageTemperature = averageTemperature;
		AverageTemperatureUncertainty = averageTemperatureUncertainty;
		City = city;
		Country = country;
		Longitude = longitude;
		Latitude = latitude;
	}



	private Date dt;
	private double AverageTemperature;
	private double AverageTemperatureUncertainty;
	private String City;
	private String Country;
	private String Longitude;
	private String Latitude;
	/**
	 * @return the dt
	 */
	public Date getDt() {
		return dt;
	}
	/**
	 * @param dt the date to set
	 */
	public void setDt(Date dt) {
		this.dt = dt;
	}
	/**
	 * @return the averageTemperature
	 */
	public double getAverageTemperature() {
		return AverageTemperature;
	}
	/**
	 * @param averageTemperature the averageTemperature to set
	 */
	public void setAverageTemperature(double averageTemperature) {
		AverageTemperature = averageTemperature;
	}
	/**
	 * @return the averageTemperatureUncertainty
	 */
	public double getAverageTemperatureUncertainty() {
		return AverageTemperatureUncertainty;
	}
	/**
	 * @param averageTemperatureUncertainty the averageTemperatureUncertainty to set
	 */
	public void setAverageTemperatureUncertainty(double averageTemperatureUncertainty) {
		AverageTemperatureUncertainty = averageTemperatureUncertainty;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return City;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		City = city;
	}
	/**
	 * @return the country
	 */
	public String getCountry() {
		return Country;
	}
	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		Country = country;
	}
	/**
	 * @return the longitude
	 */
	public String getLongitude() {
		return Longitude;
	}
	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(String longitude) {
		Longitude = longitude;
	}
	/**
	 * @return the latitude
	 */
	public String getLatitude() {
		return Latitude;
	}
	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(String latitude) {
		Latitude = latitude;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ClimateDataBean [dt=" + (dt.getYear() + 1900) + ", AverageTemperature=" + AverageTemperature
				+ ", AverageTemperatureUncertainty=" + AverageTemperatureUncertainty + ", City=" + City + ", Country="
				+ Country + ", Longitude=" + Longitude + ", Latitude=" + Latitude + "]";
	}
	
	@Override
	public int compareTo(ClimateData o) {
		int yearRes = Integer.compare(getDt().getYear(), o.getDt().getYear());
		if (yearRes != 0) return yearRes;
		return getCountry().compareTo(o.getCountry());
	}

}

