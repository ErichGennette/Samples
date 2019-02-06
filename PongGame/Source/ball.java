package a11rallypong;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

/**
 * The ball class creates a ball that moves around on the screen and is hit back and forth between two paddles.
 * It extends the base class just like the paddle.
 * @author Erich
 *
 */
public class ball extends base {
	
	int bb = 5;
	
	Random r = new Random();
	
	/**
	 * The base constructor takes in four values to setup the items on the screen in their designated spots.
	 * @param x the desired initial position of the object on the x axis.
	 * @param y the desired initial position of the object on the y axis.
	 * @param screenH the height of the screen the object is being displayed on.
	 * @param screenW the width of the screen the object is being displayed on.
	 */
	public ball(int x, int y, int screenH, int screenW) {
		super(x, y, screenW, screenW);
		this.getVels();
		this.initX = x;
		this.initY = y;
		this.length = 20;
		this.vGround = screenH - 45;
		this.hitcount = 0;
	}
	
	/**
	 * The Paint method draws the ball on the frame.
	 */
	@Override
	public void Paint(Graphics g) {
		g.setColor(Color.LIGHT_GRAY);
		g.fillOval((int)x, (int)y - length/2, length, length);
		g.setColor(Color.WHITE);
		g.fillOval((int)x + bb/2, (int)y - (length - bb)/2, length - bb, length - bb);
	}
	
	/**
	 * The bounceOnX method is called whenever the ball hits a paddle. It first determines how fast the ball is going and then
	 * decides if it should increment it by 10% or just leave it at its defined speed limit. After determining the multiplier it
	 * multiplys the balls dx by this multiplier and increments hitcount. Finally the preventReverb method is called.
	 */
	@Override
	public void bounceOnX() {
		double factor = 1;
		if (dx <= 25 || dx >= -25)
			factor = 1.1;
		if (dx > 25 || dx < -25)
			factor = 1;
		dx = dx * -factor;
		hitcount++;
		this.preventReverb();
	}

	/**
	 * The bounceOnY method is called whenever the ball comes in contact with the top or bottom of the screen,
	 * it simply inverts the balls y velocity.
	 */
	@Override
	public void bounceOnY() {
		dy = dy * -1;
	}
	
	/*
	 * The preventReverb method determines what paddle the ball just hit by seeing what side of the screen it is on.
	 * Then it adds a set number of units to the x location of the ball. This is done to prevent the ball from getting stuck in the paddles hit box
	 * effectively giving the user multiple points in one hit.
	 */
	public void preventReverb() {
		if (x < screenW/2)
			x = x + 5;
		x = x - 5;
	}

	/**
	 * The despawn method sets the despawned boolean to true when called.
	 */
	@Override
	public void despawn() {
		this.despawned = true;
		
	}
	
	/**
	 * The randomDx method gets a random dx of either 2 or -2 to launch the ball,
	 * this way it is random as to which side the ball is served to.
	 */
	public void randomDx() {
		int val = 0;
		val = r.nextInt(2);
		if (val == 0) {
			this.dx = 3;
		} else {
			this.dx = -3;
		}
	}
	
	/**
	 * The randomDy method returns a value to be assigned to the balls y velocity. It ranges from 1 to -1 in decimals.
	 * @return a double to be assigned to dy.
	 */
	public double randomDy() {
		double val = 0;
		while(val == 0 || val == 1 || val == -1) {
			val = r.nextDouble();
			val = val * r.nextInt(3)-1;
		}
		return val;
	}
	
	/**
	 * getVels sets the dx and dy of the ball when it is instantiated by calling their assigned
	 * methods.
	 */
	public void getVels() {
		this.randomDx();
		this.dy = this.randomDy();
	}
}
