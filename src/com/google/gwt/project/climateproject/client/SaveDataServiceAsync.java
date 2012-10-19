package com.google.gwt.project.climateproject.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SaveDataServiceAsync {
	public void SaveData(DataColumn column, AsyncCallback<Void> async);
}
