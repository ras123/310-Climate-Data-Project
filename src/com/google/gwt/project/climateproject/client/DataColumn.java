package com.google.gwt.project.climateproject.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javax.jdo.annotations.*;

/**
 * A column of numeric data with a name.
 * @author Jeremy Johnson
 *
 */
@PersistenceCapable
public class DataColumn implements Serializable {
	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;
	
	@Persistent
	private String name;
	@Persistent
	private ArrayList<DataValue> values;
	
	@Persistent
	private boolean lastSortReverse = false;
	
	/**
	 * @param name The name of the column.
	 * @param values All the values in the column.
	 */
	public DataColumn(String name, DataValue[] values) {
		this.name = name;
		
		this.values = new ArrayList<DataValue>();
		for (int i=0; i< values.length; i++) {
			this.values.add(values[i]);
		}
	}
	
	public DataColumn() {
	}

	/**
	 * @return The name of the column.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return All the values in the column.
	 */
	public DataValue[] getValues() {
		DataValue[] ret = new DataValue[values.size()];
		for (int i=0; i < values.size(); i++) {
			ret[i] = values.get(i);
		}
		return ret;
	}
	
	
	/**
	 * Permute the column data by an array of indices.  For example, given
	 * values = [111, 222, 333] and indices = [3, 1, 2],
	 * the result is values = [333, 111, 222]
	 * @param indices The indices to permute by
	 */
	public void permuteByIndices(Integer[] indices) {
		DataValue[] newValues = new DataValue[values.size()];
		// Compute the values after the permutation into a temporary array
		for (int i = 0; i < values.size(); i++) {
			newValues[i] = values.get(indices[i]);
		}
		
		// Copy the result into values.
		for (int i = 0; i < values.size(); i++) {
			values.set(i, newValues[i]);
		}
	}
	
	/**
	 * @param index The index of the value.
	 * @return The value.
	 */
	public DataValue getValue(int index) {
		return values.get(index);
	}
	
	/**
	 * @return The number of elements in the column.
	 */
	public int getCount() {
		return values.size();
	}
	
	/**
	 * @return True if the last sort was a reverse sort.
	 */
	public boolean getLastSortReverse() {
		return lastSortReverse;
	}

	/**
	 * Toggle if the last sort was a reverse sort.
	 */
	public void toggleLastSort() {
		lastSortReverse = !lastSortReverse;
	}
	
	/**
	 * @return The average value in the column.
	 */
	public double getAverage() {	
		double sum = 0;
		int count = 0;
		
		for (int i=0; i < values.size(); i++) {
			if (values.get(i).isNumeric()) {
				sum += values.get(i).toDouble();
				count++;
			}
		}
		
		// No numeric values.
		if (count == 0) {
			return Double.NaN;
		}
		return sum / count;
	}
	
	/**
	 * @return The median in the column.
	 */
	public double getMedian() {
		DataValue[] sortedVals = new DataValue[values.size()];
		
		for (int i = 0; i < values.size(); i++) {
			sortedVals[i] = values.get(i);
		}
		Arrays.sort(sortedVals);
		
		int endOfNumeric = sortedVals.length;
		for (int i = 0; i < sortedVals.length; i++) {
			if (!sortedVals[i].isNumeric()) {
				endOfNumeric = i;
				break;
			}
		}
		
		// No numeric values
		if (endOfNumeric == 0) {
			return Double.NaN;
		}
		
		if (endOfNumeric % 2 == 1) {
			return sortedVals[endOfNumeric / 2].toDouble();
		} else {
			double a = sortedVals[endOfNumeric / 2].toDouble();
			double b = sortedVals[endOfNumeric / 2 - 1].toDouble();
			return (a + b) / 2;
		}
	}
	
	/**
	 * @return The mode of the column.
	 */
	public double getMode() {
		HashMap<Double, Integer> counts = new HashMap<Double, Integer>();
		for (DataValue d : values) {
			if (d.isNumeric()) {
				Integer oldVal = counts.get(d.toDouble());
				counts.put(d.toDouble(), oldVal == null ? 1 : oldVal + 1);
			}
		}
		
		// No numeric data
		if (counts.size() == 0) {
			return Double.NaN;
		}
		
		int maxCount = -1;
		double mode = Double.NEGATIVE_INFINITY;
		
		for (Double d : counts.keySet()) {
			int count = counts.get(d);
			if (count > maxCount) {
				maxCount = count;
				mode = d;
			}
		}
		return mode;
	}
	
	/**
	 * @return The standard deviation of the column.
	 */
	public double getStdev() {
		return Math.sqrt(getVariance());
	}
	
	/**
	 * @return The standard deviation of the column.
	 */
	public double getVariance() {
		double av = getAverage();
		double sum = 0;
		double count = 0;
		
		
		for (int i = 0; i < values.size(); i++) {
			if (values.get(i).isNumeric()) {
				sum += Math.pow(values.get(i).toDouble() - av, 2);
				count++;
			}
		}
		
		// Standard deviation doesn't make sense with 0 or 1 numeric values.
		if (count == 0 || count == 1) {
			return Double.NaN;
		}
		return sum / (count - 1);
	}
}
