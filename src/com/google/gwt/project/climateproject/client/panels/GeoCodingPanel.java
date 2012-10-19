package com.google.gwt.project.climateproject.client.panels;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.*;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.maps.client.MapOptions;
import com.google.gwt.maps.client.MapTypeId;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.base.HasLatLng;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.maps.client.event.Event;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.project.climateproject.client.ClimateProject;
import com.google.gwt.project.climateproject.client.Data;
import com.google.gwt.project.climateproject.client.DataColumn;

public class GeoCodingPanel extends FlowPanel{
	private MapWidget mapWidget;
	
	private ListBox latitudeListBox;
	private ListBox longitudeListBox;
	private ListBox selectDataListBox;
	
	private ClimateProject project;
	
	private ArrayList<Marker> markerList;
	 
	public GeoCodingPanel(ClimateProject project) {
		markerList = new ArrayList<Marker>();
		
		this.project = project;		
		this.addStyleName("geoCodingPanel");
		
		FlowPanel mapOptionsPanel = new FlowPanel();
		mapOptionsPanel.setStyleName("mapOptionsPanel");
		
		Label mapOptionsLabel = new Label("Map Config");
		mapOptionsLabel.setStyleName("mapOptionsLabel");
		
		FlowPanel selectColumnsPanel = new FlowPanel();
		selectColumnsPanel.setStyleName("selectColumnsPanel");
		Label selectColumnsLabel = new Label("Select Data Columns");
		selectColumnsLabel.setStyleName("selectColumnsLabel");
		
		Label selectLatitudeLabel = new Label("Latitude Data");
		selectLatitudeLabel.addStyleName("selectLatitudeLabel");
		latitudeListBox = new ListBox();
		latitudeListBox.addStyleName("mapOptionsListBox");
		latitudeListBox.addItem("No Value Selected");
		
		Label selectLongitudeLabel = new Label("Longitude Data");
		
		longitudeListBox = new ListBox();
		longitudeListBox.addStyleName("mapOptionsListBox");
		longitudeListBox.addItem("No Value Selected");
		
		Label selectDataLabel = new Label("Data to be displayed");
		selectDataListBox = new ListBox();
		selectDataListBox.addStyleName("mapOptionsListBox");
		selectDataListBox.addItem("No Data Loaded");
		
		
		selectColumnsPanel.add(selectLatitudeLabel);
		selectColumnsPanel.add(latitudeListBox);
		selectColumnsPanel.add(selectLongitudeLabel);
		selectColumnsPanel.add(longitudeListBox);
		selectColumnsPanel.add(selectDataLabel);
		selectColumnsPanel.add(selectDataListBox);
		
		Button plotButton = new Button();
		plotButton.setText("Plot");		
		plotButton.setStyleName("plotButton");
		
		plotButton.addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				UpdateMap();
		    }
		});
		
		mapOptionsPanel.add(mapOptionsLabel);
		mapOptionsPanel.add(selectColumnsLabel);
		mapOptionsPanel.add(selectColumnsPanel);
		mapOptionsPanel.add(plotButton);
		
		buildUi();			
		this.add(mapOptionsPanel);
		this.add(mapWidget);
	}

	/**
	 * Updates the current map with the data columns specified within the column selectors
	 */
	private void UpdateMap() {
	    Reset(true);
		DataColumn latColumn = new DataColumn();
		DataColumn longColumn = new DataColumn();
		DataColumn dataColumn = new DataColumn();
				
		for (DataColumn column: project.getDataMap().get(project.dataSetFlowPanel.dataSetListBox.getValue(project.dataSetFlowPanel.dataSetListBox.getSelectedIndex())).getColumns()) {
						
			if(column.getName().equals(latitudeListBox.getValue(latitudeListBox.getSelectedIndex()))){
				latColumn = column;							
			}
			
			if(column.getName().equals(longitudeListBox.getValue(longitudeListBox.getSelectedIndex()))){
				longColumn = column;				
			}
			
			if(column.getName().equals(selectDataListBox.getValue(selectDataListBox.getSelectedIndex()))){
				dataColumn = column;				
			}			
		}
		
		MapOptions options = new MapOptions();
		
	    
	    options.setZoom(1);
	    options.setCenter(new LatLng(49.15, -123.6));
	    options.setMapTypeId(new MapTypeId().getRoadmap());
	    options.setScrollwheel(true);
	    options.setDraggable(true);
	    options.setNavigationControl(true);
	    options.setMapTypeControl(true);
	    	    
	    int markerCount = dataColumn.getValues().length;
	    for(int i = 0; i < markerCount; i++)
	    {
	    	LatLng coordinates = new LatLng(latColumn.getValue(i).toDouble(),
	    			longColumn.getValue(i).toDouble());
	    	Marker marker = new Marker();
	    	marker.setPosition(coordinates);
	    	marker.setTitle(String.valueOf(dataColumn.getValue(i)));
	    	marker.setMap(mapWidget.getMap());
	    	markerList.add(marker);
	    	   
	    }
	    
	    mapWidget.getMap().setOptions(options);
		
	}
		
	/**
	 * Must be called when the GeoCoding Panel is selected to resize the Map Widget
	 * This fixes the Grey Space Bug
	 */
	public void ResizeMap() {
		HasLatLng center = mapWidget.getMap().getCenter();
		Event.trigger(mapWidget.getMap(), "resize");
		mapWidget.getMap().setCenter(center);
	}
	
	/**
	 * Creates the initial map to be displayed
	 */
	private void buildUi() {
		
		MapOptions options = new MapOptions();			
	    options.setZoom(5);
	    options.setCenter(new LatLng(49.15, -123.6));
	    options.setMapTypeId(new MapTypeId().getRoadmap());
	    options.setScrollwheel(true);
	    options.setDraggable(true);
	    options.setNavigationControl(true);
	    options.setMapTypeControl(true);
	    mapWidget = new MapWidget(options);
	    mapWidget.setSize("570px", "400px");
	    mapWidget.setStyleName("geoMap");
	  }
	
	public void UpdateColumnOptions(Data data) {
		latitudeListBox.clear();
		longitudeListBox.clear();
		selectDataListBox.clear();
				
		for(DataColumn column : data.getColumns()) {			
			latitudeListBox.addItem(column.getName());
			longitudeListBox.addItem(column.getName());
			selectDataListBox.addItem(column.getName());
		}
		
	}
	
	/**
	 * Clears the map of all data
	 * @param resetTableOnly determines whether to clear the drop down column selectors
	 */
	public void Reset(boolean resetTableOnly) {
		
		if(!resetTableOnly) {
			latitudeListBox.clear();
			latitudeListBox.addItem("No Value Selected");
			longitudeListBox.clear();
			longitudeListBox.addItem("No Value Selected");
			selectDataListBox.clear();
			selectDataListBox.addItem("No Value Selected");
		}
		
		mapWidget.removeFromParent();
		
		MapOptions options = new MapOptions();		
	    options.setZoom(5);
	    options.setCenter(new LatLng(49.15, -123.6));
	    options.setMapTypeId(new MapTypeId().getRoadmap());
	    options.setScrollwheel(true);
	    options.setDraggable(true);
	    options.setNavigationControl(true);
	    options.setMapTypeControl(true);
	    
	    mapWidget = new MapWidget(options);
	    mapWidget.setSize("570px", "400px");
	    mapWidget.setStyleName("geoMap");	    
	    this.add(mapWidget);
	    
	}
	
	
	

}
