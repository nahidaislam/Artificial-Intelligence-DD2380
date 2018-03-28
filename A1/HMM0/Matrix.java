/*
This simple program multiplies two double matrices.
Uses BigDecimal in order to be more accurate.
*/

import java.math.BigDecimal;


public class Matrix {

    public static double[][] multiply(double[][] a, double[][] b) {
    		long LONG_MILLION = 1000000L;
        int m1 = a.length;
        int n1 = a[0].length;
        int m2 = b.length;
        int n2 = b[0].length;
        if (n1 != m2) throw new RuntimeException("Illegal matrix dimensions.");
        double[][] c = new double[m1][n2];
        for (int i = 0; i < m1; i++)
            for (int j = 0; j < n2; j++)
                for (int k = 0; k < n1; k++) {
                	double num = a[i][k] * b[k][j];
                	long atril = (long)(a[i][k] * LONG_MILLION);
                	long btril = (long)(b[k][j] * LONG_MILLION);
                	long restril = atril * btril;
                	double resnorm = restril / 1.0e12;
                  c[i][j] += resnorm;
                }
        return c;
    }
 
     public static BigDecimal[][] bgmultiply(double[][] a, double[][] b) {
    		long LONG_MILLION = 1000000L;
        int m1 = a.length;
        int n1 = a[0].length;
        int m2 = b.length;
        int n2 = b[0].length;
        if (n1 != m2) throw new RuntimeException("Illegal matrix dimensions.");
        BigDecimal[][] c = new BigDecimal[m1][n2];
        
        for(int i = 0; i < c.length; i++) for(int j = 0; j < c[0].length; j++)
        	c[i][j] = new BigDecimal("0");
        
        
        for (int i = 0; i < m1; i++)
            for (int j = 0; j < n2; j++)
                for (int k = 0; k < n1; k++) {
                	BigDecimal bg  = new BigDecimal(Double.toString(a[i][k]));                	
                	BigDecimal bg2  = new BigDecimal(Double.toString(b[k][j]));
                	BigDecimal bg3 = bg.multiply(bg2);
                  c[i][j] = c[i][j].add(bg3);
                }
        return c;
    }

    
}
