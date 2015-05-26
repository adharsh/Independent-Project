import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
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

public class DataVisualizationDraft {

	JFrame frame;
	JPanel contentPane;
	JPanel drawingPane;
	private DenseMatrix64F X;
	private boolean isCell;
	private int figure;
	//	isCell = true -> draw a cell
	//	isCell = false -> draw a grid


	public DataVisualizationDraft( DenseMatrix64F X, boolean isCell, int figure ){

		this.X = X;
		this.isCell = isCell;

		/*
		 * Methods that create and show a GUI shoudl be run from an event-dispatching thread
		 */
		javax.swing.SwingUtilities.invokeLater( new Runnable() {

			public void run(){
				JFrame.setDefaultLookAndFeelDecorated( true );
				displayData( );
			}
		});


	}

	private JPanel drawGrid( ){
		/*
		 * creates a grid of label pictures 
		 * 			using for loop for each row in X -> label array[ X.getNumRows() ] calling drawCell() method
		 * organizes using GridLayout Manager
		 * links them together under name
		 * applies black border
		 * returns object
		 */
		/*		
		label = new JLabel( "Data Vsiualization Text");
		//	new JLabel(image, horizontalAlignment)

		label.setAlignmentX( JLabel.CENTER_ALIGNMENT );
		label.setBorder( BorderFactory.createMatteBorder( 10, 10, 10, 10, Color.black) );
		//	label.setBorder( BorderFactory.createEmptyBorder(100, 100, 100, 100));
		 */

		JPanel comp = new JPanel();

		if( isCell ){ 

			JLabel label; 

			label = drawCell( X ); 
			label.setBorder( BorderFactory.createMatteBorder( 10, 10, 10, 10, Color.black) );
			label.setAlignmentX( JLabel.CENTER_ALIGNMENT );
			
			comp.add(label);

			return comp;

		} 

		comp.setLayout( new GridLayout(10,10) );

		DenseMatrix64F row = new DenseMatrix64F( 1, X.getNumCols() );  // or 400

		for(int i = 1; i <= X.getNumRows(); i++){

			CommonOps.extract( X, 0, (X.getNumCols() - 1), i, i, row, 0, 0);
			JLabel temp = drawCell( row );
			temp.setBorder( BorderFactory.createMatteBorder( 10, 10, 10, 10, Color.black) );
			temp.setAlignmentX( JLabel.CENTER_ALIGNMENT );
			comp.add( temp );

		}


		return comp;
	}


	private JLabel drawCell( DenseMatrix64F data ){
		
		//if isCell scale dimensions to make larger otherwise just concatenate
		if( isCell ){
			
			
			
			
		}
		
		
		
		return null;
	}




	private void displayData( ){


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

		frame.setContentPane( contentPane );

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		//	frame.setLocation( dim.width/2 - frame.getSize().width/2, dim.height/2 - frame.getSize().height/2 );
		frame.setLocation( (int) (dim.width*(3.0/4.6)), (int) (dim.height*(1.0/8.3)) );


		frame.pack();
		frame.setVisible( true );



		// centers to screen regardless frame.setLocationRelativeTo(null);

	}




	public static void main(String[] args) {
		
		
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
