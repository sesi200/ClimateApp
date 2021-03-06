package ch.uzh.ifi.climateapp.client;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import ch.uzh.ifi.climateapp.shared.AverageData;
import ch.uzh.ifi.climateapp.shared.ClimateData;
import ch.uzh.ifi.climateapp.shared.Filter;
import ch.uzh.ifi.climateapp.shared.FilterField;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.SliderBar;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ClimateApp implements EntryPoint {

	private static final long STARTING_MAX_UNCERTAINTY = 4l;
	private static final long STARTING_MIN_UNCERTAINTY = 0l;
	private static final int STARTING_YEAR = 2013;

	int firstDataYear = 1743;
	int lastDataYear = 2013;

	SliderBar sliderBar;
	VerticalPanel verticalPanelSlider;
	VerticalPanel verticalPanel;
	private VerticalPanel verticalMapPanel = new VerticalPanel();
	private VerticalPanel verticalTablePanel = new VerticalPanel();
	private VerticalPanel mainPanel;
	private MapVisualization map = new MapVisualization();
	private TableVisualization table = new TableVisualization();
	private int currentBatch;
	private DataFetcherServiceAsync dataFetcherService = GWT.create(DataFetcherService.class);
	private AverageYearServiceAsync averageService = GWT.create(AverageYearService.class);
	private ArrayList<Filter> filters = new ArrayList<Filter>(); /*filters[1+] is for cities and countries*/
	private FlexTable currentFilterDisplay = new FlexTable();
	private MultiWordSuggestOracle countriesOracle = new MultiWordSuggestOracle(); 
	private MultiWordSuggestOracle citiesOracle = new MultiWordSuggestOracle(); 
	private HorizontalPanel currentMapYearDisplay = new HorizontalPanel();
	private YearBox currentMapYearBox = new YearBox();

	//needed class-wide text boxes to add filter values
	TextBox uncertaintyFrom;
	TextBox uncertaintyTo;
	YearBox yearFrom;
	YearBox yearTo;
	SuggestBox countryName = new SuggestBox(countriesOracle);
	SuggestBox cityName = new SuggestBox(citiesOracle);

	//needed class-wide check boxes to use for filtering
	CheckBox showCountry = new CheckBox("show country");
	CheckBox showCity = new CheckBox("show city");
	CheckBox showDate = new CheckBox("show date");
	CheckBox showTemperature = new CheckBox("show temperature");
	CheckBox showUncertainty = new CheckBox("show uncertainty");
	CheckBox showLongitude= new CheckBox("show longitude");
	CheckBox showLatitude = new CheckBox("show latitude");


	@Override
	public void onModuleLoad() {
		//populate table
		setFilterToDefault();
		updateCurrentFilterDisplay();
		reloadTable();

		buildUI();
		populateSuggestBoxes();
	}

	/**
	 * resets all filters to their default values and sets the checkboxes and texboxes to their original states
	 */
	private void setFilterToDefault() {
		filters.clear();
		Filter f = new Filter();
		f.setMaxDeviation(STARTING_MAX_UNCERTAINTY);
		f.setMinDeviation(STARTING_MIN_UNCERTAINTY);
		f.setStartYear(STARTING_YEAR);
		f.setEndYear(STARTING_YEAR);
		filters.add(f);

		//set default values for each check box
		showCountry.setValue(true); 
		showCity.setValue(true); 
		showDate.setValue(true); 
		showTemperature.setValue(true); 
		showUncertainty.setValue(true); 
		showLongitude.setValue(true);
		showLatitude.setValue(true);
	}

	/**
	 * creates the UI of the application and adds it to the root panel
	 */
	private void buildUI(){

		/**
		 * Create the main panel of the UI 
		 * 
		 * */
		mainPanel = new VerticalPanel();
		mainPanel.setWidth("100%");
		/*attach the main panel to the root panel*/
		RootPanel.get().add(mainPanel);

		/**
		 * Create the tab panels of UI
		 * 
		 */
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

		//Creating MapView Layout
		mapViewLayout = createMapViewLayout(mapViewLayout);

		//Creating TableViewLayout
		tableViewLayout = createTableViewLayout(tableViewLayout);


		/*
		 *  Create Source Label
		 */
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

	}



	/** Creating the map view panel. Takes a vertical panel as input and adds all the panels and widget for the map view to it.
	 * @param VerticalPanel mapViewLayout
	 * @return VerticalPanel mapViewLayout
	 *  */
	private VerticalPanel createMapViewLayout(VerticalPanel mapViewLayout){
		mapViewLayout.add(getSlider());
		VerticalPanel viewMap = new VerticalPanel();
		verticalMapPanel.setWidth("1200px");
		Label currentYearLabel = new Label("Average temperatures for the year");
		currentYearLabel.addStyleName("mapLabel");
		currentMapYearDisplay.add(currentYearLabel);
		currentMapYearBox.setStyleName("mapYear");
		currentMapYearBox.addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode()==KeyCodes.KEY_ENTER) {
					sliderBar.setCurrentValue(currentMapYearBox.getValue());
					reloadMapForYear(currentMapYearBox.getValue());
				}
			}
		});
		currentMapYearDisplay.add(currentMapYearBox);
		viewMap.add(currentMapYearDisplay);
		viewMap.add(verticalMapPanel);
		mapViewLayout.add(viewMap);
		return mapViewLayout;
	}

	private Widget getSlider(){

		verticalPanelSlider = new VerticalPanel();
		sliderBar = new SliderBar(firstDataYear, lastDataYear);
		sliderBar.setStepSize(1);
		sliderBar.setCurrentValue(STARTING_YEAR);
		sliderBar.setNumTicks(lastDataYear-firstDataYear);
		sliderBar.setNumLabels(27);

		verticalPanelSlider.add(sliderBar);
		sliderBar.setVisible(true);
		sliderBar.setHeight("52px");
		sliderBar.setWidth("1200px");

		sliderBar.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				reloadMapForYear((int)sliderBar.getCurrentValue());
			}
		});
		reloadMapForYear((int)sliderBar.getCurrentValue());
		return verticalPanelSlider;
	}

	private void reloadMapForYear(int year) {
		currentMapYearBox.setValue(year);

		averageService.getAverageForYear(year, new AsyncCallback<AverageData[]>() {

			@Override
			public void onSuccess(AverageData[] result) {
				map.replaceData(result);
				map.getVisualization(verticalMapPanel);
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("map RPC failed");
			}
		});

	}

	private VerticalPanel createTableViewLayout(VerticalPanel tableViewLayout){

		/**
		 * Create Horizontal Customize Table 
		 */
		HorizontalPanel customizePanel = new HorizontalPanel();
		customizePanel.setStyleName("paddedHorizontalPanel");
		customizePanel.setSpacing(25);


		//add check-box listeners
		ClickHandler checkboxClickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				table.setColumnOptions(getTableColumnShowValues());
				table.getVisualization(verticalTablePanel);
			}
		};
		showCountry.addClickHandler(checkboxClickHandler);
		showCity.addClickHandler(checkboxClickHandler);
		showDate.addClickHandler(checkboxClickHandler);
		showTemperature.addClickHandler(checkboxClickHandler);
		showUncertainty.addClickHandler(checkboxClickHandler);
		showLatitude.addClickHandler(checkboxClickHandler);
		showLongitude.addClickHandler(checkboxClickHandler);

		//add check-box widgets to the customize panel
		customizePanel.add(showCountry);
		customizePanel.add(showCity);
		customizePanel.add(showTemperature);
		customizePanel.add(showUncertainty);
		customizePanel.add(showDate);
		customizePanel.add(showLatitude);
		customizePanel.add(showLongitude);

		/*
		 * Create Filter Data Table
		 */
		/*Create horizontal panel for the filter options (filters for location, temperature and precision)*/
		HorizontalPanel filterPanel = new HorizontalPanel();
		filterPanel.setStyleName("paddedHorizontalPanel");
		filterPanel.setSpacing(25);

		// Create vertical panel for the city and country filters

		VerticalPanel locationFilter = new VerticalPanel();

		HorizontalPanel countryFilter = createSuggestFilterPanel(countryName, "country", FilterField.COUNTRY);
		HorizontalPanel cityFilter = createSuggestFilterPanel(cityName, "city", FilterField.CITY);

		//Assemble country filter panel
		locationFilter.add(countryFilter);
		locationFilter.add(cityFilter);

		// Create year range filter
		VerticalPanel yearRangeFilter = new VerticalPanel();

		HorizontalPanel yearFromFilter = new HorizontalPanel();

		Label yearFromLabel = new Label("Year from:");
		yearFromLabel.setStyleName("filterLabel");
		yearFromLabel.setWidth("100px");

		yearFrom = new YearBox();
		yearFrom.setValue(STARTING_YEAR);
		yearFrom.setStyleName("gwt-TextBox");
		yearFrom.addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if(event.getNativeKeyCode()==KeyCodes.KEY_ENTER){
					addYearFromFilter();
				}
			}
		});
		Button addYearFromButton = new Button("Add");
		addYearFromButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event){
				addYearFromFilter();
			}
		});

		yearFromFilter.add(yearFromLabel);
		yearFromFilter.add(yearFrom);
		yearFromFilter.add(addYearFromButton);

		HorizontalPanel yearToFilter = new HorizontalPanel();

		Label yearToLabel = new Label("Year to:");
		yearToLabel.setStyleName("filterLabel");
		yearToLabel.setWidth("100px");

		yearTo = new YearBox();
		yearTo.setValue(STARTING_YEAR);
		yearTo.setStyleName("gwt-TextBox");
		yearTo.addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if(event.getNativeKeyCode()==KeyCodes.KEY_ENTER){
					addYearToFilter();
				}
			}
		});
		Button addYearToButton = new Button("Add");
		addYearToButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event){
				addYearToFilter();
			}
		});

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
		uncertaintyFrom.setText(""+STARTING_MIN_UNCERTAINTY);
		uncertaintyFrom.addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if(event.getNativeKeyCode()==KeyCodes.KEY_ENTER){
					addUncertaintyFromFilter();
				}
			}
		});
		Button addUncertaintyFromButton = new Button("Add");
		addUncertaintyFromButton.addClickHandler(new ClickHandler() {
			@Override
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
		uncertaintyTo.setText(""+STARTING_MAX_UNCERTAINTY);
		uncertaintyTo.addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if(event.getNativeKeyCode()==KeyCodes.KEY_ENTER){
					addUncertaintyToFilter();
				}
			}
		});
		Button addUncertaintyToButton = new Button("Add");
		addUncertaintyToButton.addClickHandler(new ClickHandler() {
			@Override
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
		resetFilterButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				setFilterToDefault();
				updateCurrentFilterDisplay();
				reloadTable();
				yearFrom.setValue(STARTING_YEAR);
				yearTo.setValue(STARTING_YEAR);
				uncertaintyFrom.setText(""+STARTING_MIN_UNCERTAINTY);
				uncertaintyTo.setText(""+STARTING_MAX_UNCERTAINTY);
			}
		});

		//Assemble filter panel
		filterPanel.add(locationFilter);
		filterPanel.add(yearRangeFilter);
		filterPanel.add(uncertaintyFilter);
		filterPanel.add(resetFilterButton);

		/**
		 * 
		 * Create horizontal panel to place the CellTable with climate data into it
		 * 
		 * */
		HorizontalPanel tableView = new HorizontalPanel();

		tableView.add(verticalTablePanel);
		verticalTablePanel.setSpacing(30);
		tableView.add(currentFilterDisplay);


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

		tableViewLayout.add(tableView);


		return tableViewLayout;
	}

	/**
	 * @param suggestBox the variable that becomes the new SuggestBox
	 * @param labelText The SuggestBox will be labelled "Select labelText"
	 * @return HorizontalPanel that contains the SuggestBox and the label 
	 */
	private HorizontalPanel createSuggestFilterPanel(final SuggestBox suggestBox, String labelText, 
													 final FilterField field) {
		HorizontalPanel horizBox = new HorizontalPanel();

		Label label = new Label("Select " + labelText);
		label.setWidth("100px");
		label.setStyleName("filterLabel");

		suggestBox.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if(event.getNativeKeyCode()==KeyCodes.KEY_ENTER){
					addFilter(field, suggestBox.getText());
					suggestBox.setValue("");
				}
			}
		});
		Button submitButton = new Button("Add");
		submitButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event){
				addFilter(field, suggestBox.getText());
				suggestBox.setValue("");
			}
		});

		// Assemble countryFilter panel
		horizBox.add(label);
		horizBox.add(suggestBox);
		horizBox.add(submitButton);
		return horizBox;
	}

	/**
	 * adds a new Filter to filters with minDeviation set to value read from textbox
	 * does not work when there are some non-alphanumeric symbols in the textbox
	 */
	private void addUncertaintyFromFilter() {
		if (Double.parseDouble(uncertaintyFrom.getText()) > Double.parseDouble(uncertaintyTo.getText())){
			Window.alert("\"Uncertainty from\" must be smaller or equal \"Uncertainty to\"");
			return;
		}
		
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
			updateCurrentFilterDisplay();
			reloadTable();
		}
	}

	/**
	 * adds a new Filter to filters with maxDeviation set to value read from textbox
	 * does not work when there are some non-alphanumeric symbols in the textbox
	 */
	private void addUncertaintyToFilter() {
		if (Double.parseDouble(uncertaintyFrom.getText()) > Double.parseDouble(uncertaintyTo.getText())){
			Window.alert("\"Uncertainty to\" must be greater or equal \"Uncertainty from\"");
			return;
		}
		
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
			updateCurrentFilterDisplay();
			reloadTable();
		}
	}

	/**
	 * adds a new Filter to filters with endYear set to value read from textbox
	 * does not work when there are some non-alphanumeric symbols in the textbox
	 */
	private void addYearToFilter() {
		if (Integer.parseInt(yearFrom.getText()) > Integer.parseInt(yearTo.getText())){
			Window.alert("\"Year to\" must be greater or equal \"Year from\" \n year to >= year from");
			return;
		}
		
		if (yearTo.getText()!=null) {
			Filter newFilter;
			if (filters.size()==0) {
				newFilter = new Filter();
				newFilter.setEndYear(Integer.parseInt(yearTo.getText()));
				filters.add(newFilter);
			} else {
				newFilter = filters.get(0);
				newFilter.setEndYear(Integer.parseInt(yearTo.getText()));
			}
			updateCurrentFilterDisplay();
			reloadTable();
		}
	}

	/**
	 * adds a new Filter to filters with startYear set to value read from textbox
	 * does not work when there are some non-alphanumeric symbols in the textbox
	 */
	private void addYearFromFilter() {
		if (Integer.parseInt(yearFrom.getText()) > Integer.parseInt(yearTo.getText())){
			Window.alert("\"Year from\" must be smaller or equal \"Year to\" \n year from <= year to");
			return;
		}
		
		if (yearFrom.getText()!=null) {
			Filter newFilter;
			if (filters.size()==0) {
				newFilter = new Filter();
				newFilter.setStartYear(Integer.parseInt(yearFrom.getText()));
				filters.add(newFilter);
			} else {
				newFilter = filters.get(0);
				newFilter.setStartYear(Integer.parseInt(yearFrom.getText()));
			}
			updateCurrentFilterDisplay();
			reloadTable();
		}
	}

	/**
	 * adds a new Filter to filters with country set to value read from textbox
	 */
	private void addFilter(FilterField field, String value) {
			Filter newFilter = new Filter();
			newFilter.setField(field, value);
			if(isFilterDuplicate(newFilter)){
				Window.alert("Filter already exist");
			}
			else{
				filters.add(newFilter);
				updateCurrentFilterDisplay();
				reloadTable();
			}		
	}
	
	/**
	 * checks if a filter is a duplicate or if no value is in the filter
	 * @param Filter newFilter
	 * @return true if filter is duplicate or has no valid value
	 * @return false if filter is new and has value
	 */
	private boolean isFilterDuplicate(Filter newFilter){

		if (isNullOrBlank(newFilter.getCountry()) && isNullOrBlank(newFilter.getCity())){
			return true;
		}
		
		for (Filter filter : filters){			
			if(filter.getCountry().equals(newFilter.getCountry()) && filter.getCity().equals(newFilter.getCity())){
				return true;
			}
		}
		return false;
	}

	/**
	 * reloads the table view with currently set filters
	 */
	private void reloadTable() {
		currentBatch = 0;
		table.clearData();
		requestAndAddNextBatchForTable();
	}

	/**
	 * requests the next batch of data and adds it to the table
	 */
	private void requestAndAddNextBatchForTable() {
		if (dataFetcherService==null) {
			dataFetcherService = GWT.create(DataFetcherService.class);
		}
		filters.get(0).setBatch(currentBatch++);

		//set up callback object
		AsyncCallback<ClimateData[]> callback = new AsyncCallback<ClimateData[]>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("RPC failed");
			}

			@Override
			public void onSuccess(ClimateData[] result) {
				if(result.length == 0 && currentBatch==1){
					Window.alert("No data found for current selection.");
				}
				if(result.length==5000) {
					requestAndAddNextBatchForTable();
				}
				table.addData(result);
				table.getVisualization(verticalTablePanel);
			}

		};

		dataFetcherService.getClimateData(filters.toArray(new Filter[0]), callback);
	}

	/**
	 * reloads the display that shows the current filter options
	 */
	private void updateCurrentFilterDisplay() {
		int currentLine=0;
		currentFilterDisplay.removeAllRows();
		currentFilterDisplay.setText(currentLine++, 0, "Current Filter:");
		//deviation is always set
		currentFilterDisplay.setText(currentLine, 0, "Uncertainty:");
		currentFilterDisplay.setText(currentLine, 1, filters.get(0).getMinDeviation()+" - "+filters.get(0).getMaxDeviation());
		++currentLine;
		//add year if some year filtering is in place
		if (filters.get(0).getStartYear()!=-1 || filters.get(0).getEndYear()!=-1) {
			currentFilterDisplay.setText(currentLine, 0, "Year:");
			//assemble range display
			String text = "any";
			if (filters.get(0).getStartYear()!=-1 && filters.get(0).getEndYear()==-1) {
				//if there is some start year to display but no end year
				text = filters.get(0).getStartYear()+" and later";
			}else if (filters.get(0).getStartYear() == -1 && filters.get(0).getEndYear()!=-1) {
				//if there is some end year to display but no start year
				text = filters.get(0).getEndYear()+" and earlier";
			} else {
				//if there is a start year and an end year set
				text = filters.get(0).getStartYear()+" - "+filters.get(0).getEndYear();
			}
			currentFilterDisplay.setText(currentLine, 1, text);
			++currentLine;
		}

		if (filters.size()>1/*there can be a filter set for cities or countries*/) {
			//find out if there exist filters for country/city
			boolean hasCity = false;
			boolean hasCountry = false;
			for (Filter filter : filters) {
				if (filter.getCity()!=null)hasCity=true;
				if (filter.getCountry()!=null)hasCountry=true;
			}
			//populate with existing filters
			if (hasCity) {
				currentFilterDisplay.setText(currentLine, 0, "Cities:");
				for (Filter filter: filters) {
					if(filter.getCity()!= null){ 
						currentFilterDisplay.setText(currentLine, 1, filter.getCity());
						/*Button for removing the filter for city*/
						Button removeButtonCity = new Button("X");
						currentFilterDisplay.setWidget(currentLine++, 2, removeButtonCity);

						/*ClickHandler for deleting the filter for cities one by one*/
						removeButtonCity.addClickHandler(new ClickHandler() {
							public void onClick(ClickEvent event) {						
								int rowIndex = currentFilterDisplay.getCellForEvent(event).getRowIndex();
								String val=currentFilterDisplay.getText(rowIndex, 1);
								for(Filter filter:filters){
									if(filter.getCity().equals(val) && val != null) filters.remove(filter);
								}
								updateCurrentFilterDisplay();
								reloadTable();
							}
						});
					}
				}
			}

			if (hasCountry) {
				currentFilterDisplay.setText(currentLine, 0, "Countries:");
				for (Filter filter: filters) {
					if(filter.getCountry()!= null) {
						currentFilterDisplay.setText(currentLine, 1, filter.getCountry());
						/*Button for removing the filter for country*/
						Button removeButtonCountry = new Button("X");
						currentFilterDisplay.setWidget(currentLine++, 2, removeButtonCountry);

						/*ClickHandler for deleting the filter for cities one by one*/
						removeButtonCountry.addClickHandler(new ClickHandler() {
							public void onClick(ClickEvent event) {						
								int rowIndex = currentFilterDisplay.getCellForEvent(event).getRowIndex();
								String val=currentFilterDisplay.getText(rowIndex, 1);
								for(Filter filter:filters){
									if(filter.getCountry().equals(val) && val != null) filters.remove(filter);
								}
								updateCurrentFilterDisplay();
								reloadTable();
							}
						});
					}
				}
			}
		}
	}

	/**
	 * 
	 * @return LinkedHashMap with boolean values that show the checkbox status
	 */
	private LinkedHashMap<String, Boolean> getTableColumnShowValues() {
		LinkedHashMap<String, Boolean> checkboxValues = new LinkedHashMap<String, Boolean>();
		checkboxValues.put("Country", showCountry.getValue());
		checkboxValues.put("City", showCity.getValue());
		checkboxValues.put("Date", showDate.getValue());
		checkboxValues.put("Temperature", showTemperature.getValue());
		checkboxValues.put("Uncertainty", showUncertainty.getValue());
		checkboxValues.put("Latitude", showLatitude.getValue());
		checkboxValues.put("Longitude", showLongitude.getValue());
		return checkboxValues;
	}
	
	/**
	 * Method populates the oracles for the suggest boxes with the results from the server
	 * The arrays are static: the countries and cities names are independent of chosen year
	 */
	private void populateSuggestBoxes(){
		dataFetcherService.getCityNames(new AsyncCallback<String[]>() {
			
			@Override
			public void onSuccess(String[] result) {
				for (String s : result){
					citiesOracle.add(s);
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Caught an exception", caught);
			}
		});
		
		dataFetcherService.getCountryNames(new AsyncCallback<String[]>() {
					
					@Override
					public void onSuccess(String[] result) {
						for (String s : result){
							countriesOracle.add(s);
						}
					}
					
					@Override
					public void onFailure(Throwable caught) {
						GWT.log("Caught an exception", caught);
					}
				});
	}
	
	private boolean isNullOrBlank(String s) {
		return s == null || s.isEmpty() || s.trim().isEmpty();
	}
}

