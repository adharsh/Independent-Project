import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;



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
	
	
	public void destroy(){
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		figure = new Integer(figure.intValue() - 1);
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

		int exWidth = (int) (Math.sqrt( X.getNumCols() ) + 0.5);

		int m = X.getNumRows();
		int n = X.getNumCols();
		int exHeight = (int) (n/exWidth);
		//	int exHeight = (int) ( (n / exWidth) + 0.5);

		int dispRows = (int) ( Math.floor( Math.sqrt(m) ) );
		//	int dispRows = (int) ( Math.floor( Math.sqrt(m) ) + 0.5 );
		int dispCols = (int) Math.ceil( m/dispRows );

		int pad = 1;

		DenseMatrix64F dispArray = new DenseMatrix64F( (pad + dispRows * (exHeight + pad)), (pad + dispCols * (exWidth + pad)) );

		dispArray = NNstaticmethods.subNegativeOnes( dispArray );

		int currEx = 0;

		for(int j = 0; j < dispRows; j++){

			for(int i = 0; i < dispCols; i++){
				if( currEx > m){
					break;
				}

				double max_val = CommonOps.elementMaxAbs( NNstaticmethods.getRow( X , currEx)); 

				DenseMatrix64F reshapedMatrix = NNstaticmethods.scale(1.0/max_val, NNstaticmethods.reshape( NNstaticmethods.getRow(X, currEx), exHeight, exWidth) );

				// pad + (j - 1) * (exHeight + pad) + (1:exHeight), pad + (i - 1) * (exWidth + pad) + (1:exWidth))
				dispArray = NNstaticmethods.replace(dispArray, reshapedMatrix, pad + (j) * (exHeight + pad) + 1, 
						pad + (j) * (exHeight + pad) + exHeight,  pad + (i) * (exWidth + pad) + 1,
						pad + (i) * (exWidth + pad) + exWidth);
			//	dispArray.print();
			//	while(!io.nextLine().equals(""));

				currEx++;
			}
			if(currEx > m){
				break;
			}
		}

		BufferedImage display = new BufferedImage(dispArray.numCols, dispArray.numRows, BufferedImage.TYPE_INT_RGB);
		CommonOps.add(dispArray, 1);
		CommonOps.scale(127.5, dispArray);

		for(int x = 0; x < dispArray.numCols; x++){
			for(int y = 0; y < dispArray.numRows; y++){
				int value = (int)(dispArray.get(y, x) + 0.5);
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
