package ch.uzh.ifi.climateapp.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.events.PageHandler;
import com.google.gwt.visualization.client.visualizations.Table;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;

import ch.uzh.ifi.climateapp.shared.ClimateData;

public class TableVisualization implements IVisualization {
	
	private ArrayList<ClimateData> data = new ArrayList<ClimateData>();
	private DataTable dataTable;
	private Table table;
	private Table.Options options;
	private HashMap<String,Boolean> columnOptions = null;
	private int currentPage;
	
	@Override
	public Widget getVisualization(final VerticalPanel verticalPanel) {
		Runnable onLoadCallback = new Runnable() {

			@Override
			public void run() {
				//remember user scroll position
				int topScroll = Window.getScrollTop();
				int leftScroll = Window.getScrollLeft();
				
				// Building 
				dataTable = DataTable.create();
				dataTable.addColumn(ColumnType.STRING, "Country");
				dataTable.addColumn(ColumnType.STRING, "City");
				dataTable.addColumn(ColumnType.NUMBER, "Temperature");
				dataTable.addColumn(ColumnType.NUMBER, "Temperature deviation");
				dataTable.addColumn(ColumnType.DATE, "Date");
				dataTable.addColumn(ColumnType.STRING, "Latitude");
				dataTable.addColumn(ColumnType.STRING, "Longitude");
				
				dataTable.addRows(data.size());
				int i = 0;
				for (ClimateData dataPoint : data){
					dataTable.setValue(i, 0, dataPoint.getCountry());
					dataTable.setValue(i, 2, dataPoint.getAverageTemperature());
					dataTable.setValue(i, 1, dataPoint.getCity());
					dataTable.setValue(i, 3, dataPoint.getAverageTemperatureUncertainty());
					dataTable.setValue(i, 4, dataPoint.getDt());
					dataTable.setValue(i, 5, dataPoint.getLatitude());
					dataTable.setValue(i, 6, dataPoint.getLongitude());
					++i;
				}
				
				options = Table.Options.create();
				options.setPageSize(50);
				options.setStartPage(currentPage);
				options.set("pagingButtons", 10d);
				
				//remove columns which should not be shown
				//perform right to left to not mess with the indices
				if (columnOptions!=null) {
					if(!columnOptions.get("Longitude")) {
						dataTable.removeColumn(6);
					}
					if(!columnOptions.get("Latitude")) {
						dataTable.removeColumn(5);
					}
					if(!columnOptions.get("Date")) {
						dataTable.removeColumn(4);
					}
					if(!columnOptions.get("Uncertainty")) {
						dataTable.removeColumn(3);
					}
					if(!columnOptions.get("Temperature")) {
						dataTable.removeColumn(2);
					}
					if(!columnOptions.get("City")) {
						dataTable.removeColumn(1);
					}
					if(!columnOptions.get("Country")) {
						dataTable.removeColumn(0);
					}
				}
				
				table = new Table(dataTable, options);
				table.addPageHandler(new PageHandler(){
					public void onPage(PageEvent event) {
						currentPage = event.getPage();
					}
				});
				
				verticalPanel.clear();
				verticalPanel.add(table);
				
				//return user to previous scroll position
				Window.scrollTo(leftScroll, topScroll);
			}
			
		};
		
		//draw table
		VisualizationUtils.loadVisualizationApi(onLoadCallback, Table.PACKAGE);
		
		return verticalPanel;
	}

	public void replaceData(ClimateData[] newData) {
		data.clear();
		data.addAll(Arrays.asList(newData));
		
	}
	
	public void addData(ClimateData[] add) {
		data.addAll(Arrays.asList(add));
	}
	
	public void setColumnOptions(HashMap<String,Boolean> options) {
		//only set correct options
		if(options.containsKey("Country")
				&&options.containsKey("City")
				&&options.containsKey("Temperature")
				&&options.containsKey("Uncertainty")
				&&options.containsKey("Date")
				&&options.containsKey("Latitude")
				&&options.containsKey("Longitude")){
			columnOptions = options;
		}
	}
	
	public ClimateData[] getData() {
		return (ClimateData[])data.toArray();
	}
	
	public void clearData() {
		data.clear();
		currentPage = 0;
	}

}
