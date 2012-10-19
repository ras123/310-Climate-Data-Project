package com.google.gwt.project.climateproject.client;

import java.util.HashMap;

import com.google.gwt.junit.client.GWTTestCase;

public class XmlParserTests extends GWTTestCase {
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
		String xml = "<Extents></Extents>";
		Data data = XmlParser.getInstance().parseData("empty", xml, dataMap);
		assert(data.getColumnCount() == 0);
	}
	
	public void testParse() throws Exception {
		String xml = "<Extents><Record><a>1</a><b>2</b><c>3</c></Record>" +
				"<Record><a>4</a><b>null</b><c></c></Record>" +
				"<Record><a>5</a><b>6</b><c>7</c></Record></Extents>";
		Data data = XmlParser.getInstance().parseData("nonEmpty", xml, dataMap);

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
