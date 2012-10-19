package com.google.gwt.project.climateproject.client;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * A data source for reading data (currently only json) from a url.
 * 
 * @author Jeremy Johnson
 * 
 */
public class WebDataSource extends DataSource {
	static FetchURLServiceAsync fetchURLSvc = GWT.create(FetchURLService.class);
	
	String url; // url of data.

	/**
	 * @param url The url being read.
	 * @param parser The parser to use for the data.
	 * @param listener The listener to notify upon completion.
	 */
	public WebDataSource(String url, DataParser parser, DataListener listener,
			HashMap<String, Data> dataMap) {
		this.url = url;
		this.parser = parser;
		this.listener = listener;
		this.dataMap = dataMap;
	}

	/**
	 * Begin reading from the URL
	 */
	public void read() {		
		if (WebDataSource.fetchURLSvc == null) {
			fetchURLSvc = GWT.create(FetchURLService.class);
		}
		
		AsyncCallback<String> callback = new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				doneReading(null);
			}

			@Override
			public void onSuccess(String result) {
				doneReading(parser.parseData(url, result, dataMap));
			}
		};
		// Fetch the URL through rpc.
		WebDataSource.fetchURLSvc.getURL(url, callback);
	}
}
