package ch.uzh.ifi.climateapp.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
	@Override
	public void contextInitialized(ServletContextEvent sce) {

		AverageCalculator avgCalc = new AverageCalculator();

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

		climateData = Collections.unmodifiableList(climateData);
		long timeTaken = System.currentTimeMillis() - before;
		System.out.println("Loaded " + climateData.size() + " climate data entries in " + timeTaken + "ms.");

		Map<Integer, List<AverageData>> averageDataMap = avgCalc.calculateAveragePerYearAndCountry(climateData);
		averageDataMap = Collections.unmodifiableMap(averageDataMap);

		sce.getServletContext().setAttribute(ContextContent.CLIMATE_DATA, climateData);
		sce.getServletContext().setAttribute(ContextContent.AVERAGE_PER_YEAR, averageDataMap);
		
		// test for getting the data out of the context

//		 @SuppressWarnings("unchecked")
//		 List<ClimateData> list = (List<ClimateData>) sce.getServletContext().getAttribute("climateData");
//		 for (int i=0; i < 100; i++){
//		 ClimateData data = list.get(i);
//		 System.out.println(data);
//		 }
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// shutdown code here
	}
}
