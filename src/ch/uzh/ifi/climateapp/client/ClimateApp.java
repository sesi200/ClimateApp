package ch.uzh.ifi.climateapp.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.RootPanel;
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
		// TODO TestTable
		tbl.setText(0, 0, "hello");
		tbl.setText(0, 1, "hello");
		tbl.setText(0, 2, "hello");
		tbl.setText(1, 0, "hello");
		tbl.setText(2, 0, "hello");
		
		mainPanel.add(tbl);
		
		
		
		
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
		
		data = new ClimateData[4];
		data[0] = d1;
		data[1] = d2;
		data[2] = d3;
		data[3] = d4;
		/*  -------- End Test Data for MAP --------- */

		/*  -------- Start Map Visualization --------- */
		map = new MapVisualization();
		map.replaceData(data);
		
		map.getVisualization(verticalPanel);
		mainPanel.add(verticalPanel);
		/*  -------- Start Map Visualization --------- */

		RootPanel.get("climateapp").add(mainPanel);
	
	}
	
}
