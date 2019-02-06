package a11rallypong;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

//Imports are listed in full to show what's being used
//could just import javax.swing.* and java.awt.* etc..

import javax.swing.JPanel;

/**
 * The DrawHere class takes care of initializing and running all of the threads in the app.
 * It is also where the objects are drawn onto the buffer.
 * @author Erich
 */
@SuppressWarnings("serial")
public class DrawHere extends JPanel implements ActionListener {
// Pass score on to game over GUI
	public static int score;

	protected int screenH, screenW;

	ball b;
	paddle p1;
	paddle p2;
	
	/**
	 * The DrawHere contructor initializes all of the objects that will be
	 * bouncing around on the JFrame as well as the background for the scene.
	 */
	public DrawHere() {
		Rectangle screenBounds = getScreenTotalArea(null);
		screenH = (int) screenBounds.height;
		screenW = (int) screenBounds.width;

		b = new ball(screenW/2, screenH/2, screenH, screenW);
		p1 = new paddle(100, screenH/2, screenH, screenW);
		p2 = new paddle(screenW - 100, screenH/2, screenH, screenW);

		b.start();
		p1.start();
		p2.start();
		
		//Set up key-pressed events
		this.addKeyListener(p1);
		this.addKeyListener(p2);

		this.setFocusable(true);
		this.requestFocusInWindow();

	}

	/**
	 * The paintComponent method is called by using "this.repaint();", 
	 * it is part of the JPanel library. This method is in charge of painting 
	 * objects on the JFrame as they get refreshed.
	 */
	public void paintComponent(Graphics g) {
		
		// Setup and clear the buffer
		BufferedImage bufferedImage = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_BGR);
		Graphics2D g2d = bufferedImage.createGraphics();
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, this.getWidth(), this.getHeight()); // fill with background color
		g2d.setColor(Color.WHITE);
		g2d.fillRect(screenW/2 + 5, 0, 10, screenH);

		// Draw on the buffer
		b.Paint(g2d);
		p1.Paint(g2d);
		p2.Paint(g2d);
		PaintScore(g2d);
		
		// Set the buffer to be visible
		Graphics2D g2dComponent = (Graphics2D) g;
		g2dComponent.drawImage(bufferedImage, null, 0, 0);
	}

	/**
	 * This method is called when the timer fires The Timer sets off an
	 * "actionPerformed" event every so many milliseconds.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		this.repaint();
		b.hitCheck(p1);
		b.hitCheck(p2);
		score = b.getHitcount();
		// Take focus if we don't have it
		if (!this.isFocusOwner()) {
			this.requestFocusInWindow();
		}
	}
	
	/**
	 * The getScreenTotalArea method finds the size of the users screen so that the game can be fullscreen
	 * on any screen size and the play area will adapt to this size.
	 * @param windowOrNull the window the method should find the size of.
	 * @return a Rectangle representing the dimesions of the screen.
	 */
	static public Rectangle getScreenTotalArea(Window windowOrNull) {
	    Rectangle bounds;
	    if (windowOrNull == null) {
	        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	        bounds = ge.getDefaultScreenDevice().getDefaultConfiguration().getBounds();
	    } else {
	        GraphicsConfiguration gc = windowOrNull.getGraphicsConfiguration();
	        bounds = gc.getBounds();
	    }
	    return bounds;
	}
	
	/**
	 * The PaintScore method draws the users current score on the Frame
	 * @param g the Graphics library used by the Frame
	 */
	public void PaintScore(Graphics g) {
		g.setColor(Color.WHITE);
		g.drawString("Score: " + score, screenW/2 - 50, (int)(screenH * 0.95));
	}

}
