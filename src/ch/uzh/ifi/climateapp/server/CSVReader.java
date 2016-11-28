package ch.uzh.ifi.climateapp.server;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import ch.uzh.ifi.climateapp.shared.ClimateData;

public class CSVReader {
	private static CSVReader instance;

	private CSVReader() {
	}

	public static CSVReader getInstance() {
		if (instance == null) {
			instance = new CSVReader();
		}

		return instance;
	}

	/**
	 * Sets up the processors used for the columns of the data set. There are 7
	 * CSV columns, so 7 processors are defined. Empty columns are read as null
	 * (hence the NotNull() for mandatory columns).
	 * 
	 * @return the array of cell processors
	 * 
	 * @author lada
	 */
	private static CellProcessor[] getProcessors() {

		final CellProcessor[] processors = new CellProcessor[] { 
				new ParseDate("yyyy-MM-dd"), // the date of the measurement
				new Optional(new ParseDouble()), // average temperature may be null
				new Optional(new ParseDouble()), // average temperature inaccuracy may be null
				new NotNull(), // city
				new NotNull(), // country
				new NotNull(), // latitude
				new NotNull(), // longitude
		};

		return processors;
	}

	/**
	 * This method is using the CsvBeanReader from the super-csv-2.4.0 library
	 * to read the .csv file line-by-line and save the data from column of csv
	 * file into the corresponding field of the given JavaBean object.
	 * 
	 * The names of the .csv header must correspond to the field names of the
	 * class.
	 * 
	 * @return list with ClimateData objects
	 * @throws IOException
	 */
	public List<ClimateData> readWithCsvBeanReader() throws IOException {

		List<ClimateData> dataList = new ArrayList();

		ICsvBeanReader beanReader = null;
		try {

			URL url = CSVReader.class.getClassLoader().getResource("GlobalLandTemperaturesByMajorCity_v1.csv");
			String filesPathAndName = url.getPath();

			beanReader = new CsvBeanReader(new FileReader(filesPathAndName), CsvPreference.STANDARD_PREFERENCE);

			// the header elements are used to map the values to the bean (names
			// must match)
			final String[] header = beanReader.getHeader(true);
			final CellProcessor[] processors = getProcessors();

			ClimateData climateData;
			while ((climateData = beanReader.read(ClimateData.class, header, processors)) != null) {
				dataList.add(climateData);

				// prints the read lines into console for testing
//				System.out.println(String.format("rowNo=%s: %s", beanReader.getRowNumber(),
//						climateData));
			}

		} finally {
			if (beanReader != null) {
				beanReader.close();
			}
		}
		return dataList;
	}

}
