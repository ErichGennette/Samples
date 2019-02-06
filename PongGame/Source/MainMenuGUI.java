package a11rallypong;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.Color;
import javax.swing.border.LineBorder;
/**
 * The MainMenuGUI class creates a GUI that the user can decide what to do in the game.
 * Three buttons are presented in this GUI; Play Game, High Scores and Exit Game.
 * @author Erich
 *
 */
public class MainMenuGUI {

	private JFrame frame;
	
	private int rectwidth = 500;
	private int rectheight = 500;

	/**
	 * Launch the application.
	 * @param args null.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainMenuGUI window = new MainMenuGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainMenuGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.BLACK);
		frame.setResizable(false);
		Rectangle bounds = getScreenTotalArea(null);
		int x = bounds.x + (bounds.width - rectwidth) / 2;
        int y = bounds.y + (bounds.height - rectheight) / 2;
		frame.setBounds(x, y, 500, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblPong = DefaultComponentFactory.getInstance().createTitle("Pong!");
		lblPong.setForeground(Color.WHITE);
		lblPong.setFont(new Font("Tahoma", Font.PLAIN, 35));
		lblPong.setHorizontalAlignment(SwingConstants.CENTER);
		lblPong.setBounds(170, 43, 140, 48);
		frame.getContentPane().add(lblPong);
		
		JButton btnPlayPong = new JButton("Play Pong");
		btnPlayPong.setFont(new Font("Arial Black", Font.BOLD, 14));
		btnPlayPong.setBorder(new LineBorder(Color.LIGHT_GRAY, 5));
		btnPlayPong.setBackground(Color.WHITE);
		btnPlayPong.setToolTipText("Play the game");
		btnPlayPong.setBounds(170, 150, 140, 60);
		btnPlayPong.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					@SuppressWarnings("unused")
					GameFrame g = new GameFrame();
					frame.dispose();
			}
		});
		frame.getContentPane().add(btnPlayPong);
		
		JButton btnHighScores = new JButton("High Scores");
		btnHighScores.setBorder(new LineBorder(new Color(192, 192, 192), 5));
		btnHighScores.setBackground(Color.WHITE);
		btnHighScores.setFont(new Font("Arial Black", Font.BOLD, 14));
		btnHighScores.setToolTipText("View High Scores");
		btnHighScores.setBounds(170, 250, 140, 55);
		btnHighScores.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				HighScoresGUI.main(null);
				frame.dispose();
			}
		});
		frame.getContentPane().add(btnHighScores);
		
		JButton btnExitGame = new JButton("Exit Game");
		btnExitGame.setBorder(new LineBorder(Color.LIGHT_GRAY, 5));
		btnExitGame.setBackground(Color.WHITE);
		btnExitGame.setFont(new Font("Arial Black", Font.BOLD, 14));
		btnExitGame.setToolTipText("Exit to desktop");
		btnExitGame.setBounds(170, 350, 140, 55);
		btnExitGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
			}
		});
		frame.getContentPane().add(btnExitGame);
	}
	
	/**
	 * The getScreenTotalArea method finds the size of the users screen so that the game can be fullscreen
	 * on any screen size and the play area will adapt to this size.
	 * @param windowOrNull the window the method should find the size of.
	 * @return a Rectangle representing the dimesions of the screen.
	 */
	static public Rectangle getScreenTotalArea(Window windowOrNull) {
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
