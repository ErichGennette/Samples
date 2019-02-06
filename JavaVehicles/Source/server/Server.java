package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

import common.ListOfNetworkVehicles;
import common.NetReader;
import common.NetWriter;
import common.NetworkCommand;
import common.NetworkVehicle;

/**
 * The Server is responsible for reading each client's sockets and sending any vehicles they add to the rest of the clients
 * using their NetWriters.
 * @author Erich
 *
 */
public class Server extends Thread implements Observer {

	Socket sock = null;
	ServerSocket serverSocket = null;

	private boolean more = true;
	private final Integer serverport = NetworkVehicle.serverport;
	
	ListOfNetworkVehicles networkVehicles  = new ListOfNetworkVehicles();
	
	DBManager db;

	ArrayList<NetWriter> clients = new ArrayList<NetWriter>();

	/**
	 * Creates the server on the local host and pulls the vehicles from the Object DB
	 */
	public Server() {
		super("Vehicle Server");
		try {
			serverSocket = new ServerSocket(serverport.intValue());
			System.out.println("Vehicle Server listening/Getting on port: " + serverSocket.getLocalPort());
		} catch (java.io.IOException e) {
			e.printStackTrace();
			System.err.println("Could not create datagram socket.");
		}
		db = new DBManager();
		networkVehicles.addVehicles(db.query());
	}

	Boolean duplicate = false;

	@Override
	public void run() {
		if (serverSocket == null)
			return;
		while (more) {
			System.out.println("Waiting for next client.....");
			try {
				sock = serverSocket.accept();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// Launch Reader Thread to handle this new client
			NetReader reader = new NetReader(sock);
			(new Thread(reader)).start();
			reader.addObserver(this);

			NetWriter writer = new NetWriter(sock);
			writer.start();

			clients.add(writer);
			System.out.println("Writer Array Size: " + clients.size());
			
			for (NetWriter w : clients)
				System.out.print(" :: " + w.getIP());
			
			sendMasterListToClient(writer, networkVehicles.getAllNetworkVehicles());
		}

		try {
			serverSocket.close();
		} catch (IOException ex) {
			Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	/**
	 * Sends the entire contents of the master list to the given client.
	 * @param w the NetWriter of the client to be written to
	 * @param list the list of vehicles to write to the client
	 */
	private void sendMasterListToClient(NetWriter w, ArrayList<NetworkVehicle> list) {
		for(NetworkVehicle v: list) {
			w.sendThisMsgOnQueue(v);
			System.out.print(" send new animal to " + w.getIP());
		}
	}

	@Override
	public void update(Observable arg0, Object event) {

		System.out.println("Trigger Class: " + arg0.getClass());
		System.out.println(" event= " + event.toString());

		if (event instanceof NetworkVehicle) {
			NetworkVehicle e = new NetworkVehicle((NetworkVehicle) event);
			networkVehicles.addVehicle(e);

			for (NetWriter w : clients) {
				if (w.isConnected()) {
					w.sendThisMsgOnQueue(e);
					System.out.print(" send new animal to " + w.getIP());
				} else {
					clients.remove(w);
					System.err.print(" remove Client at " + w.getIP());
				}
			}
		}

		if (event instanceof NetworkCommand) {
			NetworkCommand e = new NetworkCommand(
					(NetworkCommand) event);

			switch (e.getComm()) {
			case Add_Vehicle:
				break;
			case Change_Movement:
				break;
			case No_Command:
				break;
			case Remove_Vehicle:
				break;
			case Reset:
				for (NetWriter w : clients) {
					w.sendThisMsgOnQueue(e);
					System.out.print("send reset to " + w.getIP());
				}
				break;
			default:
				break;
			}
		}
	}
	
	/**
	 * Updates the Object DB with the new master list data
	 */
	public void close() {
		db.update(networkVehicles.getAllNetworkVehicles());
		more = false;
	}

}
