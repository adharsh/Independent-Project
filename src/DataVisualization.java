import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;

import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLDouble;



/*
 *	Use GridLayout Manager 
 *	Black Border with Empty Border Surrounding it by stringing labels together into one pane and then displaying it to the contentPane
 *	Alignment is Center
 *
 */

public class DataVisualization {

	JFrame frame;
	JPanel contentPane;
	JPanel drawingPane;
	private DenseMatrix64F X;
	private static Integer figure;
	//	isCell = true -> draw a cell
	//	isCell = false -> draw a grid


	public DataVisualization( DenseMatrix64F X ){

		this.X = X;
		
		
		if( figure == null){
			figure = new Integer(1);
		} else{
		
		figure = new Integer( figure.intValue() + 1);
		}
		
		// System.out.println(figure);
		
		/*
		 * Methods that create and show a GUI shoudl be run from an event-dispatching thread
		 */
		javax.swing.SwingUtilities.invokeLater( new Runnable() {

			public void run(){
				JFrame.setDefaultLookAndFeelDecorated( true );
				runGUI( );
			}
		});


	}

	
	private void runGUI( ){

		frame = new JFrame("Data Visualization");
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

		contentPane = new JPanel(); //new JPanel();
		contentPane.setBorder( BorderFactory.createEmptyBorder(30, 100, 30, 100) );
		contentPane.setBackground( new Color( 240, 240, 240) );

		/*		label = new JLabel( "Data Vsiualization Text");
		//	new JLabel(image, horizontalAlignment)

		label.setAlignmentX( JLabel.CENTER_ALIGNMENT );
		label.setBorder( BorderFactory.createMatteBorder( 10, 10, 10, 10, Color.black) );
		//	label.setBorder( BorderFactory.createEmptyBorder(100, 100, 100, 100));
		contentPane.add( label );
		 */		

		//	adds grid of pics label
		//	contentPane.add( drawGrid() );
		
		contentPane.add( displayData() );
		
		
		frame.setContentPane( contentPane );

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		//	frame.setLocation( dim.width/2 - frame.getSize().width/2, dim.height/2 - frame.getSize().height/2 );
		// centers to screen regardless -> frame.setLocationRelativeTo(null);
		
		frame.setLocation( (int) (dim.width*(3.0/4.6)), (int) (dim.height*(1.0/8.3)) );

		frame.pack();
		frame.setVisible( true );
	}

	public JLabel displayData(){

		int example_width = (int) (Math.sqrt( X.getNumCols() ) + 0.5);


		int m = X.getNumRows();
		int n = X.getNumCols();
		int example_height = (int) (n/example_width);
		//	int example_height = (int) ( (n / example_width) + 0.5);

		int display_rows = (int) ( Math.floor( Math.sqrt(m) ) );
		//	int display_rows = (int) ( Math.floor( Math.sqrt(m) ) + 0.5 );
		int display_cols = (int) Math.ceil( m/display_rows );
		
		int pad = 1;
		
		DenseMatrix64F display_array = new DenseMatrix64F( (pad + display_rows * (example_height + pad)), (pad + display_cols * (example_width + pad)) );
		
		display_array = NNstaticmethods.subNegativeOnes( display_array );
		
		int curr_ex = 1;
		
		for(int j = 0; j < display_rows; j++){
			for(int i = 0; i < display_cols; i++){
				if( curr_ex > m){
					break;
				}
				
				double max_val = CommonOps.elementMaxAbs( NNstaticmethods.getRow( X , curr_ex)); 
					
				DenseMatrix64F tempX = new DenseMatrix64F( 1, X.getNumCols() );
				DenseMatrix64F tempDisplayArray = new DenseMatrix64F( 1, X.getNumCols() );
				
				//	extract submatrix
				//	set oiginal submatrix to zeros
				//	submatrix = returned value of reshape()
				//	insert submatrix
				
			}
		}
		
		
		return null;
	}

	
	public static void main(String[] args){
		
		new DataVisualization(null);
		new DataVisualization(null);
		new DataVisualization(null);
		new DataVisualization(null);
		
	}
	
	public static void main2(String[] args) {
		
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

		double[][] Xarray = XmlArray.getArray();

		DenseMatrix64F X = new DenseMatrix64F(Xarray);
		
		new DataVisualizationDraft( X, true, 1);

	}

}
