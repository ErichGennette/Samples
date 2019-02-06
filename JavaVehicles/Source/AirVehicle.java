package display2D;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.persistence.Entity;

/**
 * The AirVehicle class extends the vehicle class and moves like an Air Vehicle with controls such as banking, pitching, pedaling faster and pedaling slower.
 * The only methods that are public are the controls for the vehicle and the constructors.
 * @author Erich
 *
 */
@Entity
public class AirVehicle extends Vehicle implements KeyListener{
	private double pedalSpeed;
	private double pedalSpeedLimit = 100;
	public enum banking {LEFT, CENTER, RIGHT};
	public enum pitching {UP, CENTER, DOWN};
	
	/**
	 * This constructor initializes x, y, z and the direction of the AirVehicle to random values and sets the pitch to zero.
	 * It takes a string to name the vehicle so it may be identified.
	 * @param model - The name of the vehicle.
	 */
	public AirVehicle(String model) {
		Random r = new Random();
		this.model = model;
		this.x = (double)r.nextInt(xUpLimit) + xLowLimit;
		this.y = (double)r.nextInt(yUpLimit) + yLowLimit;
		this.z = (double)(r.nextInt(zUpLimit - -10) + 10);
		this.direction = (double)r.nextInt(directionBound);
		this.pitch = 0;
	}
	
	/**
	 * This constructor is used to for testing mostly. It allows the AirVehicle to be initialized with specified values, except pitch.
	 * @param model - The name of the vehicle.
	 * @param x - The x position of the vehicle.
	 * @param y - The y position of the vehicle.
	 * @param z - The z position of the vehicle.
	 * @param direction - The direction the vehicle is heading.
	 */
	public AirVehicle(String model, double x, double y, double z, double speed, double acceleration, double direction) {
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
	 * The Pedals method takes the pedal input from the user and increments or decrements acceleration accordingly.
	 */
	private void pedals() {
		if(pedalSpeed > 50) {
			if(this.acceleration < 0)
				this.acceleration = 0;
			this.acceleration += (50 - (pedalSpeed / 2)) / 10;
		}
		if(pedalSpeed < 50) {
			if(this.acceleration > 0)
				this.acceleration = 0;
			this.acceleration -= (50 - (pedalSpeed / 2)) / 10;
		}
		if(pedalSpeed == 50) {
			this.acceleration = 0;
		}
	}
	
	/**
	 * pedalFaster is called when the user uses the control to make the AirVehicle speed up. It increments pedalSpeed by one every time
	 * it is called until it reaches the pedalSpeedLimit.
	 */
	public void pedalFaster() {
		if(this.pedalSpeed < pedalSpeedLimit)
			this.pedalSpeed++;
		landed = false;
		pedals();
	}
	
	/**
	 * pedalSlower is called when the user uses the control to slow the AirVehicle down. This method decrements pedalSpeed until it reaches
	 * zero which is the slowest the pedals can go.
	 */
	public void pedalSlower() {
		if(this.pedalSpeed > 0)
			this.pedalSpeed--;
		pedals();
	}
	
	
	/**
	 * The bank method takes in one of the three possible enumerations of 'banking' and turns the AirVehicle by incrementing or
	 * decrementing its direction, essentially rotating the vehicle.
	 * @param in - the banking input from the users control.
	 */
	public void bank(banking in) {
		if(in == banking.LEFT)
			this.direction--;
		if(in == banking.RIGHT)
			this.direction++;
		this.directionCheck();
	}
	
	/**
	 * The pitch method takes in one of the three possible enumerations of 'pitching' and points the nose of the AirVehicle up or down
	 * by calling the rise method or or the fall method.
	 * @param in - the pitching input from the users control.
	 */
	public void pitch(pitching in) {
		if(in == pitching.UP)
			this.rise();
		if(in == pitching.DOWN)
			this.fall();
	}
	
	/**
	 * The fall method makes sure the nose of the AirVehicle isn't already pointed down at the maximum angle and then decrements
	 * the pitch value by one.
	 */
	private void fall() {
		if(this.pitch > this.pitchLowerLimit)
			this.pitch--;
		this.isPitching = true;
	}
	
	/**
	 * The rise method makes sure the nose of the Airvehicle isn't already pointed upward at the maximum angle and then increments
	 * the pitch value by one.
	 */
	private void rise() {
		if(this.pitch < this.pitchUpperLimit)
			this.pitch++;
		this.isPitching = true;
	}
	
	/**
	 * The land method is called in the 'doSpecificThings' method which gets called in the vehicles 'move' method higher up.
	 * This method is triggered when checkIfLanded returns true, at this point the method determines if the landing is slow enough
	 * that the AirVehicle does not crash. If it is going too fast it will crash, otherwise it will stop on the ground unharmed.
	 */
	private void land() {
		if(this.speed >= 5)
			crash(this);
		this.acceleration = 0;
		this.speed = 0;
		this.pedalSpeed = 0;
		this.pitch = 0;
		this.z = 0;
		landed = true;
	}
	
	/**
	 * checkIfLanded checks to see if the AirVehicle's z position is below 5.
	 * @return true if below a Z of 5, false if above a Z of 5.
	 */
	private boolean checkIfLanded() {
		if(!landed) {
			if(this.z <= groundLevel + 5)
				return true;
		}
		return false;
	}

	@Override
	public void paint(Graphics g)  {
		int sizeL = 10;
		int sizeW = 10;
//		if(this.bearing == bearings.EAST || this.bearing == bearings.WEST) {
//			sizeL = 10;
//			sizeW = 20;
//		}
		if(crashed)
			g.setColor(Color.BLACK);
		else
			g.setColor(Color.RED);
		g.fillOval((int)this.x, (int)this.y, (int)(sizeL * ((z/25) + 1)), (int)(sizeW * ((z/25) + 1)));
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_W) {
			this.pitch(pitching.DOWN);
		}
		if(key == KeyEvent.VK_S) {
			this.pitch(pitching.UP);
		}
		if(key == KeyEvent.VK_A) {
			this.bank(banking.LEFT);
		}
		if(key == KeyEvent.VK_D) {
			this.bank(banking.RIGHT);
		}
		if(key == KeyEvent.VK_Q) {
			this.pedalFaster();
		}
		if(key == KeyEvent.VK_Z) {
			this.pedalSlower();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_A) {
			this.bank(banking.CENTER);
		}
		if(key == KeyEvent.VK_D) {
			this.bank(banking.CENTER);
		}
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void collide() {
		if(this.direction >= 180)
			this.direction -= 180;
		else
			this.direction += 180;
		this.pitch = -this.pitch;
	}
	
	@Override
	public void crash(Vehicle them) {
		if(this != them) {
			if(this.speed > them.speed)
				this.speed -= them.speed;
			else {
				this.collide();
				this.speed = them.speed;
			}
		}
		this.pitch = pitchLowerLimit;
		crashed = true;
	}

	@Override
	public void doSpecificThings() {
		if(checkIfLanded())
			this.land();
	}

}
