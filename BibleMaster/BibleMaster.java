import java.io.*;
import java.util.*;

public class BibleMaster {
	public static String[] verseList = TextIO.readFile("pentateuch.txt");
	public static Scanner kbd = new Scanner(System.in);
	public static String[] remove = { ".", "?", "!", ",", "[", "]", "(", ")", ":", ";", "\"", "'s", "'", "-" };

	public static void showPressEnter() {
		System.out.println("Press enter to continue..");
		kbd.nextLine();
	}

	// 1
	public static void printBookOf(String book) {
		for (int j = 0; j < verseList.length; j++) {
			String[] data = verseList[j].split("\t");
			if (book.equalsIgnoreCase(data[0])) {
				System.out.println(book + "\t" + data[1] + "\t" + data[2] + "\t" + data[3]);
			}
		}
	}

	// 2
	public static boolean checkExistenceOfChapterAndVerseInBook(String book, String chapter, String verseNumber) {
		boolean chapterFound = false;
		boolean verseFound = false;
		boolean found = false;

		for (int j = 0; j < verseList.length; j++) {
			String[] data = verseList[j].split("\t");
			if (book.equalsIgnoreCase(data[0])) {
				if (chapter.equals(data[1])) {
					chapterFound = true;
					if (verseNumber.equals(data[2])) {
						verseFound = true;
						break;
					}
				}
			}
		}

		if (!chapterFound && !verseFound) {
			System.out.println("\t [!] Chapter and Verse Number does not exist!");
		} else if (chapterFound && !verseFound) {
			System.out.println("\t [!] Chapter is existent but Verse Number do not exist!");
		} else if (chapterFound && verseFound) {
			System.out.println("\t > Chapter and Verse is existent!");
			found = true;
		}

		return found;
	}

	// 3
	public static boolean checkExistenceOfTextInBook(String book, String chapter, String verseNumber,
			String inputText) {
		boolean existentChapterAndVerseNum = checkExistenceOfChapterAndVerseInBook(book, chapter, verseNumber);
		boolean wordFound = false;
		int counter = 0;
		if (existentChapterAndVerseNum) {
			for (int k = 0; k < verseList.length; k++) {
				String[] data = verseList[k].split("\t");
				if (book.equalsIgnoreCase(data[0]) && chapter.equalsIgnoreCase(data[1])
						&& verseNumber.equalsIgnoreCase(data[2])) {

					String d = data[3];
					// this code removes the symbols and characters
					for (int r = 0; r < remove.length; r++) {
						d = d.replace(remove[r], "");
					}
					// end remover
					String[] sentence = d.split(" ");

					for (int m = 0; m < sentence.length; m++) {
						if (inputText.equalsIgnoreCase(sentence[m])) {
							wordFound = true;
							counter++;
						}
					}
				}
			}
		}
		if (!wordFound) {
			System.out.println("\t [!] Word does not exist!");
		} else {
			System.out.println("\t > Word is existent! (" + counter + ")");
		}

		return wordFound;
	}

	// 4
	public static int numberOfChaptersInBook(String book) {
		int number = 0;
		for (int j = 0; j < verseList.length; j++) {
			String[] data = verseList[j].split("\t");
			if (book.equalsIgnoreCase(data[0])) {
				if (number < Integer.parseInt(data[1])) {
					number++;
				}
			}
		}
		System.out.println();
		System.out.println("Book:\t\t" + book);
		System.out.println("Chapters:\t" + number);
		// System.out.println("There are " + number + " chapters in the Book of " +
		// book);

		return number;
	}

	// 5
	public static void numberOfChaptersInTheBible() {
		int number = 0;

		number += numberOfChaptersInBook("GEN");
		number += numberOfChaptersInBook("EXO");
		number += numberOfChaptersInBook("LEV");
		number += numberOfChaptersInBook("NUM");
		number += numberOfChaptersInBook("DEU");

		System.out.println();
		System.out.println("Book:\t\t Entire Bible");
		System.out.println("Chapters:\t" + number);
		// System.out.println("There are " + number + " chapters in the Book of " +
		// book);
	}

	// 6
	public static void totalNumberOfVersesInTheBible() {
		int number = 0;

		number += numberOfChaptersInBook("GEN");
		number += numberOfChaptersInBook("EXO");
		number += numberOfChaptersInBook("LEV");
		number += numberOfChaptersInBook("NUM");
		number += numberOfChaptersInBook("DEU");

		System.out.println("Book:\t\t Entire Bible");
		System.out.println("Verses:\t\t" + number);
	}

	// 7
	public static int totalNumberOfVersesInBookOf(String book) {
		int number = 0;
		for (int j = 0; j < verseList.length; j++) {
			String[] data = verseList[j].split("\t");
			if (book.equalsIgnoreCase(data[0])) {
				number++;
			}
		}
		System.out.println();
		System.out.println("Book:\t\t" + book);
		System.out.println("Verses:\t\t" + number);
		// System.out.println("There are " + number + " verses in the Book of " + book);

		return number;
	}

	// 8
	public static void numberOfVersesInBookWithChapter(String book, String chapter) {
		int number = 0;
		for (int j = 0; j < verseList.length; j++) {
			String[] data = verseList[j].split("\t");
			if (book.equalsIgnoreCase(data[0]) && chapter.equals(data[1])) {
				if (number < Integer.parseInt(data[2])) {
					number++;
				}
			}
		}
		System.out.println();
		System.out.println("Book:\t\t" + book);
		System.out.println("Chapter:\t\t" + chapter);
		System.out.println("Verses:\t\t" + number);
		// System.out.println("There are " + number + " verses in chapter " + chapter +
		// " the Book of " + book);
	}

	// 9
	public static void numberOfTimesTheWordAppearedInTheBible(String inputText) {
		int number = 0;
		for (int j = 0; j < verseList.length; j++) {
			String[] data = verseList[j].split("\t");

			String d = data[3];
			// this code removes the symbols and characters
			for (int r = 0; r < remove.length; r++) {
				d = d.replace(remove[r], "");
			}
			// end remover
			String[] sentence = d.split(" ");

			for (int k = 0; k < sentence.length; k++) {
				if (inputText.equalsIgnoreCase(sentence[k])) {
					number++;
				}
			}
		}
		System.out.println();
		System.out.println("Book:\t\t Entire Bible");
		System.out.println("Word:\t\t" + inputText);
		System.out.println("Matches:\t\t" + number);
	}

	// 10
	public static void numberOfTimesTheWordAppearedInBookOf(String book, String inputText) {
		int number = 0;
		for (int j = 0; j < verseList.length; j++) {
			String[] data = verseList[j].split("\t");
			if (book.equalsIgnoreCase(data[0])) {

				String d = data[3];
				// this code removes the symbols and characters
				for (int r = 0; r < remove.length; r++) {
					d = d.replace(remove[r], "");
				}
				// end remover
				String[] sentence = d.split(" ");

				for (int k = 0; k < sentence.length; k++) {
					if (inputText.equalsIgnoreCase(sentence[k])) {
						number++;
					}
				}
			}
		}
		System.out.println();
		System.out.println("Book:\t\t" + book);
		System.out.println("Word:\t\t" + inputText);
		System.out.println("Matches:\t\t" + number);

	}

	// 11
	public static void numberOfTimesTheWordAppearedInBookOfWithChapter(String book, String chapter, String inputText) {
		int number = 0;
		for (int j = 0; j < verseList.length; j++) {
			String[] data = verseList[j].split("\t");
			if (book.equalsIgnoreCase(data[0]) && chapter.equals(data[1])) {

				String d = data[3];
				// this code removes the symbols and characters
				for (int r = 0; r < remove.length; r++) {
					d = d.replace(remove[r], "");
				}
				// end remover
				String[] sentence = d.split(" ");

				for (int k = 0; k < sentence.length; k++) {
					if (inputText.equalsIgnoreCase(sentence[k])) {
						number++;
					}
				}
			}
		}
		System.out.println();
		System.out.println("Book:\t\t" + book);
		System.out.println("Chapter:\t\t" + chapter);
		System.out.println("Word:\t\t" + inputText);
		System.out.println("Matches:\t\t" + number);
		// System.out.println("There are " + number + " number of times the word " +
		// inputText + " appeared in chapter " + chapter + " in the Book of " + book);
	}

	// 12
	public static void numberOfTimesTheWordAppearedInBookOfWithChapterAndVerseNum(String book, String chapter,
			String verseNum, String inputText) {
		int number = 0;
		for (int j = 0; j < verseList.length; j++) {
			String[] data = verseList[j].split("\t");
			if (book.equalsIgnoreCase(data[0]) && chapter.equals(data[1]) && verseNum.equals(data[2])) {

				String d = data[3];
				// this code removes the symbols and characters
				for (int r = 0; r < remove.length; r++) {
					d = d.replace(remove[r], "");
				}
				// end remover
				String[] sentence = d.split(" ");

				for (int k = 0; k < sentence.length; k++) {
					if (inputText.equalsIgnoreCase(sentence[k])) {
						number++;
					}
				}
			}
		}
		System.out.println();
		System.out.println("Book:\t\t" + book);
		System.out.println("Chapter:\t\t" + chapter);
		System.out.println("Verse Number:\t\t" + chapter);
		System.out.println("Word:\t\t" + inputText);
		System.out.println("Matches:\t\t" + number);
		// System.out.println("There are " + number + " number of times the word " +
		// inputText + " appeared in chapter " + chapter + " in the Book of " + book);
	}

	public static void whichBooksContain(String inputText) {
		String books = "";
		boolean gen = false, exo = false, lev = false, num = false, deu = false;
		for (int j = 0; j < verseList.length; j++) {
			String[] data = verseList[j].split("\t");
			String d = data[3];
			// this code removes the symbols and characters
			for (int r = 0; r < remove.length; r++) {
				d = d.replace(remove[r], "");
			}
			// end remover
			String[] sentence = d.split(" ");

			for (int k = 0; k < sentence.length; k++) {
				if (inputText.equalsIgnoreCase(sentence[k])) {
					books += data[0] + " ";
					break;
				}
			}
		}

		String[] rem = books.split(" ");
		for (int y = 0; y < rem.length; y++) {
			if (!gen || !exo || !lev || !num || !deu) {
				if (rem[y].equalsIgnoreCase("GEN")) {
					gen = true;
				}
				if (rem[y].equalsIgnoreCase("EXO")) {
					exo = true;
				}
				if (rem[y].equalsIgnoreCase("LEV")) {
					lev = true;
				}
				if (rem[y].equalsIgnoreCase("NUM")) {
					num = true;
				}
				if (rem[y].equalsIgnoreCase("DEU")) {
					deu = true;
				}
			} else {
				break;
			}
		}
		books = "";

		if (gen)
			books += "GEN";
		if (exo)
			books += ", EXO";
		if (lev)
			books += ", LEV";
		if (num)
			books += ", NUM";
		if (deu)
			books += ", DEU";

		System.out.println("Word: \"" + inputText + "\"");
		System.out.println("Existence: " + books);
	}

	// CheckExistenceOfText
	public static boolean checkExistence(String[] arr, String t) {
		boolean repeated = false;
		for (int j = 0; j < arr.length; j++) {
			if (t.equalsIgnoreCase(arr[j])) {
				repeated = true;
				break;
			}
		}
		return repeated;
	}

	// Run Method
	public static void run() {
		boolean stop = false;
		while (!stop) {
			int choice = getNumber(13);
			System.out.println();
			switch (choice) {
			case 1:
				printBookOf(getText("Book"));
				System.out.println();
				showPressEnter();
				break;
			case 2:
				checkExistenceOfChapterAndVerseInBook(getText("Book"), getText("Chapter"), getText("Verse Number"));
				System.out.println();
				showPressEnter();
				break;
			case 3:
				checkExistenceOfTextInBook(getText("Book"), getText("Chapter"), getText("Verse Number"),
						getText("Word"));
				System.out.println();
				showPressEnter();
				break;

			case 4:
				numberOfChaptersInTheBible();
				System.out.println();
				showPressEnter();
				break;
			case 5:
				numberOfChaptersInBook(getText("Book"));
				System.out.println();
				showPressEnter();
				break;

			case 6:
				totalNumberOfVersesInTheBible();
				System.out.println();
				showPressEnter();
				break;
			case 7:
				totalNumberOfVersesInBookOf(getText("Book"));
				System.out.println();
				showPressEnter();
				break;
			case 8:
				numberOfVersesInBookWithChapter(getText("Book"), getText("Chapter"));
				System.out.println();
				showPressEnter();
				break;

			case 9:
				numberOfTimesTheWordAppearedInTheBible(getText("Word"));
				System.out.println();
				showPressEnter();
				break;
			case 10:
				numberOfTimesTheWordAppearedInBookOf(getText("Book"), getText("Word"));
				System.out.println();
				showPressEnter();
				break;
			case 11:
				numberOfTimesTheWordAppearedInBookOfWithChapter(getText("Book"), getText("Chapter"), getText("Word"));
				System.out.println();
				showPressEnter();
				break;
			case 12:
				numberOfTimesTheWordAppearedInBookOfWithChapterAndVerseNum(getText("Book"), getText("Chapter"),
						getText("Verse Number"), getText("Word"));
				System.out.println();
				showPressEnter();
				break;
			case 13:
				stop = true;
			}
		}
	}

	public static int getNumber(int limit) {
		int c = 0;
		while (c < 1) {

			System.out.println("PRINTING");
			System.out.println("[1]  Print all contents of a book");

			System.out.println("\nCHECK EXISTENCE");
			System.out.println("[2]  If a chapter and verse exists");
			System.out.println("[3]  If a word exists in a particular chapter and verse of a book");

			System.out.println("\nSHOW NUMBER OF CHAPTERS");
			System.out.println("[4]  Chapters in the Bible");
			System.out.println("[5]  Chapters in a book");

			System.out.println("\nSHOW NUMBER OF VERSES");
			System.out.println("[6]  Number of verses in the Bible");
			System.out.println("[7]  Number of verses in a book");
			System.out.println("[8]  Number of verses in a particular chapter of a book");

			System.out.println("\nCHECK HOW MANY TIMES A WORD IS USED");
			System.out.println("[9]  In the Bible");
			System.out.println("[10] In a book");
			System.out.println("[11] In a chapter of a book");
			System.out.println("[12] In a chapter and a particular verse number of a book");

			System.out.println("\nSHOW WHO HAS THE WORD");
			System.out.println("[13]  Which books");
			System.out.println("[14]  Which chapters in a particular book");
			System.out.println("[15] Which verses in a chapter of a book");

			System.out.println("\n[16] EXIT");

			System.out.print("\nEnter your choice: ");
			try {
				c = Integer.parseInt(kbd.nextLine());
				if (c < 1 || c > limit) {
					System.out.println("\n\t > Invalid choice!  \n");
					c = -1;
				}
			} catch (Exception j) {
				System.out.println("\n\t > A number is required!  \n");
			}
		}
		return c;
	}

	public static String getText(String entryFor) {
		String t = "";
		while (t.length() <= 0) {
			System.out.print("Enter " + entryFor + ": ");
			t = kbd.nextLine();
		}
		return t;
	}

	public static void main(String[] args) {
		/**
		 * Each verse in verseList is split into four (4) smaller substrings.
		 *
		 * These are... data[0] - Book (e.g. GEN, EXO, LEV, NUM, DEU) data[1] - Chapter
		 * # data[2] - Verse # data[3] - Verse (text)
		 */
		// whichBooksContain(getText("Word"));
		try {
			BibleMaster program = new BibleMaster();
			BibleMaster.run();
		} catch (Exception g) {
			g.printStackTrace();
			System.exit(1);
		} finally {
			System.exit(0);
		}
	}

}
