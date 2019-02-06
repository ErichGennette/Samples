package a11rallypong;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * The paddle class extends the base class to make a rectangle on the screen used to hit the ball back and forth.
 * It implements the KeyListener interface to listen for keystrokes and move accordingly.
 * @author Erich
 *
 */
public class paddle extends base implements KeyListener{
	
	int pb = 5;
	private int paddledy = 10;
	
	/**
	 * The paddle constructor takes in four values to setup the items on the screen in their designated spots.
	 * @param x the desired initial position of the object on the x axis.
	 * @param y the desired initial position of the object on the y axis.
	 * @param screenH the height of the screen the object is being displayed on.
	 * @param screenW the width of the screen the object is being displayed on.
	 */
	public paddle(int x, int y, int screenH, int screenW) {
		super(x, y, screenW, screenW);
		this.initY = (double)screenH/2;
		this.initX = x;
		this.length = 150;
		this.width = 20;
		this.dy = 0;
		this.vGround = screenH - 45;
	}

	/**
	 * The Paint method draws the paddle on the frame.
	 */
	@Override
	public void Paint(Graphics g) {
		// TODO Auto-generated method stub
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect((int)this.x + width/2, (int)this.y - length/2, this.width, this.length);
		g.setColor(Color.WHITE);
		g.fillRect((int)this.x + (width + pb)/2, (int)this.y - (length - pb)/2, this.width - pb, this.length - pb);
		
	}
	
	/**
	 * The paddle_up method is in charge of setting the paddles dy in a way
	 * that will make it move up the screen when called in the keyPressed method.
	 */
	public void paddle_up() {
		this.dy = 0;
		if (y - length/2 >= 0) {
			this.dy = -paddledy;
		}
	}
	
	
	/**
	 * The paddle_down method is in charge of setting the paddles dy in a way
	 * that will make it move down the screen when called in the keyPressed method.
	 */
	public void paddle_down() {
		this.dy = 0;
		if (y + length/2 <= this.vGround) {
			this.dy = paddledy;
		}
	}
	
	/**
	 * The despawn method sets the despawned boolean to true when called.
	 */
	@Override
	public void despawn() {
		this.despawned = true;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int ch = e.getKeyCode();
		switch (ch) {
		case KeyEvent.VK_UP:
			paddle_up();
			break;
		case KeyEvent.VK_DOWN:
			paddle_down();
			break;
		case KeyEvent.VK_ESCAPE:
			System.exit(0);  // end if esc pressed
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int ch = e.getKeyCode();
		switch (ch) {
		case KeyEvent.VK_UP:
			this.dy = -0;
			break;
		case KeyEvent.VK_DOWN:
			this.dy = 0;
			break;
		}
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {}

	/**
	 * bounceOnX is unused in this class
	 */
	@Override
	public void bounceOnX() {}
	
	/**
	 * bounceOnY is unused in this class
	 */
	@Override
	public void bounceOnY() {}
}
