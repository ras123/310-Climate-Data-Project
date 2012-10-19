package com.google.gwt.project.climateproject.server;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import com.google.gwt.project.climateproject.client.DataColumn;
import com.google.gwt.project.climateproject.client.SaveDataService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class SaveDataServiceImpl extends RemoteServiceServlet implements SaveDataService {
	 private static final PersistenceManagerFactory PMF =
	      JDOHelper.getPersistenceManagerFactory("transactions-optional");
	@Override
	public void SaveData(DataColumn column) {
		PersistenceManager pm = PMF.getPersistenceManager();
	    try {
	      pm.makePersistent(column);
	    } 
	    catch(Exception e) {
	    	e.printStackTrace();
	    }
	    finally {
	      pm.close();
	    }
	}

}
