package ch.uzh.ifi.climateapp.shared;

import java.io.Serializable;

public class AverageData implements Serializable {
	
	/**
	 * The class stores the data for the map view: the year is stored as an integer
	 * for easy manipulation, the average temperature is stored as a string
	 * to allow more precise representation on the map 
	 * (since it can only represent integers or strings)
	 */
	private static final long serialVersionUID = 8388654543381139460L;
	private String country;
	private double avgTemp;
	private int year;
	
	public AverageData() {

	}
	public AverageData(String country, double avgTemp, int year) {
		super();
		this.country = country;
		this.avgTemp = avgTemp;
		this.year = year;
	}
	public String getCountry() {
		return country;
	}
	
	public double getAvgTemp() {
		return avgTemp;
	}
	
	public int getYear() {
		return year;
	}
	
	
	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	/**
	 * @param avgTemp the avgTemp to set
	 */
	public void setAvgTemp(double avgTemp) {
		this.avgTemp = avgTemp;
	}
	/**
	 * @param year the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}
	
	@Override
	public String toString() {
		return "AverageData [country=" + country + ", avgTemp=" + avgTemp + ", year=" + year + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(avgTemp);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + year;
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
		AverageData other = (AverageData) obj;
		if (Double.doubleToLongBits(avgTemp) != Double.doubleToLongBits(other.avgTemp))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (year != other.year)
			return false;
		return true;
	}
	

}
