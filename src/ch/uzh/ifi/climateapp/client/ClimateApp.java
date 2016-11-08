package ch.uzh.ifi.climateapp.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ClimateApp implements EntryPoint {
	
	private FlexTable tbl = new FlexTable();
	private VerticalPanel mainPanel = new VerticalPanel();

	@Override
	public void onModuleLoad() {
		// TODO Auto-generated method stub
		tbl.setText(0, 0, "hello");
		tbl.setText(0, 1, "hello");
		tbl.setText(0, 2, "hello");
		tbl.setText(1, 0, "hello");
		tbl.setText(2, 0, "hello");
		
		mainPanel.add(tbl);
		RootPanel.get("climateapp").add(mainPanel);
		
	}
	
}
