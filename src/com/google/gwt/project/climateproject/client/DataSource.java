package com.google.gwt.project.climateproject.client;

import java.util.HashMap;

/**
 * A class representing a general data source that reads data and informs a listener.
 * @author Jeremy Johnson
 *
 */
public abstract class DataSource {
	protected DataListener listener;
	protected DataParser parser;
	protected HashMap<String, Data> dataMap;
	
	/**
	 * Begin reading the data from the source.
	 */
	public abstract void read();
	
	/**
	 * Set the listener to be informed upon completion.
	 * @param listener
	 */
	public void setListener(DataListener listener) {
		this.listener = listener;
	}
	
	/**
	 * Notify the listener when reading is complete.
	 * @param data The data that has been read.
	 */
	protected void doneReading(Data data) {
		this.listener.dataUploaded(data);
	}
}
