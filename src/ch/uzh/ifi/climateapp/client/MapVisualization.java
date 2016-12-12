package ch.uzh.ifi.climateapp.client;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.GeoMap;

import ch.uzh.ifi.climateapp.shared.AverageData;

public class MapVisualization implements IVisualization{

	private DataTable dataTable;
	private GeoMap.Options options;
	private GeoMap geomap = null;

	private AverageData[] averageData;

	public MapVisualization(){}

	@Override
	public Widget getVisualization(final VerticalPanel verticalPanel) {
		Runnable onLoadCallback = new Runnable(){

			/**
			 * This method uses he gwt-visualization-1.1.2 libarary to visualize the climate data on to a map
			 * @return visualized map widget
			 */
			@Override
			public void run() {

				// Building 
				dataTable = DataTable.create();
				dataTable.addColumn(ColumnType.STRING, "Country");
				dataTable.addColumn(ColumnType.NUMBER, "Temperature");

				/** Alternative colorful presentation of the map with the fixed legend
				 * for all the visualizations
				 */
				dataTable.addRows(averageData.length + 2);
				dataTable.setValue(0, 0, "");
				dataTable.setValue(0, 1, -7);
				
				for (int i = 1; i < averageData.length; i++){
					dataTable.setValue(i, 0, averageData[i].getCountry());
					dataTable.setValue(i, 1, averageData[i].getAvgTemp());
				}
				
				dataTable.setValue(averageData.length + 1, 0, "");
				dataTable.setValue(averageData.length + 1, 1, 32);

				if(verticalPanel.getWidgetCount()==0) {
					if (geomap == null) {
						options = GeoMap.Options.create();
						options.setDataMode(GeoMap.DataMode.REGIONS);
						options.setRegion("world");
						options.setWidth(1200);
						options.setHeight(750);
						options.setColors(0xCCFFFF,0xCCFFCC, 0xFFFF99, 0xFFCC99,0xFF9999,0xFF0000);
						options.setShowLegend(true);
						geomap = new GeoMap(dataTable, options);
					}
					verticalPanel.add(geomap);
				}
				geomap.draw(dataTable,options);

			}
		};

		VisualizationUtils.loadVisualizationApi(onLoadCallback, GeoMap.PACKAGE);
		return verticalPanel;
	}

	/**
	 * Removes the current climate data from the map and adds the new to be visualized climate data to the
	 * map visualization object. To get he new visualization the method getVisualization(VerticalPanel verticalPanel)
	 * must be called
	 * 
	 * @return void
	 */
	public void replaceData(AverageData[] newData) {
		this.averageData = newData;
	}

}


