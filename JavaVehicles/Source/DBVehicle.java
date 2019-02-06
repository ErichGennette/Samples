package display2D;

import javax.persistence.Entity;

@Entity
public class DBVehicle {
	//Name of Vehicle
	public String model;
	
	//Control Variables
	protected String type;
	protected double x, y, z, dx, dy, dz;
	protected double speed, acceleration, direction;
	
	public DBVehicle(String type, String model, double x, double y, double z, double speed, double acceleration, double direction) {
		this.type = type;
		this.model = model;
		this.x = x;
		this.y = y;
		this.z = z;
		this.speed = speed;
		this.acceleration = acceleration;
		this.direction = direction;
	}
}