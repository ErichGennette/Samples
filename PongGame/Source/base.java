package a11rallypong;

import java.awt.Graphics;

import javax.swing.JFrame;

/**
 * The base class is an abstract class that holds the essential methods and values to be inherited by
 * the ball and paddle classes.
 * @author Erich
 *
 */
public abstract class base extends Thread {
	protected int width, length, screenH, screenW, vGround;
	protected int interval = 1, delay = 20, hitcount = 0, dylimit = 5;
	protected double x, y, dx, dy, initX, initY;
	Boolean despawned = false;
	
	/**
	 * The base constructor takes in four values to setup the items on the screen in their designated spots.
	 * @param x the desired initial position of the object on the x axis.
	 * @param y the desired initial position of the object on the y axis.
	 * @param screenH the height of the screen the object is being displayed on.
	 * @param screenW the width of the screen the object is being displayed on.
	 */
	public base(int x, int y, int screenH, int screenW) {
		this.initX = x;
		this.initY = y;
		this.x = initX;
		this.y = initY;
		this.screenW = screenW;
		this.screenH = screenH;
		this.vGround = screenH - 5;
	}
	
	/**
	 * The getHitcount method is a getter that returns the current hitcount of the object.
	 * @return a count of hits made.
	 */
	public int getHitcount() {
		return hitcount;
	}

	/**
	 * The move method is in charge of moving the object by its x and y velocities
	 */
	public void move() {
		this.x = this.x + this.dx/delay;
		this.y = this.y + this.dy/delay;
	}
	
	/**
	 * An abstract method which is called when the object is to reverse its y velocity.
	 */
	public abstract void bounceOnY();
	
	/**
	 * An abstract method which is called when the object is to reverse its x velocity.
	 */
	public abstract void bounceOnX();
	
	/**
	 * The deflect method applies a 'spin' on the ball relative to how far from the center of the paddle the ball hit.
	 * @param o2 The object representing the paddle.
	 */
	public void deflect(base o2) {
		double defVal = 0;
		defVal = (this.y - o2.y)/(o2.length/2);
		this.dy = this.dy + defVal;
		if (this.dy > dylimit)
			this.dy = dylimit;
		if (this.dy < -dylimit)
			this.dy = -dylimit;
	}
	
	/**
	 * An abstract method that is called when the ball misses the paddle.
	 */
	public abstract void despawn();
	
	/**
	 * The run method does all of the work for the object. It calls its move method with each iteration of the while loop until
	 * the despawned boolean is true.
	 */
	public void run() {
		this.sleep(1000);
		while (!despawned) {
			this.sleep(interval);
			this.move();
			if (y > vGround || y < 0) {
				this.bounceOnY();
			}
			if (x > screenW || x < 0) {
				this.despawn();
			}
		}
		this.GameOver();
	}
	
	/**
	 * The GameOver method is called once the while loop in the run method is finished (aka when the game is over).
	 * It closes the GameFrame window and opens the GameOverGUI where the user can save their score.
	 */
	public void GameOver() {
		GameFrame.frame.dispose();
		GameOverGUI g = new GameOverGUI();
		g.setCurrentScore(this.hitcount);
		g.frame.setVisible(true);
		g.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	/**
	 * The sleep method stops the process for a given amount of time.
	 * @param milli a given amount of time.
	 */
	public void sleep(int milli) {
		try {
        	Thread.sleep((milli));
        } catch (InterruptedException e){
        	e.printStackTrace();
        }
	}
	
	/**
	 * The Paint method is an abstract method which draws the object on the Frame.
	 * @param g Graphics employed by the frame the object is being drawn on.
	 */
	public abstract void Paint(Graphics g);
	
	/**
	 * The collisionCheck method compares the locations of the ball (this) and the paddle (o2) and returns true if they
	 * have collided.
	 * @param o2 The paddle
	 * @return True if the objects collided, false if not.
	 */
	public Boolean collisionCheck(base o2) {
		Boolean hit = false;
		if (this == o2) {
			System.out.println("Same object as me");
		}
		if (this.x >= o2.x && this.x < o2.x + o2.width) {
			if (this.y > o2.y - o2.length/2 && this.y < o2.y + o2.length/2) {
				hit = true;
			}
		}
		return hit;
	}
	
	/**
	 * A function that sees if collisionCheck is true and calls the deflect and bounce methods if it is true.
	 * @param o2 The paddle
	 */
	public void hitCheck(base o2) {
		if (this.collisionCheck(o2)) {
			this.deflect(o2);
			this.bounceOnX();
		}
	}
}
