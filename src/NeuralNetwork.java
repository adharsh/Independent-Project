import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.ejml.data.DenseMatrix64F;

import com.jmatio.io.MatFileReader;
import com.jmatio.io.MatFileWriter;
import com.jmatio.types.MLDouble;

public class NeuralNetwork {

	public static void NeuralNetworkDemo( ) {
		System.out.println("\tArtificial Intelligence Program Written By Adharsh Babu");
		System.out.print("Handwritten Digit Classification and Recognition ");
		System.out.println("Using Artificial Neural Networks");
		System.out.println();


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

		File dataMat = new File( "/Users/ababu/Dropbox/Adharsh/APrograms/JavaWorkspace/Independent-Project/Data/data.mat" );

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

		DenseMatrix64F X = new DenseMatrix64F(XmlArray.getArray());
		DenseMatrix64F y = new DenseMatrix64F(YmlArray.getArray());

		//	MatrixVisualization.show( Xdata, "Xdata" );

		int m = X.getNumRows();

		//%%%%%% Display Data %%%%%%%%%
		DenseMatrix64F showDataAsPics = new DenseMatrix64F( 100, X.getNumCols() );

		//	(High - low + 1) * Math.random( ) + low
		for(int i = 0; i < 100; i++){
			int rowInd = (int) ((4999-0+1)*Math.random() + 0);
			DenseMatrix64F RowOfshowDataAsPics = NNstaticmethods.getRow(X, rowInd);
			NNstaticmethods.setMatrixWithRow(showDataAsPics, RowOfshowDataAsPics, i);
		}

		String continueTestCase = "";
		DataVisualization showDataAsPicsDV = new DataVisualization( showDataAsPics );
		System.out.println("Program paused. Press enter to continue.");
		continueTestCase = io.nextLine();
		if(!continueTestCase.equals("")){ showDataAsPicsDV.destroy(); System.out.println(); }

		/**************** Initializing Neural Network Parameters ***************/					

		System.out.print("Would you like to initialize parameters from given, preset values? (FileName/NO): ");
		String response = io.next();

		DenseMatrix64F theta1 = null;
		DenseMatrix64F theta2 = null;


		if( !response.toUpperCase().equals("NO")){

			File parameters = new File( response );

			MatFileReader weightReader = null;
			try{
				weightReader = new MatFileReader(parameters);
			}catch(IOException e){
				e.printStackTrace();
			}

			MLDouble theta1ML = (MLDouble) weightReader.getMLArray("theta1");
			MLDouble theta2ML = (MLDouble) weightReader.getMLArray("theta2");

			theta1 = new DenseMatrix64F(theta1ML.getArray());
			theta2 = new DenseMatrix64F(theta2ML.getArray());


		}else{

			System.out.println("\nInitializing Neural Network Paramters...");
			theta1 = NNstaticmethods.randInitializeWeights(inputLayerSize, hiddenLayerSize);
			theta2 = NNstaticmethods.randInitializeWeights(hiddenLayerSize, kLabels);

			continueTestCase = "";
			DataVisualization initializingfilteredTheta1DV = new DataVisualization( NNstaticmethods.filter(theta1) );
	//		System.out.println("Program paused. Press enter to continue.");
	//		continueTestCase = io.nextLine();
	//		if(!continueTestCase.equals("")){ initializingfilteredTheta1DV.destroy(); System.out.println(); }

			//	DenseMatrix64F theta1 = NNstaticmethods.randInitializeWeights(inputLayerSize, hiddenLayerSize);
			//	DenseMatrix64F theta2 = NNstaticmethods.randInitializeWeights(hiddenLayerSize, kLabels);


			/**************** Training Neural Network ***************/
			System.out.println("\nTraining Neural Network...");

			double lambda = 30;
			double Jcost = 0.0;
			double tempJcost = ((Double) (GradientDescent.nnCostFunction(theta1, theta2, kLabels, X, y, lambda))[0]).doubleValue();

			outer:for(int i = 1; i <= 1000; i++){

				Object[] data = GradientDescent.nnCostFunction(theta1, theta2, kLabels, X, y, lambda);

				Jcost = ((Double) data[0]).doubleValue();
				theta1 = (DenseMatrix64F) data[1];
				theta2 = (DenseMatrix64F) data[2];

				if( tempJcost < Jcost){	break outer; }
				tempJcost = Jcost;

				System.out.println("Iteration\t" + i + " | Cost: " + Jcost);

			}

		}
		System.out.println();

		/**************** Visualizing Hidden Layer of Neural Network ***************/
		System.out.println("Visualizing Hidden Layer of Neural Network...");

		//%%%%%% Display Data %%%%%%%%%
		continueTestCase = "";
		DataVisualization filteredTheta1DV = new DataVisualization( NNstaticmethods.filter(theta1) );
		System.out.println("Program paused. Press enter to continue. \n");
		continueTestCase = io.nextLine();
		if(!continueTestCase.equals("")){ filteredTheta1DV.destroy(); System.out.println(); }

		//%%%%%% Training Set Accuracy %%%%%%%%%
		System.out.println("Training Set Accuracy: " + NeuralNetwork.trainingSetAccuracy(X, y, theta1, theta2 )*100 + "%");
		System.out.println("Program paused. Press enter to continue. \n");
		while(!io.nextLine().equals(""));


		//%%%%%% Display Data %%%%%%%%%

		System.out.print("Test Cases: ");
		//	int testCases = io.nextInt();
		System.out.println("Displaying Random Test Case Image...");
		continueTestCase = "";

		while( continueTestCase.equals("") ){
			int i = (int)(Math.random()*5000);
			DataVisualization dataVis = new DataVisualization(NNstaticmethods.getRow(X, i));
			int pred = NeuralNetwork.forwardPropagationNeuraNetwork(X, i, theta1, theta2);
			System.out.println("Neural Network Prediction: " + pred );
			System.out.println("Actual: " + actualValue(y, i));
			System.out.println("Program paused. Press enter to continue. \n");

			continueTestCase = io.nextLine();
			dataVis.destroy();

		}

		System.out.println();

		continueTestCase = "";

		while( continueTestCase.equals("") ){

			FreeHandPainter drawer = new FreeHandPainter(theta1, theta2);

			continueTestCase = io.nextLine();

			if(drawer.inputImageDV != null){
				drawer.inputImageDV.destroy();
			}
			drawer.destroy();

		}

		System.out.println();

		System.out.print("Would you like to save Neural Network Parameters? (FileName/NO): ");
		String save = io.next();

		if(!save.toUpperCase().equals("NO")){
			MLDouble mlTheta1 = new MLDouble( "theta1", NNstaticmethods.getDoubleData(theta1) );
			MLDouble mlTheta2 = new MLDouble( "theta2", NNstaticmethods.getDoubleData(theta2) );
			ArrayList list = new ArrayList();
			list.add(mlTheta1);
			list.add(mlTheta2);
			try {
				new MatFileWriter( save, list);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println();
		System.out.println("Process Completed.");
		System.exit(0);
	}


	public static double trainingSetAccuracy( DenseMatrix64F X, DenseMatrix64F y, DenseMatrix64F theta1, DenseMatrix64F theta2 ){

		int nLabels = y.getNumElements();
		int counter = 0;

		for(int i = 0; i < nLabels; i++){

			if( NeuralNetwork.forwardPropagationNeuraNetwork( X, i, theta1, theta2 ) == NeuralNetwork.actualValue( y, i ) ){
				counter++;
			}
		}

		return (double) ((double)counter/(double)nLabels);
	}


	public static int actualValue( DenseMatrix64F y, int element ){
		return (int)(y.get(element)%10);
	}

	public static int forwardPropagationNeuraNetwork( DenseMatrix64F X, int row, DenseMatrix64F theta1, DenseMatrix64F theta2 ){
		return ((NNstaticmethods.maxIndex(BooleanOperatorsNN.feedForwardPropTwoLayers(NNstaticmethods.getRow(X, row), theta1, theta2))+1)%10);
	}

	public static int forwardPropagationNeuraNetworkProcessingImages( DenseMatrix64F inputImage, DenseMatrix64F theta1, DenseMatrix64F theta2 ){
		return ((NNstaticmethods.maxIndex(BooleanOperatorsNN.feedForwardPropTwoLayers(inputImage, theta1, theta2))+1)%10);
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