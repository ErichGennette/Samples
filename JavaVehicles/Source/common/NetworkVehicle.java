package common;

import java.io.Serializable;

import javax.persistence.Entity;

/**
 * The NetworkVehicle is a cut down version of the Vehicle class that only holds the variables needed to
 * control the vehicle.
 * @author Erich
 *
 */
@Entity
public class NetworkVehicle implements Serializable {
	
	private static final long serialVersionUID = 5283150831160753547L;
	public static final Integer serverport = 53475;
	private int vehicleNumber = 0;
	public NetworkCommand comm;

	//Name of Vehicle
	public String model;
	
	//Control Variables
	protected Type type;
	protected double x, y, z;
	protected double speed, acceleration, direction;
	
	/**
	 * This constructor is used to initialize a NetworkVehicle using values given to the constructor
	 * @param type One of the two Type enumerations used to save which kind of vehicle was saved as a NetworkVehicle
	 * @param model The name of the vehicle
	 * @param x The x location of the vehicle
	 * @param y The y location of the vehicle
	 * @param z The z location of the vehicle
	 * @param speed The speed of the vehicle
	 * @param acceleration The acceleration of the vehicle
	 * @param direction The direction of the vehicle
	 */
	public NetworkVehicle(Type type, String model, double x, double y, double z, double speed, double acceleration, double direction) {
		this.type = type;
		this.model = model;
		this.x = x;
		this.y = y;
		this.z = z;
		this.speed = speed;
		this.acceleration = acceleration;
		this.direction = direction;
	}
	
	/**
	 * This constructor is used to initialize a NetworkVehicle using another vehicles values
	 * @param v A NetworkVehicle to take the values from
	 */
	public NetworkVehicle(NetworkVehicle v) {
		this.type = v.type;
		this.model = v.model;
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
		this.speed = v.speed;
		this.acceleration = v.acceleration;
		this.direction = v.direction;
	}
	
	public int getVehicleNumber() {
		return vehicleNumber;
	}
	
	public void setVehicleNumber(int newNumber) {
		this.vehicleNumber = newNumber;
	}
	
	public Type getType() {
		return this.type;
	}
	
	public void setComm(NetworkCommand comm) {
		this.comm = comm;
	}

	public NetworkCommand getComm() {
		return comm;
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
}