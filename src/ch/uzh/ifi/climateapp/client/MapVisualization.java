package ch.uzh.ifi.climateapp.client;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.GeoMap;

import ch.uzh.ifi.climateapp.shared.ClimateData;

public class MapVisualization implements IVisualization{
	
	
	private int mapWidth = 700;
	private int mapHeight = 500;
	
	private DataTable dataTable;
	private GeoMap.Options options;
	private GeoMap geomap;
	
	private ClimateData[] climateData;
	
	public MapVisualization(){}
	
	public MapVisualization(int mapWidth, int mapHeight, ClimateData[] climateData){
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
		this.climateData = climateData;
	}
	
	@Override
	public Widget getVisualization(final VerticalPanel verticalPanel) {
		Runnable onLoadCallback = new Runnable(){

			@Override
			public void run() {
				
				dataTable = DataTable.create();
				dataTable.addColumn(ColumnType.STRING, "Country");
				dataTable.addColumn(ColumnType.NUMBER, "Temperature");
				//dataTable.addColumn(ColumnType.STRING, "Precise Temperature");
				
				dataTable.addRows(climateData.length);
				for (int i = 0; i < climateData.length; i++){
					dataTable.setValue(i, 0, climateData[i].getCountry());
					dataTable.setValue(i, 1, climateData[i].getTemperature());
					//dataTable.setValue(i, 2, "Chocolate" + climateData[i].getTemperature());
				}
				
				options = GeoMap.Options.create();
				options.setDataMode(GeoMap.DataMode.REGIONS);
				options.setRegion("world");
				options.setWidth(mapWidth);
				options.setHeight(mapHeight);
				options.setShowZoomOut(true);
				options.setShowLegend(true);
				geomap = new GeoMap(dataTable, options);
				
				verticalPanel.clear();
				verticalPanel.add(geomap);
				
				System.out.println("dataTable done");
				
			}
		};
		
		VisualizationUtils.loadVisualizationApi(onLoadCallback, GeoMap.PACKAGE);
		return verticalPanel;
	}

	@Override
	public void replaceData(ClimateData[] newData) {
		// TODO Auto-generated method stub
		this.climateData = newData;
	}

}


/* Copy to not loose anything because Dominik doesn't know GitHub that well.
 * 
 * 
package ch.uzh.ifi.climateapp.client;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.GeoMap;

import ch.uzh.ifi.climateapp.shared.ClimateData;

public class MapVisualization implements IVisualization{
	
	
	private int mapWidth = 700;
	private int mapHeight = 500;
	
	private DataTable dataTable;
	private GeoMap.Options options;
	private GeoMap geomap;
	
	private ClimateData[] climateData;
	
	public MapVisualization(){}
	
	public MapVisualization(int mapWidth, int mapHeight, ClimateData[] climateData){
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
		this.climateData = climateData;
	}
	
	@Override
	public Widget getVisualization(final VerticalPanel verticalPanel) {
		Runnable onLoadCallback = new Runnable(){

			@Override
			public void run() {
				
				dataTable = DataTable.create();
				dataTable.addColumn(ColumnType.STRING, "Country");
				dataTable.addColumn(ColumnType.NUMBER, "Temperature");
				//dataTable.addColumn(ColumnType.STRING, "Precise Temperature");
				
				dataTable.addRows(climateData.length);
				for (int i = 0; i < climateData.length; i++){
					dataTable.setValue(i, 0, climateData[i].getCountry());
					dataTable.setValue(i, 1, climateData[i].getTemperature());
					//dataTable.setValue(i, 2, "Chocolate" + climateData[i].getTemperature());
				}
				
				options = GeoMap.Options.create();
				options.setDataMode(GeoMap.DataMode.REGIONS);
				options.setRegion("world");
				options.setWidth(mapWidth);
				options.setHeight(mapHeight);
				options.setShowZoomOut(true);
				options.setShowLegend(true);
				geomap = new GeoMap(dataTable, options);
				
				verticalPanel.clear();
				verticalPanel.add(geomap);
				
				System.out.println("dataTable done");
				
			}
		};
		
		VisualizationUtils.loadVisualizationApi(onLoadCallback, GeoMap.PACKAGE);
		return verticalPanel;
	}

	@Override
	public void replaceData(ClimateData[] newData) {
		// TODO Auto-generated method stub
		this.climateData = newData;
	}

}
*/

/* ClimateApp Class

//  -------- Start Test Data for MAP --------- 
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
//  -------- End Test Data for MAP --------- 

//  -------- Start Map Visualization --------- 
map = new MapVisualization();
map.replaceData(data);

map.getVisualization(verticalPanel);
mainPanel.add(verticalPanel);
//  -------- Start Map Visualization --------- 

*/