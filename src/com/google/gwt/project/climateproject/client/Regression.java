package com.google.gwt.project.climateproject.client;

/*****************************************************************
This program was written by Dr. Benny Raphael.  
The purpose of this program is only to demonstrate some concepts 
discussed in the book "Fundamentals of Computer Aided Engineering" by
Benny Raphael and Ian F. C. Smith, John Wiley, UK, 2003. 
(http://www.wiley.com/WileyCDA/WileyTitle/productCd-0471487155.html)
The author (Benny Raphael) grants everyone permission to copy and
use this code freely provided that a) this copyright notice is not
modified b)  Any change to this code is clearly indicated c) the
user takes complete responsibility for the use, misuse or non-use of
this code.  No care has been made to ensure that the implementation
is efficient, or entirely accurate with respect to the concept that is 
demonstrated.
*******************************************************************/

public class Regression  {

	/*  Function to solve linear simultaneous equations of the form 
   		AX = Y
   		Returns true if successfully solved.  Else false
		The A matrix is stored as a one dimensional array such that the actualA[i][j]
		is obtained using the expression A[i*n+j] - n is the number of columns
		This is a static method.  So you can call this method directly without creating an instance of this class
		In fact you can remove all the code in this file except this function.  All other code is only for testing this function.
	 */
	public static boolean solveLinearSimultaneousEquations(double []A, double []Y,
			double []X) {

		int i,j,k;
		double sum, fact;
		int n = Y.length;	// The number of unknown variables = length of the array

		/*  This loop reduces equations from 0 to n  */
		for (i=0; i< n; i++) {

			/*  This loop sets the i-th column to zero from equations (i+1) to n */
			for (j=i+1; j< n; j++) {
				if ( A[j*n+i] == 0) continue;  /*  No need to reduce this equation */
				fact = A[i*n+i]/ A[j*n+i];

				/*  This loop updates coefficients in the j-th row from columns (i+1) to n  */
				for (k=i+1; k< n; k++)  A[j*n+k] = A[j*n+k]*fact - A[i*n+k];

				Y[j] = Y[j]*fact - Y[i];	// Updating the right hand side
			}
		}

		/* Back substitution */
		for (i=n-1; i>=0; i--) {

			if (A[i*n+i] == 0) return false;	// Diagonal element zero.  Equations cannot be solved

			sum = Y[i];
			for (k=i+1; k< n; k++)  sum = sum - A[i*n+k]*X[k];
			X[i] = sum/A[i*n+i];
		}

		return true;
	}
	//-----------------------------------------------------------------------


	/*  Function to multiply two matrices of dimensions nxm and mxp.  Result is in C */
	static void matrixProduct(int n, int m, int p, double []A, double []B, double []C) {

		int i, j, k;
		for (i=0; i< n; i++) {
			for (j=0; j< p; j++) {
				C[i*p+j] = 0;
				for (k=0; k< m; k++) C[i*p+j] += A[i*m+k]*B[k*p+j];
			}
		}
		return;
	}
	//-----------------------------------------------------------------------

	/*  Function to transpose a matrix A.  Result is in B */
	static void  transposeMatrix(int m, int n, double []A, double []B) {
		int i, j;

		for (i=0; i< n; i++)
			for (j=0; j< m; j++) 
				B[i*m+j] = A[j*n+i];
		
		return ;
	}
	//-----------------------------------------------------------------------

	static double average(int n, double []x) {
		int i;
		double sum;

		sum = 0;
		if (n == 0) return 0;
		for (i=0; i< n; i++)  sum += x[i];
		return sum/n;
	}
	//-----------------------------------------------------------------------


	public static boolean linearRegression(int m, int n, double []X, double []Y, 
			double []A, double []correlation) {

		double []XTX;
		double []XTY;
		double []XT;
		int i, j;
		double ybar, ycap,sumr, sumt;
		boolean stat;

		if (m < 1 || n < 1) return false;
		XT  = new double[n*m]; 
		XTX =  new double[n*n]; 
		XTY =  new double[n]; 
		
		transposeMatrix(m,n,X,XT);
		matrixProduct(n,m,n, XT, X,XTX);
		matrixProduct(n,m,1, XT, Y,XTY);
		stat = solveLinearSimultaneousEquations(XTX, XTY, A);

		if (!stat) return false;
		{
			ybar = average(n, Y);
			sumr = 0;
			sumt = 0;
			for (i=0; i< m; i++) {
				ycap = 0;
				for (j=0; j< n; j++) ycap += A[j]*X[i*n+j];
				sumr += (ycap - ybar)*(ycap - ybar);
				sumt += (Y[i] - ybar)*(Y[i] - ybar);
				/* printf("\t%8.2f\t%8.2f\n", ycap, Y[i] ); */
			}
			if (sumt == 0) correlation[0]=0;
			else correlation[0] = sumr/sumt;
		}

		return true;
	}
};

