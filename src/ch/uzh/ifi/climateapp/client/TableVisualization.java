package ch.uzh.ifi.climateapp.client;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.FlexTable;
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
				
				dataTable.addRows(data.length);
				for (int i = 0; i < data.length; i++){
					dataTable.setValue(i, 0, data[i].getCountry());
					dataTable.setValue(i, 2, data[i].getAverageTemperature());
					dataTable.setValue(i, 1, data[i].getCity());
				}
				options = Table.Options.create();
				options.setHeight("400");
				options.setWidth("900");
				
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

}
