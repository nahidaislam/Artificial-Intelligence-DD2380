import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.List;

//import java.math.BigDecimal;


public class HMM2{


private static void print_vector(double[] a){
//		System.out.println();

	for(int i = 0; i < a.length; i++)
		System.out.print(a[i] + " ");
	System.out.println();
//				System.out.println();

}

private static void print_vector(int[] a){
//		System.out.println();

	for(int i = 0; i < a.length; i++){
		System.out.print(a[i] + " ");
	}
	System.out.println();
//				System.out.println();

}

private static void print_matrix(double[][] a){
		System.out.println();

	for(int i = 0; i < a.length; i++){ for(int j = 0; j < a[0].length; j++){
			System.out.print(a[i][j] + " ");
		}
		System.out.println();
	}
		System.out.println();

}

private static void print_matrix(int[][] a){
		System.out.println();

	for(int i = 0; i < a.length; i++){ for(int j = 0; j < a[0].length; j++){
			System.out.print(a[i][j] + " ");
		}
		System.out.println();
	}
		System.out.println();

}


/*
s = string starting with the number of rows and
columns in the matrix and then all the elements
of the matrix, in one line

matrix = matrix represented by the string s
*/
private static double[][] strToMatrix(String s){

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


private static int[][] strToMatrixInt(String s){

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

// backtracking across the paths matrix
// to find the best sequence of states
private static int[] getStateSeq(int[][] paths, int endState){

	int[] stateSeq = new int[paths[0].length];
	stateSeq[paths[0].length-1] = endState;
	int curr = endState;
	for(int i = paths[0].length-1; i > 0; i--){
		stateSeq[i-1] = paths[curr][i];
		curr = paths[curr][i];
	}

	return stateSeq;


}

private static void viterbi(double[][] A, double[][] B, int[] em, int curr){

	if(curr >= em.length-1) return;

	for(int index = curr; index < em.length; index++){
		double[][] deltax_all = Matrix.transpose(Matrix.elementWiseMultiply(Matrix.getCol(deltas, index-1), A));
		deltax_all = Matrix.elementWiseMultiply(Matrix.getCol(B, em[index]), deltax_all);

		double[] deltax = Matrix.maxrow(deltax_all);

		double[] deltasCol = Matrix.getCol(deltas, index);

		Matrix.changeCol(deltas, deltax, index);

		int[] pathx = Matrix.argmaxrow(deltax_all);
		Matrix.changeCol(paths, pathx, index);

	}

}


static int[][] paths;
static double[][] deltas;


public static void main(String[] args){

	// read input
		List<String> linelist = new ArrayList<String>();
    String line = null;
    int number = 0;
    try {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        line = br.readLine();

  			while (line != null && !line.isEmpty()) {
					linelist.add(line);
					line = br.readLine();
				}
    } catch (NumberFormatException ex) {
       System.out.println("Not a number !");
    } catch (IOException e) {
        e.printStackTrace();
    }

		// printing
//		for(int i = 0; i < linelist.size(); i++){
//			System.out.println(linelist.get(i));
//		}

		// making A, B and pi matrix, and sequence of emissions
		double[][] A = strToMatrix(linelist.get(0));
		double[][] B = strToMatrix(linelist.get(1));
		double[][] pi = strToMatrix(linelist.get(2));
		int[][] em = strToMatrixInt("1 " + linelist.get(3));

		// initialise paths and deltas
		deltas = new double[A.length][em[0].length];

		paths = new int[A.length][em[0].length];

		// calculate first delta
		double[] Bcol = Matrix.getCol(B, em[0][0]);

		double[] delta1 = Matrix.elementWiseMultiply(pi[0], Bcol);

		//take the delta1 and put it in deltas
		Matrix.changeCol(deltas, delta1, 0);

		viterbi(A, B, em[0], 1);

		// last step = the most probable of the states
		// in the last timestep, represented by the last
		// column of deltas

//		System.out.println("deltas numcol = " + deltas[0].length + " deltas numrow = " + deltas.length + " em[0].length-1 = " +
//		(em[0].length-1));
		int last_step = Matrix.argmax(Matrix.getCol(deltas, em[0].length-1));

		int[] stateSeq = getStateSeq(paths, last_step);

//		System.out.println("stateSeq");
		print_vector(stateSeq);

}

}
