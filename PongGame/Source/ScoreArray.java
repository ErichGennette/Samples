package a11rallypong;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The ScoreArray class is an object that holds an ArrayList of score objects to be saved to a file
 * and read later in the HighScoresGUI.
 * @author Erich
 *
 */
public class ScoreArray implements Serializable {
	
	private static final long serialVersionUID = 2384446230138363715L;
	public ArrayList<score> scores = new ArrayList<score>();;
	
	/**
	 * The addScore method appends a score into a certain spot of the list depending on its value.
	 * This is done so a sort method does not need to be employed each time something is written as the
	 * method sorts on the fly.
	 * @param score an integer representing a score achieved in the game.
	 * @param name a name representing the name of the user that earned the score.
	 */
	public void addScore(int score, String name) {
		Boolean worst = false;
		int index = 0;
		for (score s : scores) {
			if (score > s.score) {
				index = scores.indexOf(s);
				worst = false;
				break;
			} else
				worst = true;
		}
		if (!worst)
			scores.add(index, new score(score, name));
		else
			scores.add(new score(score, name));
	}
}
