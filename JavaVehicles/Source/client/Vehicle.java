package client;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyListener;

/**
 * The Vehicle class mimics a realistic vehicle. It uses the control values speed, acceleration and direction to manipulate
 * dx, dy and dz. These values manipulate the vehicles x, y and z position.
 * @author Erich
 *
 */
public abstract class Vehicle extends Thread implements KeyListener{
	
	//Name of Vehicle
	public String model;
	
	//Control Variables
	private double x, y, z, dx, dy, dz;
	private double speed, acceleration, direction;
	private double pitch = 0;
	private quadrants quadrant;
	
	//Limiters
	protected double pitchUpperLimit = 45, pitchLowerLimit = -45;
	protected int directionBound = 359;
	protected int accelerationUpperLimit = 10, accelerationLowerLimit = -10;
	protected int speedLimit = 120;
	protected int xUpLimit = 1900, xLowLimit = 0;
	protected int yUpLimit = 1000, yLowLimit = 0;
	protected int zUpLimit = 50, zLowLimit = 0;
	protected int groundLevel = 0;
	
	//Dimensions
	protected int width = 10;
	protected int length = 10;
	protected int thickness = 10;
	
	//Directions
	public enum quadrants {NE, SE, SW, NW};
	
	//Booleans
	protected boolean running = true;
	protected boolean isPitching = false;
	protected boolean crashed = false;
	protected boolean landed = false;
	
	/**
	 * The run method is called when the vehicle's '.start()' method is called.
	 */
	public void run() {
		while(running) {
			this.move();
			this.sleep(10);
		}
	}
	
	/**
	 * The kill method is used to kill the thread. It sets the running boolean to false which allows the while loop
	 * in the run method to end.
	 */
	public void kill() {
		this.running = false;
	}
	
	/**
	 * The move method is in charge of calling all the methods to make the vehicle move and doing bounce checks.
	 */
	private void move() {
		if(!landed) {
			this.speed += this.acceleration/100;
			this.limitSpeed();
			this.setDxDyDz();
			if(!this.isPitching)
				this.returnPitchToCenter();
			isPitching = false;
			
			this.checkBounce();
			this.doSpecificThings();
			
			this.x += this.dx/100;
			this.y += this.dy/100;
			this.z += this.dz/100;
		}
	}
	
	/**
	 * doSpecificThings is a workaround to get the vehicle class to call methods inside classes below this one in the hierarchy.
	 */
	protected abstract void doSpecificThings();

	/**
	 * The paint method is used to make a graphic to paint on the screen to represent the vehicle.
	 * @param g - graphics used to paint.
	 */
	public abstract void paint(Graphics g);
	
	/**
	 * The collide method is called when two vehicles collide and deals with repelling them away from eachother.
	 */
	public abstract void collide();
	
	/**
	 * The crash method is called when a vehicle gets into a collision that would warrant a crash
	 */
	public abstract void crash();
	
	/**
	 * setDxDyDz is a gigantic method which does a lot of the dirty work involved in converting the vehicles direction, speed and acceleration
	 * values into a dx, dy and dz value. This method is responsible for setting these values so that the x, y and z position can be modified.
	 */
	private void setDxDyDz() {
		int tempDirection = (int)this.getUseableDirection();
		double flatSpeed = this.speed;
		if(this.pitch != 0) {
			if(this.pitch < 0) {
				flatSpeed = this.speed * (Math.cos(Math.toRadians(-pitch)));
				this.dz = -(this.speed * Math.sin(Math.toRadians(-pitch)));
			} else {
				flatSpeed = this.speed * (Math.cos(Math.toRadians(pitch)));
				this.dz = (this.speed * Math.sin(Math.toRadians(pitch)));
			}
		}
		if(tempDirection == 0) {
			switch ((int)this.direction) {
			case 90:
				this.dx = flatSpeed;
				this.dy = 0;
				break;
			case 180:
				this.dy = flatSpeed;
				this.dx = 0;
				break;
			case 270:
				this.dx = -flatSpeed;
				this.dy = 0;
				break;
			case 0:
				this.dy = -flatSpeed;
				this.dx = 0;
				break;
			}
		} else {
			switch (this.quadrant) {
			case NE:
				this.dx = flatSpeed * (Math.sin(Math.toRadians(tempDirection)));
				this.dy = flatSpeed * (Math.cos(Math.toRadians(tempDirection)));
				this.dy *= -1;
				this.dx *= 1;
				break;
			case SE:
				this.dx = flatSpeed * (Math.cos(Math.toRadians(tempDirection)));
				this.dy = flatSpeed * (Math.sin(Math.toRadians(tempDirection)));
				this.dy *= 1;
				this.dx *= 1;
				break;
			case SW:
				this.dx = flatSpeed * (Math.sin(Math.toRadians(tempDirection)));
				this.dy = flatSpeed * (Math.cos(Math.toRadians(tempDirection)));
				this.dy *= 1;
				this.dx *= -1;
				break;
			case NW:
				this.dx = flatSpeed * (Math.cos(Math.toRadians(tempDirection)));
				this.dy = flatSpeed * (Math.sin(Math.toRadians(tempDirection)));
				this.dy *= -1;
				this.dx *= -1;
				break;
			}
		}
	}
	
	/**
	 * The getUseableDirection method returns a direction value that can be used in right angle trigonometry. Without this method a
	 * direction value of anything over 89 could be plugged into the trigonometry formulas inside 'setDxDyDz' causing issues. It also sets
	 * the quadrant that the object is in so that the math can be later applied back to the right direction.
	 * @return - a direction below 90
	 */
	private double getUseableDirection() {
		double useableDirection = 0;
		if(this.direction < 90) {
			useableDirection = this.direction;
			this.quadrant = quadrants.NE;
		}
		if(this.direction >= 90 && this.direction < 180) {
			useableDirection = this.direction - 90;
			this.quadrant = quadrants.SE;
		}
		if(this.direction >= 180 && this.direction < 270) {
			useableDirection = this.direction - 180;
			this.quadrant = quadrants.SW;
		}
		if(this.direction >= 270 && this.direction < 360) {
			useableDirection = this.direction - 270;
			this.quadrant = quadrants.NW;
		}
		return useableDirection;
	}
	
	/**
	 * The checkCollision method checks if two vehicles are in the same place, meaning they've hit each other.
	 * @param them - the other vehicle being checked against the current vehicle.
	 * @return true if they are in the same position, false if they aren't or if they're the same vehicle.
	 */
	public boolean checkCollision(Vehicle them) {
		if(this == them)
			return false;
		if(this.x >= them.x && this.x <= (them.x + them.width)) {
			if(this.y >= them.y && this.y <= (them.y + them.length)) {
				if(this.z >= them.z && this.z <= (them.z + them.thickness)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * directionCheck makes sure direction does not go over 359 or under 0, it should allow the direction value to
	 * wrap back over itself at those two endpoints.
	 */
	protected void directionCheck() {
		if(this.direction > this.directionBound)
			this.direction -= (double)this.directionBound;
		if(this.direction < 0)
			this.direction += (double)this.directionBound;
	}
	
	/**
	 * The checkBounce method is another massive method which does a lot of math to send the vehicle back where it came from
	 * realistically when it hits any of the barriers in the space.
	 */
	private void checkBounce() {
		if(this.x > this.xUpLimit) {
			double newDir = 0;
			switch(this.quadrant) {
			case NE:
				newDir = (90 - this.getUseableDirection()) + 270;
				break;
			case SE:
				newDir = (90 - this.getUseableDirection()) + 180;
				break;
			case SW:
				newDir = this.direction;
				break;
			case NW:
				newDir = this.direction;
				break;
			}
			this.direction = newDir;
			this.x = this.xUpLimit - 10;
			this.directionCheck();
		}
		if(this.y > this.yUpLimit) {
			double newDir = 0;
			//This doesnt check out in real math but Javas Y is upside down
			switch(this.quadrant) {
			case NE:
				newDir = this.direction;
				break;
			case SE:
				newDir = (90 - this.getUseableDirection());
				break;
			case SW:
				newDir = (90 - this.getUseableDirection()) + 270;
				break;
			case NW:
				newDir = this.direction;
				break;
			}
			this.direction = newDir;
			this.y = this.yUpLimit - 10;
			this.directionCheck();
		}
		if(this.z > this.zUpLimit) {
			this.z = this.zUpLimit - 10;
			this.pitch = 0;
		}
		if(this.x < this.xLowLimit) {
			double newDir = 0;
			switch(this.quadrant) {
			case NE:
				newDir = this.direction;
				break;
			case SE:
				newDir = this.direction;
				break;
			case SW:
				newDir = (90 - this.getUseableDirection()) + 90;
				break;
			case NW:
				newDir = (90 - this.getUseableDirection());
				break;
			}
			this.direction = newDir;
			this.x = this.xLowLimit + 10;
			this.directionCheck();
		}
		if(this.y < this.yLowLimit) {
			double newDir = 0;
			//This also doesnt check out in real math but Javas Y is upside down
			switch(this.quadrant) {
			case NE:
				newDir = (90 - this.getUseableDirection()) + 90;
				break;
			case SE:
				newDir = this.direction;
				break;
			case SW:
				newDir = this.direction;
				break;
			case NW:
				newDir = (90 - this.getUseableDirection()) + 180;
				break;
			}
			this.direction = newDir;
			this.y = this.yLowLimit + 10;
			this.directionCheck();
		}
	}
	
	/**
	 * The limitAccel method makes sure the vehicle's acceleration does not exceed the upper and lower limits. It will also set
	 * acceleration to zero if the speedLimit has been reached.
	 */
	protected void limitAccel() {
		if(this.acceleration > this.accelerationUpperLimit)
			this.acceleration = (double)this.accelerationUpperLimit;
		if(this.acceleration < this.accelerationLowerLimit)
			this.acceleration = (double)this.accelerationLowerLimit;
		if(this.speed >= this.speedLimit || this.speed <= 0) {
			this.acceleration = 0;
		}
	}
	
	/**
	 * The limitSpeed method makes sure the vehicle's speed does not exceed the speedLimit or go below zero.
	 */
	protected void limitSpeed() {
		if(this.speed > this.speedLimit)
			this.speed = (double)this.speedLimit;
		if(this.speed < 0)
			this.speed = 0;
	}
	
	/**
	 * returnPitchToCenter is responsible for making the plane slowly level out when the pitch controls aren't being touched.
	 */
	protected void returnPitchToCenter() {
		if(!landed) {
			if(this.pitch != 0) {
				if(this.pitch < 0)
					this.pitch++;
				if(this.pitch > 0)
					this.pitch--;
			}
		}
	}
	
	public double getX() {
		return this.x;
	}
	
	public void setX(double newX) {
		this.x = newX;
	}
	
	public double getY() {
		return this.y;
	}
	
	public void setY(double newY) {
		this.y = newY;
	}
	
	public double getZ() {
		return this.z;
	}
	
	public void setZ(double newZ) {
		this.z = newZ;
	}
	
	public double getSpeed() {
		return this.speed;
	}
	
	public void setSpeed(double newSpd) {
		this.speed = newSpd;
	}
	
	public double getAccel() {
		return this.acceleration;
	}
	
	public void setAccel(double newAccel) {
		this.acceleration = newAccel;
	}
	
	public double getDir() {
		return this.direction;
	}
	
	public void setDir(double newDir) {
		this.direction = newDir;
	}
	
	public void decrementDir() {
		this.direction--;
	}
	
	public void incrementDir() {
		this.direction++;
	}
	
	public double getPitch() {
		return this.pitch;
	}
	
	public void setPitch(double newPitch) {
		this.pitch = newPitch;
	}
	
	public void incrementPitch() {
		this.pitch++;
	}
	
	public void decrementPitch() {
		this.pitch--;
	}
	
	public void setScreenBounds(Rectangle bounds) {
		this.xUpLimit = bounds.width;
		this.yUpLimit = bounds.height;
	}
	
	private void sleep(int milli) {
        try {
            Thread.sleep(milli);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	}
}