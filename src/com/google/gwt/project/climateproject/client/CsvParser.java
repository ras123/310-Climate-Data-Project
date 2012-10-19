package com.google.gwt.project.climateproject.client;

import java.util.HashMap;

/**
 * Singleton CSV parser class.
 * @author Jeremy Johnson
 *
 */
public class CsvParser implements DataParser {
	private static CsvParser instance = null;
	
	// Protect from initialization
	protected CsvParser() {}
	
	/**
	 * @return An instance of JsonParser
	 */
	public static CsvParser getInstance() {
		if (instance == null) {
			instance = new CsvParser();
		}
		return instance;
	}
	
	@Override
	public Data parseData(String name, String dataStr, HashMap<String, Data> dataMap) {
		Data data = new Data(name, dataMap);

		if (dataStr == "") {
			return data;
		}
		
		String[] lines = dataStr.split("\\n");
		String[] titles = lines[0].split(",");
		
		// Array of columns
		DataValue[][] vals = new DataValue[titles.length][];
		
		
		// Initialize the column arrays.
		for (int j = 0; j < titles.length; j++) {
			vals[j] = new DataValue[lines.length - 1];
		}
		

		for (int i = 1; i < lines.length; i++) {
			String[] s = lines[i].split(",");
			
			// Add each value to the correct column.
			int j = 0;
			for (; j < s.length; j++) {
				vals[j][i - 1] = new DataValue(s[j]);
			}
			for (; j < titles.length; j++) {
				vals[j][i - 1] = new DataValue("");
			}
		}

		// Create the columns and add them to the data.
		for (int i = 0; i < titles.length; i++) {
			DataColumn col = new DataColumn(titles[i], vals[i]);
			data.addColumn(col);
		}
		return data;
	}
}
