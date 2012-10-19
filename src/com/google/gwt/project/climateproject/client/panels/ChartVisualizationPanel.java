package com.google.gwt.project.climateproject.client.panels;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.project.climateproject.client.ClimateProject;
import com.google.gwt.project.climateproject.client.Data;
import com.google.gwt.project.climateproject.client.DataColumn;
import com.google.gwt.project.climateproject.client.Regression;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.visualizations.corechart.*;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;

/**
 * Class responsible for plotting data sets.
 *
 */
public class ChartVisualizationPanel extends FlowPanel{
	
	private ClimateProject project;
	
	private FlowPanel chartContainer = new FlowPanel();
	
	private ScatterChart scatterChart;
	private ScatterChart lineChart;
	private BarChart barChart;
	
	private ListBox xAxisOptionsList;
	private ListBox yAxisOptionsList;
	
	private RadioButton lineChartRadio;
	private RadioButton barChartRadio;
	private RadioButton scatterChartRadio;
	
	private CheckBox regressionCheckBox;
	
	public ChartVisualizationPanel(final ClimateProject project)
	{
		this.project = project;
		
		this.addStyleName("chartVisualizationPanel");
						
		chartContainer.addStyleName("chartContainer");
		
		FlowPanel chartConfigSectionContainer = new FlowPanel();
		chartConfigSectionContainer.addStyleName("chartConfigSectionContainer");
		add(chartConfigSectionContainer);
		
		//Navigation Bar
		FlowPanel chartConfigNavBar = new FlowPanel();
		chartConfigNavBar.addStyleName("chartConfigNavBar");
		
		Label generalSettingsLabel = new Label("General Settings");
		generalSettingsLabel.addStyleName("chartConfigLabel");
		
		chartConfigNavBar.add(generalSettingsLabel);
		
		//Configuration Content
		FlowPanel chartConfigSection = new FlowPanel();
		chartConfigSection.addStyleName("chartConfigSection");
		
		Label selectChartTypeLabel = new Label("Select Chart Type");
		selectChartTypeLabel.addStyleName("selectChartTypeLabel");
		
		FlowPanel selectChartPanel = new FlowPanel();
		selectChartPanel.addStyleName("selectChartPanel");
		
		lineChartRadio = new RadioButton("chartType", "Line Graph");
		lineChartRadio.addStyleName("radioButton");
		lineChartRadio.setValue(true);
		barChartRadio = new RadioButton("chartType", "Bar Graph");
		barChartRadio.addStyleName("radioButton");
		scatterChartRadio = new RadioButton("chartType", "Scattered Chart");
		scatterChartRadio.addStyleName("radioButton");
		
		selectChartPanel.add(lineChartRadio);
		selectChartPanel.add(barChartRadio);
		selectChartPanel.add(scatterChartRadio);
		
		//Select Columns
		Label selectColumnsLabel = new Label ("Select Columns");
		selectColumnsLabel.addStyleName("selectColumnsLabel");
		FlowPanel selectColumnsPanel = CreateSelectColumnsPanel();
				
		Button generateChartButton = new Button();
		generateChartButton.setText("Generate");
		generateChartButton.setStyleName("generateChartButton");
		
		generateChartButton.addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				UpdateChartVisibility();
				if (scatterChart.isVisible()) { 
					Options op = createOptions();
					op.setPointSize(3);
					scatterChart.draw(createTable(), op);
				}
				if (lineChart.isVisible()) {
					Options op = createOptions();
					op.setPointSize(0);
					op.setLineWidth(2);
					lineChart.draw(createTable(), op);
				}
				if (barChart.isVisible()) barChart.draw(createTable(), createOptions());
		    }
		});
				
		chartConfigSection.add(selectChartTypeLabel);
		chartConfigSection.add(selectChartPanel);
		chartConfigSection.add(selectColumnsLabel);
		chartConfigSection.add(selectColumnsPanel);
		chartConfigSection.add(generateChartButton);
				
		chartConfigSectionContainer.add(chartConfigNavBar);
		chartConfigSectionContainer.add(chartConfigSection);
				
		Runnable onLoadCallback = new Runnable() {

			public void run() {
				scatterChart = new ScatterChart(createTable(), createOptions());
				lineChart = new ScatterChart(createTable(), createOptions());
				barChart = new BarChart(createTable(), createOptions());
		        
				scatterChart.addStyleName("chart");
				lineChart.addStyleName("chart");
				barChart.addStyleName("chart");
				
				chartContainer.add(scatterChart);
				chartContainer.add(lineChart);
				chartContainer.add(barChart);

				scatterChart.setVisible(false);
				lineChart.setVisible(false);
				barChart.setVisible(false);
			}
		};
		// Load the visualization api, passing the onLoadCallback to be called
		// when loading is done.
		VisualizationUtils.loadVisualizationApi(onLoadCallback, LineChart.PACKAGE);

		add(chartContainer);
	}
	
	/**
	 * Adjusts graph options such as size of the graph, X & Y - axes labels
	 * @return options: instance of Options class that is used to change graph options
	 */
	private Options createOptions() {
	    Options options = Options.create();
	    AxisOptions  Xop = AxisOptions.create();
	    AxisOptions  Yop = AxisOptions.create();
	     
	    Xop.setTitle(xAxisOptionsList.getValue(xAxisOptionsList.getSelectedIndex()));
	    Yop.setTitle(yAxisOptionsList.getValue(yAxisOptionsList.getSelectedIndex()));
	      
	    options.setWidth(500);
	    options.setHeight(400);
	    options.setVAxisOptions(Yop);
	    options.setHAxisOptions(Xop);
	    
	    return options;
	}
	
	/**
	 * Changes the visibility of graphs based on the selected radio buttons.
	 */
	private void UpdateChartVisibility() {
		if(lineChartRadio.getValue()){
			scatterChart.setVisible(false);
			lineChart.setVisible(true);
			barChart.setVisible(false);
		}
		
		if(barChartRadio.getValue()){
			scatterChart.setVisible(false);
			lineChart.setVisible(false);
			barChart.setVisible(true);
		}
		
		if(scatterChartRadio.getValue()){
			scatterChart.setVisible(true);
			lineChart.setVisible(false);
			barChart.setVisible(false);
		}
	}
	
	/**
	 * Reads the columns and rows of a data set and creates a DataTable for the graph.
	 * @return data: the rows and columns to be plotted on the graph.
	 */
	private AbstractDataTable createTable() {
		DataTable data = DataTable.create();
				
		DataColumn xAxis;
		DataColumn yAxis;
		
		String selectedXAxis = xAxisOptionsList.getValue(xAxisOptionsList.getSelectedIndex());
		String selectedYAxis = yAxisOptionsList.getValue(yAxisOptionsList.getSelectedIndex());
					
		if(project.dataSetFlowPanel.dataSetListBox.getSelectedIndex() < 0) return data;
		
		Data selectedDataSet = project.getDataMap().get(project.dataSetFlowPanel.dataSetListBox.getValue(project.dataSetFlowPanel.dataSetListBox.getSelectedIndex()));
		
		if(selectedDataSet == null) return data;		
		
		xAxis = selectedDataSet.getColumn(selectedXAxis);
		selectedDataSet.sortByColumn(xAxis, false);
		
		data.addRows(xAxis.getCount());
		data.addColumn(ColumnType.NUMBER, xAxis.getName());
		
		for(int i=0; i < xAxis.getCount(); i++) {
			data.setValue(i, 0, xAxis.getValue(i).toDouble()); //x-axis is always at column 0
		}
		
		yAxis = selectedDataSet.getColumn(selectedYAxis);
		data.addColumn(ColumnType.NUMBER, yAxis.getName());
					
		for(int i=0; i < yAxis.getCount(); i++) {
			data.setValue(i, 1, yAxis.getValue(i).toDouble());
		}
		
		// counts number of rows that have non-null values for both columns
		if (regressionCheckBox.getValue()) {
			int m1 = 0, m2 = 0;
			for (int i = 0; i < xAxis.getCount(); i++) {
				if (xAxis.getValue(i).isNumeric())
					m1 += 1;
				if (yAxis.getValue(i).isNumeric())
					m2 += 1;
			}
			
			int m = (m2 < m1 ?m2 :m1), n = 2;
			double []X = new double[m*n];
			double []Y = new double[m];
			double []A = new double[n];
			double []correlation = new double[1];
			
			int idx = 0;
			// Consider only numbers when computing linear regression
			for (int i=0; i< xAxis.getCount(); i++) {
				if (xAxis.getValue(i).isNumeric() &&
						yAxis.getValue(i).isNumeric()) {
					X[idx*2] = xAxis.getValue(i).toDouble();
					X[idx*2+1] = 1;
					Y[idx] = yAxis.getValue(i).toDouble();
					idx++;
				}
			}		
			
			Regression.linearRegression(m, 2, X, Y, A, correlation); 
			data.addColumn(ColumnType.NUMBER, "Best Fit");
			
			idx = 0;
			// find first xValue that is non-null since xAxis values are sorted
			while (!xAxis.getValue(idx).isNumeric())
				idx++;
			
			for (int i = idx; i < xAxis.getCount(); i++) {  
				data.setValue(i, 2, A[0]*xAxis.getValue(i).toDouble() + A[1]);
			}
		}
		return data;
	}

	public void setBarGraphVisible(boolean b) {
		barChart.setVisible(b);
	}

	public void setScatterGraph(boolean b) {
		scatterChart.setVisible(b);
	}

	public void setLineGraphVisible(boolean b) {
		lineChart.setVisible(b);
	}
	
	/**
	 * Creates the panel that holds X & Y-axis to be plotted and regression check box.
	 * @return selectColumnsPanel: the panel holding all the UI for selecting what to graph.
	 */
	private FlowPanel CreateSelectColumnsPanel() {
		FlowPanel selectColumnsPanel = new FlowPanel();
		selectColumnsPanel.addStyleName("selectColumnsPanel");
				
		Label selectXAxisLabel = new Label("X Axis");
		selectXAxisLabel.addStyleName("selectXAxisLabel");
		
		xAxisOptionsList = new ListBox();
		xAxisOptionsList.addStyleName("xAxisOptionsList");
		xAxisOptionsList.addItem("No Data Loaded");		
		xAxisOptionsList.setVisibleItemCount(1);
		
		Label selectYAxisLabel = new Label("Y Axis");
		selectYAxisLabel.addStyleName("selectYAxisLabel");
		
		yAxisOptionsList = new ListBox();
		yAxisOptionsList.addStyleName("yAxisOptionsList");
		yAxisOptionsList.addItem("No Data Loaded");
		yAxisOptionsList.setVisibleItemCount(1);
		
		selectColumnsPanel.add(selectXAxisLabel);
		selectColumnsPanel.add(xAxisOptionsList);
		selectColumnsPanel.add(selectYAxisLabel);
		selectColumnsPanel.add(yAxisOptionsList);
		
		regressionCheckBox = new CheckBox("Linear Regression");
		regressionCheckBox.addStyleName("regressionCheckBox");
		regressionCheckBox.setValue(false);
		selectColumnsPanel.add(regressionCheckBox);
	  	
		return selectColumnsPanel;
	}
	
	/**
	 * Resets the graph, it is called when a different data set gets selected.
	 * @param data: the newly selected data set, it is used to populate X & Y-axes options.
	 */
	public void UpdateAxisOptions(Data data) {
		setBarGraphVisible(false); 
		setScatterGraph(false); 
		setLineGraphVisible(false); 
				
		xAxisOptionsList.clear();
		yAxisOptionsList.clear();
		
		for(DataColumn column : data.getColumns()) {
			
			xAxisOptionsList.addItem(column.getName());
			yAxisOptionsList.addItem(column.getName());
		}
	}
	
	/**
	 * Resets all the UI widgets for the Chart Visualization Panel to their default values.
	 */
	public void Reset() {
		xAxisOptionsList.clear();
		xAxisOptionsList.addItem("No Data Set Selected");
		yAxisOptionsList.clear();
		yAxisOptionsList.addItem("No Data Set Selected");
		
		regressionCheckBox.setValue(false);
		
		scatterChart.setVisible(false);
		lineChart.setVisible(false);
		barChart.setVisible(false);
	
	}
	
}
