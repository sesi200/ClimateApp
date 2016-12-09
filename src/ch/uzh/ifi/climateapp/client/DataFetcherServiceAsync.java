package ch.uzh.ifi.climateapp.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import ch.uzh.ifi.climateapp.shared.ClimateData;
import ch.uzh.ifi.climateapp.shared.Filter;

public interface DataFetcherServiceAsync {

	void getClimateData(Filter[] filter, AsyncCallback<ClimateData[]> callback);
	void getCountryNames(AsyncCallback<String[]> callback);
	void getCityNames(AsyncCallback<String[]> callback);
}
