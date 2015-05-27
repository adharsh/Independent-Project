import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;

import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLDouble;

public class BooleanOperatorsNN {

	public static DenseMatrix64F feedForwardPropOneLayer( DenseMatrix64F input, DenseMatrix64F theta ){

		DenseMatrix64F hypothesis;
		DenseMatrix64F thetaTranspose = new DenseMatrix64F( theta.getNumCols(), theta.getNumRows() );
		CommonOps.transpose( theta, thetaTranspose );

		DenseMatrix64F inputWithBias = NNstaticmethods.addBias( input );
		hypothesis = new DenseMatrix64F( inputWithBias.getNumRows(), thetaTranspose.getNumCols() );
		CommonOps.mult( inputWithBias, thetaTranspose, hypothesis);
		hypothesis = NNstaticmethods.sigmoid( hypothesis );
		
		return hypothesis;
	}

	public static DenseMatrix64F feedForwardPropTwoLayers( DenseMatrix64F input, DenseMatrix64F theta1, DenseMatrix64F theta2){

		DenseMatrix64F thetaTranspose1 = new DenseMatrix64F( theta1.getNumCols(), theta1.getNumRows() );
		DenseMatrix64F thetaTranspose2 = new DenseMatrix64F( theta2.getNumCols(), theta2.getNumRows() );
		CommonOps.transpose( theta1, thetaTranspose1 );
		CommonOps.transpose( theta2, thetaTranspose2 );

		DenseMatrix64F inputWithBias = NNstaticmethods.addBias( input );

		DenseMatrix64F z2 = new DenseMatrix64F( inputWithBias.getNumRows(), thetaTranspose1.getNumCols());
		CommonOps.mult( inputWithBias, thetaTranspose1, z2);

		DenseMatrix64F a2 = new DenseMatrix64F( z2.getNumRows(), z2.getNumCols());
		a2 = NNstaticmethods.sigmoid(z2);

		a2 = NNstaticmethods.addBias(a2);
		DenseMatrix64F z3 = new DenseMatrix64F( a2.getNumRows(), thetaTranspose2.getNumCols());
		CommonOps.mult( a2, thetaTranspose2, z3);

		DenseMatrix64F a3 = new DenseMatrix64F( z3.getNumRows(), z3.getNumCols() );
		a3 = NNstaticmethods.sigmoid(z3);
		
		CommonOps.transpose(a3);
		
		return a3;
	}



	public static DenseMatrix64F ORneuralnetwork( DenseMatrix64F input ){

		File dataMat = new File( "/Users/ababu/Documents/JavaWorkspace/Independent Project/Data/ORweights.mat" );

		MatFileReader matFileReader = null;
		try {
			matFileReader = new MatFileReader( dataMat );
		} catch (IOException e) {
			e.printStackTrace();
		}

		MLDouble mlWeights = (MLDouble) matFileReader.getMLArray("weights");
		double[][] dWeights = mlWeights.getArray();
		DenseMatrix64F weights = new DenseMatrix64F(dWeights);

		DenseMatrix64F hypothesis = feedForwardPropOneLayer( input, weights );

		return hypothesis;
	}

	public static DenseMatrix64F ANDneuralnetwork( DenseMatrix64F input ){

		File dataMat = new File( "/Users/ababu/Documents/JavaWorkspace/Independent Project/Data/ANDweights.mat" );

		MatFileReader matFileReader = null;
		try {
			matFileReader = new MatFileReader( dataMat );
		} catch (IOException e) {
			e.printStackTrace();
		}

		MLDouble mlWeights = (MLDouble) matFileReader.getMLArray("weights");
		double[][] dWeights = mlWeights.getArray();
		DenseMatrix64F weights = new DenseMatrix64F(dWeights);

		DenseMatrix64F hypothesis = feedForwardPropOneLayer( input, weights );

		return hypothesis;
	}



	public static DenseMatrix64F XNORneuralnetwork( DenseMatrix64F input ){

		File dataMat = new File( "/Users/ababu/Documents/JavaWorkspace/Independent Project/Data/XNORweights.mat" );

		MatFileReader matFileReader = null;
		try {
			matFileReader = new MatFileReader( dataMat );
		} catch (IOException e) {
			e.printStackTrace();
		}

		MLDouble mlTheta1 = (MLDouble) matFileReader.getMLArray("theta1");
		MLDouble mlTheta2 = (MLDouble) matFileReader.getMLArray("theta2");	

		double[][] dTheta1 = mlTheta1.getArray();
		double[][] dTheta2 = mlTheta2.getArray();

		DenseMatrix64F theta1 = new DenseMatrix64F(dTheta1);
		DenseMatrix64F theta2 = new DenseMatrix64F(dTheta2);

		DenseMatrix64F hypothesis = feedForwardPropTwoLayers(input, theta1, theta2);

		return hypothesis;

	}



	public static void main(String[] args) {
		/*
		 * asks for input
		 * applies to all neural networks
		 * prints out results for each
		 * 	
		 */
		Scanner io = new Scanner(System.in);

		System.out.println("Would You Like To Provide Input? (Yes/No)");
		String answer = io.next();
		System.out.println();
		long time = System.currentTimeMillis();

		if(answer.toUpperCase().substring(0,1).equals("Y")){

			double[][] dInput = new double[1][2];

			System.out.print( "First Boolean Value: ");
			dInput[0][0] = io.nextDouble();
			System.out.print( "Second Boolean Value: ");
			dInput[0][1] = io.nextDouble();
			time = System.currentTimeMillis();

			DenseMatrix64F input = new DenseMatrix64F(dInput);

			double ORresult = ORneuralnetwork(input).get(0);
			double ANDresult = ANDneuralnetwork(input).get(0);
			double XNORresult = XNORneuralnetwork(input).get(0);

			System.out.println("\nResult of OR Neural Network:\n" + ORresult +" -> " + (int)(ORresult+0.5) );
			System.out.println("\nResult of AND Neural Network:\n" + ANDresult +" -> " + (int)(ANDresult+0.5) );
			System.out.println("\nResult of XNOR Neural Network:\n" + XNORresult +" -> " + (int)(XNORresult+0.5) );
			System.out.println();

		}else{

			double[][] dInput = { 
					{0,0}, 
					{0,1},
					{1,0},
					{1,1}
			};

			DenseMatrix64F input = new DenseMatrix64F(dInput);

			System.out.println("Truth Cases: ");
			System.out.println( "1)\t{0,0}\n2)\t{0,1}\n3)\t{1,0}\n4)\t{1,1}");
			System.out.println();

			for(int i = 0; i < 4; i++){


				double ORresult = ORneuralnetwork(input).get(i);
				double ANDresult = ANDneuralnetwork(input).get(i);
				double XNORresult = XNORneuralnetwork(input).get(i);

				System.out.println("---------------------------------");
				System.out.println((i+1)+")\t{" + (int)(dInput[i][0]) + "," + (int)(dInput[i][1]) + "}\n");
				System.out.println("Result of OR Neural Network:\n" + ORresult +" -> " + (int)(ORresult+0.5) );
				System.out.println("\nResult of AND Neural Network:\n" + ANDresult +" -> " + (int)(ANDresult+0.5) );
				System.out.println("\nResult of XNOR Neural Network:\n" + XNORresult +" -> " + (int)(XNORresult+0.5) );
				System.out.println();
			}
		}
		System.out.println("Time Taken: " + ( (System.currentTimeMillis()-time)/1000.0 ) + " seconds.");

	}


}
