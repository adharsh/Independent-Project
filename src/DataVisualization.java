import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
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

		frame = new JFrame("Figure " + figure.intValue());
		frame.setAlwaysOnTop(true);
		frame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE);

		contentPane = new JPanel(); //new JPanel();
		contentPane.setBorder( BorderFactory.createEmptyBorder(30, 60, 30, 60) );
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

		frame.setLocation( (int) (dim.width*(3.0/5.1)), (int) (dim.height*(1.0/9.5)) );

		frame.pack();
		frame.setVisible( true );
	}

	public JLabel displayData(){
		Scanner io = new Scanner(System.in);

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

		int curr_ex = 0;

		for(int j = 0; j < display_rows; j++){

			for(int i = 0; i < display_cols; i++){
				if( curr_ex > m){
					break;
				}

				double max_val = CommonOps.elementMaxAbs( NNstaticmethods.getRow( X , curr_ex)); 

				DenseMatrix64F reshapedMatrix = NNstaticmethods.scale(1.0/max_val, NNstaticmethods.reshape( NNstaticmethods.getRow(X, curr_ex), example_height, example_width) );

				// pad + (j - 1) * (example_height + pad) + (1:example_height), pad + (i - 1) * (example_width + pad) + (1:example_width))
				display_array = NNstaticmethods.replace(display_array, reshapedMatrix, pad + (j) * (example_height + pad) + 1, 
						pad + (j) * (example_height + pad) + example_height,  pad + (i) * (example_width + pad) + 1,
						pad + (i) * (example_width + pad) + example_width);
			//	display_array.print();
			//	while(!io.nextLine().equals(""));

				curr_ex++;
			}
			if(curr_ex > m){
				break;
			}
		}

		BufferedImage display = new BufferedImage(display_array.numCols, display_array.numRows, BufferedImage.TYPE_INT_RGB);
		CommonOps.add(display_array, 1);
		CommonOps.scale(127.5, display_array);

		for(int x = 0; x < display_array.numCols; x++){
			for(int y = 0; y < display_array.numRows; y++){
				int value = (int)(display_array.get(y, x) + 0.5);
				display.setRGB(x, y, (new Color( value, value, value )).getRGB() );
			}
		}
		
		BufferedImage scaleImageGraphics = new BufferedImage( 320, 320, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = scaleImageGraphics.createGraphics();
		graphics.drawImage( display, 0, 0, 320, 320, null );
		graphics.dispose();
		graphics.setComposite( AlphaComposite.Src);
		graphics.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR );
		graphics.setRenderingHint( RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY );
		graphics.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
		
		JLabel answer = new JLabel( new ImageIcon(scaleImageGraphics) );
		
		answer.setBorder( BorderFactory.createMatteBorder( 10, 10, 10, 10, Color.black) );
		
		return answer;
	}


	public static void main(String[] args){

		NeuralNetwork.NeuralNetworkDemo();

	}

}
