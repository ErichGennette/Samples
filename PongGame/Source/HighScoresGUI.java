package a11rallypong;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JLabel;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * The HighScoresGUI class creates a GUI that displays the top ten high scores achieved in the game.
 * It is accessed via the main menu and has a button to bring the user back to the main menu.
 * @author Erich
 *
 */
public class HighScoresGUI {

	private JFrame frame;
	private FileRW fo;
	private ScoreArray sa;
	private ArrayList<score> scores;
	
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
					HighScoresGUI window = new HighScoresGUI();
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
	public HighScoresGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		fo = new FileRW();
		sa = new ScoreArray();
		sa = fo.readFromFile();
		scores = new ArrayList<score>();
		scores = sa.scores;
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.BLACK);
		frame.getContentPane().setLayout(null);
		
		System.out.println("");
		
		JButton btnBack = new JButton("Main Menu");
		btnBack.setFont(new Font("Arial Black", Font.BOLD, 16));
		btnBack.setToolTipText("Back to Main Menu");
		btnBack.setEnabled(true);
		btnBack.setForeground(Color.WHITE);
		btnBack.setBackground(Color.BLACK);
		btnBack.setBounds(200, 391, 100, 29);
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MainMenuGUI.main(null);
				frame.dispose();
			}
		});
		frame.getContentPane().add(btnBack);
		
		JLabel lblTopHigh = DefaultComponentFactory.getInstance().createTitle("Top 10 High Scores");
		lblTopHigh.setFont(new Font("Arial Black", Font.BOLD, 16));
		lblTopHigh.setHorizontalAlignment(SwingConstants.CENTER);
		lblTopHigh.setForeground(Color.WHITE);
		lblTopHigh.setBounds(150, 25, 200, 61);
		frame.getContentPane().add(lblTopHigh);
		
		JLabel lbl1 = new JLabel(getAScore(scores, 1));
		lbl1.setHorizontalAlignment(SwingConstants.CENTER);
		lbl1.setForeground(Color.WHITE);
		lbl1.setBounds(200, 97, 100, 14);
		frame.getContentPane().add(lbl1);
		
		JLabel lbl2 = new JLabel(getAScore(scores, 2));
		lbl2.setHorizontalAlignment(SwingConstants.CENTER);
		lbl2.setForeground(Color.WHITE);
		lbl2.setBounds(200, 122, 100, 14);
		frame.getContentPane().add(lbl2);
		
		JLabel lbl3 = new JLabel(getAScore(scores, 3));
		lbl3.setHorizontalAlignment(SwingConstants.CENTER);
		lbl3.setForeground(Color.WHITE);
		lbl3.setBounds(200, 147, 100, 14);
		frame.getContentPane().add(lbl3);	
		
		JLabel lbl4 = new JLabel(getAScore(scores, 4));
		lbl4.setHorizontalAlignment(SwingConstants.CENTER);
		lbl4.setForeground(Color.WHITE);
		lbl4.setBounds(200, 172, 100, 14);
		frame.getContentPane().add(lbl4);
		
		JLabel lbl5 = new JLabel(getAScore(scores, 5));
		lbl5.setHorizontalAlignment(SwingConstants.CENTER);
		lbl5.setForeground(Color.WHITE);
		lbl5.setBounds(200, 197, 100, 14);
		frame.getContentPane().add(lbl5);
		
		JLabel lbl6 = new JLabel(getAScore(scores, 6));
		lbl6.setHorizontalAlignment(SwingConstants.CENTER);
		lbl6.setForeground(Color.WHITE);
		lbl6.setBounds(200, 222, 100, 14);
		frame.getContentPane().add(lbl6);
		
		JLabel lbl7 = new JLabel(getAScore(scores, 7));
		lbl7.setHorizontalAlignment(SwingConstants.CENTER);
		lbl7.setForeground(Color.WHITE);
		lbl7.setBounds(200, 247, 100, 14);
		frame.getContentPane().add(lbl7);
		
		JLabel lbl8 = new JLabel(getAScore(scores, 8));
		lbl8.setHorizontalAlignment(SwingConstants.CENTER);
		lbl8.setForeground(Color.WHITE);
		lbl8.setBounds(200, 272, 100, 14);
		frame.getContentPane().add(lbl8);
		
		JLabel lbl9 = new JLabel(getAScore(scores, 9));
		lbl9.setHorizontalAlignment(SwingConstants.CENTER);
		lbl9.setForeground(Color.WHITE);
		lbl9.setBounds(200, 322, 100, 14);
		frame.getContentPane().add(lbl9);
		
		JLabel lbl10 = new JLabel(getAScore(scores, 10));
		lbl10.setHorizontalAlignment(SwingConstants.CENTER);
		lbl10.setForeground(Color.WHITE);
		lbl10.setBounds(200, 297, 100, 14);
		frame.getContentPane().add(lbl10);
		
		frame.setBackground(Color.WHITE);
		frame.setResizable(false);
		Rectangle bounds = getScreenTotalArea(null);
		int x = bounds.x + (bounds.width - rectwidth) / 2;
        int y = bounds.y + (bounds.height - rectheight) / 2;
		frame.setBounds(x, y, 500, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/**
	 * The getAScore method pulls a score object out of the list of scores according to the location specified
	 * @param sa A given ArrayList of scores to pull an object from.
	 * @param Location The position of the desired score in the array.
	 * @return the record from the score object that was selected.
	 */
	public String getAScore(ArrayList<score> sa, int Location) {
		String score = null;
		try {
			score scoreObject = sa.get(Location - 1);
			score = scoreObject.record;
		} catch (Exception e) {
			score = "No Score Saved";
		}
		return score;
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
