import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;
import org.ejml.ops.SpecializedOps;

import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLDouble;

public class NeuralNetwork {

	public static void NeuralNetworkDemo( ) {
		Scanner io = new Scanner(System.in);
		//			Flow:
		//			1. Read data from mat file into MLarray
		//			2. convert MLarray into doubles
		//			3. use EJML and instantiate SimpleMatrix with double array
		//			4. Save results from file
		//			5. Goto 1

		//create new filter instance
		//MatFileFilter(String[] names) 
		//			MatFileFilter filter = new MatFileFilter();


		//add a needle
		//			filter.addArrayName( "ex4data1" );

		//read array form file (haystack) looking _only_ for pecified array (needle)
		//MatFileReader(File file, MatFileFilter filter) 
		//File(String pathname)  look at documentation of file

		/**************** Loading Data ***************/	
		System.out.println("Loading Data...");
		int inputLayerSize = 400;
		int hiddenLayerSize = 25;
		int kLabels = 10;

		File dataMat = new File( "/Users/ababu/Documents/JavaWorkspace/Independent Project/mlclass/data.mat" );

		MatFileReader matFileReader = null;
		try {
			matFileReader = new MatFileReader( dataMat );
		} catch (IOException e) {
			e.printStackTrace();
		}

		//double[][] getArray() Gets two-dimensional real array.
		//MLArray array = mfr.getMLArray("ex4data1");
		//MLDouble j = (MLDouble)matfilereader.getMLArray("dataname");
		MLDouble XmlArray = (MLDouble) matFileReader.getMLArray("X");
		MLDouble YmlArray = (MLDouble) matFileReader.getMLArray("y");

		double[][] Xarray = XmlArray.getArray();
		double[][] Yarray = YmlArray.getArray();

		DenseMatrix64F X = new DenseMatrix64F(Xarray);
		DenseMatrix64F y = new DenseMatrix64F(Yarray);

		//	MatrixVisualization.show( Xdata, "Xdata" );

		int m = X.getNumRows();

		//		//%%%%%% Display Data %%%%%%%%%
		//		DenseMatrix64F showDataAsPics = new DenseMatrix64F( 100, X.getNumCols() );
		//
		//		//	(High - low + 1) * Math.random( ) + low
		//		for(int i = 0; i < 100; i++){
		//			int rowInd = (int) ((4999-0+1)*Math.random() + 0);
		//			DenseMatrix64F RowOfshowDataAsPics = NNstaticmethods.getRow(X, rowInd);
		//			NNstaticmethods.setMatrixWithRow(showDataAsPics, RowOfshowDataAsPics, i);
		//		}
		//
		//		new DataVisualization( showDataAsPics );


		System.out.println("Program paused. Press enter to continue. \n");
		while(!io.nextLine().equals(""));

		/**************** Initializing Neural Network Parameters ***************/					
		System.out.println("Initializing Neural Network Paramters...");


		dataMat = new File( "/Users/ababu/Documents/JavaWorkspace/Independent Project/mlclass/randWeights.mat" );
		matFileReader = null;
		try {
			matFileReader = new MatFileReader( dataMat );
		} catch (IOException e) {
			e.printStackTrace();
		}

		MLDouble mlTheta1 = (MLDouble) matFileReader.getMLArray("Theta1");
		MLDouble mlTheta2 = (MLDouble) matFileReader.getMLArray("Theta2");

		double[][] Theta1array = mlTheta1.getArray();
		double[][] Theta2array = mlTheta2.getArray();

		DenseMatrix64F theta1 = new DenseMatrix64F(Theta1array);
		DenseMatrix64F theta2 = new DenseMatrix64F(Theta2array);


		//	DenseMatrix64F theta1 = NNstaticmethods.randInitializeWeights(inputLayerSize, hiddenLayerSize);
		//	DenseMatrix64F theta2 = NNstaticmethods.randInitializeWeights(hiddenLayerSize, kLabels);

		System.out.println("Program paused. Press enter to continue. \n");
		while(!io.nextLine().equals(""));

		/**************** Training Neural Network ***************/
		System.out.println("Training Neural Network...");

		double lambda = 100;
		double Jcost = 0.0;
		double tempJcost = ((Double) (GradientDescent.nnCostFunction(theta1, theta2, kLabels, X, y, lambda))[0]).doubleValue();

		outer:for(int i = 1; i <= 100; i++){

			Object[] data = GradientDescent.nnCostFunction(theta1, theta2, kLabels, X, y, lambda);

			Jcost = ((Double) data[0]).doubleValue();
			theta1 = (DenseMatrix64F) data[1];
			theta2 = (DenseMatrix64F) data[2];

			if( tempJcost < Jcost){	break outer; }
			tempJcost = Jcost;

			System.out.println("Iteration\t" + i + " | Cost: " + Jcost);

		}
		System.out.println();

		System.out.println("Program paused. Press enter to continue. \n");
		while(!io.nextLine().equals(""));

		/**************** Visualizing Hidden Layer of Neural Network ***************/
		System.out.println("Visualizing Hidden Layer of Neural Network...");

		//%%%%%% Display Data %%%%%%%%%

		System.out.println("Program paused. Press enter to continue. \n");
		while(!io.nextLine().equals(""));

		//%%%%%% Training Set Accuracy %%%%%%%%%



		//%%%%%% Display Data %%%%%%%%%

		System.out.println("Displaying Example Image...");

		for(int i = 0; i < 5000; i += 100){
			System.out.println("Row: " + i);
			double pred = NNstaticmethods.maxIndex(BooleanOperatorsNN.feedForwardPropTwoLayers(NNstaticmethods.getRow(X, i), theta1, theta2));
			System.out.println("Neural Network Predictions: " + (int)(pred%10));
			System.out.println("Actual: " + (int)(y.get(i)%10));
			System.out.println("Program paused. Press enter to continue. \n");
			while(!io.nextLine().equals(""));

		}



	}

	/*
		while(io.nextLine().equals("")){

			System.out.println("Displaying Example Image...");
			//	(High - low + 1) * Math.random( ) + low
			int rowNum = (int) ( (4999 - 0 + 1)*Math.random() + 0);
			new DataVisualization( NNstaticmethods.getRow(X, rowNum));

			int prediction = (int) ( (BooleanOperatorsNN.feedForwardPropTwoLayers(X, theta1, theta2)).get(0) + 0.5);
			System.out.println("Neural Network Prediction: " + prediction);
		}
	 */


	public static void main( String args[] ){
		NeuralNetworkDemo();
	}

}
