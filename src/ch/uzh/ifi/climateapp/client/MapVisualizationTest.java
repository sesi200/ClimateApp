package ch.uzh.ifi.climateapp.client;
import static org.junit.Assert.*;

import org.junit.Test;

import ch.uzh.ifi.climateapp.shared.ClimateData;

public class MapVisualizationTest {

	@Test
	public void test() {
		MapVisualization map = new MapVisualization();
		
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
		ClimateData d5 = new ClimateData();
		d5.setCountry("Germany");
		d5.setCity("Berlin");
		d5.setAverageTemperature(20);
		


		ClimateData [] dataOne = new ClimateData[5];
		dataOne[0] = d1;
		dataOne[1] = d2;
		dataOne[2] = d3;
		dataOne[3] = d4;
		dataOne[4] = d5;
		
		
		map.replaceData(dataOne);
		ClimateData[] result = map.getData();
		
		
		assertArrayEquals(dataOne, result);
	}

	
}
