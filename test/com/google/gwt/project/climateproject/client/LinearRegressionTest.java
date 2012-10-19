package com.google.gwt.project.climateproject.client;

import junit.framework.TestCase;

public class LinearRegressionTest extends TestCase {

	public void testRegression() throws Exception {
		double[] X1 = {2, 1, 3, 1, 4, 1};
		double[] Y1 = {2, 3, 4};
		double[] A = new double[2];
		double[] correlation = new double[1];
		Regression.linearRegression(3, 2, X1, Y1, A, correlation);
		
		double[] expRes1 = {1.0, 0.0};
		assertEquals(A[0], expRes1[0]);
		assertEquals(A[1], expRes1[1]);
	
		
		double[] X2 = {1, 1, 2, 1, 4, 1};
		double[] Y2 = {-0.5, 0, 1};
		Regression.linearRegression(3, 2, X2, Y2, A, correlation);
		
		double[] expRes2 = {0.5, -1.0};
		assertEquals(A[0], expRes2[0]);
		assertEquals(A[1], expRes2[1]);
	
		double[] X3 = {1, 1, 2, 1, 3, 1};
		double[] Y3 = {1, 0, 1};
		Regression.linearRegression(3, 2, X3, Y3, A, correlation);
		double[] expRes3 = {0, 0.667};
		assertEquals(A[0], expRes3[0], 0.001);
		assertEquals(A[1], expRes3[1], 0.001);		
	}	
}
