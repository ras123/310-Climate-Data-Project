package com.google.gwt.project.climateproject.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.gwt.project.climateproject.client.FetchURLService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Implementation of RPC FetchURLService.
 * Fetches data from a remote url.
 * @author Jeremy Johnson
 *
 */
public class FetchURLServiceImpl extends RemoteServiceServlet implements
		FetchURLService {

	@Override
	public String getURL(String url) {
		try {
			URL urlObj = new URL(url);
		    // Read all the text returned by the server
		    BufferedReader in = new BufferedReader(new InputStreamReader(urlObj.openStream()));
		    String str;
		    String result = "";
		    while ((str = in.readLine()) != null) {
		    	result += str + "\n";
		    }
		    in.close();
		    return result;
		} catch (MalformedURLException e) {
			return "";
		} catch (IOException e) {
			return "";
		}
	}
}
