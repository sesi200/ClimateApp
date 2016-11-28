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
	private String avgTemp;
	private int year;
	
	public AverageData() {

	}
	public AverageData(String country, String avgTemp, int year) {
		super();
		this.country = country;
		this.avgTemp = avgTemp;
		this.year = year;
	}
	public String getCountry() {
		return country;
	}
	
	public String getAvgTemp() {
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
	public void setAvgTemp(String avgTemp) {
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
		result = prime * result + ((avgTemp == null) ? 0 : avgTemp.hashCode());
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
		if (avgTemp == null) {
			if (other.avgTemp != null)
				return false;
		} else if (!avgTemp.equals(other.avgTemp))
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
