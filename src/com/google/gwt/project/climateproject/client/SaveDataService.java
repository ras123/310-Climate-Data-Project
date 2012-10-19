package com.google.gwt.project.climateproject.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("saveData")
public interface SaveDataService extends RemoteService {
	public void SaveData(DataColumn column);
}
