package display2D;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.persistence.Entity;

import display2D.Vehicle.bearings;

/**
 * The LandVehicle class extends the vehicle class and moves like a Land Vehicle with controls such as steering, a gas pedal and brake pedal.
 * The only methods that are public are the controls for the vehicle and the constructors.
 * @author Erich
 *
 */
@Entity
public class LandVehicle extends Vehicle implements KeyListener {
	private double gasPedal, brakePedal;
	private double gasPedalLimit = 100;
	private double brakePedalLimit = 100;
	public enum steering {LEFT, CENTER, RIGHT};
	private boolean gasPressed = false;
	private boolean brakePressed = false;
	
	/**
	 * This constructor initializes x, y and the direction of the LandVehicle to random values and sets the pitch and z to zero.
	 * It takes a string to name the vehicle so it may be identified.
	 * @param model - The name of the vehicle.
	 */
	public LandVehicle(String model) { 
		Random r = new Random();
		this.model = model;
		this.x = (double)r.nextInt(xUpLimit) + xLowLimit;
		this.y = (double)r.nextInt(yUpLimit) + yLowLimit;
		this.z = 0;
		this.direction = (double)r.nextInt(directionBound);
		this.pitch = 0;
	}
	
	/**
	 * This constructor is used to for testing mostly. It allows the LandVehicle to be initialized with specified values, except pitch.
	 * @param model - The name of the vehicle.
	 * @param x - The x position of the vehicle.
	 * @param y - The y position of the vehicle.
	 * @param z - The z position of the vehicle.
	 * @param direction - The direction the vehicle is heading.
	 */
	public LandVehicle(String model, double x, double y, double z, double speed, double acceleration, double direction) {
		this.model = model;
		this.x = x;
		this.y = y;
		this.z = z;
		this.speed = speed;
		this.acceleration = acceleration;
		this.direction = direction;
		this.pitch = 0;
	}
	
	/**
	 * The accelerate method is called when the gas pedal is pressed. It increments acceleration depending on how hard the gas pedal
	 * is being pressed.
	 */
	private void accelerate() {
		this.acceleration += this.gasPedal / 10;
	}
	
	/**
	 * The decelerate method is called when the brake pedal is pressed. It decrements acceleration depending on how hard the brake
	 * pedal is being pressed.
	 */
	private void decelerate() {
		this.acceleration -= this.brakePedal / 10;
	}
	
	/**
	 * The steer method takes one of the three possible enumerations of 'steering' and increments or decrements the vehicles direction
	 * accordingly.
	 * @param in - the steering input from the users controls.
	 */
	public void steer(steering in) {
		if(in == steering.LEFT)
			this.direction--;
		if(in == steering.RIGHT)
			this.direction++;
		this.directionCheck();
	}
	
	/**
	 * pressGas is called when the user uses the control to move the LandVehicle forward. This method increments the gasPedal value
	 * until it reaches its limit and sets the brake pedal value to zero.
	 */
	public void pressGas() {
		this.gasPressed = true;
		this.brakePressed = false;
		this.brakePedal = 0;
		if(this.gasPedal < this.gasPedalLimit)
			this.gasPedal++;
		this.accelerate();
	}
	
	/**
	 * pressBrake is called when the user uses the control to slow the LandVehicle down. This method increments the brakePedal value
	 * until it reaches its limit and sets the gas pedal value to zero.
	 */
	public void pressBrake() {
		this.gasPressed = false;
		this.brakePressed = true;
		this.gasPedal = 0;
		if(this.brakePedal < this.brakePedalLimit)
			this.brakePedal++;
		this.decelerate();
	}

	@Override
	public void paint(Graphics g)  {
		int sizeL = 10;
		int sizeW = 10;
//		if(this.bearing == bearings.EAST || this.bearing == bearings.WEST) {
//			sizeL = 10;
//			sizeW = 20;
//		}
		g.setColor(Color.BLUE);
		g.fillRect((int)this.x, (int)this.y, (int)(sizeW * ((z/100) + 1)), (int)(sizeL * ((z/100) + 1)));
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_W) {
			this.pressGas();
		}
		if(key == KeyEvent.VK_S) {
			this.pressBrake();
		}
		if(key == KeyEvent.VK_A) {
			this.steer(steering.LEFT);
		}
		if(key == KeyEvent.VK_D) {
			this.steer(steering.RIGHT);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_A) {
			this.steer(steering.CENTER);
		}
		if(key == KeyEvent.VK_D) {
			this.steer(steering.CENTER);
		}
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void collide() {
		if(this.direction >= 180)
			this.direction -= 180;
		else
			this.direction += 180;
		this.acceleration = -10;
	}
	
	@Override
	public void crash(Vehicle them) {
		if(this.speed > them.speed)
			this.speed -= them.speed;
		else {
			this.collide();
			this.speed = them.speed;
		}
		this.acceleration = -10;
		crashed = true;
	}

	@Override
	public void doSpecificThings() {
		// TODO Auto-generated method stub
		
	}
}
