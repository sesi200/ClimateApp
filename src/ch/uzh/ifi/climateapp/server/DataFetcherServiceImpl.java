package ch.uzh.ifi.climateapp.server;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import ch.uzh.ifi.climateapp.client.DataFetcherService;
import ch.uzh.ifi.climateapp.shared.ClimateData;
import ch.uzh.ifi.climateapp.shared.Filter;

public class DataFetcherServiceImpl extends RemoteServiceServlet implements DataFetcherService {

	@Override
	public ClimateData[] getClimateData(Filter[] filter) {
		ServletContext context = MyContextListener.getContext();
		List<ClimateData> list = (List<ClimateData>) context.getAttribute("climateData");
		ArrayList<ClimateData> returnData = new ArrayList<ClimateData>();
		for (ClimateData data : list) {
			if (filter.length>0 && (data.getAverageTemperatureUncertainty() < filter[0].getMaxDeviation() || filter[0].getMaxDeviation()==-1)
					&& filter[0].getMinDeviation()<=data.getAverageTemperatureUncertainty()) {
				returnData.add(data);
			} else if (filter.length==0){
				returnData.add(data);
			}
		}
		System.out.println("Returning list with "+returnData.size()+" elements.");
		return returnData.toArray(new ClimateData[0]);
	}

}
