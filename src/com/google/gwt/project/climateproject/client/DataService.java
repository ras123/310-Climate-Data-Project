package com.google.gwt.project.climateproject.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("data")
public interface DataService extends RemoteService {
	
	// Look up DataServiceImpl.java for comments
	public void addData(String[] data) throws NotLoggedInException;
	public void removeData(String fileName) throws NotLoggedInException;
	public List<String[]> getData() throws NotLoggedInException;
	
}
