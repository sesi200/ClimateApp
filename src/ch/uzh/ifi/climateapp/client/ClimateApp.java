package ch.uzh.ifi.climateapp.client;

import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
//import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
//import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.visualizations.GeoMap;

import ch.uzh.ifi.climateapp.shared.ClimateData;
import ch.uzh.ifi.climateapp.shared.Filter;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ClimateApp implements EntryPoint {
	
	private static final double STARTING_MAX_UNCERTAINTY = 0.3;
	private static final double STARTING_MIN_UNCERTAINTY = 0.0;
	
	private VerticalPanel verticalMapPanel = new VerticalPanel();
	private VerticalPanel verticalTablePanel = new VerticalPanel();
    private VerticalPanel mainPanel;
	private MapVisualization map = new MapVisualization();
	private TableVisualization table = new TableVisualization();
	private DataFetcherServiceAsync dataFetcherService = GWT.create(DataFetcherService.class);
	private ArrayList<Filter> filters = new ArrayList<Filter>();

	//needed class-wide textboxes to add filter values
	TextBox uncertaintyFrom;
	TextBox uncertaintyTo;
	SuggestBox countryName;
	SuggestBox cityName;

	private DataTable dataTable;
	private GeoMap.Options options;
	private GeoMap geomap;
	private int mapWidth = 500;
	private int mapHeight = 500;



	@Override
	public void onModuleLoad() {
		buildUI();
		
		//populate table
		Filter f = new Filter();
		f.setMaxDeviation(STARTING_MAX_UNCERTAINTY);
		filters.add(f);
		reloadTable();
        
        
		/*  -------- Start Test Data for MAP --------- */
		ClimateData d1 = new ClimateData();
		d1.setCountry("US");
		d1.setCity("Atlanta");
		d1.setAverageTemperature(-100);
		ClimateData d2 = new ClimateData();
		d2.setCountry("India");
		d2.setCity("New Delhi");
		d2.setAverageTemperature(30);
		ClimateData d3 = new ClimateData();
		d3.setCountry("Germany");
		d3.setCity("Munich");
		d3.setAverageTemperature(0);
		ClimateData d4 = new ClimateData();
		d4.setCountry("GB");
		d4.setCity("Stonehenge");
		d4.setAverageTemperature(14);


		ClimateData [] dataOne = new ClimateData[4];
		dataOne[0] = d1;
		dataOne[1] = d2;
		dataOne[2] = d3;
		dataOne[3] = d4;

		ClimateData d5 = new ClimateData();
		d5.setCountry("France");
		d5.setAverageTemperature(15);
		ClimateData d6 = new ClimateData();
		d6.setCountry("Spain");
		d6.setAverageTemperature(20);
		ClimateData d7 = new ClimateData();
		d7.setCountry("Greece");
		d7.setAverageTemperature(0);
		ClimateData d8 = new ClimateData();
		d8.setCountry("Poland");
		d8.setAverageTemperature(30);

		ClimateData [] dataTwo = new ClimateData[4];
		dataTwo[0] = d5;
		dataTwo[1] = d6;
		dataTwo[2] = d7;
		dataTwo[3] = d8;

		/*  -------- End Test Data for MAP --------- */





		/*  -------- Start Map Visualization --------- */
		map = new MapVisualization();
		map.replaceData(dataOne);
		
		map.getVisualization(verticalMapPanel);
		/*  -------- End Map Visualization --------- */

	}


	private void buildUI(){

		/*Create the main panel of the UI */
		mainPanel = new VerticalPanel();
		mainPanel.setWidth("100%");

		//Create the tab panel which is contained in the main vertical panel
		TabPanel tabPanel = new TabPanel();
		mainPanel.add(tabPanel);

		//Create two tabs of the tab panel to switch between the map/table view 
		VerticalPanel mapViewLayout = new VerticalPanel();
		VerticalPanel tableViewLayout = new VerticalPanel();

		tabPanel.add(mapViewLayout, "Map View");
		tabPanel.add(tableViewLayout, "Table View");
		tabPanel.setWidth("100%");

		//use the first tab as default 
		tabPanel.selectTab(0);

		/** Creating the map view panel 
		 * 
		 *  */
		
		Label mapTimelineLabel = new Label("here appears the timeline filter from 1849 to 2013, say in sprint 2 ;)");
		mapTimelineLabel.setStyleName("warningLabel");
		mapViewLayout.add(mapTimelineLabel);

		Label mapLabel = new Label("Climate Data Map");
		mapLabel.setStyleName("titleLabel");
		mapViewLayout.add(mapLabel);


		HorizontalPanel viewMap = new HorizontalPanel();
		viewMap.add(verticalMapPanel);
		viewMap.setSpacing(30);

		Button exportPNG = new Button("Export as PNG");
		viewMap.add(exportPNG);

		mapViewLayout.add(viewMap);

		/**
		 * Create horizontal panel to customize the table 
		 * (to select which attributes will be shown in the table)
		 * 
		 * */  

		HorizontalPanel customizePanel = new HorizontalPanel();
		customizePanel.setStyleName("paddedHorizontalPanel");
		customizePanel.setSpacing(25);

		//Create check-box widgets to select attributes
		CheckBox showCountry = new CheckBox("show country");
		CheckBox showCity = new CheckBox("show city");
		CheckBox showDate = new CheckBox("show date");
		CheckBox showTemperature = new CheckBox("show temperature");
		CheckBox showUncertainty = new CheckBox("show uncertainty");
		CheckBox showLongitude= new CheckBox("show longitude");
		CheckBox showLatitude = new CheckBox("show latitude");
		CheckBox showAvg = new CheckBox("show average");
		CheckBox showMax = new CheckBox("show maximum");
		CheckBox showMin = new CheckBox("show minimum");

		//add check-box widgets to the customize panel
		customizePanel.add(showCountry);
		customizePanel.add(showCity);
		customizePanel.add(showDate);
		customizePanel.add(showTemperature);
		customizePanel.add(showUncertainty);
		customizePanel.add(showLongitude);
		customizePanel.add(showLatitude);
		customizePanel.add(showAvg); 
		customizePanel.add(showMax); 
		customizePanel.add(showMin); 

		//set default values for each attribute
		showCountry.setValue(true); 
		showCity.setValue(true); 
		showDate.setValue(true); 
		showTemperature.setValue(true); 
		showUncertainty.setValue(true); 
		showLongitude.setValue(true);
		showLatitude.setValue(true);
		showAvg.setValue(false);
		showMax.setValue(false);
		showMin.setValue(false);


		/*Create horizontal panel for the filter options (filters for location, temperature and precision)*/

		HorizontalPanel filterPanel = new HorizontalPanel();
		filterPanel.setStyleName("paddedHorizontalPanel");
		filterPanel.setSpacing(25);

		// Create vertical panel for the city and country filters

		VerticalPanel locationFilter = new VerticalPanel();

		//Create country filter panel

		HorizontalPanel countryFilter = new HorizontalPanel();

		Label countryLabel = new Label("Select country");
		countryLabel.setWidth("100px");
		countryLabel.setStyleName("filterLabel");

		countryName = new SuggestBox();
		Button addCountryButton = new Button("Add");
		addCountryButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event){
				addCountryNameFilter();
			}
		});

		// Assemble countryFilter panel
		countryFilter.add(countryLabel);
		countryFilter.add(countryName);
		countryFilter.add(addCountryButton);

		// Create city filter
		HorizontalPanel cityFilter = new HorizontalPanel();

		Label cityLabel = new Label("Select city");
		cityLabel.setWidth("100px");
		cityLabel.setStyleName("filterLabel");

		cityName = new SuggestBox();
		Button addCityButton = new Button("Add");
		addCityButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event){
				addCityNameFilter();
			}
		});

		// Assemble cityFilter panel
		cityFilter.add(cityLabel);
		cityFilter.add(cityName);
		cityFilter.add(addCityButton);


		//Assemble country filter panel
		locationFilter.add(countryFilter);
		locationFilter.add(cityFilter);


		// Create year range filter
		VerticalPanel yearRangeFilter = new VerticalPanel();

		HorizontalPanel yearFromFilter = new HorizontalPanel();

		Label yearFromLabel = new Label("Year from:");
		yearFromLabel.setStyleName("filterLabel");
		yearFromLabel.setWidth("100px");

		TextBox yearFrom = new TextBox();
		Button addYearFromButton = new Button("Add");

		yearFromFilter.add(yearFromLabel);
		yearFromFilter.add(yearFrom);
		yearFromFilter.add(addYearFromButton);

		HorizontalPanel yearToFilter = new HorizontalPanel();

		Label yearToLabel = new Label("Year to:");
		yearToLabel.setStyleName("filterLabel");
		yearToLabel.setWidth("100px");

		TextBox yearTo = new TextBox();
		Button addYearToButton = new Button("Add");

		yearToFilter.add(yearToLabel);
		yearToFilter.add(yearTo);
		yearToFilter.add(addYearToButton);

		// Assemble year range filter panel
		yearRangeFilter.add(yearFromFilter);
		yearRangeFilter.add(yearToFilter);


		// Create uncertainty filter
		VerticalPanel uncertaintyFilter = new VerticalPanel();

		HorizontalPanel uncertaintyFromFilter = new HorizontalPanel();

		Label uncertaintyFromLabel = new Label("Uncertainty from:");
		uncertaintyFromLabel.setStyleName("filterLabel");
		uncertaintyFromLabel.setWidth("120px");

		uncertaintyFrom = new TextBox();
		uncertaintyFrom.setText(Double.toString(STARTING_MIN_UNCERTAINTY));
		Button addUncertaintyFromButton = new Button("Add");
		addUncertaintyFromButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event){
				addUncertaintyFromFilter();
			}
		});

		uncertaintyFromFilter.add(uncertaintyFromLabel);
		uncertaintyFromFilter.add(uncertaintyFrom);
		uncertaintyFromFilter.add(addUncertaintyFromButton);

		HorizontalPanel uncerataintyToFilter = new HorizontalPanel();

		Label uncertaintyToLabel = new Label("Uncertainty to:");
		uncertaintyToLabel.setStyleName("filterLabel");
		uncertaintyToLabel.setWidth("120px");

		uncertaintyTo = new TextBox();
		uncertaintyTo.setValue(Double.toString(STARTING_MAX_UNCERTAINTY));
		Button addUncertaintyToButton = new Button("Add");
		addUncertaintyToButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event){
				addUncertaintyToFilter();
			}
		});



		uncerataintyToFilter.add(uncertaintyToLabel);
		uncerataintyToFilter.add(uncertaintyTo);
		uncerataintyToFilter.add(addUncertaintyToButton);

		// Assemble uncertainty filter panel
		uncertaintyFilter.add(uncertaintyFromFilter);
		uncertaintyFilter.add(uncerataintyToFilter);

		//Creates the button that resets the filters
		Button resetFilterButton = new Button("Reset filter");
		
		//Assemble filter panel
		filterPanel.add(locationFilter);
		filterPanel.add(yearRangeFilter);
		filterPanel.add(uncertaintyFilter);
		filterPanel.add(resetFilterButton);





		/* To do:

		 * 
		 * add check up for valid entries in text box widgets
		 * 
		 * add suggestions of the country and city names to suggest box widgets (use MultiWordSuggestOracle?)
		 * 
		 * add slider container and create slider widget (look up in FilmVisualizer)
		 *		
		 * add event handler  (for example (check box ----> flex table changes, key events in text boxes or suggest boxes ---> setting filter), etc.
		 * 
		 * */

		
		/**
		 * 
		 * Create horizontal panel to place the CellTable with climate data into it
		 * 
		 * */

		Label tableLabel = new Label("Climate Data Table");
		tableLabel.setStyleName("titleLabel");
		tableViewLayout.add(tableLabel);
	
		HorizontalPanel tableView = new HorizontalPanel();
			
		Button exportCSV = new Button("Export as CSV");
		exportCSV.setWidth("120px");
		
		tableView.add(verticalTablePanel);
		tableView.add(exportCSV);


		tableViewLayout.add(tableView);
		
		
		/**
		 * Assemble the whole table view panel 
		 * 
		 * */

		Label customizeLabel = new Label("Customize table ");
		customizeLabel.setStyleName("panelLabel");
		tableViewLayout.add(customizeLabel);
		tableViewLayout.add(customizePanel);

		Label filterLabel = new Label("Filter data ");
		filterLabel.setStyleName("panelLabel");
		tableViewLayout.add(filterLabel);
		tableViewLayout.add(filterPanel);
		
		Label sliderLabel = new Label("Set the range of years");
		sliderLabel.setStyleName("panelLabel");
		tableViewLayout.add(sliderLabel);
		
		
		tableViewLayout.add(tableView);

		/* To do:
		 * 
		 * generate sample data to add to the table
		 * 
		 * */


		/* add the source of data to the main panel*/

		//Create label to show the data source
		Label dataSourceLabel = new Label("Source of raw data: Berkeley Earth");
		dataSourceLabel.setStyleName("sourceLabel");

		//Create an anchor to show the link to the external source
		Anchor sourceAnchor = new Anchor("http://www.berkeleyearth.org", "http://www.berkeleyearth.org");
		sourceAnchor.setStyleName("sourceLabel");

		//Create label to show the last update of the data source
		Label updateSourceLabel = new Label("Last data update: 01.08.2013"); //do we have to to mention last date of measurement or last upload of the csv file?
		updateSourceLabel.setStyleName("sourceLabel");

		//Create vertical panel to show the data source and the link one over another and add the label and the anchor to it 
		VerticalPanel sourcePanel = new VerticalPanel();
		sourcePanel.add(dataSourceLabel);
		sourcePanel.add(sourceAnchor);
		sourcePanel.add(updateSourceLabel);

		//Add source panel to the main panel
		mainPanel.add(sourcePanel);

		/*attach the main panel to the root panel*/
		RootPanel.get().add(mainPanel);

	}

	/**
	 * adds a new Filter to filters with minDeviation set to value read from textbox
	 * does not work when there are some non-alphanumeric symbols in the textbox
	 */
	private void addUncertaintyFromFilter() {
		if (uncertaintyFrom.getText()!=null) {
			Filter newFilter;
			if (filters.size()==0) {
				newFilter = new Filter();
				newFilter.setMinDeviation(Double.parseDouble(uncertaintyFrom.getText()));
				filters.add(newFilter);
			} else {
				newFilter = filters.get(0);
				newFilter.setMinDeviation(Double.parseDouble(uncertaintyFrom.getText()));
			}
			
			reloadTable();
		}
	}

	/**
	 * adds a new Filter to filters with maxDeviation set to value read from textbox
	 * does not work when there are some non-alphanumeric symbols in the textbox
	 */
	private void addUncertaintyToFilter() {
		if (uncertaintyTo.getText()!=null) {
			Filter newFilter;
			if (filters.size()==0) {
				newFilter = new Filter();
				newFilter.setMaxDeviation(Double.parseDouble(uncertaintyTo.getText()));
				filters.add(newFilter);
			} else {
				newFilter = filters.get(0);
				newFilter.setMaxDeviation(Double.parseDouble(uncertaintyTo.getText()));
			}
			
			reloadTable();
		}
	}

	/**
	 * adds a new Filter to filters with country set to value read from textbox
	 */
	private void addCountryNameFilter() {
		if (countryName.getText()!=null) {
			Filter newFilter = new Filter();
			newFilter.setCountry(countryName.getText());
			filters.add(newFilter);
			reloadTable();
		}
	}

	/**
	 * adds a new Filter to filters with city set to value read from textbox
	 */
	private void addCityNameFilter() {
		if (cityName.getText()!=null) {
			Filter newFilter = new Filter();
			newFilter.setCity(cityName.getText());
			filters.add(newFilter);
			reloadTable();
		}
	}
	
	/**
	 * reloads the table view with currently set filters
	 */
	private void reloadTable() {
		if (dataFetcherService==null)
			dataFetcherService = GWT.create(DataFetcherService.class);

		//set up callback object
		AsyncCallback<ClimateData[]> callback = new AsyncCallback<ClimateData[]>() {

			@Override
			public void onFailure(Throwable caught) {}

			@Override
			public void onSuccess(ClimateData[] result) {
				table.replaceData(result);
				table.getVisualization(verticalTablePanel);
			}

		};
		
		dataFetcherService.getClimateData(filters.toArray(new Filter[0]), callback);
	}

}
