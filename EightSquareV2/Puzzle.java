import java.io.*;
import java.util.*;

public class Puzzle {

	static Scanner k = new Scanner(System.in);
	static State goalState = new State();
	static State initialState = new State();

	// not sure if needed
	static int totalFrontierItems = 1; // starts at 1 w/ initial state
	static int iterations = 0; // aka depth of tree

	// set to true to print generated items' details
	static boolean showGen = false;
	static boolean showChild = false;

	// all initializations happen here
	public static void main(String[] args) throws IOException {

		// initialize goal values | 0 represents the vacant tile
		/*
		 * 1 2 3 4 5 6 7 8 0
		 */
		ArrayList<Block> topG = new ArrayList<Block>();
		topG.add(new Block(1));
		topG.add(new Block(2));
		topG.add(new Block(3));
		ArrayList<Block> midG = new ArrayList<Block>();
		midG.add(new Block(4));
		midG.add(new Block(5));
		midG.add(new Block(6));
		ArrayList<Block> botG = new ArrayList<Block>();
		botG.add(new Block(7));
		botG.add(new Block(8));
		botG.add(new Block(0));
		goalState = new State(topG, midG, botG);

		// get initial state values
		System.out.println(
				"Please enter the placement of the numbers. \nUse zero for the blank space. eg. 123 for top. etc.");
		System.out.print("top blocks: ");
		String top = k.nextLine();
		System.out.print("mid blocks: ");
		String mid = k.nextLine();
		System.out.print("bot blocks: ");
		String bot = k.nextLine();

		try {
			Integer.parseInt(top);
			Integer.parseInt(mid);
			Integer.parseInt(bot);
		} catch (Exception e) {
			System.out.println("Incorrect input format!");
			System.exit(0);
		}

		System.out.println();

		// place the initial values into Blocks and insert those into an initial State
		ArrayList<Block> iTop = new ArrayList<Block>();
		String[] iTopNums = top.split("");
		iTop.add(new Block(Integer.parseInt(iTopNums[0])));
		iTop.add(new Block(Integer.parseInt(iTopNums[1])));
		iTop.add(new Block(Integer.parseInt(iTopNums[2])));
		ArrayList<Block> iMid = new ArrayList<Block>();

		String[] iMidNums = mid.split("");
		iMid.add(new Block(Integer.parseInt(iMidNums[0])));
		iMid.add(new Block(Integer.parseInt(iMidNums[1])));
		iMid.add(new Block(Integer.parseInt(iMidNums[2])));
		ArrayList<Block> iBot = new ArrayList<Block>();

		String[] iBotNums = bot.split("");
		iBot.add(new Block(Integer.parseInt(iBotNums[0])));
		iBot.add(new Block(Integer.parseInt(iBotNums[1])));
		iBot.add(new Block(Integer.parseInt(iBotNums[2])));

		initialState = new State(iTop, iMid, iBot);
		try {
			run();
		} catch (Exception e) {
			// System.out.println(e.printStackTrace());
		} finally {
			System.out.println("Iterations: " + iterations);
			System.out.println("Items added to Frontier: " + totalFrontierItems);
			System.out.println("\nInitial State: \n" + initialState.toString());
		}
	}

	// actual program
	public static void run() throws FileNotFoundException {

		ArrayList<State> frontier = new ArrayList<State>();
		frontier.add(initialState);

		// show initial config
		System.out.println(frontier.get(0).getPart("top"));
		System.out.println(frontier.get(0).getPart("mid"));
		System.out.println(frontier.get(0).getPart("bot"));
		System.out.println();

		// States that are already seen aka generated
		// Key, Value
		Map<String, Boolean> seenStates = new HashMap<String, Boolean>();
		// HashSet<String> seenStates = new HashSet<String>();

		// long startTime = System.currentTimeMillis();

		// Iterate through the frontier
		State current;
		boolean keepGoing = true;
		boolean goal = false;
		while (frontier.size() != 0 && keepGoing) {

			// gets the leftmost item in the frontier
			current = frontier.get(0);

			// add to seen states
			seenStates.put(current.toString(), true);
			// seenStates.add(current.toString());

			// generate the children of the current state
			ArrayList<State> nextStates = current.getChildren();

			// iterate through the generated children
			for (int s = 0; s < nextStates.size(); s++) {

				// check if generated child is not seen before | else dont process it
				if (seenStates.get(nextStates.get(s).toString()) == null) { // null = not seen yet
					// if(seenStates.contains(nextStates.get(s).toString()) == false) {
					// check if this child is a goal
					goal = checkGoal(nextStates.get(s));

					// set the parent of the generated child
					nextStates.get(s).setParent(current.toString());

					// prints generated child (for debug)
					// variable showChild is declard as a global variable in Puzzle class
					if (showChild)
						System.out.println(" child" + s + ": " + nextStates.get(s).toString());

					// increment the counter for the items added to the frontier
					totalFrontierItems++;

					// add this child to the frontier
					frontier.add(nextStates.get(s));

					// stops the loop once a goal is found
					if (goal) {
						keepGoing = false;
						break;
					}

				}
			}

			// increment the depth of the tree aka iterations
			iterations++;

			// remove the current item in the frontier aka "leftmost" item
			frontier.remove(0);
		}

		if (!goal) {
			System.out.println("\nA GOAL CANNOT BE FOUND!");
		} else {
			System.out.println("\nSOLUTION FOUND!");
		}
	}

	public static boolean checkGoal(State tryState) {

		// equality checks
		if (!goalState.getPart("top").equals(tryState.getPart("top"))) {
			return false;
		}
		if (!goalState.getPart("mid").equals(tryState.getPart("mid"))) {
			return false;
		}
		if (!goalState.getPart("bot").equals(tryState.getPart("bot"))) {
			return false;
		}

		// all checks go through then they are equal
		return true;
	}
}

// the tiles
class Block {
	int num;

	public Block() {
	}

	public Block(int num) {
		this.num = num;
	}

	public int getNumber() {
		return this.num;
	}
}

/*
 * State (basically the board) is composed of 3 parts: top, mid, and bottom.
 */
class State {
	ArrayList<Block> top = new ArrayList<Block>();
	ArrayList<Block> mid = new ArrayList<Block>();
	ArrayList<Block> bot = new ArrayList<Block>();
	String parentState = "";

	public State() {
	}

	public State(ArrayList<Block> t, ArrayList<Block> m, ArrayList<Block> b) {
		this.top = t;
		this.mid = m;
		this.bot = b;
	}

	public void setParent(String p) {
		this.parentState = p;
	}

	// returns the part of the board (eg. top, mid, bot)
	public String getPart(String part) {
		ArrayList<Block> tempBlock = new ArrayList<Block>();

		if (part == "top") {
			tempBlock = this.top;
		} else if (part == "mid") {
			tempBlock = this.mid;
		} else if (part == "bot") {
			tempBlock = this.bot;
		}

		// use SB for "perf issues" lmao
		StringBuilder returnPart = new StringBuilder();
		for (int j = 0; j < tempBlock.size(); j++) {
			returnPart.append(tempBlock.get(j).getNumber());
		}

		return returnPart.toString();
	}

	// basically returns the location of 0 (vacant block) in this state
	public String getVacant() {
		String res = "";
		String vTop = this.getPart("top");
		String vMid = this.getPart("mid");
		String vBot = this.getPart("bot");

		// zero is in top
		if (vTop.contains("0")) {
			res = "t-" + vTop.indexOf("0");
		} else if (vMid.contains("0")) {
			res = "m-" + vMid.indexOf("0");
		} else if (vBot.contains("0")) {
			res = "b-" + vBot.indexOf("0");
		}

		return res;
	}

	// this method checks the vacant slot and returns the possible children out of
	// it
	public ArrayList<State> getChildren() {

		// the children
		ArrayList<State> children = new ArrayList<State>();

		// check which slot is vacant, then act accordingly
		String vac = this.getVacant();

		// top left is vacant (2 possibilities) t-1 and m-0
		if (vac.equals("t-0")) {
			if (Puzzle.showGen) // debug purposes | showGen is declared as a global variable in Puzzle class
				System.out.println("current: " + this.toString() + " (generating from Top Left):");

			State topLeft = new State();

			// pos.1] top left and top center (only top changes)
			ArrayList<Block> blockSet1 = new ArrayList<Block>();
			blockSet1.add(this.top.get(1));
			blockSet1.add(this.top.get(0));
			blockSet1.add(this.top.get(2));
			topLeft = new State(blockSet1, this.mid, this.bot);
			children.add(topLeft);

			// pos.2] top left and middle left (top and middle changes)
			ArrayList<Block> blockSet2_1 = new ArrayList<Block>();
			blockSet2_1.add(this.mid.get(0));
			blockSet2_1.add(this.top.get(1));
			blockSet2_1.add(this.top.get(2));

			ArrayList<Block> blockSet2_2 = new ArrayList<Block>();
			blockSet2_2.add(this.top.get(0));
			blockSet2_2.add(this.mid.get(1));
			blockSet2_2.add(this.mid.get(2));

			topLeft = new State(blockSet2_1, blockSet2_2, this.bot);
			children.add(topLeft);
		} else

		// top center is vacant (3 possibilities) t-0, t-2, and m-1
		if (vac.equals("t-1")) {
			if (Puzzle.showGen) // debug purposes | showGen is declared as a global variable in Puzzle class
				System.out.println("current: " + this.toString() + " (generating from Top Center):");

			State topCenter = new State();

			// pos.1] top center and top left (only top changes)
			ArrayList<Block> blockSet1 = new ArrayList<Block>();
			blockSet1.add(this.top.get(1));
			blockSet1.add(this.top.get(0));
			blockSet1.add(this.top.get(2));
			topCenter = new State(blockSet1, this.mid, this.bot);
			children.add(topCenter);

			// pos.2] top center and top right (only top changes)
			ArrayList<Block> blockSet2 = new ArrayList<Block>();
			blockSet2.add(this.top.get(0));
			blockSet2.add(this.top.get(2));
			blockSet2.add(this.top.get(1));
			topCenter = new State(blockSet2, this.mid, this.bot);
			children.add(topCenter);

			// pos.3] top center and middle center (top and mid changes)
			ArrayList<Block> blockSet3 = new ArrayList<Block>();
			blockSet3.add(this.top.get(0));
			blockSet3.add(this.mid.get(1));
			blockSet3.add(this.top.get(2));

			ArrayList<Block> blockSet4 = new ArrayList<Block>();
			blockSet4.add(this.mid.get(0));
			blockSet4.add(this.top.get(1));
			blockSet4.add(this.mid.get(2));

			topCenter = new State(blockSet3, blockSet4, this.bot);
			children.add(topCenter);
		} else

		// top right is vacant (2 possibilities) t-1 and m-2
		if (vac.equals("t-2")) {
			if (Puzzle.showGen) // debug purposes | showGen is declared as a global variable in Puzzle class
				System.out.println("current: " + this.toString() + " (generating from Top Right):");

			State topRight = new State();

			// pos.1] top right and top center (only top changes)
			ArrayList<Block> blockSet1 = new ArrayList<Block>();
			blockSet1.add(this.top.get(0));
			blockSet1.add(this.top.get(2));
			blockSet1.add(this.top.get(1));
			topRight = new State(blockSet1, this.mid, this.bot);
			children.add(topRight);

			// pos.2] top right and middle right (top and middle changes)
			ArrayList<Block> blockSet2_1 = new ArrayList<Block>();
			blockSet2_1.add(this.top.get(0));
			blockSet2_1.add(this.top.get(1));
			blockSet2_1.add(this.mid.get(2));

			ArrayList<Block> blockSet2_2 = new ArrayList<Block>();
			blockSet2_2.add(this.mid.get(0));
			blockSet2_2.add(this.mid.get(1));
			blockSet2_2.add(this.top.get(2));

			topRight = new State(blockSet2_1, blockSet2_2, this.bot);
			children.add(topRight);
		} else

		// mid left is vacant (3 possibilities) t-0, m-1, and b-0
		if (vac.equals("m-0")) {
			if (Puzzle.showGen) // debug purposes | showGen is declared as a global variable in Puzzle class
				System.out.println("current: " + this.toString() + " (generating from Middle Left):");

			State midLeft = new State();

			// pos.1] mid left and top left (top and mid changes)
			ArrayList<Block> blockSet1_1 = new ArrayList<Block>();
			blockSet1_1.add(this.mid.get(0));
			blockSet1_1.add(this.top.get(1));
			blockSet1_1.add(this.top.get(2));

			ArrayList<Block> blockSet1_2 = new ArrayList<Block>();
			blockSet1_2.add(this.top.get(0));
			blockSet1_2.add(this.mid.get(1));
			blockSet1_2.add(this.mid.get(2));

			midLeft = new State(blockSet1_1, blockSet1_2, this.bot);
			children.add(midLeft);

			// pos.2] mid left and mid center (only mid changes)
			ArrayList<Block> blockSet2 = new ArrayList<Block>();
			blockSet2.add(this.mid.get(1));
			blockSet2.add(this.mid.get(0));
			blockSet2.add(this.mid.get(2));

			midLeft = new State(this.top, blockSet2, this.bot);
			children.add(midLeft);

			// pos.3] mid left and bot left (mid and bot changes)
			ArrayList<Block> blockSet3_1 = new ArrayList<Block>();
			blockSet3_1.add(this.bot.get(0));
			blockSet3_1.add(this.mid.get(1));
			blockSet3_1.add(this.mid.get(2));

			ArrayList<Block> blockSet3_2 = new ArrayList<Block>();
			blockSet3_2.add(this.mid.get(0));
			blockSet3_2.add(this.bot.get(1));
			blockSet3_2.add(this.bot.get(2));

			midLeft = new State(this.top, blockSet3_1, blockSet3_2);
			children.add(midLeft);
		} else

		// mid center (4 possibilities) t-1, m-0, m-2, and b-1
		if (vac.equals("m-1")) {
			if (Puzzle.showGen) // debug purposes | showGen is declared as a global variable in Puzzle class
				System.out.println("current: " + this.toString() + " (generating from Middle Center):");

			State midCenter = new State();

			// pos.1] middle center and top center (top and mid changes)
			ArrayList<Block> blockSet1_1 = new ArrayList<Block>();
			blockSet1_1.add(this.top.get(0));
			blockSet1_1.add(this.mid.get(1));
			blockSet1_1.add(this.top.get(2));

			ArrayList<Block> blockSet1_2 = new ArrayList<Block>();
			blockSet1_2.add(this.mid.get(0));
			blockSet1_2.add(this.top.get(1));
			blockSet1_2.add(this.mid.get(2));

			midCenter = new State(blockSet1_1, blockSet1_2, this.bot);
			children.add(midCenter);

			// pos.2] middle center and middle left (only mid changes)
			ArrayList<Block> blockSet2 = new ArrayList<Block>();
			blockSet2.add(this.mid.get(1));
			blockSet2.add(this.mid.get(0));
			blockSet2.add(this.mid.get(2));

			midCenter = new State(this.top, blockSet2, this.bot);
			children.add(midCenter);

			// pos.3] middle center and middle right (only mid changes)
			ArrayList<Block> blockSet3 = new ArrayList<Block>();
			blockSet3.add(this.mid.get(0));
			blockSet3.add(this.mid.get(2));
			blockSet3.add(this.mid.get(1));

			midCenter = new State(this.top, blockSet3, this.bot);
			children.add(midCenter);

			// pos.4] middle center and bot center (mid and bot changes)
			ArrayList<Block> blockSet4_1 = new ArrayList<Block>();
			blockSet4_1.add(this.mid.get(0));
			blockSet4_1.add(this.bot.get(1));
			blockSet4_1.add(this.mid.get(2));

			ArrayList<Block> blockSet4_2 = new ArrayList<Block>();
			blockSet4_2.add(this.bot.get(0));
			blockSet4_2.add(this.mid.get(1));
			blockSet4_2.add(this.bot.get(2));

			midCenter = new State(this.top, blockSet4_1, blockSet4_2);
			children.add(midCenter);

		} else

		// mid right is vacant (3 possibilities) t-2, m-1, and b-2
		if (vac.equals("m-2")) {
			if (Puzzle.showGen) // debug purposes | showGen is declared as a global variable in Puzzle class
				System.out.println("current: " + this.toString() + " (generating from Middle Right):");

			State midRight = new State();

			// pos.1] mid right and top right (top and mid changes)
			ArrayList<Block> blockSet1_1 = new ArrayList<Block>();
			blockSet1_1.add(this.top.get(0));
			blockSet1_1.add(this.top.get(1));
			blockSet1_1.add(this.mid.get(2));

			ArrayList<Block> blockSet1_2 = new ArrayList<Block>();
			blockSet1_2.add(this.mid.get(0));
			blockSet1_2.add(this.mid.get(1));
			blockSet1_2.add(this.top.get(2));

			midRight = new State(blockSet1_1, blockSet1_2, this.bot);
			children.add(midRight);

			// pos.2] mid right and mid center (only mid changes)
			ArrayList<Block> blockSet2 = new ArrayList<Block>();
			blockSet2.add(this.mid.get(0));
			blockSet2.add(this.mid.get(2));
			blockSet2.add(this.mid.get(1));

			midRight = new State(this.top, blockSet2, this.bot);
			children.add(midRight);

			// pos.3] mid right and bot right (mid and bot changes)
			ArrayList<Block> blockSet3_1 = new ArrayList<Block>();
			blockSet3_1.add(this.mid.get(0));
			blockSet3_1.add(this.mid.get(1));
			blockSet3_1.add(this.bot.get(2));

			ArrayList<Block> blockSet3_2 = new ArrayList<Block>();
			blockSet3_2.add(this.bot.get(0));
			blockSet3_2.add(this.bot.get(1));
			blockSet3_2.add(this.mid.get(2));

			midRight = new State(this.top, blockSet3_1, blockSet3_2);
			children.add(midRight);
		} else

		// bottom left is vacant (2 possibilities) m-0 and b-1
		if (vac.equals("b-0")) {
			if (Puzzle.showGen) // debug purposes | showGen is declared as a global variable in Puzzle class
				System.out.println("current: " + this.toString() + " (generating from Bottom Left):");

			State botLeft = new State();

			// pos.1] bottom left and bottom center (only bottom changes)
			ArrayList<Block> blockSet1 = new ArrayList<Block>();
			blockSet1.add(this.bot.get(1));
			blockSet1.add(this.bot.get(0));
			blockSet1.add(this.bot.get(2));

			botLeft = new State(this.top, this.mid, blockSet1);
			children.add(botLeft);

			// pos.2] bottom left and middle left (bottom and middle changes)
			ArrayList<Block> blockSet2_1 = new ArrayList<Block>();

			blockSet2_1.add(this.bot.get(0));
			blockSet2_1.add(this.mid.get(1));
			blockSet2_1.add(this.mid.get(2));

			ArrayList<Block> blockSet2_2 = new ArrayList<Block>();
			blockSet2_2.add(this.mid.get(0));
			blockSet2_2.add(this.bot.get(1));
			blockSet2_2.add(this.bot.get(2));

			botLeft = new State(this.top, blockSet2_1, blockSet2_2);
			children.add(botLeft);

		} else

		// bottom center is vacant (3 possibilities) b-0, b-2, and m-1
		if (vac.equals("b-1")) {
			if (Puzzle.showGen) // debug purposes | showGen is declared as a global variable in Puzzle class
				System.out.println("current: " + this.toString() + " (generating from Bottom Center):");

			State botCenter = new State();

			// pos.1] bottom center and bottom left (only bot changes)
			ArrayList<Block> blockSet1 = new ArrayList<Block>();
			blockSet1.add(this.bot.get(1));
			blockSet1.add(this.bot.get(0));
			blockSet1.add(this.bot.get(2));
			botCenter = new State(this.top, this.mid, blockSet1);
			children.add(botCenter);

			// pos.2] bottom center and middle center (bottom and mid changes)
			ArrayList<Block> blockSet2_1 = new ArrayList<Block>();
			blockSet2_1.add(this.mid.get(0));
			blockSet2_1.add(this.bot.get(1));
			blockSet2_1.add(this.mid.get(2));

			ArrayList<Block> blockSet2_2 = new ArrayList<Block>();
			blockSet2_2.add(this.bot.get(0));
			blockSet2_2.add(this.mid.get(1));
			blockSet2_2.add(this.bot.get(2));

			botCenter = new State(this.top, blockSet2_1, blockSet2_2);
			children.add(botCenter);

			// pos.3] bottom center and bottom right (only bot changes)
			ArrayList<Block> blockSet3 = new ArrayList<Block>();
			blockSet3.add(this.bot.get(0));
			blockSet3.add(this.bot.get(2));
			blockSet3.add(this.bot.get(1));
			botCenter = new State(this.top, this.mid, blockSet3);
			children.add(botCenter);
		} else

		// bottom right is vacant (2 possibilities) b-1 and m-2
		if (vac.equals("b-2")) {
			if (Puzzle.showGen) // debug purposes | showGen is declared as a global variable in Puzzle class
				System.out.println("current: " + this.toString() + " (generating from Bottom Right):");

			State botRight = new State();

			// pos.1] bottom right and bottom center (only bottom changes)
			ArrayList<Block> blockSet1 = new ArrayList<Block>();
			blockSet1.add(this.bot.get(0));
			blockSet1.add(this.bot.get(2));
			blockSet1.add(this.bot.get(1));
			botRight = new State(this.top, this.mid, blockSet1);
			children.add(botRight);

			// pos.2] bottom right and middle left (bottom and middle changes)
			ArrayList<Block> blockSet2_1 = new ArrayList<Block>();
			blockSet2_1.add(this.mid.get(0));
			blockSet2_1.add(this.mid.get(1));
			blockSet2_1.add(this.bot.get(2));

			ArrayList<Block> blockSet2_2 = new ArrayList<Block>();
			blockSet2_2.add(this.bot.get(0));
			blockSet2_2.add(this.bot.get(1));
			blockSet2_2.add(this.mid.get(2));

			botRight = new State(this.top, blockSet2_1, blockSet2_2);
			children.add(botRight);
		}

		return children;
	}

	// return a string version of this state
	public String toString() {
		StringBuilder returnStr = new StringBuilder();

		for (int j = 0; j < this.top.size(); j++) {
			returnStr.append(this.top.get(j).getNumber());
		}
		returnStr.append(" | ");
		for (int i = 0; i < this.mid.size(); i++) {
			returnStr.append(this.mid.get(i).getNumber());
		}
		returnStr.append(" | ");
		for (int m = 0; m < this.bot.size(); m++) {
			returnStr.append(this.bot.get(m).getNumber());
		}
		return returnStr.toString();

	}
}