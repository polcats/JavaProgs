import java.util.*;
import java.io.*;

public class Preprocessor {
	static HashMap<String, Boolean> verbs = new HashMap<String, Boolean>();
	static HashMap<String, Boolean> adverbs = new HashMap<String, Boolean>();
	static HashMap<String, Boolean> prepositions = new HashMap<String, Boolean>();
	static HashMap<String, Boolean> conjunctions = new HashMap<String, Boolean>();
	static Scanner data;
	static ArrayList<String> processed = new ArrayList<String>();
	static ArrayList<String> clean = new ArrayList<String>();
	static HashMap<String, Boolean> wordsChecked = new HashMap<String, Boolean>();

	public static void main(String[] args) {

		// initialize
		try {
			data = new Scanner(new File("dictionaries/selectedverbs.txt"));
			while(data.hasNext()) {
				verbs.put(data.nextLine().toLowerCase(), true);
			}

			//data = new Scanner(new File("adverbs.txt"));
			//while(data.hasNext()) {
			//	adverbs.put(data.nextLine().toLowerCase(), true);
			//}

			//data = new Scanner(new File("prepositions.txt"));
			//while(data.hasNext()) {
			//	prepositions.put(data.nextLine().toLowerCase(), true);
			//}

			data = new Scanner(new File("dictionaries/conjunctions.txt"));
			while(data.hasNext()) {
				conjunctions.put(data.nextLine().toLowerCase(), true);
			}
		} catch(Exception e) {
			System.out.println(e);
		}
		processData();

		//System.out.println(countVerbs("jump"));
	}

	public static void processData() {
		try {

			data = new Scanner(new File("rawexemps.txt"));
			while(data.hasNext()) {
				String inp = data.nextLine();
				String inpArr[] = inp.split(" ~ ");
				String procData = countVerbs(inpArr[0].trim().toLowerCase()) + "" + countConjunctions(inpArr[0].trim().toLowerCase()) + "" + inpArr[1] + ""/* + inpArr[2] + ","*/  + "" + transformCat(inpArr[4]) + " " + transformDiff(inpArr[3]);
				wordsChecked.clear(); // reset
				processed.add(procData);
				clean.add("'"+inpArr[0] +"',"+inpArr[3]);
			}


			FileWriter writer = new FileWriter(new File("procssedexemps.txt"));
			//writer.write("vrb cnj o cat cls\n");
			for(int y = 0; y < processed.size(); y++) {
				String ex = processed.get(y);
				String end = "\n";
				if(y == clean.size()-1) {
					end = "";
				}
				writer.write(ex + end);
			}
			writer.close();

			/*writer = new FileWriter(new File("clean.txt"));
			for(int y = 0; y < clean.size(); y++) {
				String ex = clean.get(y);
				writer.write(ex + "\n");
			}
			writer.close();*/

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static String transformDiff(String inp) {
		String fin = "";
		if(inp.equalsIgnoreCase("simple")) {
			fin = "001";
		} else if(inp.equalsIgnoreCase("moderate")) {
			fin = "010";
		} else if(inp.equalsIgnoreCase("hard")){
			fin = "100";
		}
		return fin;
	}

	public static String transformCat(String inp) {
		String fin = "";
		if(inp.equalsIgnoreCase("categ1")) {
			fin = "001";
		} else if(inp.equalsIgnoreCase("categ2")) {
			fin = "010";
		} else if(inp.equalsIgnoreCase("categ3")){
			fin = "011";
		} else if(inp.equalsIgnoreCase("categ4")){
			fin = "100";
		}
		return fin;
	}

	public static String countVerbs(String inp) {
		int verbCount = 0;
		String words[] = inp.trim().split(" ");
		for(int i = 0; i < words.length; i++) {
			if(verbs.get(words[i]) != null && wordsChecked.get(words[i]) == null) {
				wordsChecked.put(words[i], true);
				verbCount++;
				//System.out.println("verb " + words[i]);
			}
		}

		String binary = Integer.toString(verbCount, 2);
		int bits = 3;
		String fin = "";
		bits = bits - binary.length();
		for(int k = 0; k < bits; k++) {
			fin += "0";
		}
		fin += binary;

		return fin;
	}

	public static int countAdverbs(String inp) {
		int adverbCount = 0;
		String words[] = inp.trim().split(" ");
		for(int i = 0; i < words.length; i++) {
			if(adverbs.get(words[i]) != null && wordsChecked.get(words[i]) == null) {
				wordsChecked.put(words[i], true);
				adverbCount++;
			}
		}
		return adverbCount;
	}

	public static int countPrepositions(String inp) {
		int prepositionCount = 0;
		String words[] = inp.trim().split(" ");
		for(int i = 0; i < words.length; i++) {
			if(prepositions.get(words[i]) != null && wordsChecked.get(words[i]) == null) {
				wordsChecked.put(words[i], true);
				prepositionCount++;
			}
		}
		return prepositionCount;
	}

	public static String countConjunctions(String inp) {
		int conjunctionCount = 0;
		String words[] = inp.trim().split(" ");
		for(int i = 0; i < words.length; i++) {
			if(conjunctions.get(words[i]) != null && wordsChecked.get(words[i]) == null) {
				wordsChecked.put(words[i], true);
				conjunctionCount++;
				//System.out.println("conj " + words[i]);
			}
		}

		String binary = Integer.toString(conjunctionCount, 2);
		int bits = 3;
		String fin = "";
		bits = bits - binary.length();
		for(int k = 0; k < bits; k++) {
			fin += "0";
		}
		fin += binary;

		return fin;

	}
/*
			FileWriter writer = new FileWriter(new File("data2.txt"));
			Scanner file  = new Scanner(new File("data.txt"));


			while(file.hasNext()) {
				String k = file.nextLine();
				k = preProc(k);
				exemps.add(k);
				System.out.println(k);
				writer.write(k + "\n");
			}
			writer.close();
*/

	public static String preProc(String inp) {
		StringBuilder res = new StringBuilder();


		return res.toString();
	}
}