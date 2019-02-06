package common;

import java.util.ArrayList;

import common.Type;

/**
 * A Class that holds and manages a list of NetworkVehicles used to save and load the state of the server upon closing and opening.
 * @author Erich
 *
 */
public class ListOfNetworkVehicles {

		private ArrayList<NetworkVehicle> master_list = new ArrayList<NetworkVehicle>();
		private int master_list_count = 0;
		
		/**
		 * Returns the entire ArrayList of network vehicles.
		 * @return master_list ArrayList of network vehicles
		 */
		public ArrayList<NetworkVehicle> getAllNetworkVehicles() {
			return master_list;
		}
		
		/**
		 * Adds a Network Vehicle to the master_list.
		 * @param v A Network Vehicle to be added to the list
		 * @return the count of how many vehicles are in the list.
		 */
		public int addVehicle(NetworkVehicle v) {
			master_list.add(v);
			return master_list_count;
		}
		
		/**
		 * Adds multiple vehicles to the master_list from a given ArrayList.
		 * @param vehicles ArrayList of vehicles to be added
		 * @return the count of how many vehicles are in the list.
		 */
		public int addVehicles(ArrayList<NetworkVehicle> vehicles) {
			for(NetworkVehicle v: vehicles)
				addVehicle(v);
			return master_list_count;
		}
}
