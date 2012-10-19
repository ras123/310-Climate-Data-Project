package com.google.gwt.project.climateproject.client;

import java.util.HashMap;

/**
 * An interface representing a parser for some format of data.
 * This will be implemented for a json parser, xml parser, and a csv parser.
 * @author Jeremy Johnson
 *
 */
public interface DataParser {
	public Data parseData(String name, String dataStr, HashMap<String, Data> dataMap);
}
