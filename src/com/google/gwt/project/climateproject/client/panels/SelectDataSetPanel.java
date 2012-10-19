package com.google.gwt.project.climateproject.client.panels;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.project.climateproject.client.ClimateProject;
import com.google.gwt.project.climateproject.client.CsvParser;
import com.google.gwt.project.climateproject.client.Data;
import com.google.gwt.project.climateproject.client.DataListener;
import com.google.gwt.project.climateproject.client.DataParser;
import com.google.gwt.project.climateproject.client.FileDataSource;
import com.google.gwt.project.climateproject.client.JsonParser;
import com.google.gwt.project.climateproject.client.WebDataSource;
import com.google.gwt.project.climateproject.client.XmlParser;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;


public class SelectDataSetPanel extends FlowPanel {
	private ClimateProject _project;

	public ListBox dataSetListBox;

	public TextBox urlUploadTextBox;
	public Label dataSetLabel;
	private Button urlUploadButton;

	private RadioButton jsonRadioButton;
	private RadioButton csvRadioButton;
	private RadioButton xmlRadioButton;

	public Label urlUploadStatusLabel;
	public Label fileUploadStatusLabel;


	public SelectDataSetPanel(ClimateProject project)
	{
		_project = project;
		this.setStylePrimaryName("dataSetFlowPanel");

		//Create the UI containers
		FlowPanel dataSetContainer = new FlowPanel();
		dataSetContainer.addStyleName("dataSetContainer");

		FlowPanel uploadContainer = new FlowPanel();
		uploadContainer.addStyleName("uploadContainer");

		//Create everything within the Data Set Container
		dataSetListBox = createDataSetListBox();
		dataSetLabel = new Label();
		dataSetLabel.setText("Available Data Sets");
		dataSetLabel.addStyleName("genericLabel");

		dataSetListBox.addChangeHandler(new ChangeHandler(){	 
			public void onChange(ChangeEvent event) {

				if(dataSetListBox.getSelectedIndex() < 0 ) return;

				_project.selectedDataSetLabel.setText(dataSetListBox.getValue(dataSetListBox.getSelectedIndex()));

				String selected = dataSetListBox.getValue(dataSetListBox.getSelectedIndex());
				Data currentData = _project.getDataMap().get(selected);				
				_project.displayChartFlowPanel.UpdateAxisOptions(currentData);
				_project.geoCodingPanel.UpdateColumnOptions(currentData);
			}	  
		});

		Button removeButton = new Button();
		removeButton.setText("Remove");
		removeButton.setStyleName("removeButton");

		/**
		 * Removes a data set from dataMap and dataSetListBox.
		 */
		removeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				int indexToRemove = dataSetListBox.getSelectedIndex();
				if (indexToRemove == -1) return;   // nothing is selected
					
				String fileToRemove = dataSetListBox.getValue(indexToRemove);

				_project.getDataMap().remove(dataSetListBox.getValue(indexToRemove));
				_project.dataService.removeData(fileToRemove, new AsyncCallback<Void>() {
					public void onFailure(Throwable error) {GWT.log("ERROR deleting file");}
					public void onSuccess(Void ignore) {}
				});
				dataSetListBox.removeItem(indexToRemove);
				_project.dataShowing = null;
				_project.selectedDataSetLabel.setText("none");
				_project.geoCodingPanel.Reset(false);
				_project.dataGridFlowPanel.Reset();
				_project.displayChartFlowPanel.Reset();	          
			}
		});

		dataSetContainer.add(dataSetLabel);
		dataSetContainer.add(dataSetListBox);
		dataSetContainer.add(removeButton);

		//Create everything within the Upload Container
		Label uploadLabel = new Label();
		uploadLabel.setText("Upload A New Data Set");
		uploadLabel.addStyleName("genericLabel");


		//Create everything within the Url Upload Container
		Label urlUploadLabel = new Label();
		urlUploadLabel.setText("Url Upload:");
		urlUploadLabel.setStyleName("urlUploadPanelLabel");

		urlUploadTextBox = new TextBox();	  
		urlUploadTextBox.setStyleName("uploadTextBox");

		//urlUploadTextBox.setText("Enter Url Here");
		urlUploadTextBox.setText("http://www.ugrad.cs.ubc.ca/~cs310/data.php?q=seaice");

		urlUploadStatusLabel = new Label();
		urlUploadStatusLabel.setStyleName("urlUploadStatusLabel");


		urlUploadButton = new Button(); 
		urlUploadButton.setText("Upload");
		urlUploadButton.setStyleName("uploadButton");

		urlUploadButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				importFileFromUrl();

			}
		});



		FlowPanel urlUploadPanel = new FlowPanel();
		urlUploadPanel.setStyleName("urlUploadPanel");

		urlUploadPanel.add(urlUploadLabel);
		urlUploadPanel.add(urlUploadStatusLabel);
		urlUploadPanel.add(urlUploadTextBox);		  
		urlUploadPanel.add(urlUploadButton);



		uploadContainer.add(uploadLabel);
		uploadContainer.add(urlUploadPanel);

		//Create everything within fileUploadContainer
		FlowPanel fileUploadPanel = new FlowPanel();
		fileUploadPanel.addStyleName("fileUploadPanel");

		Label fileUploadLabel = new Label("Local File Upload:");
		fileUploadLabel.setStyleName("urlUploadPanelLabel");

		fileUploadStatusLabel = new Label();
		fileUploadStatusLabel.setStyleName("fileUploadStatusLabel");

		TextBox fileUploadTextBox = new TextBox();
		//fileUploadTextBox.setStyleName("uploadTextBox");
		fileUploadTextBox.setText("Enter File Path Here");



		Button fileUploadButton = new Button();
		fileUploadButton.addStyleName("uploadButton");
		fileUploadButton.setText("Upload");


		fileUploadPanel.add(fileUploadLabel);	  
		fileUploadPanel.add(fileUploadStatusLabel);
		fileUploadPanel.add(importFileFromDisk());

		uploadContainer.add(fileUploadPanel);

		//Create The Selection Radio Buttons for the file type we are uploading
		FlowPanel fileTypePanel = CreateFileTypePanel();

		uploadContainer.add(fileTypePanel);

		this.add(dataSetContainer);
		this.add(uploadContainer);
	}

	private FlowPanel CreateFileTypePanel() {
		FlowPanel fileTypePanel = new FlowPanel();
		fileTypePanel.addStyleName("fileTypePanel");

		Label fileTypeLabel = new Label("Select File Type");
		fileTypeLabel.addStyleName("genericLabel");

		jsonRadioButton = new RadioButton("fileType");
		jsonRadioButton.setText("JSON");
		jsonRadioButton.addStyleName("radioButton");
		jsonRadioButton.setValue(true);
		csvRadioButton = new RadioButton("fileType");
		csvRadioButton.setText("CSV");
		csvRadioButton.addStyleName("radioButton");
		xmlRadioButton = new RadioButton("fileType");
		xmlRadioButton.setText("XML");
		xmlRadioButton.addStyleName("radioButton");

		fileTypePanel.add(fileTypeLabel);

		fileTypePanel.add(jsonRadioButton);
		fileTypePanel.add(csvRadioButton);
		fileTypePanel.add(xmlRadioButton);

		return fileTypePanel;

	}

	private ListBox createDataSetListBox()
	{
		final ListBox dataSetListBox = new ListBox();

		dataSetListBox.setVisibleItemCount(4);
		dataSetListBox.setStyleName("dataSetListBox");

		return dataSetListBox;

	}

	/**
	 * Uploads a local file from the user's computer to the server for processing.
	 * @return FormPanel that holds the FileUpload and the Upload button
	 */
	private Widget importFileFromDisk() {
		final FormPanel form = new FormPanel();
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);

		VerticalPanel panel = new VerticalPanel();

		final FileUpload upload = new FileUpload();
		upload.addStyleName("fileUploadWidget");
		upload.setName("FileUpload");

		form.setAction(GWT.getModuleBaseURL()+"FileUploadServlet");

		panel.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);

		final Button submitButton = new Button();
		submitButton.setText("Upload");
		submitButton.setStyleName("uploadButton");
		submitButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				form.submit();
			}
		});

		panel.add(upload);
		panel.add(submitButton);

		form.addSubmitHandler(new FormPanel.SubmitHandler() {
			public void onSubmit(SubmitEvent event) {
				if (!"".equalsIgnoreCase(upload.getFilename())) {
					fileUploadStatusLabel.setText("");
					submitButton.setEnabled(false);
				}
				else{
					fileUploadStatusLabel.setText("Please Select a File");
					event.cancel(); 
				}
			}
		});

		form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			public void onSubmitComplete(SubmitCompleteEvent event) {
				submitButton.setEnabled(true);
				String dataStr = event.getResults();
				int n1 = dataStr.indexOf('>');
				int n2 = dataStr.lastIndexOf('<');
				dataStr = dataStr.substring(n1+1, n2);

				// Extract just the file name w/o the path
				String fileName = upload.getFilename();  
				n1 = 0;
				n1 = fileName.lastIndexOf('\\');      // Win
				if (n1 != 0)		
					fileName = fileName.substring(n1+1);
				else {
					n1 = fileName.lastIndexOf('/');    // Linux
					fileName = fileName.substring(n1+1);
				}

				FileDataSource source = new FileDataSource(fileName, dataStr, getParserType(), new DataListener() {
					public void dataUploaded(Data data) {
						_project.dataUploaded(data);
					}
				}, _project.getDataMap());

				source.read();
			}
		});

		form.add(panel);
		return form; 
	}

	/**
	 * Read data from the url in the upload textarea.
	 * @throws Exception 
	 */
	private void importFileFromUrl(){
		String url = urlUploadTextBox.getText().trim();
		urlUploadTextBox.setEnabled(false);
		urlUploadButton.setEnabled(false);

		WebDataSource source = new WebDataSource(url, getParserType(), new DataListener() {
			@Override
			public void dataUploaded(Data data) {
				urlUploadTextBox.setEnabled(true);
				urlUploadButton.setEnabled(true);
				_project.dataUploaded(data);
			}
		}, _project.getDataMap());
		source.read();
	}

	/**
	 * Determines which parser to use when fetching data from a url
	 * Defaults to JsonParser
	 * @return instance of Json/Csv/Xml parser
	 */
	private DataParser getParserType() {

		if(csvRadioButton.getValue()) {
			return CsvParser.getInstance();
		}

		if(xmlRadioButton.getValue()) {
			return XmlParser.getInstance();
		}

		return JsonParser.getInstance();

	}
}
