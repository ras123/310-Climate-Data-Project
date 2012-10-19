package com.google.gwt.project.climateproject.client;

import java.util.HashMap;

/**
 * A data source for reading data (currently only json) from a local file.
 * 
 * @author Rastislav Ondrasek
 * 
 */
public class FileDataSource extends DataSource {
	static int requestId = 0; // id of the current json request.
	String fileName;
	String file;     // data file

	/**
	 * @param file The file being uploaded.
	 * @param listener The listener to notify upon completion.
	 */
	public FileDataSource(String fileName, String file, DataParser parser, DataListener listener,
			HashMap<String, Data> dataMap) {
		this.fileName = fileName;
		this.file = file;
		this.parser = parser;
		this.listener = listener;
		this.dataMap = dataMap;
	}
	
	/**
	 * Begin reading from the file
	 */
	public void read() {
		this.doneReading(parser.parseData(fileName, file, dataMap));
	}
}
