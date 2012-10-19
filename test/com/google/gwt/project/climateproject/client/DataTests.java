package com.google.gwt.project.climateproject.client;

import java.util.HashMap;

import junit.framework.TestCase;

public class DataTests extends TestCase {

	private Data data;
	private DataColumn colA;
	private DataColumn colB;
	private DataColumn colC;
	private HashMap<String, Data> dataMap = new HashMap<String, Data>();

	
	protected void setUp() throws Exception {
		super.setUp();
		int len = 5;
		double[] aaa = {0, 2, 3, 5, 5};
		double[] bbb = {5, 10, 8, 9, 2};
		double[] ccc = {1, 3, -1, 7, 2};
		DataValue[] aaad = new DataValue[len];
		DataValue[] bbbd = new DataValue[len];
		DataValue[] cccd = new DataValue[len];
		for (int i = 0; i < len; i++) {
			aaad[i] = new DataValue(""+aaa[i]);
			bbbd[i] = new DataValue(""+bbb[i]);
			cccd[i] = new DataValue(""+ccc[i]);
		}
		
		colA = new DataColumn("aaa", aaad);
		colB = new DataColumn("bbb", bbbd);
		colC = new DataColumn("ccc", cccd);
		data = new Data("Test Data", dataMap);
		data.addColumn(colA);
		data.addColumn(colB);
		data.addColumn(colC);
	}
	
	public void testGetColumn() throws Exception {
		assertEquals(data.getColumn("aaa"), colA);
		assertEquals(data.getColumn("bbb"), colB);
		assertEquals(data.getColumn("ccc"), colC);
	}

	public void assertArrayEquals(DataValue[] a, DataValue[] b, double tol) {
		if (a.length != b.length) {
			fail("Arrays different length");
		}
		for (int i = 0; i < a.length; i++) {
			if (a[i].isNumeric() && b[i].isNumeric()) {
				assertEquals(a[i].toDouble(), b[i].toDouble(), tol);
			} else {
				assertEquals(a[i], b[i]);
			}
		}
	}
	
	private DataValue[] arrToDataValues(double[] d) {
		DataValue[] res = new DataValue[d.length];
		for (int i = 0; i < d.length; i++) {
			res[i] = new DataValue("" + d[i]);
		}
		return res;
	}
	public void testSortByColumn() throws Exception {
		data.sortByColumn(colB);
		
		double[] expAv = {5, 0, 3, 5, 2};
		double[] expBv = {2, 5, 8, 9, 10};
		double[] expCv = {2, 1, -1, 7, 3};
		double[] expDv = {2, 5, 3, 0, 5};
		double[] expEv = {10, 9, 8, 5, 2};
		double[] expFv = {3, 7, -1, 1, 2};
		double[] expGv = {0, 2, 3, 5, 5};
		double[] expHv = {5, 10, 8, 9, 2};
		double[] expIv = {1, 3, -1, 7, 2};
		
		DataValue[] expA = arrToDataValues(expAv);
		DataValue[] expB = arrToDataValues(expBv);
		DataValue[] expC = arrToDataValues(expCv);
		DataValue[] expD = arrToDataValues(expDv);
		DataValue[] expE = arrToDataValues(expEv);
		DataValue[] expF = arrToDataValues(expFv);
		DataValue[] expG = arrToDataValues(expGv);
		DataValue[] expH = arrToDataValues(expHv);
		DataValue[] expI = arrToDataValues(expIv);

		
		
		assertArrayEquals(colA.getValues(), expA, 0.001);
		assertArrayEquals(colB.getValues(), expB, 0.001);
		assertArrayEquals(colC.getValues(), expC, 0.001);
		
		data.sortByColumn(colB);

		assertArrayEquals(colA.getValues(), expD, 0.001);
		assertArrayEquals(colB.getValues(), expE, 0.001);
		assertArrayEquals(colC.getValues(), expF, 0.001);
		
		data.sortByColumn(colA);
		
		assertArrayEquals(colA.getValues(), expG, 0.001);
		assertArrayEquals(colB.getValues(), expH, 0.001);
		assertArrayEquals(colC.getValues(), expI, 0.001);
	}
}
