package client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

/**
 * The LandVehicle class extends the vehicle class and moves like a Land Vehicle with controls such as steering, a gas pedal and brake pedal.
 * The only methods that are public are the controls for the vehicle and the constructors.
 * @author Erich
 *
 */
public class LandVehicle extends Vehicle implements KeyListener {
	private double gasPedal, brakePedal;
	private double gasPedalLimit = 100;
	private double brakePedalLimit = 100;
	public enum steering {LEFT, CENTER, RIGHT};
	
	Random r = new Random();
	
	private int sizeL = 10;
	private int sizeW = 10;
	
	/**
	 * This constructor initializes x, y and the direction of the LandVehicle to random values and sets the pitch and z to zero.
	 * It takes a string to name the vehicle so it may be identified.
	 * @param model The name of the vehicle.
	 */
	public LandVehicle(String model) { 
		this.model = model;
		setX((double)r.nextInt(xUpLimit) + xLowLimit);
		setY((double)r.nextInt(yUpLimit) + yLowLimit);
		setZ(0);
		setDir((double)r.nextInt(directionBound));
		setPitch(0);
	}
	
	/**
	 * This constructor is used when the vehicle is created from NetworkVehicle data passed in
	 * from the network.
	 * @param model the name of the vehicle
	 * @param x the location on the x-axis
	 * @param y the location on the y-axis
	 * @param z the location on the z-axis
	 * @param speed the speed the vehicle is traveling at
	 * @param acceleration the acceleration the vehicle is carrying
	 * @param direction the direction the vehicle is heading
	 */
	public LandVehicle(String model, double x, double y, double z, double speed, double acceleration, double direction) {
		this.model = model;
		setX(x);
		setY(y);
		setZ(z);
		setSpeed(speed);
		setAccel(acceleration);
		setDir(direction);
		setPitch(0);
	}
	
	/**
	 * The accelerate method is called when the gas pedal is pressed. It increments acceleration depending on how hard the gas pedal
	 * is being pressed.
	 */
	private void accelerate() {
		setAccel(getAccel() + gasPedal / 10);
	}
	
	/**
	 * The decelerate method is called when the brake pedal is pressed. It decrements acceleration depending on how hard the brake
	 * pedal is being pressed.
	 */
	private void decelerate() {
		setAccel(getAccel() - brakePedal / 10);
	}
	
	/**
	 * The steer method takes one of the three possible enumerations of 'steering' and increments or decrements the vehicles direction
	 * accordingly.
	 * @param in - the steering input from the users controls.
	 */
	public void steer(steering in) {
		if(in == steering.LEFT)
			decrementDir();
		if(in == steering.RIGHT)
			incrementDir();
		directionCheck();
	}
	
	/**
	 * pressGas is called when the user uses the control to move the LandVehicle forward. This method increments the gasPedal value
	 * until it reaches its limit and sets the brake pedal value to zero.
	 */
	public void pressGas() {
		brakePedal = 0;
		if(gasPedal < gasPedalLimit)
			gasPedal++;
		accelerate();
	}
	
	/**
	 * pressBrake is called when the user uses the control to slow the LandVehicle down. This method increments the brakePedal value
	 * until it reaches its limit and sets the gas pedal value to zero.
	 */
	public void pressBrake() {
		gasPedal = 0;
		if(brakePedal < brakePedalLimit)
			brakePedal++;
		decelerate();
	}

	@Override
	public void paint(Graphics g)  {
		g.setColor(Color.BLUE);
		g.fillRect((int)getX(), (int)getY(), (int)(sizeW * ((getZ()/100) + 1)), (int)(sizeL * ((getZ()/100) + 1)));
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_W) {
			pressGas();
		}
		if(key == KeyEvent.VK_S) {
			pressBrake();
		}
		if(key == KeyEvent.VK_A) {
			steer(steering.LEFT);
		}
		if(key == KeyEvent.VK_D) {
			steer(steering.RIGHT);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_A) {
			steer(steering.CENTER);
		}
		if(key == KeyEvent.VK_D) {
			steer(steering.CENTER);
		}
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void collide() {
		double tempDir = getDir();
		if(getDir() >= 180)
			setDir(tempDir - 180);
		else
			setDir(tempDir + 180);
		setAccel(-10);
	}

	@Override
	public void doSpecificThings() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void crash() {
		// TODO Auto-generated method stub
		
	}
}
