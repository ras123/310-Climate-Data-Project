package com.google.gwt.project.climateproject.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * A service for fetching a remote url through rpc (to avoid SOP issues).
 * @author Jeremy Johnson
 *
 */
@RemoteServiceRelativePath("fetchURL")
public interface FetchURLService extends RemoteService {
	String getURL(String url);
}
