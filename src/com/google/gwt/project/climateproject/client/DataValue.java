package com.google.gwt.project.climateproject.client;

import java.io.Serializable;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * Class representing a single string or double data value.
 * @author Jeremy Johnson
 */
@PersistenceCapable
public class DataValue implements Comparable<DataValue>, Serializable {
	
	// because this class is a child class, can not use Key or Long for key, have to use encoded key
	// due to persistence issue
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
    private String encodedKey; 
	
	@Persistent
	String strVal;
	@Persistent
	double doubleVal = Double.NaN;
	
	public DataValue() {
		strVal = "";
		doubleVal = Double.NaN;
	}
	
	/**
	 * Create a data val
	 * @param The string value representing the data.
	 */
	public DataValue(String val) {
		try {
			if (val == "" || val == "null") {
				strVal = val;
			} else {
				doubleVal = Double.parseDouble(val);
			}
		} catch (NumberFormatException e) {
			strVal = val;
		}
	}
	
	/**
	 * @return Is this numeric data?
	 */
	public boolean isNumeric() {
		return !Double.isNaN(doubleVal);
	}
	
	/**
	 * @return Return the double value of that data (NaN is non numeric).
	 */
	public double toDouble() {
		return doubleVal;
	}
	
	/**
	 * @return The value of the data as a string
	 */
	public String toString() {
		return (strVal != null) ? strVal : "" + toDouble();
	}

	@Override
	public int compareTo(DataValue o) {
		if (isNumeric()) {
			if (o.isNumeric()) {
				if (toDouble() > o.toDouble()) {
					return -1;
				} else if (toDouble() == o.toDouble()) {
					return 0;
				}
				return 1;
			}
			// All non-numeric data is less than numeric data
			return -1;
		}
		if (o.isNumeric()) {
			// All numeric data is greater than non-numeric data.
			return 1;
		}
		return toString().compareTo(o.toString());
	}
	
	/**
	 * Compare two values for equality.
	 * @param o The other value
	 * @return True if the values have the same value.
	 */
	public boolean equals(DataValue o) {
		if (isNumeric()) {
			return o.isNumeric() && toDouble() == o.toDouble();
		}
		return !o.isNumeric() && toString().equals(o.toString());
	}
}
