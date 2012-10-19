package com.google.gwt.project.climateproject.client;

import java.util.HashMap;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

/**
 * Singleton data parser implementation for Json.
 * @author Jeremy Johnson
 *
 */
public class JsonParser implements DataParser {
	private static JsonParser instance = null;
	
	// Protect from initialization
	protected JsonParser() {}
	
	/**
	 * @return An instance of JsonParser
	 */
	public static JsonParser getInstance() {
		if (instance == null) {
			instance = new JsonParser();
		}
		return instance;
	}
	
	@Override
	public Data parseData(String name, String dataStr, HashMap<String, Data> dataMap) {
		Data data = new Data(name, dataMap);
		JavaScriptObject jso = convertToJSO(dataStr);
		
		// Get an array of data columns from the json.
		JsArray<OverlayDataColumn> columns = parseToDataColumns(jso);

		// Populate the Data object.
		for (int i = 0; i < columns.length(); i++) {
			data.addColumn(columns.get(i).toDataColumn());
		}
		return data;
	}

	/**
	 * Convert the string of JSON into JavaScript object.
     */
	private final native JavaScriptObject convertToJSO(String json) /*-{
	  return eval(json);
	}-*/;
	  
	/**
	 * Cast JavaScriptObject as JsArray of JsonDataColumn. Note: Currently, this
	 * simply treats all data as numeric.
	 * 
	 * @param jso The json object
	 */
	private final native JsArray<OverlayDataColumn> parseToDataColumns(JavaScriptObject jso) /*-{
	 	// fields is a map from field names to field data.  It is used to create the data columns.
	 	var fields = {};
	 	for (var i = 0; i < jso.length; i++) {
	 		for (var field in jso[i]) {
	 			
	 			var val = ""+jso[i][field];
	 			
	 			// Add the data to the field or create a new field if necessary.
	 			if (fields[field]) {
	 				fields[field].push(val);
	 			} else {
	 				fields[field] = [val];
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
}
