package a11rallypong;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * The FileRW class is responsible for reading and writing data to files.
 * @author Erich
 *
 */
public class FileRW {
	
	BufferedReader z = new BufferedReader(new InputStreamReader(System.in));

	/**
	 * The writeToFile method writes a ScoreArray object to a .ser file to be read and altered later.
	 * @param n the ScoreArray object to be written to the file.
	 */
	public void writeToFile(ScoreArray n) {
		FileOutputStream fout;
		try {
			fout = new FileOutputStream("HighScores.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(n);
			oos.flush();
			oos.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * The readFromFile method reads a ScoreArray object from a .ser file to be altered
	 * @return A ScoreArray object
	 */
	public ScoreArray readFromFile() {
		ObjectInputStream objectinputstream = null;
		// Instantiate Serializable Object for writing to disk
		ScoreArray n = new ScoreArray();
		try {
			FileInputStream streamIn = new FileInputStream("HighScores.ser");
			objectinputstream = new ObjectInputStream(streamIn);
			n = (ScoreArray) objectinputstream.readObject();
			objectinputstream.close();
		} catch (Exception e) {
			System.out.println("The High Scores file was not found... New 'HighScores.ser' file will be created.");
		} finally {
			if (objectinputstream != null) {
				try {
					objectinputstream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return n;
	}
}
