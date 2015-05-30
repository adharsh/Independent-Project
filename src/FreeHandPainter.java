import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;

import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;


public class FreeHandPainter extends JFrame implements ActionListener {

	public DataVisualization inputImageDV;
	
	int xCoordinate = Integer.MIN_VALUE;
	int yCoordinate = Integer.MIN_VALUE;
	private DenseMatrix64F theta1;
	private DenseMatrix64F theta2;
	
	public FreeHandPainter( DenseMatrix64F theta1, DenseMatrix64F theta2) {
		super( "Drag to Paint" );

		this.theta1 = new DenseMatrix64F( theta1 );
		this.theta2 = new DenseMatrix64F( theta2 );
		
		JButton processImage = new JButton("Process Image");
		//	processImage.setActionCommand("Process Image");
		processImage.addActionListener((ActionListener) this);
		add( processImage, BorderLayout.SOUTH );
		
		addMouseMotionListener( new MouseMotionAdapter() {

			public void mouseDragged( MouseEvent event ) {
				xCoordinate = event.getX();
				yCoordinate = event.getY();
				repaint();
			}
		});

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation( (int) (dim.width*(1.0/6.1)), (int) (dim.height*(1.0/9.5)) );
		setSize( 200, 230 );
		setUndecorated(true);
		setResizable(false);
		setAlwaysOnTop(true);
		setBackground(new Color( 0.501960784f, 0.501960784f, 0.501960784f));
		setVisible( true );
	}

	public void actionPerformed( ActionEvent event ){
		
	//	setSize(getPreferredSize());
	/*	BufferedImage XdataImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = XdataImage.createGraphics();
		paint(graphics);
		printAll(graphics);
		BufferedImage dataImage = (BufferedImage) createImage(200,200);
		graphics.dispose();
		
		try {
			ImageIO.write(dataImage,"bmp", new File("/Users/ababu/Desktop/DataImage.bmp"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	*/	
		Point corner = getLocation();
		Robot screenShot = null;
		try {
			screenShot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Rectangle areaOfDrawing = new Rectangle( corner.x, corner.y, 200, 200);
		BufferedImage dataImage = screenShot.createScreenCapture(areaOfDrawing);
		
		BufferedImage scaledDataImage = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = scaledDataImage.createGraphics();
		AffineTransform transformation = AffineTransform.getScaleInstance(0.1, 0.1);
		graphics.drawRenderedImage( dataImage, transformation );
		
		DenseMatrix64F inputImage = new DenseMatrix64F( 1, 400 );
		
		
/*		// Column Wise Adding Values
		int counter = 0;
		for(int x = 0; x < scaledDataImage.getWidth(); x++ ){
			for(int y = 0; y < scaledDataImage.getHeight(); y++){
				Color pixelColor = new Color( scaledDataImage.getRGB(x, y) );
				inputImage.data[counter] = ( (pixelColor.getRed() + pixelColor.getGreen() + pixelColor.getBlue() )/3.0 );
				counter++;
			}
		}
		
		CommonOps.scale( (1.0/127.5), inputImage);
		CommonOps.add( inputImage, -1.0 );
*/		
		// Row Wise Adding Values		
		int counter = 0;
		for(int x = 0; x < scaledDataImage.getWidth(); x++ ){
			for(int y = 0; y < scaledDataImage.getHeight(); y++){
				Color pixelColor = new Color( scaledDataImage.getRGB(x, y) );
				inputImage.data[counter] = ( (pixelColor.getRed() + pixelColor.getGreen() + pixelColor.getBlue() )/3.0 );
				counter++;
			}
		}
		
		CommonOps.scale( (1.0/127.5), inputImage);
		CommonOps.add( inputImage, -1.0 );
		
		
		inputImageDV = new DataVisualization(inputImage);
		int pred = NeuralNetwork.forwardPropagationNeuraNetworkProcessingImages(inputImage, theta1, theta2);
		System.out.println("Neural Network Predictions: " + pred );
		System.out.println("Program paused. Press enter to clear. \n");
		
		/*try {
			ImageIO.write(dataImage,"bmp", new File("/Users/ababu/Desktop/DataImage.bmp"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Image Saved.");*/

	}
	
	public void paint ( Graphics graphics ){
		
		graphics.setColor(new Color( 230, 230, 230));
		graphics.fillOval( xCoordinate, yCoordinate, 20, 20);
		
		graphics.setColor(new Color( 240, 240, 240));
		graphics.fillOval( xCoordinate + 4, yCoordinate + 4, 14, 14);
		
		graphics.setColor(Color.WHITE);
		graphics.fillOval( xCoordinate + 2, yCoordinate + 2, 12, 12);
		
		
	//	graphics.fillOval( xCoordinate, yCoordinate, 22, 22 ); 
		
	}
	
	public void destroy(){
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}

	

}

