package com.google.gwt.project.climateproject.client.panels;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.project.climateproject.client.ClimateProject;
import com.google.gwt.project.climateproject.client.DataColumn;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
//import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.HTMLTable.Cell;

public class DataGridPanel extends FlowPanel {
		
	public Grid dataGrid;
	public FlexTable summaryTable;
	public Label dataTableStats = new Label();
	public Label dataTableLabel = new Label();
	
	public DataGridPanel(final ClimateProject project) {
				
		//Summary Statistics Widget 
		FlowPanel summaryStatsContainer = new FlowPanel();
		summaryStatsContainer.addStyleName("summaryStatsContainer");
		
		Label summaryStatsLabel = new Label();
		summaryStatsLabel.setStyleName("genericLightLabel");
		summaryStatsLabel.setText("Summary Statistics");
		
		summaryTable = new FlexTable();
		summaryTable.setStyleName("summaryStatsTable");
		
		summaryTable.setText(0, 0, "Selected Column");		
		summaryTable.setText(0, 1, "Average");
		summaryTable.setText(0, 2, "Median");
		summaryTable.setText(0, 3, "Mode");
		summaryTable.setText(0, 4, "Standard Deviation");
		summaryTable.setText(0, 5, "Variance");
		
		summaryTable.setText(1, 0, "N/A");		
		summaryTable.setText(1, 1, "N/A");
		summaryTable.setText(1, 2, "N/A");
		summaryTable.setText(1, 3, "N/A");
		summaryTable.setText(1, 4, "N/A");
		summaryTable.setText(1, 5, "N/A");
		
		
		summaryTable.setCellPadding(10);
		summaryTable.getRowFormatter().addStyleName(0, "tableHeader");
		summaryTable.getRowFormatter().addStyleName(1, "tableValues");
		
		summaryStatsContainer.add(summaryStatsLabel);
		summaryStatsContainer.add(summaryTable);
		
		
		//Data Grid Widget
		FlowPanel dataGridContainer = new FlowPanel();
		dataGridContainer.setStyleName("dataGridContainer");
		
		Label dataGridLabel = new Label("Data Set");
		dataGridLabel.setStyleName("genericLightLabel");
	    
		dataGrid = new Grid(2, 2);
		
		dataGrid.setCellPadding(10);
		dataGrid.setCellSpacing(2);
		dataGrid.getRowFormatter().addStyleName(0, "tableHeader");
		dataGrid.getRowFormatter().addStyleName(1, "tableValues");
		dataGrid.setText(0, 0, "N/A");
		dataGrid.setText(0, 1, "N/A");
		dataGrid.setText(1, 0, "N/A");
		dataGrid.setText(1, 1, "N/A");
		 
			  
		dataGrid.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					Cell cell = dataGrid.getCellForEvent(event);
					if (cell == null) return;
					String colName = dataGrid.getText(0, cell.getCellIndex());
					DataColumn col = project.dataShowing.getColumn(colName);
					project.updateDataTableStats(col);
					project.dataShowing.sortByColumn(col);
					project.updateDataTable(project.dataShowing);
					
				}

				 
			  });
		  
		dataGridContainer.add(dataGridLabel);
		dataGridContainer.add(dataGrid);
		
		  this.add(summaryStatsContainer);
		  this.add(dataTableStats);
		  this.add(dataTableLabel);
		  this.add(dataGridContainer);
		 
	}
	
	
	/**
	 * Resets all the UI widgets for the DataGrid Panel to their default values
	 */
	public void Reset() {
		summaryTable.setText(1, 0, "N/A");		
		summaryTable.setText(1, 1, "N/A");
		summaryTable.setText(1, 2, "N/A");
		summaryTable.setText(1, 3, "N/A");
		summaryTable.setText(1, 4, "N/A");
		summaryTable.setText(1, 5, "N/A");
					
		dataGrid.setText(0, 0, "N/A");
		dataGrid.setText(0, 1, "N/A");
		dataGrid.setText(1, 0, "N/A");
		dataGrid.setText(1, 1, "N/A");
	    dataGrid.resize(2, 2);
	}
}
