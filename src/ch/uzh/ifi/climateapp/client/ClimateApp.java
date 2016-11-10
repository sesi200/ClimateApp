package ch.uzh.ifi.climateapp.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.visualizations.GeoMap;

import ch.uzh.ifi.climateapp.shared.ClimateData;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ClimateApp implements EntryPoint {
	
	private FlexTable tbl = new FlexTable();
	private VerticalPanel mainPanel = new VerticalPanel();
	private VerticalPanel verticalPanel = new VerticalPanel();
	private MapVisualization map;
	
	private ClimateData [] data;
	
	
	
	private DataTable dataTable;
	private GeoMap.Options options;
	private GeoMap geomap;
	private int mapWidth = 300;
	private int mapHeight = 300;

	

	@Override
	public void onModuleLoad() {

		
		
		/*  -------- Start Test Data for MAP --------- */
		ClimateData d1 = new ClimateData();
		d1.setCountry("US");
		d1.setTemperature(-100);
		ClimateData d2 = new ClimateData();
		d2.setCountry("India");
		d2.setTemperature(30);
		ClimateData d3 = new ClimateData();
		d3.setCountry("Germany");
		d3.setTemperature(0);
		ClimateData d4 = new ClimateData();
		d4.setCountry("GB");
		d4.setTemperature(14);
		
		
		ClimateData [] dataOne = new ClimateData[4];
		dataOne[0] = d1;
		dataOne[1] = d2;
		dataOne[2] = d3;
		dataOne[3] = d4;
		
		ClimateData d5 = new ClimateData();
		d5.setCountry("France");
		d5.setTemperature(15);
		ClimateData d6 = new ClimateData();
		d6.setCountry("Spain");
		d6.setTemperature(20);
		ClimateData d7 = new ClimateData();
		d7.setCountry("Greece");
		d7.setTemperature(0);
		ClimateData d8 = new ClimateData();
		d8.setCountry("Poland");
		d8.setTemperature(30);
		
		ClimateData [] dataTwo = new ClimateData[4];
		dataTwo[0] = d5;
		dataTwo[1] = d6;
		dataTwo[2] = d7;
		dataTwo[3] = d8;
		
		/*  -------- End Test Data for MAP --------- */
		
		
		
		
		
		
		

		/*  -------- Start Map Visualization --------- */
		map = new MapVisualization();
		map.replaceData(dataOne);
		
		map.getVisualization(verticalPanel);
		mainPanel.add(verticalPanel);
		/*  -------- Start Map Visualization --------- */

		
		/* ----------- Adding Panels ------ */
		RootPanel.get("climateapp").add(mainPanel);
		
		/*
	    // Create a three-item tab panel, with the tab area 1.5em tall.
	    TabLayoutPanel p = new TabLayoutPanel(1.5, Unit.EM);
	    //p.add(verticalPanel);
	    //p.add(verticalPanel);
	    //p.add(verticalPanel);

	    // Attach the LayoutPanel to the RootLayoutPanel. The latter will listen for
	    // resize events on the window to ensure that its children are informed of
	    // possible size changes.
	    RootLayoutPanel rp = RootLayoutPanel.get();
	    rp.add(p);
	    */
	
	}
	
}
