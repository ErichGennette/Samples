package display2D;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.Timer;

/**
 * The GameFrame class is responsible for creating a Frame for the pong game to be rendered in.
 * @author Erich
 *
 */
public class GameFrame {

	// A timer to refresh the screen
	private Timer timer;
	private DrawHere d = new DrawHere();
	private JFrame frame;
	
	protected int screenH, screenW;
	protected int initDelay = 100, delay = 0;

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
		d.addKeyListener(d);
		d.addMouseListener(d);
		
		// Set up jFrame window for drawing
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
	        public void windowClosing(WindowEvent event) {
				d.close();
				frame.dispose();
				System.exit(0);
	        }
		});
		frame.setSize(screenW, screenH);
		frame.setVisible(true);
		frame.setContentPane(d);
		frame.setResizable(false);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.getRootPane().setBackground(Color.LIGHT_GRAY);
		
		// Set up Timer
		timer = new Timer(delay, d); // Set time, and this object gets event
		timer.setInitialDelay(initDelay); // Initial wait
		timer.setCoalesce(true); // Don't stack up events
		timer.start(); // Start the timer object
		
	}
}