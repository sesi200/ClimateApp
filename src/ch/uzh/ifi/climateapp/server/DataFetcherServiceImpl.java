package ch.uzh.ifi.climateapp.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import ch.uzh.ifi.climateapp.client.DataFetcherService;
import ch.uzh.ifi.climateapp.shared.ClimateData;
import ch.uzh.ifi.climateapp.shared.Filter;

@SuppressWarnings("serial")
public class DataFetcherServiceImpl extends RemoteServiceServlet implements DataFetcherService {
	
	private SimpleDateFormat date = new SimpleDateFormat();

	@Override
	/**
	 * @return all data from the context that matches the filter
	 */
	public ClimateData[] getClimateData(Filter[] filter) {
		ServletContext context = MyContextListener.getContext();
		date.applyPattern("yyyy");
		@SuppressWarnings("unchecked")
		List<ClimateData> list = (List<ClimateData>) context.getAttribute("climateData");
		int currentBatch = filter[0].getBatch();
		System.out.println("processing batch "+currentBatch);
		int matchedEntries = 0;
		
		ArrayList<String> countryFilter = new ArrayList<String>();
		ArrayList<String> cityFilter = new ArrayList<String>();
		//read all filtered countries and cities
		for (Filter myFilter : filter) {
			if (myFilter.getCity()!=null) {
				cityFilter.add(myFilter.getCity());
			}
			if(myFilter.getCountry()!=null) {
				countryFilter.add(myFilter.getCountry());
			}
		}
		
		//create array to store all matching data
		ArrayList<ClimateData> returnData = new ArrayList<ClimateData>();
		for (ClimateData data : list) {
			
			//check whether the data matches
			//check whether filters are set
			if (filter.length>0 ) {
				//check for temperature deviation
				if ((data.getAverageTemperatureUncertainty() <= filter[0].getMaxDeviation() || filter[0].getMaxDeviation()==-1)
						&& filter[0].getMinDeviation()<=data.getAverageTemperatureUncertainty()) {
					//check for city
					if(cityFilter.contains(data.getCity())||cityFilter.isEmpty()) {
						//check for country
						if (countryFilter.contains(data.getCountry())||countryFilter.isEmpty()) {
							//check for date
							int year = Integer.parseInt(date.format(data.getDt()));
							if (year<=filter[0].getEndYear()||filter[0].getEndYear()==-1) {
								if (year>=filter[0].getStartYear()) {
									//data is correct, return if it belongs to current batch
									++matchedEntries;
									if (matchedEntries>=currentBatch*5000) {
										returnData.add(data);
										//send data if batch is full
										if (returnData.size()==5000) {
											break;
										}
									}
								}
								
							}
						}
					}
			
				}
			} else if (filter.length==0){
				returnData.add(data);
			}
		}
		System.out.println("Returning list with "+returnData.size()+" elements.");
		return returnData.toArray(new ClimateData[0]); //return all matching data
	}

}
