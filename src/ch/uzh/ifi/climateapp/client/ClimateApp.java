package ch.uzh.ifi.climateapp.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ClimateApp implements EntryPoint {
	
	private FlexTable tbl = new FlexTable();

	@Override
	public void onModuleLoad() {
		// TODO Auto-generated method stub
		tbl.setText(0, 0, "hello");
		RootPanel.get("climateapp").add(tbl);
		
	}
	
}
