package com.google.gwt.project.climateproject.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Async interface for FetchURLService as required by RPC.
 * @author Jeremy Johnson
 *
 */
public interface FetchURLServiceAsync {
	void getURL(String url, AsyncCallback<String> callback);
}
