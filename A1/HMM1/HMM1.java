import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.List;

public class HMM1{

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

/*
	uses recursion (forward algorithm) to calculate the
	probability of the sequence of emissions em(1, .., curr)
	given in em happening given transition matrix A, emission
	probability matrix B on the	current timestep curr
*/
private static double calcProb(double[] alpha, double[][] A, double[][] B, int[] em, int curr){

if(curr == em.length) return Matrix.sum(alpha);

double[] new_alpha = Matrix.elementWiseMultiply(Matrix.multiply(alpha, A), Matrix.getCol(B, em[curr]));

//double sum = Matrix.sum(new_alpha);
//sum = 1/sum;
//new_alpha = Matrix.elementWiseMultiply(new_alpha, sum);
//for(int i = 0; i < new_alpha.length; i++) new_alpha[i] = new_alpha[i] * sum;
//print_vector(new_alpha);
return calcProb(new_alpha, A, B, em, curr+1);

}

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

		// first step of alpha-pass (forward algorithm)
		double[] alpha1 = Matrix.elementWiseMultiply(pi[0], Matrix.getCol(B, em[0][0]));

		//double w = Matrix.sum(alpha1);
		//w = 1/w;
		//new_alpha = Matrix.elementWiseMultiply(new_alpha, sum);
		//for(int i = 0; i < alpha1.length; i++) alpha1[i] = alpha1[i] * w;
		//print_vector(alpha1);
		// use recursive function to calculate final probability
		// of the given emissions
		double sum = calcProb(alpha1, A, B, em[0], 1);
		System.out.println(sum);


}

}
