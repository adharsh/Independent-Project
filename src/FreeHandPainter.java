import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;


public class FreeHandPainter extends JFrame implements ActionListener {

	private static BufferedImage image;
	int xvalue, yvalue;// = -10, yvalue = -10;
	
	public FreeHandPainter() {
		super( "Drag to Paint" );
		
		JButton processImage = new JButton("Process Image");
	//	processImage.setActionCommand("Process Image");
		processImage.addActionListener((ActionListener) this);
		add( processImage, BorderLayout.SOUTH );
		
		addMouseMotionListener( new MouseMotionAdapter() {

			public void mouseDragged( MouseEvent event ) {
				xvalue = event.getX();
				yvalue = event.getY();
				repaint();
			}
		});
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation( (int) (dim.width*(1.0/6.1)), (int) (dim.height*(1.0/9.5)) );
		setSize( 200,200 );
		setBackground(new Color( 127, 127, 127));
		setVisible( true );
	}
	
	public void actionPerformed( ActionEvent event ){
		
		BufferedImage dataImage = new BufferedImage( getWidth(),getHeight(), BufferedImage.TYPE_INT_RGB );
		Graphics2D graphics = dataImage.createGraphics();
		paint(graphics);
	/*	try {
			ImageIO.write(dataImage,"jpeg", new File("/Users/ababu/Desktop/DataImage.jpeg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		image = dataImage;
		System.out.println("Image Saved");
	}
	
	 public void paint ( Graphics g ){
		 
		 g.setColor(Color.WHITE);
		 g.fillOval( xvalue, yvalue, 10, 10 ); 
	 
	 }

	 
	public static void main(String[] args) {

		FreeHandPainter application = new FreeHandPainter();

	      application.addWindowListener( new WindowAdapter() {

	            public void windowClosing(){
	               System.exit( 0 );
	            }
	         });

	   }

}
