package ch.uzh.ifi.climateapp.client;

import com.google.gwt.user.client.ui.Widget;
import ch.uzh.ifi.climateapp.shared.ClimateData;

public interface IVisualization {
	public abstract Widget getVisualization();
	public abstract void replaceData(ClimateData[] newData);
}
 