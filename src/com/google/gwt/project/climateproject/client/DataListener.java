package com.google.gwt.project.climateproject.client;

/**
 * An interface to represent an object that will be informed when data is uploaded.
 * @author Jeremy Johnson
 *
 */
public interface DataListener {
	/**
	 * A callback for when data is finished uploading.
	 * @param data The uploaded data.
	 */
	public void dataUploaded(Data data);
}
