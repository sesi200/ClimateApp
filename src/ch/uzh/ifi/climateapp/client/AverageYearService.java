package ch.uzh.ifi.climateapp.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import ch.uzh.ifi.climateapp.shared.AverageData;

@RemoteServiceRelativePath("averageYear")
public interface AverageYearService extends RemoteService {
	AverageData[] getAverageForYear(int year);
}
