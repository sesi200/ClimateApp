package ch.uzh.ifi.climateapp.server;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import ch.uzh.ifi.climateapp.shared.AverageData;
import ch.uzh.ifi.climateapp.shared.ClimateData;

/**
 * Class is used to store all the methods for grouping and calculating 
 * the average temperature per year and country. The result of this calculations 
 * will be used to provide the data for the map representation.
 * 
 * @author lada
 *
 */
public class AverageCalculator {
	private final DecimalFormat formatter = new DecimalFormat("#.00");

	/**
	 * Method calculates the average temperature per country and year
	 * and groups the resulting AverageData by year
	 * 
	 * @param list of ClimateData
	 * @return map from year to AverageData
	 */
	public Map<Integer, List<AverageData>> calculateAveragePerYearAndCountry(List<ClimateData> list){
		
		Map<CountryYear, List<Double>> countryYearToList = groupByCountryYear(list);
		Map<CountryYear, Double> countryYearToAverage = aggregateByCountryYear(countryYearToList);
		
		return groupPerYear(countryYearToAverage);
	}


	/**
	 * Method gets the map from the CountryYear to average temperature and 
	 * for each entry in the map creates new AverageData object.
	 * AverageData objects are grouped by year in the resulting map.
	 * 
	 * @param countryYearToAverage
	 * @return map from year to AverageData
	 */
	private Map<Integer, List<AverageData>> groupPerYear(Map<CountryYear, Double> countryYearToAverage) {
		
		Map<Integer,List<AverageData>> result = new LinkedHashMap<Integer, List<AverageData>>();
		
		for (Map.Entry<CountryYear, Double> entry : countryYearToAverage.entrySet()){
			String country = entry.getKey().getCountry();
			double avgTemp = round(entry.getValue(), 2); //as double with 2 digits after decimal point
			int year = entry.getKey().getYear();
			AverageData averageData = new AverageData(country, avgTemp, year);
			
			if(!result.containsKey(entry.getKey().getYear())){
				result.put(entry.getKey().getYear(), new ArrayList<AverageData>());
			}
			result.get(entry.getKey().getYear()).add(averageData);
		}
		return result;
	}
	
	/**
	 * Method rounds the double to two digits after decimal point and 
	 * transforms it to string (to be able to output more precise temperature on the map)
	 * 
	 * @param value
	 * @return String representation of the value with 2 digits after decimal point
	 */
	private String roundAndToString(Double value) {		
		double rounded = round(value, 2);
		return formatter.format(rounded);
	}
	
	/**
	 * Method transforms the multimap with the list as a value 
	 * to map with average of that list. The key stays the same.
	 * 
	 * @param countryYearToList
	 * @return map from CountryYear to average temperature per country and year
	 */
	private Map<CountryYear, Double> aggregateByCountryYear(
			Map<CountryYear, List<Double>> countryYearToList) {
		 
		Map<CountryYear, Double> result = new LinkedHashMap<CountryYear, Double>();
		for(Map.Entry<CountryYear, List<Double>> entry : countryYearToList.entrySet()){
			double average = calculateAverage(entry.getValue());
			result.put(entry.getKey(), average);
		}
		return result;
	}

	/**
	 * @param list of temperatures
	 * @return average
	 */
	private double calculateAverage(List<Double> list) {
		double sum = 0d;
		for(double value : list){
			sum += value;
		}
		return sum/list.size();
	}

	/**
	 * Method groups the original data (ClimateData) by country and year, 
	 * using the CountryYear object as key.
	 * @param list of ClimateData
	 * @return multimap from CountryYear to list of temperatures
	 */
	private Map<CountryYear, List<Double>> groupByCountryYear(List<ClimateData> list) {
		
		Map<CountryYear, List<Double>> result = new LinkedHashMap<CountryYear, List<Double>>();
		for (ClimateData climateData : list){
			int year = getYear(climateData.getDt());
			String country = climateData.getCountry();
			CountryYear countryYear = new CountryYear(country, year);
			
			if(!result.containsKey(countryYear)){
				result.put(countryYear, new ArrayList<Double>());
			} 
			result.get(countryYear).add(climateData.getAverageTemperature());
		}
		return result;
	}
	
	
	/**
	 * Method extracts year from the date and returns the correct integer representation of it
	 * @param date
	 * @return the integer value of a year
	 */
	@SuppressWarnings("deprecation")
	private int getYear(Date date){
		return (date.getYear() + 1900);
	}

	
	/**
	 * @param value
	 * @param places
	 * @return rounded value
	 */
	private double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();
	    
	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
}
