/*
This simple program multiplies two double matrices.
Uses BigDecimal in order to be more accurate.
*/

import java.math.BigDecimal;


public class Matrix {

	public static void printVector(double[] a){
			System.out.println();

		for(int i = 0; i < a.length; i++)
			System.out.print(a[i] + " ");
		System.out.println();
					System.out.println();

	}

	public static void printVector(int[] a){
			System.out.println();

		for(int i = 0; i < a.length; i++){
			System.out.print(a[i] + " ");
		}
		System.out.println();
					System.out.println();

	}

	public static void printMatrix(double[][] a){
			System.out.println();

		for(int i = 0; i < a.length; i++){ for(int j = 0; j < a[0].length; j++){
				System.out.print(a[i][j] + " ");
			}
			System.out.println();
		}
			System.out.println();

	}

	public static void printMatrix(int[][] a){
			System.out.println();

		for(int i = 0; i < a.length; i++){ for(int j = 0; j < a[0].length; j++){
				System.out.print(a[i][j] + " ");
			}
			System.out.println();
		}
			System.out.println();

	}



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


    /* returns the element wise product of two vectors a.*b */
    public static double[] elementWiseMultiply(double[] a, double[] b){

    	if(a.length != b.length) throw new RuntimeException("Illegal vector dimensions.");

			double[] res = new double[a.length];

			for(int i = 0; i < a.length; i++)
				res[i] = (a[i] * b[i]);

			return res;

    }

    /* returns the element wise divide of vector a by scalar value b: a./b */
    public static double[] elementWiseDivide(double[] a, double b){

			double[] res = new double[a.length];

			for(int i = 0; i < a.length; i++)
				res[i] = (a[i] / b);

			return res;

    }

    /* vector-matrix : returns the element wise product of vector a and matrix b */
    public static double[][] elementWiseMultiply(double[] a, double[][] b){

    	if(a.length != b.length) throw new RuntimeException("Illegal vector dimensions.");

			double[][] res = new double[b.length][b[0].length];

			for(int i = 0; i < b.length; i++) for(int j = 0; j < b[0].length; j++)
				res[i][j] = (a[i] * b[i][j]);

			return res;

    }


    /* returns a row vector with the max element
    	 of each _row_ of a */
    public static double[] maxrow(double[][] a){

    	double[] res = new double[a.length];
    	double max = -1;
    	for(int i = 0; i < a.length; i++){
    		max = max(a[i]);
    		res[i] = max;
    	}

    	return res;

    }

    /* works like maxrow, but returns the indices
    	instead of the values of the max elements */
    public static int[] argmaxrow(double[][] a){

    	int[] res = new int[a.length];
    	int max = -1;
    	for(int i = 0; i < a.length; i++){
    		max = argmax(a[i]);
    		res[i] = max;
    	}

    	return res;

    }

    public static double[][] transpose(double [][] m){
        double[][] temp = new double[m[0].length][m.length];
        for (int i = 0; i < m.length; i++)
            for (int j = 0; j < m[0].length; j++)
                temp[j][i] = m[i][j];
        return temp;
    }

    public static int[][] transpose(int [][] m){
        int[][] temp = new int[m[0].length][m.length];
        for (int i = 0; i < m.length; i++)
            for (int j = 0; j < m[0].length; j++)
                temp[j][i] = m[i][j];
        return temp;
    }


    /* vector-matrix multiplication:
    returns a*b where a is a vector and b is a matrix */
    public static double[] multiply(double[] x, double[][] a) {
        int m = a.length;
        int n = a[0].length;
        if (x.length != m) throw new RuntimeException("Illegal matrix dimensions.");
        double[] y = new double[n];
        for (int j = 0; j < n; j++)
            for (int i = 0; i < m; i++)
                y[j] += a[i][j] * x[i];
        return y;
    }


    // turn col with nr "colnum" from a into c
    public static void changeCol(double[][] a, double[] c, int colnum){

    	if(a.length != c.length) throw new RuntimeException("Illegal matrix dimensions.");

    	for(int i = 0; i < a.length; i++)
    		a[i][colnum] = c[i];

    }

    // turn col with nr "colnum" from a into c
    public static void changeCol(int[][] a, int[] c, int colnum){

    	if(a.length != c.length) throw new RuntimeException("Illegal matrix dimensions.");

    	for(int i = 0; i < a.length; i++)
    		a[i][colnum] = c[i];

    }

    public static double[] getCol(double[][] a, int colnum){

    	if(colnum < 0 || colnum > a[0].length) throw new RuntimeException("getCol: illegal parameter");

    	double[] c = new double[a.length];

    	for(int i = 0; i < a.length; i++)
    		c[i] = a[i][colnum];

  		return c;

    }

    public static int[] getCol(int[][] a, int colnum){

    	int[] c = new int[a.length];

    	for(int i = 0; i < a.length; i++)
    		c[i] = a[i][colnum];

  		return c;

    }

    /* computes the sum of all elements in a vector */
    public static double sum(double[] a){

		  double sum = 0;
		  for(int i = 0; i < a.length; i++)
		  	sum += a[i];

	  	return sum;

    }

    public static double sum(double[][] a){

		  double sum = 0;
		  for(int i = 0; i < a.length; i++) for(int j = 0; j < a[0].length; j++)
		  	sum += a[i][j];

	  	return sum;

    }

    public static double sum(double[][][] a){

		  double sum = 0;
		  for(int i = 0; i < a.length; i++) for(int j = 0; j < a[0].length; j++) for(int k = 0; k < a[0][0].length; k++)
		  	sum += a[i][j][k];

	  	return sum;

    }

    public static int argmin(double[] a){
		  double min = 1e15;
		  int minIndex = -1;
		  for(int i = 0; i < a.length; i++)
		  	if(a[i] < min){ min = a[i]; minIndex = i; }
			return minIndex;
    }

    public static int argmax(double[] a){
		  double max = -1;
		  int maxIndex = -1;
		  for(int i = 0; i < a.length; i++)
		  	if(a[i] > max){ max = a[i]; maxIndex = i; }
			return maxIndex;
    }

    public static double max(double[] a){
		  double max = 0;
		  for(int i = 0; i < a.length; i++)
		  	if(a[i] > max){ max = a[i]; }
			return max;
    }

  	public static String MatrixToString(double[][] a){

  		String s = "";

  		s = a.length + " " + a[0].length + " ";

  		for(int i = 0; i < a.length; i++) for(int j = 0; j < a[0].length; j++){
  			s += String.valueOf(a[i][j]);
  			s += " ";
			}

  		return s;
  	}
/*
s = string starting with the number of rows and
columns in the matrix and then all the elements
of the matrix, in one line

matrix = matrix represented by the string s
*/
	public static double[][] strToMatrix(String s){

		String[] strvec = s.split(" ");

		int row = Integer.parseInt(strvec[0]);
		int col = Integer.parseInt(strvec[1]);
		double[][] matrix = new double[row][col];

		int index, irow, icol;
		for(int i = 2; i < strvec.length; i++){
			index = i-2;
			irow = index/col;
			icol = Math.max(0, index-(irow*col));
			matrix[irow][icol] = Double.parseDouble(strvec[i]);;
		}

		return matrix;

	}


	public static int[][] strToMatrixInt(String s){

		String[] strvec = s.split(" ");

		int row = Integer.parseInt(strvec[0]);
		int col = Integer.parseInt(strvec[1]);
		int[][] matrix = new int[row][col];

		int index, irow, icol;
		for(int i = 2; i < strvec.length; i++){
			index = i-2;
			irow = index/col;
			icol = Math.max(0, index-(irow*col));
			matrix[irow][icol] = Integer.parseInt(strvec[i]);;
		}

		return matrix;

	}

	public static double[] strToVector(String s){

		String[] strvec = s.split(" ");
		int row = Integer.parseInt(strvec[0]);
		if(row!=1){ throw new RuntimeException("Illegal vector dimensions."); }
		int col = Integer.parseInt(strvec[1]);
		double[] vector = new double[col];

		int index;
		for(int i = 2; i < strvec.length; i++){
			index = i-2;
			vector[index] = Double.parseDouble(strvec[i]);;
		}

		return vector;

	}

	public static int[] strToVectorInt(String s){

		String[] strvec = s.split(" ");

		int row = Integer.parseInt(strvec[0]);
		if(row!=1){ throw new RuntimeException("Illegal vector dimensions."); }
		int col = Integer.parseInt(strvec[1]);
		int[] vector = new int[col];

		int index;
		for(int i = 2; i < strvec.length; i++){
			index = i-2;
			vector[index] = Integer.parseInt(strvec[i]);;
		}

		return vector;

	}
	public static int[] strToVectorInt(String s, int limit){

		String[] strvec = s.split(" ");

		int row = Integer.parseInt(strvec[0]);
		if(row!=1){ throw new RuntimeException("Illegal vector dimensions."); }

		int[] vector = new int[limit];

		int index;
		for(int i = 2; i < limit+2; i++){
			index = i-2;
			vector[index] = Integer.parseInt(strvec[i]);;
		}

		return vector;

	}

	public static double distance(double[][] a, double[][] b){

		double dist = 0.0;

		if(a.length != b.length || a[0].length != b[0].length){ throw new RuntimeException("Illegal vector dimensions."); }

		for(int i = 0; i < a.length; i++) for(int j = 0; j < a[0].length; j++)
			dist += Math.abs(a[i][j] - b[i][j]);

		return dist;
	}

	public static double distance(double[] a, double[] b){

		double dist = 0.0;

		if(a.length != b.length){ throw new RuntimeException("Illegal vector dimensions."); }

		for(int i = 0; i < a.length; i++)
			dist += Math.abs(a[i] - b[i]);

		return dist;
	}

	public static double[][] zeros(int row, int col){

	double[][] a = new double[row][col];
	for(int i = 0; i < row; i++) for(int j = 0; j < col; j++)
		a[i][j] = 0;

	return a;
	}

	public static double[] zeros(int col){

	double[] a = new double[col];

	for(int j = 0; j < col; j++)
		a[j] = 0;

	return a;

	}

}
