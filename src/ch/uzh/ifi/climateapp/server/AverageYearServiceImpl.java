package ch.uzh.ifi.climateapp.server;

import java.text.SimpleDateFormat;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import ch.uzh.ifi.climateapp.client.AverageYearService;
import ch.uzh.ifi.climateapp.shared.AverageData;

@SuppressWarnings("serial")
public class AverageYearServiceImpl extends RemoteServiceServlet implements AverageYearService{

	@Override
	public AverageData[] getAverageForYear(int year) {
		// TODO Auto-generated method stub
		//Lada: this is the thing i meant with the date pattern:
		//private SimpleDateFormat date = new SimpleDateFormat();
		//date.applyPattern("yyyy");
		//use: date.format([object of type Date])
		return null;
	}

}
