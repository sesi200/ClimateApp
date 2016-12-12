package ch.uzh.ifi.climateapp.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import ch.uzh.ifi.climateapp.shared.ClimateData;
import ch.uzh.ifi.climateapp.shared.Filter;

@RemoteServiceRelativePath("climateData")
public interface DataFetcherService extends RemoteService{
	ClimateData[] getClimateData(Filter[] filter);
}
