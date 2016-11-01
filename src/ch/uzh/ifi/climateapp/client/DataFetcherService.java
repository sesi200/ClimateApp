package ch.uzh.ifi.climateapp.client;

import java.util.ArrayList;
import ch.uzh.ifi.climateapp.shared.ClimateData;
import ch.uzh.ifi.climateapp.shared.Filter;

public interface DataFetcherService {
	abstract public ArrayList<ClimateData> getClimateData(Filter filter);
}
