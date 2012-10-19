package com.google.gwt.project.climateproject.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DataServiceAsync {

	// Look up DataServiceImpl.java for comments
	public void addData(String[]data, AsyncCallback<Void> async);
	public void removeData(String fileName, AsyncCallback<Void> async);
	public void getData(AsyncCallback<List<String[]>> async);
}