package com.google.gwt.project.climateproject.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;

/**
 * A Json overlay type used to represent a data column from json data.
 * @author Jeremy Johnson
 */
class OverlayDataColumn extends JavaScriptObject {
  protected OverlayDataColumn() {}

  /**
   * getName for overlay type.
   * @return The name
   */
  public final native String getName() /*-{ return this.name; }-*/;
  
  /**
   * getValues for overlay type.
   * @return The values in the column.
   */
  public final native JsArrayString getValues() /*-{ return this.values; }-*/;
  
  /**
   * Get a specific value in the column.
   * @param i The index of the value.
   * @return The value at index i.
   */
  public final String getValue(int i) {
	  return this.getValues().get(i);
  }
  
  /**
   * Convert overlay data column overlay type into standard DataColumn type.
   * @return The DataColumn with identical data.
   */
  public final DataColumn toDataColumn() {
	  DataValue[] valuesArray = new DataValue[getValues().length()];
	  for (int i = 0; i < valuesArray.length; i++) {
		  valuesArray[i] = new DataValue(getValue(i));
	  }
	  return new DataColumn(getName(), valuesArray);
  }
}