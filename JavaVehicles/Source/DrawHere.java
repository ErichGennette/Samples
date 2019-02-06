package display2D;

import java.awt.Color;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//Imports are listed in full to show what's being used
//could just import javax.swing.* and java.awt.* etc..

import javax.swing.JPanel;

/**
 * The DrawHere class takes care of initializing and running all of the threads in the app.
 * It is also where the objects are drawn onto the buffer.
 * @author Erich
 */
@SuppressWarnings("serial")
public class DrawHere extends JPanel implements ActionListener, KeyListener, MouseListener {
	
	private int modNum = 0;
	private Vehicle currentVehicleInControl = null;
	private Vehicle previousVehicleInControl = null;

	protected int screenH, screenW;
	private ArrayList<Vehicle> allVehicles;
	
	private Random rand;
	private Database db;
	
	/**
	 * The DrawHere contructor initializes all of the objects that will be
	 * bouncing around on the JFrame as well as the background for the scene.
	 */
	public DrawHere() {
		rand = new Random();
		
		db = new Database("db_file.odb");
		
		allVehicles = new ArrayList<Vehicle>();
		
		getdbVehicles();
		
		if(!allVehicles.isEmpty()) {
			String lastModel = allVehicles.get(allVehicles.size() - 1).model;
			String modNumString = lastModel.substring(lastModel.length() - 1, lastModel.length());
			if(modNumString.startsWith(" "))
				modNum = Integer.parseInt(modNumString.substring(2)) + 1;
			else
				modNum = Integer.parseInt(modNumString) + 1;
		}
		
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
	
	private void doCollsionChecks() {
		for(Vehicle first : allVehicles) {
			for(Vehicle second : allVehicles) {
				if(first.checkCollision(second)) {
					if(first instanceof AirVehicle) {
						first.crash(second);
						this.removeKeyListener(first);
					} else
						first.collide();
					
					if(second instanceof AirVehicle) {
						second.crash(first);
						this.removeKeyListener(second);
					} else
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
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_TAB)
			 controlNextVehicle();
		 
		 if(key == KeyEvent.VK_BACK_SPACE)
			 controlPreviousVehicle();
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
	
	private void removeRandomVehicle() {
		int size = allVehicles.size();
		
		if(size != 0)
			allVehicles.remove(rand.nextInt(size));
	}
	
	private void addAirVehicle() {
		AirVehicle newAirV = new AirVehicle("Model " + modNum++);
		allVehicles.add(newAirV);
		newAirV.start();
		this.addKeyListener(newAirV);
	}
	
	private void addLandVehicle() {
		LandVehicle newLandV = new LandVehicle("Model " + modNum++);
		allVehicles.add(newLandV);
		newLandV.start();
		this.addKeyListener(newLandV);
	}
	
	private void removeAllVehicles() {
		for(Vehicle v : allVehicles) {
			v.kill();
		}
		allVehicles.clear();
		modNum = 0;
	}
	
	private void controlNextVehicle() {
		if(currentVehicleInControl != null) {
			previousVehicleInControl = currentVehicleInControl;
			if((allVehicles.indexOf(currentVehicleInControl)) == allVehicles.size() - 1)
				currentVehicleInControl = allVehicles.get(0);
			else
				currentVehicleInControl = allVehicles.get(allVehicles.indexOf(currentVehicleInControl) + 1);
		} else
			currentVehicleInControl = allVehicles.get(0);
		this.removeKeyListener(previousVehicleInControl);
		this.addKeyListener(currentVehicleInControl);
	}
	
	private void controlPreviousVehicle() {
		if(currentVehicleInControl != null) {
			previousVehicleInControl = currentVehicleInControl;
			if(allVehicles.indexOf(currentVehicleInControl) == 0)
				currentVehicleInControl = allVehicles.get(allVehicles.size() - 1);
			else
				currentVehicleInControl = allVehicles.get(allVehicles.indexOf(currentVehicleInControl) - 1);
		} else
			currentVehicleInControl = allVehicles.get(0);
		this.removeKeyListener(previousVehicleInControl);
		this.addKeyListener(currentVehicleInControl);
	}
	
	private void getdbVehicles() {
		List<Object> rawData = db.getObjects(DBVehicle.class);
		for(Object o : rawData) {
			DBVehicle vehicle = (DBVehicle) o;
			if(vehicle.type.equals("Air"))
				allVehicles.add(new AirVehicle(vehicle.model, vehicle.x, vehicle.y, vehicle.z, vehicle.speed, vehicle.acceleration, vehicle.direction));
			else if (vehicle.type.equals("Land"))
				allVehicles.add(new LandVehicle(vehicle.model, vehicle.x, vehicle.y, vehicle.z, vehicle.speed, vehicle.acceleration, vehicle.direction));
		}
		
		System.out.println("Size of array: " + allVehicles.size());
		
		for(Vehicle v : allVehicles) {
			v.start();
			this.addKeyListener(v);
		}
	}
	
	public ArrayList<Vehicle> getVehicles() {
		return allVehicles;
	}
	
	public void setVehicles(ArrayList<Vehicle> vehicles) {
		allVehicles = vehicles;
	}
	
	protected void close() {
		for(Vehicle v : allVehicles) {
			Object saveable = null;
			if(v instanceof AirVehicle)
				saveable = new DBVehicle("Air", v.model, v.x, v.y, v.z, v.speed, v.acceleration, v.direction);
			else if (v instanceof LandVehicle)
				saveable = new DBVehicle("Land", v.model, v.x, v.y, v.z, v.speed, v.acceleration, v.direction);
			if(saveable != null)
				db.saveNew(saveable);
			else
				System.out.println("Vehicle could not save");
		}
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
}
