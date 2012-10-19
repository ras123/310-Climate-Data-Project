package com.google.gwt.project.climateproject.client;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;

/**
 * A class representing the climate data in the application.
 * @author Jeremy Johnson
 *
 */
public class Data {
	
	// Store the columns in a hashmap so they can get retrieved by column name.
	private HashMap<String, DataColumn> columns;
	private String name;
	
	public Data(String name, HashMap<String, Data> dataMap) {
		columns = new HashMap<String, DataColumn>();
		this.name = name;

		while (dataMap.containsKey(this.name)) {
			this.name = this.name + "*";
		}		
	}
	
	/**
	 * Add a column to the data set.
	 * @param column
	 */
	public void addColumn(DataColumn column) {
		columns.put(column.getName(), column);
	}
	
	/**
	 * Retrieve a column from the data set.
	 * @param name The name of the column.
	 * @return The column.
	 */
	public DataColumn getColumn(String name) {
		return columns.get(name);
	}
	
	/**
	 * @return The number of columns in the dataset.
	 */
	public int getColumnCount() {
		return columns.size();
	}
	
	/**
	 * @return All the columns in the dataset.
	 */
	public Collection<DataColumn> getColumns()
	{
		return columns.values();
	}
	
	/**
	 * @return The name associated with the dataset
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 
	 */
	public void sortByColumn(final DataColumn col, boolean reverse) {
		final int compVal = reverse ? 1 : -1;
		
		// Initialize an array of indices.
		Integer[] indices = new Integer[col.getCount()];
		for (int i = 0; i < indices.length; i++) {
			indices[i] = i;
		}
		
		// Sort the indices, so they represent the permutation required to sort the data
		// According to the specified column.
		Arrays.sort(indices, new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				DataValue a = col.getValue(o1);
				DataValue b = col.getValue(o2);
				return compVal * a.compareTo(b);
			}
		});
		
		// Rearrange each column with the new indices
		for (DataColumn c : getColumns()) {
			c.permuteByIndices(indices);
		}
	}
	
	/**
	 * Sorts the data by a specified column.
	 * @param col The column to sort by.
	 */
	public void sortByColumn(final DataColumn col) {
		// Obtain the column to sort and determine the sort order
		boolean reverse = col.getLastSortReverse();
		col.toggleLastSort();
		sortByColumn(col, reverse);
	}
}
