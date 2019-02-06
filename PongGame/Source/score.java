package a11rallypong;

import java.io.Serializable;

/**
 * The score class is an object that holds three items. The name of the person who got the score,
 * the score itself, and a 'record' which is a combination of the two used to displaying purposes.
 * @author Erich
 *
 */
public class score implements Serializable{
	private static final long serialVersionUID = 7222724377054708107L;
	String name = "";
	int score = 0;
	String record = "";
	
	/**
	 * the score constructor takes in a score and a name to be saved in the object,
	 * it then creates the record from those two values.
	 * @param score an integer representing a score achieved in the game.
	 * @param name a string representing the user who earned the score.
	 */
	public score(int score, String name) {
		this.name = name;
		this.score = score;
		this.record = this.name + " : " + this.score;
	}
	
	/**
	 * getScore is a getter that returns the value of the score saved in the object.
	 * @return an integer representing the score saved in this object.
	 */
	public int getScore() {
		return this.score;
	}
	
	/**
	 * setScore takes a value to set the score inside the object to a different value.
	 * @param s a score to replace the previously saved score.
	 */
	public void setScore(int s) {
		this.score = s;
	}
}
