package com.google.gwt.project.climateproject.client;

import junit.framework.TestCase;

public class DataColumnTests extends TestCase {
	private DataColumn colA;
	private DataColumn colB;
	private DataColumn colC;
	protected void setUp() throws Exception {
		super.setUp();
		double[] aaa = {0, 2, 3, 5, 5};
		double[] bbb = {5, 10, 8, 9};
		double[] ccc = {2, 2, 2, 2, 2};
		
		colA = new DataColumn("aaa", arrToDataValues(aaa));
		colB = new DataColumn("bbb", arrToDataValues(bbb));
		colC = new DataColumn("ccc", arrToDataValues(ccc));
	}
	
	private DataValue[] arrToDataValues(double[] d) {
		DataValue[] res = new DataValue[d.length];
		for (int i = 0; i < d.length; i++) {
			res[i] = new DataValue("" + d[i]);
		}
		return res;
	}
	
	public void testGetValue() throws Exception {
		assertEquals(colA.getValue(2).toDouble(), 3.0, 0.001);
		assertEquals(colB.getValue(0).toDouble(), 5.0, 0.001);
		assertEquals(colC.getValue(4).toDouble(), 2.0, 0.001);
	}
	
	public void testGetCount() throws Exception {
		assertEquals(colA.getCount(), 5);
		assertEquals(colB.getCount(), 4);
		assertEquals(colC.getCount(), 5);		
	}
	
	public void testAverage() throws Exception {
		assertEquals(colA.getAverage(), 3.0);
		assertEquals(colB.getAverage(), 8.0);
		assertEquals(colC.getAverage(), 2.0);
	}
	
	public void testMedia() throws Exception {
		assertEquals(colA.getMedian(), 3.0);
		assertEquals(colB.getMedian(), 8.5);
		assertEquals(colC.getMedian(), 2.0);
	}
	
	public void testMode() throws Exception {
		assertEquals(colA.getMode(), 5.0);
		assertEquals(colC.getMode(), 2.0);
	}
	
	public void testStdev() throws Exception {
		assertEquals(colA.getStdev(), 2.12132, 0.001);
		assertEquals(colB.getStdev(), 2.16024, 0.001);
		assertEquals(colC.getStdev(), 0.0, 0.001);
	}

	public void testVariance() throws Exception {
		assertEquals(colA.getVariance(), 4.5, 0.001);
		assertEquals(colB.getVariance(), 4.66666, 0.001);
		assertEquals(colC.getVariance(), 0.0, 0.001);
	}
}
