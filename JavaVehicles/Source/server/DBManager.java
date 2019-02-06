package server;

import java.util.ArrayList;
import java.util.List;

import common.NetworkVehicle;

/**
 * The DBManager is used to make a smooth transition between the Server and the Database.
 * It exists so that the server must only call query or update to manipulate the Database.
 * @author Erich
 *
 */
public class DBManager {
	Database db;
	
	/**
	 * The DBManager constructor initializes a database with the filename
	 */
	public DBManager() {
		db = new Database("vehicledb.odb");
	}
	
	/**
	 * The update method is used to call saveNew for every object in the given ArrayList
	 * @param masterList An ArrayList of vehicles to be saved to the DB
	 */
	public void update(ArrayList<NetworkVehicle> masterList) {
		db.clear();
		for(NetworkVehicle v: masterList)
			db.saveNew(v);
	}
	
	/**
	 * Query returns an ArrayList of NetworkVehicles which is created from the List of Objects returned by the DB's 'getObjects' method.
	 * @return An ArrayList of NetworkVehicles.
	 */
	public ArrayList<NetworkVehicle> query() {
		ArrayList<NetworkVehicle> queriedVehicles = new ArrayList<NetworkVehicle>();
		List<Object> rawData = db.getObjects(NetworkVehicle.class);
		for(Object o : rawData) {
			NetworkVehicle nv = (NetworkVehicle) o;
			queriedVehicles.add(nv);
		}
		return queriedVehicles;
	}

}
