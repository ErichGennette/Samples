package a11rallypong;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.Timer;

/**
 * The GameFrame class is responsible for creating a Frame for the pong game to be rendered in.
 * @author Erich
 *
 */
public class GameFrame {

	// A timer to refresh the screen
	static Timer timer;
	static DrawHere d = new DrawHere();
	static JFrame frame;
	
	protected static int screenH, screenW;
	protected static int initDelay = 100, delay = 0;

	/**
	 * Create the Frame.
	 */
	public GameFrame() {
		initialize();
	}
	
	/**
	 * Initialize contents of the frame.
	 */
	public void initialize() {
		screenH = d.screenH;
		screenW = d.screenW;
		System.out.println("JFrame Starting..");
		
		// Set up jFrame window for drawing
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(screenW, screenH);
		frame.setVisible(true);
		frame.setContentPane(d);
		frame.setResizable(false);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.getRootPane().setBackground(Color.BLACK);
		
		// Set up Timer
		timer = new Timer(delay, d); // Set time, and this object gets event
		timer.setInitialDelay(initDelay); // Initial wait
		timer.setCoalesce(true); // Don't stack up events
		timer.start(); // Start the timer object
		
		System.out.println("JFrame Started..");
		
	}

}