package common;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import client.DrawHere;
import server.Server;

public class Main {
	
	// A timer to refresh the screen
		private static Timer timer;
		private static DrawHere d;
		private static JFrame frame;
		
		protected static int screenH, screenW;
		protected static int initDelay = 100, delay = 0;
	
	public static void main(String[] args) {
		
		// Set up if we are server or client
		String Address = null;
		try {
			Address = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		// Ask user if we are server or client
		String s = (String) JOptionPane.showInputDialog(frame, "Server IP Address (null means this is server):\n", "Choose Server", JOptionPane.PLAIN_MESSAGE, null, null, Address);  // default to java server

		if ((s != null) && (s.length() > 0)) {
			// We are Client, String is server IP
			System.out.println("We ARE the >>>>>Client<<<<< => ");
			System.out.println("Server at => " + s);
			
			d = new DrawHere(s);

			Rectangle screen = d.getScreenBounds();
			screenH = screen.height;
			screenW = screen.width;
			d.addKeyListener(d);
			d.addMouseListener(d);
					
			// Set up jFrame window for drawing
			frame = new JFrame("Vehicles");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(screenW, screenH);
			frame.setVisible(true);
			frame.setContentPane(d);
			frame.setResizable(true);
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			frame.getRootPane().setBackground(Color.LIGHT_GRAY);
			
			// Set up Timer
			timer = new Timer(delay, d); // Set time, and this object gets event
			timer.setInitialDelay(initDelay); // Initial wait
			timer.setCoalesce(true); // Don't stack up events
			timer.start(); // Start the timer object
		} else {
			// Blank string....we ARE the server!
			System.out.println("We ARE the >>>>>Server<<<<< => ");
			System.out.println("No Window frames, just Console Output");
			Server server = new Server();
			server.start();
			String input = "";
			
			System.out.println("Enter 'Quit' when ready to quit");
			
			while(!input.equals("Quit")) {
				 BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); 
				 try {
					 input = reader.readLine(); 
				 } catch (Exception e) {
					 e.printStackTrace();
				 }
			}
			
			server.close();
			System.exit(0);
		}
	}
}
