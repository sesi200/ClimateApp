package ch.uzh.ifi.climateapp.server;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import ch.uzh.ifi.climateapp.client.AverageYearService;
import ch.uzh.ifi.climateapp.shared.AverageData;

@SuppressWarnings("serial")
public class AverageYearServiceImpl extends RemoteServiceServlet implements AverageYearService{

	@SuppressWarnings("unchecked")
	@Override
	public List<AverageData> getAverageForYear(int year) {
		Map<Integer, List<AverageData>> yearToAvgData= (Map<Integer, List<AverageData>>) this.getServletContext().getAttribute(ContextContent.AVERAGE_PER_YEAR);
		
		return yearToAvgData.get(year);
	}

}
