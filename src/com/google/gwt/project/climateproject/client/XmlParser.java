package com.google.gwt.project.climateproject.client;

import java.util.HashMap;

import com.google.gwt.core.client.JsArray;

/**
 * Singleton data parser implementation for XML.
 * @author Jeremy Johnson
 *
 */
public class XmlParser implements DataParser {
	private static XmlParser instance = null;
	
	// Protect from initialization
	protected XmlParser() {}
	
	/**
	 * @return An instance of JsonParser
	 */
	public static XmlParser getInstance() {
		if (instance == null) {
			instance = new XmlParser();
		}
		return instance;
	}
	
	/**
	 * Parse the XML into an array of OverlayDataColumns.
	 * @param xml The xml string to be parsed
	 * @return The array of overlay columns.
	 */
	private final native JsArray<OverlayDataColumn> parseToDataColumns(String xml) /*-{
		// Create the XML doc, we need to make sure to use the proper parser
		if (window.DOMParser) {
			parser = new DOMParser();
			xmlDoc = parser.parseFromString(xml, "text/xml");
		} else {
			xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
			xmlDoc.async = "false";
			xmlDoc.loadXML(xml);
		}
		
		// Assume records are all within <Record> tags
		var records = xmlDoc.getElementsByTagName("Record");
		var fields = {};
		for (var i = 0; i < records.length; i++) {
			var children = records[i].childNodes;
			for (var j = 0; j < children.length; j++) {
				if (children[j].nodeType == 1) {
					// The name of the field is the name of the tag
					var field = children[j].nodeName;
					// The value of the field is the value of the child
					var val = "";
					if (children[j].firstChild) {
						val = ""+children[j].firstChild.nodeValue;
					}
					
	 				// Add the data to the field or create a new field if necessary.
	 				if (fields[field]) {
	 					fields[field].push(val);
	 				} else {
	 					fields[field] = [val];
	 				}
				}
			}
		}
		
		// Put the fields into the format required for the DataColumn overlay type.
	 	var cols = [];
	 	for (var field in fields) {
	 		cols.push({"name" : field, "values" : fields[field]});
	 	}
	   return cols;
	}-*/;

	@Override
	public Data parseData(String name, String dataStr,
			HashMap<String, Data> dataMap) {
		Data data = new Data(name, dataMap);

		// Get an array of data columns from the xml.
		JsArray<OverlayDataColumn> columns = parseToDataColumns(dataStr);

		// Populate the Data object.
		for (int i = 0; i < columns.length(); i++) {
			data.addColumn(columns.get(i).toDataColumn());
		}
		return data;
	}
}
