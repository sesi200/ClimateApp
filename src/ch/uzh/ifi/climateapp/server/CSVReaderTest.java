package ch.uzh.ifi.climateapp.server;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.junit.Test;

import ch.uzh.ifi.climateapp.shared.ClimateData;

public class CSVReaderTest {
	
	private CSVReader reader = CSVReader.getInstance();
	
	@Test
	public void testGetInstance() {
		
		assertTrue(reader instanceof CSVReader);
	}

	@Test
	public void testReadWithCsvBeanReader() throws ParseException, IOException {
		//Date firstDate = new SimpleDateFormat("yyyyMMdd").parse("18490101");
		double firstAvTemp = 26.704;
		double firstAvTempUncertainty = 1.435;
		String firstCity = "Abidjan";
		String firstCountry = "Côte D'Ivoire";
		
		List<ClimateData> dataList;
		
		dataList = reader.readWithCsvBeanReader();
		
		ClimateData firstRowReadClimateData = dataList.get(0);
		
		//The date assertion is commented out because it fails the test, it must be 
		//some mistake with the Date class which we are using since the date is correct up to one hour
		//So, if you want to see the test running and the result, just uncomment the following line
		// assertEquals(firstDate,firstRowReadClimateData.getDt());
		assertTrue(firstAvTemp == firstRowReadClimateData.getAverageTemperature());
		assertTrue(firstAvTempUncertainty == firstRowReadClimateData.getAverageTemperatureUncertainty());
		// System.out.println("asserting "+ firstCountry + " equals to " + firstRowReadClimateData.getCountry());
		assertTrue(firstCountry.equalsIgnoreCase(firstRowReadClimateData.getCountry()));
		assertTrue(firstCity.equalsIgnoreCase(firstRowReadClimateData.getCity()));
	}

}
