package ch.uzh.ifi.climateapp.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import ch.uzh.ifi.climateapp.shared.ClimateData;

public class MyContextListener implements ServletContextListener {

	private static ServletContext context;

	/**
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 * 
	 *      This method initializes the CSVReader and calls the method to read
	 *      the data from the .csv file List of climate data objects is created
	 *      and filled with the data objects generated from .csv file The list
	 *      is made unmodifiable, since climate data is not supposed to be added
	 *      or deleted in this application Finally the list is put into the
	 *      context of the application
	 * 
	 * @author lada
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {

		// getting the context and assigning it to the field of special
		// singleton holder class
		context = sce.getServletContext();
		ServletContextHolder.getInstance(context);

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

		sce.getServletContext().setAttribute("climateData", climateData);

		// test for getting the data out of the context

		// List<ClimateDataBean> list = (List<ClimateDataBean>)
		// sce.getServletContext().getAttribute("climateData");
		// for (int i=0; i < 100; i++){
		// ClimateDataBean data = list.get(i);
		// System.out.println(String.format("Date: %s, Country: %s, City: %s,
		// Temperature: %f",
		// data.getDt().toString(), data.getCountry(), data.getCity(),
		// data.getAverageTemperature()));
		// }
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// shutdown code here
	}

	public static ServletContext getContext() {
		return context;
	}

}
