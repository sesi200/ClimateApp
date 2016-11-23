package ch.uzh.ifi.climateapp.client;

import java.util.HashMap;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.Table;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;

import ch.uzh.ifi.climateapp.shared.ClimateData;

public class TableVisualization implements IVisualization {
	
	private ClimateData[] data;
	private DataTable dataTable;
	private Table table;
	private Table.Options options;
	private HashMap<String,Boolean> columnOptions = null;
	
	@Override
	public Widget getVisualization(final VerticalPanel verticalPanel) {
		Runnable onLoadCallback = new Runnable() {

			@Override
			public void run() {
				// Building 
				dataTable = DataTable.create();
				dataTable.addColumn(ColumnType.STRING, "Country");
				dataTable.addColumn(ColumnType.STRING, "City");
				dataTable.addColumn(ColumnType.NUMBER, "Temperature");
				dataTable.addColumn(ColumnType.NUMBER, "Temperature deviation");
				dataTable.addColumn(ColumnType.DATE, "Date");
				dataTable.addColumn(ColumnType.STRING, "Latitude");
				dataTable.addColumn(ColumnType.STRING, "Longitude");
				
				dataTable.addRows(data.length);
				for (int i = 0; i < data.length; i++){
					dataTable.setValue(i, 0, data[i].getCountry());
					dataTable.setValue(i, 2, data[i].getAverageTemperature());
					dataTable.setValue(i, 1, data[i].getCity());
					dataTable.setValue(i, 3, data[i].getAverageTemperatureUncertainty());
					dataTable.setValue(i, 4, data[i].getDt());
					dataTable.setValue(i, 5, data[i].getLatitude());
					dataTable.setValue(i, 6, data[i].getLongitude());
				}
				options = Table.Options.create();
				options.setPageSize(15);
				
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
				
				verticalPanel.clear();
				verticalPanel.add(table);
			}
			
		};
		
		VisualizationUtils.loadVisualizationApi(onLoadCallback, Table.PACKAGE);
		return verticalPanel;
	}

	@Override
	public void replaceData(ClimateData[] newData) {
		data = newData;
		
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

	/*
	 * @return current data
	 */
	public ClimateData[] getData() {
		return data;
	}

}
