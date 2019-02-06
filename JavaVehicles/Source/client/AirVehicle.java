package client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

/**
 * The AirVehicle class extends the vehicle class and moves like an Air Vehicle with controls such as banking, pitching, pedaling faster and pedaling slower.
 * The only methods that are public are the controls for the vehicle and the constructors.
 * @author Erich
 *
 */
public class AirVehicle extends Vehicle implements KeyListener{
	private double pedalSpeed;
	private double pedalSpeedLimit = 100;
	public enum banking {LEFT, CENTER, RIGHT};
	public enum pitching {UP, CENTER, DOWN};
	
	Random r = new Random();
	
	private int sizeL = 10;
	private int sizeW = 10;
	
	/**
	 * This constructor initializes x, y, z and the direction of the AirVehicle to random values and sets the pitch to zero.
	 * It takes a string to name the vehicle so it may be identified.
	 * @param model The name of the vehicle.
	 */
	public AirVehicle(String model) {
		this.model = model;
		setX((double)r.nextInt(xUpLimit) + xLowLimit);
		setY((double)r.nextInt(yUpLimit) + yLowLimit);
		setZ((double)(r.nextInt(zUpLimit - -10) + 10));
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
	public AirVehicle(String model, double x, double y, double z, double speed, double acceleration, double direction) {
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
	 * The Pedals method takes the pedal input from the user and increments or decrements acceleration accordingly.
	 */
	private void pedals() {
		double tempAccel = getAccel();
		if(pedalSpeed > 50) {
			if(tempAccel < 0)
				setAccel(0);
			setAccel(tempAccel + (50 - (pedalSpeed / 2)) / 10);
		}
		if(pedalSpeed < 50) {
			if(tempAccel > 0)
				setAccel(0);
			setAccel(getAccel() - (50 - (pedalSpeed / 2)) / 10);
		}
		if(pedalSpeed == 50) {
			setAccel(0);
		}
	}
	
	/**
	 * pedalFaster is called when the user uses the control to make the AirVehicle speed up. It increments pedalSpeed by one every time
	 * it is called until it reaches the pedalSpeedLimit.
	 */
	public void pedalFaster() {
		if(pedalSpeed < pedalSpeedLimit)
			pedalSpeed++;
		landed = false;
		pedals();
	}
	
	/**
	 * pedalSlower is called when the user uses the control to slow the AirVehicle down. This method decrements pedalSpeed until it reaches
	 * zero which is the slowest the pedals can go.
	 */
	public void pedalSlower() {
		if(pedalSpeed > 0)
			pedalSpeed--;
		pedals();
	}
	
	
	/**
	 * The bank method takes in one of the three possible enumerations of 'banking' and turns the AirVehicle by incrementing or
	 * decrementing its direction, essentially rotating the vehicle.
	 * @param in - the banking input from the users control.
	 */
	public void bank(banking in) {
		if(in == banking.LEFT)
			decrementDir();
		if(in == banking.RIGHT)
			incrementDir();
		directionCheck();
	}
	
	/**
	 * The pitch method takes in one of the three possible enumerations of 'pitching' and points the nose of the AirVehicle up or down
	 * by calling the rise method or or the fall method.
	 * @param in - the pitching input from the users control.
	 */
	public void pitch(pitching in) {
		if(in == pitching.UP)
			rise();
		if(in == pitching.DOWN)
			fall();
	}
	
	/**
	 * The fall method makes sure the nose of the AirVehicle isn't already pointed down at the maximum angle and then decrements
	 * the pitch value by one.
	 */
	private void fall() {
		if(getPitch() > pitchLowerLimit)
			decrementPitch();
		isPitching = true;
	}
	
	/**
	 * The rise method makes sure the nose of the Airvehicle isn't already pointed upward at the maximum angle and then increments
	 * the pitch value by one.
	 */
	private void rise() {
		if(getPitch() < pitchUpperLimit)
			incrementPitch();
		isPitching = true;
	}
	
	/**
	 * The land method is called in the 'doSpecificThings' method which gets called in the vehicles 'move' method higher up.
	 * This method is triggered when checkIfLanded returns true, at this point the method determines if the landing is slow enough
	 * that the AirVehicle does not crash. If it is going too fast it will crash, otherwise it will stop on the ground unharmed.
	 */
	private void land() {
		if(getSpeed() >= 5)
			crash();
		setAccel(0);
		setSpeed(0);
		pedalSpeed = (0);
		setPitch(0);
		setZ(0);
		landed = true;
	}
	
	/**
	 * checkIfLanded checks to see if the AirVehicle's z position is below 5.
	 * @return true if below a Z of 5, false if above a Z of 5.
	 */
	private boolean checkIfLanded() {
		if(!landed) {
			if(getZ() <= groundLevel + 5)
				return true;
		}
		return false;
	}

	@Override
	public void paint(Graphics g)  {
		if(crashed)
			g.setColor(Color.BLACK);
		else
			g.setColor(Color.RED);
		g.fillOval((int)getX(), (int)getY(), (int)(sizeL * ((getZ()/25) + 1)), (int)(sizeW * ((getZ()/25) + 1)));
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_W) {
			pitch(pitching.DOWN);
		}
		if(key == KeyEvent.VK_S) {
			pitch(pitching.UP);
		}
		if(key == KeyEvent.VK_A) {
			bank(banking.LEFT);
		}
		if(key == KeyEvent.VK_D) {
			bank(banking.RIGHT);
		}
		if(key == KeyEvent.VK_Q) {
			pedalFaster();
		}
		if(key == KeyEvent.VK_Z) {
			pedalSlower();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_A) {
			bank(banking.CENTER);
		}
		if(key == KeyEvent.VK_D) {
			bank(banking.CENTER);
		}
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void collide() {
		double tempDir = getDir();
		if(getDir() >= 180)
			setDir(tempDir - 180);
		else
			setDir(tempDir + 180);
		setPitch(-getPitch());
		crash();
	}

	@Override
	public void doSpecificThings() {
		if(checkIfLanded())
			land();
	}

	@Override
	public void crash() {
		setPitch(pitchLowerLimit);
		crashed = true;
	}

}
