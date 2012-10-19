package com.google.gwt.project.climateproject.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.project.climateproject.client.panels.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.user.client.ui.Anchor;


public class ClimateProject implements EntryPoint, DataListener {
	
	private ArrayList<String> scientistGroup;
	
	private TabPanel mainTabPanel = new TabPanel();
	public final DataServiceAsync dataService = GWT.create(DataService.class);
	
	public SelectDataSetPanel dataSetFlowPanel;
	public DataGridPanel dataGridFlowPanel;
	
	public ChartVisualizationPanel displayChartFlowPanel;
	
	public GeoCodingPanel geoCodingPanel;
	
	private FlowPanel infoBar;
	private FlowPanel infoBarUserInfo;
	private FlowPanel infoBarAppInfo;
	private FlowPanel infoBarPersistence; 
	
	public Label selectedDataSetLabel;
	
	private HashMap<String, Data> dataMap = new HashMap<String, Data>();
	
	public HashMap<String, Data> getDataMap() {
		return dataMap;
	}
	
	public Data dataShowing;

	private VerticalPanel mainPanel = new VerticalPanel();
	private LoginInfo loginInfo = null;
	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label("Please sign in to your Google Account to access the Climate Companion Application.");
	private Anchor signInLink = new Anchor("Sign In");
	private Anchor signOutLink = new Anchor("Sign Out");
	
  
  /**
   * Entry point method.
   */
  public void onModuleLoad() {
    // Check login status using login service.
	loginLabel.addStyleName("loginLabel");
	signInLink.addStyleName("signIn");
    LoginServiceAsync loginService = GWT.create(LoginService.class);
    loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
      public void onFailure(Throwable error) {
          handleError(error);
      }

      public void onSuccess(LoginInfo result) {
        loginInfo = result;
        if(loginInfo.isLoggedIn()) {
        	loadClimateProject();
        } else {
          loadLogin();
        }
      }
    });
  }
  
  /**
   * Load the login page.
   */
  private void loadLogin() {
	    // Assemble login panel.
	    signInLink.setHref(loginInfo.getLoginUrl());
	    loginPanel.add(loginLabel);
	    loginPanel.add(signInLink);
	    RootPanel.get("climateProject").add(loginPanel);
  }

  /**
   * Load the main climate project
   */
  private void loadClimateProject() {
  	//Scientist List
  	scientistGroup = new ArrayList<String>();
  	//scientistGroup.add("jason.roett@gmail.com");
  	scientistGroup.add("ras.ondrasek@gmail.com");
  	scientistGroup.add("cs310.potatomountain@gmail.com");
  	scientistGroup.add("j@point-free.org");
  	scientistGroup.add("shewantiruneh186@gmail.com");

  	// If you are registered as a scientist, get persisted data from the server
  	if(scientistGroup.contains(loginInfo.getEmailAddress())) {		  
  		dataService.getData(new AsyncCallback<List<String[]>>() { 
  			public void onFailure(Throwable error) {}
  			public void onSuccess(List<String[]> data) {
  				loadData(data);
  			}
  		});		  
  	} 

  	//Create the widgets that live within each tab
  	dataSetFlowPanel = new SelectDataSetPanel(this);
  	dataGridFlowPanel = new DataGridPanel(this);	  
  	displayChartFlowPanel = new ChartVisualizationPanel(this);
  	geoCodingPanel = new GeoCodingPanel(this);


  	//Create the Info Bar
  	infoBar = new FlowPanel();
  	infoBar.addStyleName("infoBar");

  	infoBarUserInfo = new FlowPanel();
  	infoBarUserInfo.addStyleName("infoBarUserInfo");

  	infoBarPersistence = new FlowPanel();
  	infoBarPersistence.addStyleName("infoBarPersistence");

  	Label userLabel = new Label();
  	userLabel.setStyleName("infoBar_CaptionLabel");
  	userLabel.setText("Current User");
  	Label usernameLabel = new Label();
  	usernameLabel.setStyleName("infoBar_DataSetLabel");
  	usernameLabel.setText("Welcome " + loginInfo.getNickname());

  	infoBarUserInfo.add(userLabel);
  	infoBarUserInfo.add(usernameLabel);

  	infoBarAppInfo = new FlowPanel();
  	infoBarAppInfo.addStyleName("infoBarAppInfo");

  	Label captionLabel = new Label();
  	captionLabel.setStyleName("infoBar_CaptionLabel");
  	captionLabel.setText("Current Data Set");
  	selectedDataSetLabel = new Label();
  	selectedDataSetLabel.setStyleName("infoBar_DataSetLabel");
	  selectedDataSetLabel.setText("none");
	  
	  infoBarAppInfo.add(captionLabel);
	  infoBarAppInfo.add(selectedDataSetLabel);
	  
	  //Create Persistence Info Bar
	  Label persistenceLabel = new Label();
	  persistenceLabel.setStyleName("infoBar_PersistenceLabel");
	  persistenceLabel.setText("User Group");
	  
	  Label groupLabel = new Label();
	  groupLabel.setStyleName("infoBar_GroupLabel");
	  
	  if(scientistGroup.contains(loginInfo.getEmailAddress())) {
		  groupLabel.setText("Scientist (Uploaded data will be saved)");
	  }else {
		  groupLabel.setText("Public User (Uploaded data will not be saved)");
	  }
	  
	  infoBarPersistence.add(persistenceLabel);
	  infoBarPersistence.add(groupLabel);
	  
	  
	  infoBar.add(infoBarUserInfo);
	  infoBar.add(infoBarAppInfo);
	  infoBar.add(infoBarPersistence);
	  mainPanel.add(infoBar);
	  
	  	  
	  //Place the widgets within the main tab
	  mainTabPanel.add(dataSetFlowPanel, "Select Data Set");
	  mainTabPanel.add(dataGridFlowPanel, "Data Table");	  
	  mainTabPanel.add(displayChartFlowPanel, "View Chart");
	  mainTabPanel.add(geoCodingPanel, "Geo Data");
	  
	  mainTabPanel.addStyleName("mainTabPanel");
	  mainTabPanel.selectTab(0);
	  
	  mainTabPanel.addSelectionHandler(new SelectionHandler<Integer>() {
	  	@Override
	  	public void onSelection(SelectionEvent<Integer> event) {
	  		if (mainTabPanel.getWidget(event.getSelectedItem()) == geoCodingPanel) {
	  			geoCodingPanel.ResizeMap();
	  		}
	  	}  
	  });

	  mainTabPanel.addSelectionHandler(new SelectionHandler<Integer>() {
		@Override
		public void onSelection(SelectionEvent<Integer> event) {
			if (mainTabPanel.getWidget(event.getSelectedItem()) == dataGridFlowPanel) {
				
				if(dataSetFlowPanel.dataSetListBox.getItemCount() == 0) return;
				
				String selected = dataSetFlowPanel.dataSetListBox.getValue(
						dataSetFlowPanel.dataSetListBox.getSelectedIndex());
				Data currentData = dataMap.get(selected);								
								
				if (dataShowing != currentData) {
					updateDataTable(currentData);
					dataShowing = currentData;
				}
			}
		}  
	  });
	  
    // Set up sign out hyperlink.
    signOutLink.setHref(loginInfo.getLogoutUrl());
    // Assemble Main panel.
    mainPanel.add(mainTabPanel);
    
    RootPanel.get("climateProject").add(mainPanel);
        
    mainPanel.add(signOutLink);
}

  /**
   * Retrieves data sets from the server and inserts them into dataSetListBox.
   * @param dataSets: list containing arrays of Strings that represent a data set 
   */ 
  void loadData(List<String[]> dataSets) {
  	int fileIdx = 0;
  	while (fileIdx < dataSets.size()) {
  		String[] dataStr = dataSets.get(fileIdx);

  		// first line contains fileName & number of columns and rows
  		String fileInfo = dataStr[0];  
  		String [] tokens = fileInfo.split(",");
  		String fileName = tokens[0];
  		int numOfCols = Integer.parseInt(tokens[1]);
  		int numOfRows = Integer.parseInt(tokens[2]);

  		Data data = new Data(fileName, dataMap);
  		for(int i = 0; i < numOfCols; i++) {
  			String colName = dataStr[i*(numOfRows+1)+1];
  			DataValue[] rows = new DataValue[numOfRows];
  			for (int j = 0; j < numOfRows; j++) {
  				rows[j] = new DataValue(dataStr[i*(numOfRows+1)+2+j]);
  			}
  			
  			DataColumn col = new DataColumn(colName, rows);
  			data.addColumn(col);
  		}

  		getDataMap().put(data.getName(), data);
  		dataSetFlowPanel.dataSetListBox.addItem(data.getName());
  		fileIdx++;
  	}
  }
  
  /**
   * Update the data table to show a specific dataset.
   * @param dataSet is the Data object we would like to present in a data table.
   */
  public void updateDataTable(Data dataSet){	  
	  // NOTE:
	  // We are assuming that the columns are parallel (ie, they all are the same length).
	  // Therefore, it should be sufficient to simply take the length of the first one.
	  
	  int numColumns = dataSet.getColumnCount();
	  int numRows = 0;
	  if (numColumns != 0) {
		  numRows = dataSet.getColumns().iterator().next().getCount();
	  }
	  
	  dataGridFlowPanel.dataGrid.resize(numRows + 1, numColumns);
	  dataGridFlowPanel.dataGrid.setStyleName("dataGrid");
	  
	  int rowNumber = 0;
	  int columnNumber = 0;
	  for(DataColumn column: dataSet.getColumns())
	  {
		  dataGridFlowPanel.dataGrid.setText(rowNumber, columnNumber, column.getName());
		  for(DataValue value: column.getValues())
		  {
		     dataGridFlowPanel.dataGrid.setText(rowNumber + 1, columnNumber, value.toString());
		     dataGridFlowPanel.dataGrid.getRowFormatter().addStyleName(rowNumber + 1, "tableValues");
			 rowNumber++;			 
		  }
		  rowNumber= 0;
		  columnNumber++;
	  }
  }
  
  
  /**
   * Callback for when data has finished uploading.
   * @param data: Data set that gets displayed in dataSetListBox
   */
  public void dataUploaded(final Data data) {
  	Object[] array = data.getColumns().toArray();
  	int numOfCols = data.getColumnCount();
  	int numOfRows = ((DataColumn)array[0]).getCount();
  	
  	String []dataStr = new String[numOfCols*(1+numOfRows) + 1];
  	dataStr[0] = data.getName() + "," + numOfCols + "," + numOfRows;
  	
  	for(int i = 0; i < array.length; i++) {
  		DataColumn col = (DataColumn)array[i];
  		String name = col.getName();
  		dataStr[i*(numOfRows+1)+1] = name;
  		String[] values = new String[col.getCount()];
  		for(int j = 0; j < values.length; j++){
  			dataStr[i*(numOfRows+1)+2+j] = col.getValue(j).toString();
  		}
  	}
  	
  		dataService.addData(dataStr, new AsyncCallback<Void>() { 
			public void onFailure(Throwable error) {
				GWT.log("ERROR persisting data.");
			}
			public void onSuccess(Void ignore) {
				dataSetFlowPanel.dataSetListBox.addItem(data.getName());
		  	dataMap.put(data.getName(), data);
			}
		});

  	
  }

      
  /**
   * Display an error and redirect if needed.
   * @param error
   */
  private void handleError(Throwable error) {
    Window.alert(error.getMessage());
    if (error instanceof NotLoggedInException) {
      Window.Location.replace(loginInfo.getLogoutUrl());
    }
  }
  
  /**
   * Update the stats in the data table.
   * @param selectedColumn Column to display stats for.
   */
  public void updateDataTableStats(DataColumn selectedColumn){
	  
	  double average = selectedColumn.getAverage();
	  double median =  selectedColumn.getMedian();
	  double mode = selectedColumn.getMode();
	  double stdev = selectedColumn.getStdev();
	  double variance = selectedColumn.getVariance();
	  
	  dataGridFlowPanel.summaryTable.setText(1, 0, selectedColumn.getName());
	  dataGridFlowPanel.summaryTable.setText(1, 1, NumberFormat.getFormat("#.##").format(average));
	  dataGridFlowPanel.summaryTable.setText(1, 2, NumberFormat.getFormat("#.##").format(median));
	  dataGridFlowPanel.summaryTable.setText(1, 3, NumberFormat.getFormat("#.##").format(mode));
	  dataGridFlowPanel.summaryTable.setText(1, 4, NumberFormat.getFormat("#.##").format(stdev));
	  dataGridFlowPanel.summaryTable.setText(1, 5, NumberFormat.getFormat("#.##").format(variance));
  }
}