package com.google.gwt.project.climateproject.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.project.climateproject.client.DataService;
import com.google.gwt.project.climateproject.client.NotLoggedInException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Class that stores, retrieves, and deletes users' data sets from the database
 * @author Rastislav Ondrasek
 *
 */
public class DataServiceImpl extends RemoteServiceServlet implements 
DataService {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(DataServiceImpl.class.getName());
	private static final PersistenceManagerFactory PMF =
		JDOHelper.getPersistenceManagerFactory("transactions-optional");

	/**
	 * Adds a data set in to the database.
	 * @param data: data in form of a 1D String containing data info & rows and columns
	 */
  public void addData(String[] data) throws NotLoggedInException {
    checkLoggedIn();
    PersistenceManager pm = getPersistenceManager();
    try {
    	pm.makePersistent(new DataSet(getUser(), data));
    } finally {
      pm.close();
    }
  }
  
  /** 
   * Removes persisted data set from the database.
   * @param fileName: the file to be removed from the database
   */
  public void removeData(String fileName) throws NotLoggedInException {
  	checkLoggedIn();
  	PersistenceManager pm = getPersistenceManager();
    try {
    	long deleteCount = 0;
      Query q = pm.newQuery(DataSet.class, "user == u");
      q.declareParameters("com.google.appengine.api.users.User u");
      List<DataSet> dataSets = (List<DataSet>) q.execute(getUser());
      for (DataSet data : dataSets) {
      	String[] dataStr = data.getDataSet();
      	String[] tokens = dataStr[0].split(",");
      	if (tokens[0].equals(fileName)) {
      		deleteCount++;
          pm.deletePersistent(data);
        }
      }
      if (deleteCount != 1) {
        LOG.log(Level.WARNING, "removeData deleted "+deleteCount+" dataSets");
      }
    } finally {
      pm.close();
    }
  }
  
  /** 
   * Retrieves all persisted data sets from the database that belongs to the user.
   */
  public List<String[]> getData() throws NotLoggedInException {
  	checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
    List<String[]> dataFiles = new ArrayList<String[]>();
    try {
      Query q = pm.newQuery(DataSet.class, "user == u");
      q.declareParameters("com.google.appengine.api.users.User u");
      q.setOrdering("createDate");
      List<DataSet> dataSets = (List<DataSet>) q.execute(getUser());
      
      for (DataSet data : dataSets) {
        dataFiles.add(data.getDataSet());
      }
    } finally {
      pm.close();
    }
    return dataFiles;
  }

  private void checkLoggedIn() throws NotLoggedInException {
    if (getUser() == null) {
      throw new NotLoggedInException("Not logged in.");
    }
  }

  private User getUser() {
    UserService userService = UserServiceFactory.getUserService();
    return userService.getCurrentUser();
  }

  private PersistenceManager getPersistenceManager() {
    return PMF.getPersistenceManager();
  }
}
