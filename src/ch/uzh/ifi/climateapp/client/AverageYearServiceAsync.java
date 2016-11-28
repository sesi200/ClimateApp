package ch.uzh.ifi.climateapp.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import ch.uzh.ifi.climateapp.shared.AverageData;

public interface AverageYearServiceAsync {
	void getAverageForYear(int year, AsyncCallback<AverageData[]> callback);
}
