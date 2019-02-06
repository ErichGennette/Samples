package a11rallypong;

import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import com.jgoodies.forms.factories.DefaultComponentFactory;

/**
 * The GameOverGUI class creates a GUI for the user to submit their score to the save file of scores
 * or quit the game without saving it. If they submit their score, they are immediately brought to the
 * HighScoresGUI to view it. If they don't save it, they are brought to the main menu.
 * @author Erich
 *
 */
public class GameOverGUI {

	protected JFrame frame;
	private JTextField textField;
	private FileRW fo;
	private ScoreArray sa;
	
	private int score;
	private int rectwidth = 500;
	private int rectheight = 500;

	/**
	 * Create the application.
	 */
	public GameOverGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		fo = new FileRW();
		sa = new ScoreArray();
		frame.getContentPane().setBackground(Color.BLACK);
		frame.getContentPane().setLayout(null);
		
		JButton btnQuit = new JButton("Quit");
		btnQuit.setBackground(Color.LIGHT_GRAY);
		btnQuit.setForeground(Color.BLACK);
		btnQuit.setFont(new Font("Arial Black", Font.BOLD, 16));
		btnQuit.setBounds(175, 290, 150, 55);
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MainMenuGUI.main(null);
				frame.dispose();
			}
		});
		frame.getContentPane().add(btnQuit);
		
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.setFont(new Font("Arial Black", Font.BOLD, 16));
		btnSubmit.setBounds(175, 203, 150, 55);
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sa = fo.readFromFile();
				sa.addScore(score, textField.getText());
				System.out.println("Score: " + score);
				fo.writeToFile(sa);
				HighScoresGUI.main(null);
				frame.dispose();
			}
		});
		frame.getContentPane().add(btnSubmit);
		Rectangle bounds = getScreenTotalArea(null);
		int x = bounds.x + (bounds.width - rectwidth) / 2;
        int y = bounds.y + (bounds.height - rectheight) / 2;
		frame.setBounds(x, y, 500, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		textField = new JTextField();
		textField.setToolTipText("Enter a name for your high score");
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		textField.setFont(new Font("Arial Black", Font.BOLD, 12));
		textField.setBounds(175, 137, 150, 34);
		frame.getContentPane().add(textField);
		textField.setColumns(3);
		
		JLabel lblGameOver = DefaultComponentFactory.getInstance().createTitle("Game Over");
		lblGameOver.setFont(new Font("Arial Black", Font.BOLD, 30));
		lblGameOver.setHorizontalAlignment(SwingConstants.CENTER);
		lblGameOver.setForeground(Color.WHITE);
		lblGameOver.setBounds(125, 43, 250, 42);
		frame.getContentPane().add(lblGameOver);
		
		JLabel lblSaveYourHigh = DefaultComponentFactory.getInstance().createLabel("Save your high score with a name!");
		lblSaveYourHigh.setHorizontalAlignment(SwingConstants.CENTER);
		lblSaveYourHigh.setForeground(Color.WHITE);
		lblSaveYourHigh.setBounds(135, 112, 225, 14);
		frame.getContentPane().add(lblSaveYourHigh);
	}
	
	/**
	 * The getScreenTotalArea method finds the size of the users screen so that the game can be full screen
	 * on any screen size and the play area will adapt to this size.
	 * @param windowOrNull the window the method should find the size of.
	 * @return a Rectangle representing the dimensions of the screen.
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

	/**
	 * The setCurrentScore method takes an integer and sets the current score of the game
	 * for this GUI to be saved to the file.
	 * @param hitcount a count of hits representing a score.
	 */
	public void setCurrentScore(int hitcount) {
		this.score = hitcount;
		
	}
}
