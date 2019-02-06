package client;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

//Imports are listed in full to show what's being used
//could just import javax.swing.* and java.awt.* etc..

import javax.swing.JPanel;

import common.NetReader;
import common.NetWriter;
import common.NetworkCommand;
import common.NetworkVehicle;
import common.Type;

/**
 * The DrawHere class takes care of initializing and running all of the threads in the app.
 * It is also where the objects are drawn onto the buffer.
 * @author Erich
 */
@SuppressWarnings("serial")
public class DrawHere extends JPanel implements ActionListener, KeyListener, MouseListener, Observer {
	
	private String server;
	private InetAddress address;
	private Socket sock;
	private static final int TIMEOUT = 0;
	
	String myIP;
	NetWriter sender;

	protected int screenH, screenW;
	private ArrayList<Vehicle> allVehicles;
	
	private Random rand;
	
	/**
	 * The constructor for DrawHere initializes the vehicles array and its NetWriter and NetReader to get vehicles from the network.
	 * @param server The server that the client must connect to.
	 */
	public DrawHere(String server) {
		allVehicles = new ArrayList<Vehicle>();
		this.server = server;
		rand = new Random();
		
		try {
			address = (InetAddress.getByName(this.server));
			sock = new Socket(address, NetworkVehicle.serverport);
			sock.setSoTimeout(TIMEOUT);
			myIP = sock.getLocalAddress().toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		sender = new NetWriter(sock);
		sender.start();
		
		NetReader getter = new NetReader(sock);
		(new Thread(getter)).start();
		
		getter.addObserver(this);
		
		Rectangle screenBounds = getScreenTotalArea(null);
		screenH = (int) screenBounds.height;
		screenW = (int) screenBounds.width;

		this.setFocusable(true);
		this.requestFocusInWindow();

	}

	/**
	 * The paintComponent method is called by using "this.repaint();", 
	 * it is part of the JPanel library. This method is in charge of painting 
	 * objects on the JFrame as they get refreshed.
	 */
	public void paintComponent(Graphics g) {
		
		// Setup and clear the buffer
		BufferedImage bufferedImage = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_BGR);
		Graphics2D g2d = bufferedImage.createGraphics();
		
		g2d.setColor(getBackground());
		g2d.fillRect(0, 0, this.getWidth(), this.getHeight());

		// Draw on the buffer
		for(Vehicle v : allVehicles)
			v.paint(g2d);
		
		// Set the buffer to be visible
		Graphics2D g2dComponent = (Graphics2D) g;
		g2dComponent.drawImage(bufferedImage, null, 0, 0);
	}

	/**
	 * This method is called when the timer fires The Timer sets off an
	 * "actionPerformed" event every so many milliseconds.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		this.repaint();
		doCollsionChecks();
		// Take focus if we don't have it
		if (!this.isFocusOwner()) {
			this.requestFocusInWindow();
		}
	}
	
	/**
	 * Responsible for running through the ArrayList of vehicles and calling checkCollision on them
	 * to see if they are colliding with each other. Called every tick of the timer in 'actionPerformed'.
	 */
	private void doCollsionChecks() {
		for(Vehicle first : allVehicles) {
			for(Vehicle second : allVehicles) {
				if(first.checkCollision(second)) {
					first.collide();
					second.collide();
				}
			}
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		 int key = e.getKeyCode();
		 
		 if(key == KeyEvent.VK_R)
			 addAirVehicle();
		 
		 if(key == KeyEvent.VK_F)
			 addLandVehicle();
		 
		 if(key == KeyEvent.VK_SPACE)
			 removeAllVehicles();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int button = e.getButton();
		
		if(button == MouseEvent.BUTTON1)
			addRandomVehicle();
		
		if(button == MouseEvent.BUTTON3)
			removeRandomVehicle();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	private void addRandomVehicle() {
		int selector = rand.nextInt(2) + 1;
		
		switch (selector) {
		case 1:
			addAirVehicle();
			break;
		case 2:
			addLandVehicle();
			break;
		}
	}
	
	/**
	 * Removes a random vehicle from the ArrayList by choosing a random number between 0 and the current size of 
	 * the array of vehicles and then removing the vehicle at that index.
	 */
	private void removeRandomVehicle() {
		int size = allVehicles.size();
		
		if(size != 0)
			allVehicles.remove(rand.nextInt(size));
	}
	
	/**
	 * Converts an Air Vehicle to a Network Vehicle and sends it over the network so the server can distribute the data to all clients.
	 */
	private void addAirVehicle() {
		AirVehicle n = new AirVehicle("AirVehicle");
		NetworkVehicle newNetV = new NetworkVehicle(Type.Air, n.model, n.getX(), n.getY(), n.getZ(), n.getSpeed(), n.getAccel(), n.getDir());
		sender.sendThisMsgOnQueue(newNetV);
	}
	
	/**
	 * Converts an Land Vehicle to a Network Vehicle and sends it over the network so the server can distribute the data to all clients.
	 */
	private void addLandVehicle() {
		LandVehicle n = new LandVehicle("LandVehicle");
		NetworkVehicle newNetV = new NetworkVehicle(Type.Land, n.model, n.getX(), n.getY(), n.getZ(), n.getSpeed(), n.getAccel(), n.getDir());
		sender.sendThisMsgOnQueue(newNetV);
	}
	
	/**
	 * Kills all the vehicle threads and then calls '.clear()' on the ArrayList of vehicles.
	 */
	private void removeAllVehicles() {
		for(Vehicle v : allVehicles) {
			v.kill();
		}
		allVehicles.clear();
	}
	
	public ArrayList<Vehicle> getVehicles() {
		return allVehicles;
	}
	
	public void setVehicles(ArrayList<Vehicle> vehicles) {
		allVehicles = vehicles;
	}
	
	public Rectangle getScreenBounds() {
		Rectangle screen = new Rectangle(screenW, screenH);
		return screen;
	}
	
	/**
	 * The getScreenTotalArea method finds the size of the users screen so that the game can be fullscreen
	 * on any screen size and the play area will adapt to this size.
	 * @param windowOrNull the window the method should find the size of.
	 * @return a Rectangle representing the dimesions of the screen.
	 */
	private Rectangle getScreenTotalArea(Window windowOrNull) {
	    Rectangle bounds;
	    if (windowOrNull == null) {
	        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	        bounds = ge.getDefaultScreenDevice().getDefaultConfiguration().getBounds();
	    } else {
	        GraphicsConfiguration gc = windowOrNull.getGraphicsConfiguration();
	        bounds = gc.getBounds();
	    }
	    return bounds;
	}

	@Override
	public void update(Observable arg0, Object event) {
		System.out.println("Trigger Class: " + arg0.getClass());
		System.out.println(" event= " + event.toString());

		if (event instanceof NetworkVehicle) {
			NetworkVehicle e = (NetworkVehicle) event;
			Vehicle newVehicle;
			if(e.getType() == Type.Air)
				newVehicle = new AirVehicle(e.model, e.getX(), e.getY(), e.getZ(), e.getSpeed(), e.getAccel(), e.getDir());
			else
				newVehicle = new LandVehicle(e.model, e.getX(), e.getY(), e.getZ(), e.getSpeed(), e.getAccel(), e.getDir());
			newVehicle.start();
			this.addKeyListener(newVehicle);
			synchronized (this) {
				allVehicles.add(newVehicle); // add to arraylist
			}
		}

		if (event instanceof NetworkCommand) {
			NetworkCommand e = (NetworkCommand) event;

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
				this.removeKeyListener(allVehicles.get(0)); // remove key listener
				// clear all animals
				for (Vehicle v : allVehicles) {
					v.kill(); // force threads to stop
				}
				synchronized (this) {
					allVehicles.clear(); // clear the arraylist
				}
				break;
			default:
				break;
			}
		}

		// Set up key-pressed events
		if ((allVehicles != null) && (allVehicles.size() > 0)) {
			this.addKeyListener(allVehicles.get(0)); // arrow keys work on first
		}

		this.setFocusable(true);
		this.requestFocusInWindow();
	}
	
	/**
	 * Used to set all of the vehicles boundaries to the current screen size.
	 */
	public void updateScreenSize() {
		Rectangle screenBounds = getScreenTotalArea(null);
		screenH = (int) screenBounds.height;
		screenW = (int) screenBounds.width;
		for(Vehicle v: allVehicles) {
			v.setScreenBounds(screenBounds);
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
