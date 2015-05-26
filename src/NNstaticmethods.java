import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;
import org.ejml.ops.RandomMatrices;
import org.ejml.ops.SpecializedOps;

import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLDouble;

public class NNstaticmethods {
	
	
	
	public static double elementSumSq( DenseMatrix64F matrix){
		
		double answer = 0;
		
		for(int i = 0; i < matrix.getNumElements(); i++){
			answer += Math.pow( (matrix.data[i]), 2);
		}
		
		return answer;
	}
	
	
	
	public static void printFTR( DenseMatrix64F matr ){

		(NNstaticmethods.getRow(matr, 0)).print();
		(NNstaticmethods.getRow(matr, 1)).print();
		(NNstaticmethods.getRow(matr, 2)).print();

	}

	public static DenseMatrix64F scale( double value, DenseMatrix64F matrix){

		int numElements = matrix.getNumElements();

		DenseMatrix64F answer = new DenseMatrix64F( matrix.getNumRows(), matrix.getNumCols() );

		for( int i = 0; i < numElements; i++ ){
			answer.data[i] = value * matrix.data[i] ;
		}

		return answer;

	}

	public static DenseMatrix64F log( DenseMatrix64F matrix){

		int numElements = matrix.getNumElements();

		DenseMatrix64F answer = new DenseMatrix64F( matrix.getNumRows(), matrix.getNumCols() );

		for( int i = 0; i < numElements; i++ ){
			answer.data[i] = Math.log( matrix.data[i] ) ;
		}

		return answer;

	}


	public static void setMatrixOneValue( DenseMatrix64F matrix, double value, int startRow, int endRow, int startCol, int endCol ){

		for(int row = startRow; row <= endRow; row++){
			for(int col = startCol; col <= endCol; col++){
				matrix.set(row, col, value);
			}
		}

	}

	public static int maxIndex( DenseMatrix64F row){

		int numElements = row.getNumElements();

		double val1 = row.get(0);

		int index = 0;

		for(int i = 0; i < numElements; i++){

			if( val1 < row.data[i]){
				val1 = row.data[i];
				index = i;
			}

		}

		return index;
	}

	public static DenseMatrix64F abs( DenseMatrix64F matrix ){

		int numElements = matrix.getNumElements();

		DenseMatrix64F answer = new DenseMatrix64F( matrix.getNumRows(), matrix.getNumCols() );

		for( int i = 0; i < numElements; i++ ){
			answer.data[i] = Math.abs( matrix.data[i] );
		}

		return answer;
	}


	public static void setMatrixWithRow( DenseMatrix64F matrix, DenseMatrix64F row, int rowInd ){

		for(int col = 0; col < matrix.getNumCols(); col++){
			matrix.set(rowInd, col, row.get(0, col));
		}

	}


	public static DenseMatrix64F getRow( DenseMatrix64F largerMatrix, int rowNum ){

		DenseMatrix64F row = new DenseMatrix64F( 1, largerMatrix.getNumCols() );

		for(int i = 0; i < largerMatrix.getNumCols(); i++){
			row.set( 0, i, largerMatrix.get(rowNum, i) );
		}

		return row;
	}

	public static DenseMatrix64F subNegativeOnes( DenseMatrix64F matrix ){

		int numElements = matrix.getNumElements();

		DenseMatrix64F answer = new DenseMatrix64F( matrix.getNumRows(), matrix.getNumCols() );

		for( int i = 0; i < numElements; i++ ){
			answer.data[i] = -1;
		}

		return answer;
	}


	public static DenseMatrix64F sigmoidGradient( DenseMatrix64F matrix ){

		// g = sigmoid(z).*(1 - sigmoid(z));

		DenseMatrix64F answer = NNstaticmethods.sigmoid( matrix );
		CommonOps.subtract(1, answer, answer);

		CommonOps.elementMult( NNstaticmethods.sigmoid(matrix), answer, answer);

		return answer;

	}

	public static DenseMatrix64F categorizeClasses( DenseMatrix64F y, int labelSize ){

		DenseMatrix64F yAns = new DenseMatrix64F( y.getNumRows(), labelSize );

		for(int row = 0; row < y.getNumRows(); row++){
			double a = y.get(row, 0);
			yAns.set(row, (int) (a-1.0) , 1);
		}

		return yAns;
	}


	/*
	  function W = randInitializeWeights(L_in, L_out)

			W = zeros(L_out, 1 + L_in);

			epsilon_init = 0.12;
			W = rand(L_out, 1+L_in) * 2 * epsilon_init - epsilon_init;

		end
	 */
	public static DenseMatrix64F randInitializeWeights( int inputLayerSize, int hiddenLayerSize){

		double epsilon_init = 0.12;

		DenseMatrix64F Weights = RandomMatrices.createRandom( hiddenLayerSize, (1+inputLayerSize), 0, 1, new Random() );

		CommonOps.scale( (2.0*epsilon_init), Weights );
		CommonOps.add( Weights, (epsilon_init*-1.0) );

		return Weights;
	}


	public static DenseMatrix64F filter( DenseMatrix64F matrix ){

		DenseMatrix64F filtered = new DenseMatrix64F( matrix.getNumRows(), (matrix.getNumCols()-1) );

		for(int row = 0; row < matrix.getNumRows(); row++){
			for(int col = 1; col < matrix.getNumCols(); col++){
				filtered.set(row, (col-1), matrix.get(row, col) );
			}
		}

		return filtered;
	}



	public static DenseMatrix64F addBias( DenseMatrix64F matrix){

		DenseMatrix64F matrixWithBias = new DenseMatrix64F(matrix.getNumRows(), (matrix.getNumCols()+1) );

		for(int row = 0; row < matrix.getNumRows(); row++){
			for(int col = 0; col < matrix.getNumCols(); col++){
				matrixWithBias.set(row, (col+1), matrix.get(row, col));
			}
		}

		for(int row = 0; row < matrix.getNumRows(); row++){
			matrixWithBias.set(row, 0, 1);
		}

		return matrixWithBias;
	}

	public static DenseMatrix64F exp( DenseMatrix64F matrix ){

		int numElements = matrix.getNumElements();

		DenseMatrix64F matrixExp = new DenseMatrix64F( matrix.getNumRows(), matrix.getNumCols() );

		for( int i = 0; i < numElements; i++ ){
			matrixExp.data[i] = Math.exp(matrix.data[i]);
		}

		return matrixExp;
	}

	public static DenseMatrix64F pow( DenseMatrix64F matrix, double power ){

		int numElements = matrix.getNumElements();

		DenseMatrix64F matrixInv = new DenseMatrix64F( matrix.getNumRows(), matrix.getNumCols() );

		for(int i = 0; i < numElements; i++){
			matrixInv.data[i] = Math.pow( matrix.data[i], power);
		}

		return matrixInv;
	}


	/*
	 function g = sigmoid(z)

		g = zeros(size(z));

		g = 1.0 ./ (1.0 + exp(-z));
		//e^-z

		end
	 */	
	public static DenseMatrix64F sigmoid( DenseMatrix64F matrix ){

		DenseMatrix64F matrixSig = new DenseMatrix64F(matrix);

		CommonOps.scale(-1.0, matrixSig);

		matrixSig = NNstaticmethods.exp(matrixSig);

		CommonOps.add( matrixSig, 1.0 ); 	
		matrixSig = NNstaticmethods.pow( matrixSig, -1);

		return matrixSig;
	}

	public static DenseMatrix64F roundHypothesis( DenseMatrix64F hypothesis ){

		DenseMatrix64F result = new DenseMatrix64F( hypothesis.getNumRows(), hypothesis.getNumCols() );

		for(int i = 0; i < hypothesis.getNumElements(); i++){
			result.data[i] = (int) (hypothesis.data[i] + 0.5);
		}

		return result;
	}


	public static void main(String args[]){

		long time = System.currentTimeMillis();
		/*
		DenseMatrix64F a = new DenseMatrix64F(5,4);
		a.set(0, 2, 5);
		a.print();
		NNstaticmethods.exp(a).print();
		a.print();

		System.out.println();
		 */

		int a234 = 0;
		System.out.println(a234);
		System.out.println("dfsdfsdf");

		double[][] test = { 
				{5, 4, 7, 8}
			//	{3, 9, 2, 1}
		};

		//DenseMatrix64F a = RandomMatrices.createRandom(3, 5, 0, 10, new Random());

		DenseMatrix64F a = new DenseMatrix64F( test );
		a.print();
		System.out.println("maxIndex: " + maxIndex(a));
		System.out.println((System.currentTimeMillis()-time)/1000.0);

	}

}
