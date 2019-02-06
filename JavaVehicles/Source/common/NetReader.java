package common;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Observable;

/**
 * Responsible for notifying Observers that an object was written to the socket given in the constructor.
 * @author Erich
 *
 */
public class NetReader extends Observable implements Runnable {

	private Socket sock;
	ObjectInputStream in;
	private boolean connected = false;

	/**
	 * Sets up the Socket to the given socket
	 * @param sock A Socket given to connect to in the run method.
	 */
	public NetReader(Socket sock) {
		this.sock = sock;
		this.connected = true;
	}

	/**
	 * To be called when started as a thread later.
	 * The method reads the socket and notifies Observers when something is written to the socket.
	 */
	public void run() {
		System.out.println("GetPacket " + sock.getInetAddress() + ":" + sock.getPort());
		while (connected) {
			try {
				if (in == null)
					in = new ObjectInputStream(sock.getInputStream());
				Object obj;
				try {
					obj = (Object) (in.readObject());
					this.setChanged();
					this.notifyObservers(obj);
				} catch (Exception e) {
					System.out.println(" Disconnected at other end ");
					connected = false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
