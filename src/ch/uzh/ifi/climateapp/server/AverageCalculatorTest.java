package ch.uzh.ifi.climateapp.server;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import ch.uzh.ifi.climateapp.shared.AverageData;
import ch.uzh.ifi.climateapp.shared.ClimateData;

public class AverageCalculatorTest {
	// class under test
	private final AverageCalculator calc = new AverageCalculator();

	@Test
	public void testCalculateAveragePerYearAndCountry_grouped() {
		// given
		List<ClimateData> climateDatas = Arrays.asList(
				new ClimateData(createDate(2001), 20, 0d, "Zürich", "Switzerland", "", ""),
				new ClimateData(createDate(2001), 21, 0d, "Geneva", "Switzerland", "", ""),
				new ClimateData(createDate(2000), 21, 0d, "Geneva", "Switzerland", "", ""),
				new ClimateData(createDate(2000), 21, 0d, "Seattle", "USA", "", ""),
				new ClimateData(createDate(2001), 21, 0d, "Seattle", "USA", "", "")
				);
				
		// do
		Map<Integer, List<AverageData>> actual = calc.calculateAveragePerYearAndCountry(climateDatas);
		
		// verify
		Map<Integer, List<AverageData>> expected = new LinkedHashMap<Integer, List<AverageData>>();
		expected.put(2001, Arrays.asList(
				new AverageData("Switzerland", 20.50, 2001),
				new AverageData("USA", 21.00, 2001)
				));
		expected.put(2000, Arrays.asList(
				new AverageData("Switzerland", 21.00, 2000),
				new AverageData("USA", 21.00, 2000)));
		
		assertEquals(expected, actual);
	}

	@Test
	public void testCalculateAveragePerYearAndCountry_emptyInput_emptyOutput(){
		//given
		List<ClimateData> climateDatas = Collections.emptyList();
		
		//do
		Map<Integer, List<AverageData>> result = calc.calculateAveragePerYearAndCountry(climateDatas);
		
		//verify
		Assert.assertTrue(result.isEmpty());
	}
	@Test
	public void testCalculateAveragePerYearAndCountry_rounded() {
		// given
		List<ClimateData> climateDatas = Arrays.asList(
				new ClimateData(createDate(2001), 1.505, 0d, "", "CH", "", ""),
				new ClimateData(createDate(2001), 1.504, 0d, "", "UA", "", "")
				);
				
		// do
		Map<Integer, List<AverageData>> actual = calc.calculateAveragePerYearAndCountry(climateDatas);
		
		// verify
		Map<Integer, List<AverageData>> expected = new LinkedHashMap<Integer, List<AverageData>>();
		expected.put(2001, Arrays.asList(
				new AverageData("CH", 1.51, 2001),
				new AverageData("UA", 1.50, 2001)
				));
		assertEquals(expected, actual);
	}
	
	@SuppressWarnings("deprecation")
	private Date createDate(int year) {
		return new Date(year - 1900, 1, 1);
	}

}
