package common;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
/**
 * The NetWriter is responsible for writing 
 * @author Erich
 *
 */

public class NetWriter extends Thread {

	private Socket sock;
	ObjectOutputStream out;
	private boolean connected = true;

	Integer q_size = 90;
	Object[] q = new Object[q_size];
	Integer q_adding = 0;
	Integer q_sending = 0;

	/**
	 * The NetWriter is initialized with a socket to write objects to.
	 * @param socket A socket given in the constructor
	 */
	public NetWriter(Socket socket) {
		this.sock = socket;
		this.connected = true;

		try {
			out = new ObjectOutputStream(sock.getOutputStream());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public InetAddress getIP() {
		return sock.getInetAddress();
	}

	public boolean isConnected() {
		return this.connected;
	}

	/**
	 * This method adds the object to send to the queue and notifies all waiting threads.
	 * @param objectToSend The object to be sent over the network.
	 */
	public synchronized void sendThisMsgOnQueue(Object objectToSend) {
		try {
			q[q_adding] = (objectToSend);
			q_adding = (q_adding + 1) % q_size;
			System.out.println("Send Queue => " + q_adding + "::" + q_sending);
			this.notifyAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Calls this.wait until q_adding and q_sending are different (meaning something was added to the queue).
	 */
	private synchronized void waitToSend() {
		while (q_adding == q_sending) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * The run method to be called when the thread is started.
	 */
	public void run() {
		System.out.println("ClientWrite setup " + sock.getInetAddress() + ":" + sock.getPort() + ":" + sock.getLocalPort());
		
		while (connected) {
			waitToSend();
			while ((connected) && (q_adding != q_sending)) {
				Object objectToSend = q[q_sending];
				try {
					out.writeObject(objectToSend);
					out.flush();
					q_sending = (q_sending + 1) % q_size;
					System.out.println("Sent=> " + objectToSend.toString());
				} catch (java.net.SocketException e) {
					connected = false;
					System.err.println("ClientWrite disconnect " + sock.getInetAddress());
				} catch (Exception e) {
					connected = false;
					e.printStackTrace();
				}
			}
		}
	}
}
