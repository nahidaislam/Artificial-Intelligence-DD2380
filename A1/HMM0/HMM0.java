import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.List;

import java.math.BigDecimal;

/*
This program takes a transition matrix, emission matrix
and initial state probability distribution and computes
the emission probability distribution.
*/

public class HMM0 {

//Reads the input as strings and creating a matrix
private static double[][] strToMatrix(String s){

	String[] strvec = s.split(" ");

//number of row
	int row = Integer.parseInt(strvec[0]);
//number of column
	int col = Integer.parseInt(strvec[1]);
//create the matrix with the dimensions
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

public static void main(String[] args) throws IOException {

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

		// making A, B and pi matrix
		double[][] A = strToMatrix(linelist.get(0));
		double[][] B = strToMatrix(linelist.get(1));
		double[][] pi = strToMatrix(linelist.get(2));
		double[][] temp = Matrix.multiply(pi, A);

		// using BigDecimal for more precision
		BigDecimal[][] bgres = Matrix.bgmultiply(temp, B);

		System.out.print(bgres.length + " " + bgres[0].length + " ");
		for(int i = 0; i < bgres.length; i++){ for(int j = 0; j < bgres[i].length; j++){
				System.out.print(bgres[i][j] + " ");
			}
			System.out.println();
		}

}


}
