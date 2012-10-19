package com.google.gwt.project.climateproject.client;

import java.util.HashMap;

import com.google.gwt.junit.client.GWTTestCase;

public class JsonParserTests extends GWTTestCase {
	private HashMap<String, Data> dataMap = new HashMap<String, Data>();

	public String getModuleName() {
	    return "com.google.gwt.project.climateproject.ClimateProject";
	}
	
	public void assertArrayEquals(DataValue[] a, DataValue[] b, double tol) {
		if (a.length != b.length) {
			fail("Arrays different length");
		}
		for (int i = 0; i < a.length; i++) {
			if (a[i].isNumeric() && b[i].isNumeric()) {
				assertEquals(a[i].toDouble(), b[i].toDouble(), tol);
			} else {
				assert(a[i].equals(b[i]));
			}
		}
	}

	public void testParseEmptyData() throws Exception {
		String js = "[]";
		Data data = JsonParser.getInstance().parseData("empty", js, dataMap);
		assert(data.getColumnCount() == 0);
	}
	
	public void testParse() throws Exception {
		String js = "[{'a' : 1, 'b' : 2, 'c' : 3}, {'a' : 4, 'b' : null, 'c' : ''}, " +
				"{'a' : 5, 'b' : 6, 'c' : 7}]";
		Data data = JsonParser.getInstance().parseData("nonEmpty", js, dataMap);
		
		DataValue[] asExp = {new DataValue("1"),
				new DataValue("4"),
				new DataValue("5")};
		DataValue[] bsExp = {new DataValue("2"),
				new DataValue("null"),
				new DataValue("6")};
		DataValue[] csExp = {new DataValue("3"),
				new DataValue(""),
				new DataValue("7")};
		
		assert(new DataValue("null").equals(new DataValue("null")));
		DataColumn a = data.getColumn("a");
		DataColumn b = data.getColumn("b");
		DataColumn c = data.getColumn("c");
		assertNotNull(a);
		assertNotNull(b);
		assertNotNull(c);
		DataValue[] as = a.getValues();
		DataValue[] bs = b.getValues();
		DataValue[] cs = c.getValues();
		assertArrayEquals(asExp, as, 0.001);
		assertArrayEquals(bsExp, bs, 0.001);
		assertArrayEquals(csExp, cs, 0.001);
	}
}
