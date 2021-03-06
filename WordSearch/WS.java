import java.util.*;
import java.io.*;

/**
 * @(#)WordSearch.java This program lets you search for a particular word within
 *                     a file (sowpods.txt)
 *
 * @author Catalan, Paul
 * @version 1.00 2015/8/28
 */

public class WS {
	// class variables
	static Scanner kbd = new Scanner(System.in);

	// transfer the contents of (text) file to a (String) array
	public static String[] readFile(String filename) {
		int size = 0;

		// count the number of lines in the file
		try {
			Scanner source = new Scanner(new File(filename));

			while (source.hasNextLine()) {
				source.nextLine(); // ignore string input
				size++;
			}
			source.close();
		} catch (IOException e) {
			System.out.println("File not found! Quitting.");
			System.exit(1); // error code: 1 (abnormal termination)
		}

		String[] tempList = new String[size];

		// transfer the contents to an array
		try {
			Scanner source = new Scanner(new File(filename));

			for (int i = 0; i < tempList.length; i++) {
				tempList[i] = source.nextLine();
			}

			source.close();

		} catch (IOException e) {
			System.out.println("File not found! Quitting.");
			System.exit(1); // error code: 1 (abnormal termination)
		}

		return tempList;
	}

	// method to get the word from the user
	public static String getWord() {
		System.out.print("Please enter a word: ");
		return kbd.nextLine();
	}

	public static void run() {
		// declare necessary variables
		boolean found = false;
		int lineOfWord = 0;
		String[] wordList = readFile("sowpods.txt");
		String word = "";

		// ask the user to enter a word
		word = getWord();

		int searchStart = 0;

		// determine which line the search will start
		if (word.length() >= 3) {
			for (int i = 0; i < wordList.length; i++) {
				if ((word.substring(0, 1).compareToIgnoreCase(wordList[i].substring(0, 1)) == 0)) {
					searchStart = i;
					if (word.substring(1, 2).compareToIgnoreCase(wordList[i].substring(1, 2)) == 0) {
						searchStart = i;
						if (word.substring(2, 3).compareToIgnoreCase(wordList[i].substring(2, 3)) == 0) {
							searchStart = i;
							break;
						} // 3rd if
					} // 2nd if
				} // 1st if
			}
		} else if (word.length() == 2) {
			for (int i = 0; i < wordList.length; i++) {
				if ((word.substring(0, 1).compareToIgnoreCase(wordList[i].substring(0, 1)) == 0)) {
					searchStart = i;
					if (word.substring(1, 2).compareToIgnoreCase(wordList[i].substring(1, 2)) == 0) {
						searchStart = i;
						break;
					} // 2nd if
					break;
				} // 1st if
			}
		} else if (word.length() == 1) {
			for (int i = 0; i < wordList.length; i++) {
				if ((word.substring(0, 1).compareToIgnoreCase(wordList[i].substring(0, 1)) == 0)) {
					searchStart = i;
					break;
				} // 1st if
			}
		}

		// currenttime before word comparison started
		double start = System.nanoTime();

		// look if the given word will match with any of the index of words
		for (int j = searchStart; j < wordList.length; j++) {
			if (wordList[j].length() == word.length() && wordList[j].equalsIgnoreCase(word)) {
				found = true;
				lineOfWord = j;
				break; // terminate loop since word is already found
			}
		}
		// current time after word comparison is finished
		double stop = System.nanoTime();

		// show whether word is found or not
		if (!found) {
			System.out.println("	> Word is not found!");
		} else {
			System.out.println(
					"	 > Word is found! (line " + lineOfWord + "); (" + (int) (stop - start) + " Nanoseconds)");
		}
	}

	/**
	 * main method
	 */
	public static void main(String[] args) {
		try {
			WS.run();
		} catch (Exception j) {
			j.printStackTrace();
			System.exit(1);
		} finally {
			System.exit(0);
		}

	}
}
