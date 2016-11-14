package ch.uzh.ifi.climateapp.server;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.junit.Test;

import ch.uzh.ifi.climateapp.shared.ClimateData;

public class MyContextListenerTest {

	@Test
	public void testContextInitialized() throws IOException {
		CSVReader reader = CSVReader.getInstance();
		
		List<ClimateData> listOfData = reader.readWithCsvBeanReader();
		
		
		ServletContext context = ServletContextHolder.CONTEXT;
		System.out.println(context.toString());
		List<ClimateData> listFromContext = (List<ClimateData>) context.getAttribute("climateData");
		
		assertTrue(listOfData.containsAll(listFromContext) && listFromContext.containsAll(listOfData));
	}

}
