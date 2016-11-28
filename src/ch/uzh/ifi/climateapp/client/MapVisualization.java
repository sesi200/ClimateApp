package ch.uzh.ifi.climateapp.client;

import java.util.List;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.GeoMap;

import ch.uzh.ifi.climateapp.shared.AverageData;
import ch.uzh.ifi.climateapp.shared.ClimateData;

public class MapVisualization implements IVisualization{
	
	
	private int mapWidth = 900;
	private int mapHeight = 500;
	
	private DataTable dataTable;
	private GeoMap.Options options;
	private GeoMap geomap;
	
	private ClimateData[] climateData;
	private List<AverageData> averageForYear;
	
	
	public MapVisualization(){}
	
	

	public Widget getVisualization(final VerticalPanel verticalPanel, List<AverageData> averageForYear) {
		this.averageForYear = averageForYear;
		
		Runnable onLoadCallback = new Runnable(){
			
			
			/**
			 * This method uses he gwt-visualization-1.1.2 libarary to visualize the climate data on to a map
			 * 
			 * @return visualized map widget
			 */
	
	
			@Override
			public void run() {
				// Building 
				dataTable = DataTable.create();
				dataTable.addColumn(ColumnType.STRING, "Country");
				dataTable.addColumn(ColumnType.NUMBER, "Temperature");
				dataTable.addRows(averageForYear.size());
				
				
				for (int i = 0; i < averageForYear.size(); i++){
					dataTable.setValue(i, 0, averageForYear.get(i).getCountry());
					int avg = (int) Math.round(averageForYear.get(i).getAvgTemp());
					dataTable.setValue(i, 1, avg);
				}
				
				options = GeoMap.Options.create();
				options.setDataMode(GeoMap.DataMode.REGIONS);
				options.setRegion("world");
				options.setWidth(mapWidth);
				options.setHeight(mapHeight);
				options.setShowLegend(true);
				geomap = new GeoMap(dataTable, options);
				
				verticalPanel.clear();
				verticalPanel.add(geomap);
				
			}
		};
		
		VisualizationUtils.loadVisualizationApi(onLoadCallback, GeoMap.PACKAGE);
		return verticalPanel;
	}
	
	
	
	
	
	public void replaceAvgData(List<AverageData> averageForYear) {
		this.averageForYear = averageForYear;
	}
	
	public List<AverageData> getDataAvg(){
		return this.averageForYear;
	}
	
	public MapVisualization(int mapWidth, int mapHeight, List<AverageData> averageForYear){
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
		this.averageForYear = averageForYear;
	}
	
	
	/** Depricated Method to get Map Visualization*/
	
	
	public MapVisualization(int mapWidth, int mapHeight, ClimateData[] climateData){
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
		this.climateData = climateData;
	}
	
	@Override
	public Widget getVisualization(final VerticalPanel verticalPanel) {
		Runnable onLoadCallback = new Runnable(){
			
			
			/**
			 * This method uses he gwt-visualization-1.1.2 libarary to visualize the climate data on to a map
			 * 
			 * @return visualized map widget
			 */
			@Override
			public void run() {
				
				// Building 
				dataTable = DataTable.create();
				dataTable.addColumn(ColumnType.STRING, "Country");
				dataTable.addColumn(ColumnType.NUMBER, "Temperature");
				
				dataTable.addRows(climateData.length);
				for (int i = 0; i < climateData.length; i++){
					dataTable.setValue(i, 0, climateData[i].getCountry());
					dataTable.setValue(i, 1, climateData[i].getAverageTemperature());
				}
				
				options = GeoMap.Options.create();
				options.setDataMode(GeoMap.DataMode.REGIONS);
				options.setRegion("world");
				options.setWidth(mapWidth);
				options.setHeight(mapHeight);
				options.setShowLegend(true);
				geomap = new GeoMap(dataTable, options);
				
				verticalPanel.clear();
				verticalPanel.add(geomap);
				
			}
		};
		
		VisualizationUtils.loadVisualizationApi(onLoadCallback, GeoMap.PACKAGE);
		return verticalPanel;
	}

	/**
	 * Removes the current climate data from the map and adds the new to be visualized climate data to the
	 * mapvisualization object. To get he new visualization the method getVisualization(VerticalPanel verticalPanel)
	 * must be called
	 * 
	 * @return void
	 */

	public void replaceData(ClimateData[] newData) {
		this.climateData = newData;
	}
	
	public ClimateData[] getData(){
		return this.climateData;
	}
	
	


}


