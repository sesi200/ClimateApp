package ch.uzh.ifi.climateapp.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import ch.uzh.ifi.climateapp.shared.AverageData;
import ch.uzh.ifi.climateapp.shared.ClimateData;

public class MyContextListener implements ServletContextListener {

	/**
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 * 
	 *      This method initializes the CSVReader and calls the method to read
	 *      the data from the .csv file List of climate data objects is created
	 *      and filled with the data objects generated from .csv file The list
	 *      is made unmodifiable, since climate data is not supposed to be added
	 *      or deleted in this application 
	 *      Finally the list is put into the context of the application
	 *      
	 *      The climate data is aggregated per year and country and put into the
	 *      map from year to AverageData to make the access to this data easy.
	 * 
	 * @author lada
	 */
	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {

		AverageCalculator avgCalc = new AverageCalculator();
		//sets for lookup and arrays to give the list with all country and city names
		Set<String> countryNamesSet = new HashSet<String>();
		Set<String> cityNamesSet = new HashSet<String>();
		String[] countryNames;
		String[] cityNames;

		System.out.println("Context loaded!");

		CSVReader myCsvReader = CSVReader.getInstance();
		List<ClimateData> climateData = new ArrayList<ClimateData>();

		System.out.println("Starting to read the CSV file");
		long before = System.currentTimeMillis();

		try {
			climateData = myCsvReader.readWithCsvBeanReader();
		} catch (IOException e) {
			System.out.println("The CSVReader has thrown an exception.");
			e.printStackTrace();
		}
		
		/**
		 * Create lookup hashset for country names and city names
		 * For each ClimateData add the country / city name to the set,
		 * if not is not yet there
		 */
		for(ClimateData data : climateData){
			String currCountName = data.getCountry();
			String currCityName = data.getCity();
			
			if(!countryNamesSet.contains(currCountName)){
				countryNamesSet.add(currCountName);
			}
			if (!cityNamesSet.contains(currCityName)){
				cityNamesSet.add(currCityName);
			}
		}
		
		//transforming the sets to array and putting them to the context
		countryNames = countryNamesSet.toArray(new String[countryNamesSet.size()]);
		Arrays.sort(countryNames);		
		cityNames = cityNamesSet.toArray(new String[cityNamesSet.size()]);
		Arrays.sort(countryNames);
		sce.getServletContext().setAttribute(ContextContent.COUNTRIES, countryNames);
		sce.getServletContext().setAttribute(ContextContent.CITIES, cityNames);
		
		climateData = Collections.unmodifiableList(climateData);
		long timeTaken = System.currentTimeMillis() - before;
		System.out.println("Loaded " + climateData.size() + " climate data entries in " + timeTaken + "ms.");

		Map<Integer, List<AverageData>> averageDataMap = avgCalc.calculateAveragePerYearAndCountry(climateData);
		averageDataMap = Collections.unmodifiableMap(averageDataMap);

		sce.getServletContext().setAttribute(ContextContent.CLIMATE_DATA, climateData);
		sce.getServletContext().setAttribute(ContextContent.AVERAGE_PER_YEAR, averageDataMap);
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// shutdown code here
	}
}
