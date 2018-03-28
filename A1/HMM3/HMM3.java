/*
This program takes a starting guess of a HMM
(transition matrix, emission matrix and initial
state probability distribution)
and a sequence of emissions and trains the HMM
to maximize the probability of observing the
given sequence of emissions.

To run:
javac *.java
java HMM3 < input_file

*/

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.List;

import java.util.concurrent.ThreadLocalRandom;

public class HMM3 {

static int N, T, M;

private static void alphaPass(double[][] alpha, double[][] A, double[][] B, int[] em, double[] pi, double[] scale_values){

	// compute alpha_0(i)
	double c0 = 0;
	for(int i = 0; i < N; i++){
		alpha[0][i] = pi[i] * B[i][em[0]];
		c0 += alpha[0][i];
	}

	// scale alpha_0(i)
	c0 = 1/c0;
	scale_values[0] = c0;

	for(int i = 0; i < N; i++)
		alpha[0][i] = c0 * alpha[0][i];

	// compute alpha_t(i)
	double ct;
	for(int t = 1; t < T; t++){
		ct = 0;
		for(int i = 0; i < N; i++){
			alpha[t][i] = 0;
			for(int j = 0; j < N; j++){
				alpha[t][i] += alpha[t-1][j] * A[j][i];
			}
			alpha[t][i] = alpha[t][i] * B[i][em[t]];
			ct = ct + alpha[t][i];
		}

	// scale alpha_t(i)
		ct = 1/ct;
		scale_values[t] = ct;
		for(int i = 0; i < N; i++)
			alpha[t][i] = ct * alpha[t][i];
	}

}

private static void betaPass(double[][] beta, double[][] A, double[][] B, int[] em, double[] scale_values){

	// compute beta_0(i), scaled by C_T-1
	for(int i = 0; i < N; i++)
		beta[T-1][i] = scale_values[T-1];

	// compute beta_t(i)
	for(int t = T-2; t > 0; t--){
		for(int i = 0; i < N; i++){
			beta[t][i] = 0;
			for(int j = 0; j < N; j++){
				beta[t][i] += A[i][j] * B[j][em[t+1]] * beta[t+1][j];
			}
			// scale beta_t(i) with the same factor as alpha_t(i)
			beta[t][i] = beta[t][i] * scale_values[t];
		}
	}

}


private static void gammaDiGamma(double[][][] digamma, double[][] gamma, double[][] A, double[][] B, int[] em, double[][] alpha, double[][] beta){

	double denom, numer;
	for(int t = 0; t < T-1; t++){
		denom = 0;
		for(int i = 0; i < N; i++){
			for(int j = 0; j < N; j++){
				denom = denom + alpha[t][i] * A[i][j] * B[j][em[t+1]] * beta[t+1][j];
			}
		}
		for(int i = 0; i < N; i++){
			gamma[t][i] = 0;
			for(int j = 0; j < N; j++){
				digamma[t][i][j] = (alpha[t][i] * A[i][j] * B[j][em[t+1]] * beta[t+1][j]) / denom;
				gamma[t][i] += digamma[t][i][j];
			}
		}
	}
	// special case for gamma_T-1(i)
//	denom = Matrix.sum(alpha[T-1]);
//	gamma[T-1] = Matrix.elementWiseDivide(alpha[T-1], denom);

	denom = 0;
	for(int i = 0; i < N; i++)
		denom = denom + alpha[T-1][i];

	for(int i = 0; i < N; i++)
		gamma[T-1][i] = alpha[T-1][i] / denom;

}

private static void recomputeA(double[][] A, double[][][] digamma, double[][] gamma){

	double numer, denom;
	for(int i = 0; i < N; i++){
		for(int j = 0; j < N; j++){
			numer = 0;
			denom = 0;
			for(int t = 0; t < T - 1; t++){
				numer += digamma[t][i][j];
				denom += gamma[t][i];
			}
			A[i][j] = numer / denom;
		}
	}

}

private static void recomputeB(double[][] B, int[] em, double[][] gamma){

	double denom, numer;
	for(int i = 0; i < N; i++){
		for(int j = 0; j < M; j++){
			numer = 0;
			denom = 0;
			for(int t = 0; t < T; t++){
				if(em[t] == j) numer += gamma[t][i];
				denom += gamma[t][i];
			}
			B[i][j] = numer/denom;
		}
	}

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
    } catch (IOException e) {
        e.printStackTrace();
    }

		// making A, B matrix, pi vector, and sequence of emissions vector
		double[][] A = Matrix.strToMatrix(linelist.get(0));
		double[][] B = Matrix.strToMatrix(linelist.get(1));
		double[] pi = Matrix.strToVector(linelist.get(2));
		int[] em = Matrix.strToVectorInt("1 " + linelist.get(3));

		N = A.length; // number of states
		T = em.length; // number of time steps
		M = B[0].length; // number of possible observations

		double[][] alpha = new double[T][N];
		double[][] beta = new double[T][N];

		double[][][] digamma = new double[T][N][N];
		double[][] gamma = new double[T][N];

		double[] scale_values = new double[T];

		int maxIters = 150000; // maximum number of iterations
		double oldLogProb = Double.NEGATIVE_INFINITY;
		int iters = 0;
		double logProb = 0;

		// repeat until convergence
		while(true){

			// fill up alpha matrix
			alphaPass(alpha, A, B, em, pi, scale_values);

			// fill up beta matrix
			betaPass(beta, A, B, em, scale_values);

			// calculate di-gamma and gamma
			gammaDiGamma(digamma, gamma, A, B, em, alpha, beta);

			// re-estimate A, B and pi
			pi = gamma[0];
			recomputeA(A, digamma, gamma);
			recomputeB(B, em, gamma);

			// compute probability of emissions given new model
			logProb = 0;
			double prob = 1;

			for(int t = 0; t < T; t++){
				logProb += Math.log(scale_values[t]);
				prob *= scale_values[t];
			}

			logProb = -logProb;
			iters++;



			if(iters < maxIters && logProb > oldLogProb){
				oldLogProb = logProb;
				continue;
			} else {
				break;
			}
		}

		String AStr = Matrix.MatrixToString(A);
		String BStr = Matrix.MatrixToString(B);


		System.out.println(AStr);
		System.out.println(BStr);

		//System.out.println(iters);

		//Matrix.printVector(pi);
	}

}
