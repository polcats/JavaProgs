import java.util.*;

public class Queens {
	static Scanner k = new Scanner(System.in);
	static int dim = 0;

	public static void main(String[] args) {
		// get dimensions
		boolean loop = true;
		while (loop) {
			System.out.println("Input Types");
			System.out.println("   x       -> Exit");
			System.out.println(" <num>     -> Hill Climb");
			System.out.println(" <num> -rr -> HC with Random Restart");
			System.out.print("Enter Action: ");
			String input = k.nextLine();
			if (input.equalsIgnoreCase("x")) {
				loop = false;
				return;
			}
			try {
				dim = Integer.parseInt(input.split(" ")[0]);
				if (dim < 4) {
					System.out.println("Dimension must be at least 4.");
				} else {
					int board[][] = initBoard(dim);
					if (input.contains("-rr")) { // random restart mode
						hillClimb(board, true);
					} else {
						hillClimb(board, false); // default mode
					}

				}
			} catch (Exception e) {
				System.out.println("\nError. Press enter to continue.");
				k.nextLine();
			}

		}
	}

	// solver
	public static void hillClimb(int initialBoard[][], boolean restartActive) {
		// show initial details
		printBoard(initialBoard);
		System.out.println("Inital non-conflicting Queen Pairs: " + ev(initialBoard));
		System.out.println("Target non-conflicting Queen Pairs: " + solutionNum(dim));
		System.out.println();

		// counters
		int iter = 0; // overall iterations
		int restarts = 0;
		int localMaxesCount = 0; // total number of LMaxes found
		int localMaxIters = 0; // total iterations when an LMax is found

		// storage of temporary best states | per "while" iteration
		// also storage for the final answer
		int bestArrangement[][] = initialBoard;

		// how many queens should not be in conflict | for goal check
		int targetValue = solutionNum(dim);

		// find the solution
		boolean solved = false;
		while (!solved) {

			// initialize
			int potentialNewBest[][] = new int[dim][dim]; // storage of best successor states | per "for" iteration
															// (evaluated per row)
			int potentialBestArrangement[][] = new int[dim][dim]; // storage of the best arrangement found by the entire
																	// for-loop

			for (int n = 0; n < dim; n++) { // iterate through the rows and get the best successor of each row

				int parentValue = ev(potentialNewBest); // value of nthQueen (parent)
				potentialNewBest = bestSuccessorOf(bestArrangement, n); // get best successor of parent
				if (ev(potentialNewBest) > parentValue) { // if best successor is better than parent

					// update the board that the next "for" iteration will use
					potentialBestArrangement = potentialNewBest;

				} else { // its a Local Max
					localMaxIters++; // increment total number of LMaxes found
					localMaxesCount += n + 1; // add to total amount of iterations per LMax
					System.out
							.println("Local Max: " + ev(potentialNewBest) + " found within " + (n + 1) + " Iterations");

				}
				iter++; // increment overall iterations
			}

			// check if the for-loop found a better arrangement than the current final
			// arrangement
			if (ev(potentialBestArrangement) > ev(bestArrangement)) {
				bestArrangement = potentialBestArrangement; // assign it as a new final arrangement

				// goal check
				if (ev(bestArrangement) == targetValue) { // value of final arrangement is equal to target value
					solved = true;

					// print global max
					System.out.println("Global Max: " + ev(bestArrangement));
					break; // the while loop
				}

			} else { // peak value which is not the solution is reached

				// reset if running in random restart mode
				if (restartActive) {
					restarts++;
					bestArrangement = initBoard(dim);
				} else { // terminate (default hillclimbing)
					break;
				}

			}
		}

		// print details of the search
		if (solved) {
			System.out.println("\nSolution Found!");
			System.out.println("Dimensions: " + dim + "x" + dim);
			System.out.println("Iterations (Overall) : " + iter);
			if (restartActive) {
				System.out.println("Iterations Before an Lmax is Found (Average) : " + localMaxesCount / localMaxIters); // ("+localMaxesCount+"/"+localMaxIters+")
				System.out.println("Restarts: " + restarts);
			}
			System.out.println("\n\nFinal Board:");
			printBoard(bestArrangement);
			System.out.println("\n\n\n\n\n");
		} else {
			System.out.println("\nCannot Find a Solution!\n");
		}
	}

	// prints the board
	public static void printBoard(int[][] board) {
		String qLook = "[Q]";
		String blank = " x ";
		System.out.println();
		for (int d = 0; d < dim; d++) { // row
			for (int e = 0; e < dim; e++) { // columns
				String prnt = board[d][e] + "";
				System.out.print(prnt.replace("1", qLook).replace("0", blank) + " ");
			}
			System.out.println("\n");
		}
	}

	// returns the best successor of a board's row (nthQueen)
	public static int[][] bestSuccessorOf(int[][] parentState, int nthQueen) {
		ArrayList<int[][]> childN = new ArrayList<int[][]>();

		for (int k = 0; k < dim; k++) { // navigate through the columns
			if (parentState[nthQueen][k] != 1) { // this column is not the Queen location in parent, then insert child

				// create child
				int tempo[][] = new int[dim][dim];

				// place the Queen to a new location in the child
				tempo[nthQueen][k] = 1;

				// add the other rows from parent
				for (int j = 0; j < dim; j++) { // rows
					if (j != nthQueen) {
						tempo[j] = parentState[j];
					}
				}

				// add this child to the child storage
				childN.add(tempo);
			}
		}

		// determine the best child among the children
		int bestChild[][] = new int[dim][dim];
		for (int j = 0; j < childN.size(); j++) {
			int curr = ev(bestChild);
			int tes = ev(childN.get(j));
			if (tes > curr) { // better? change best
				bestChild = childN.get(j);
			} else if (tes == curr) { // equal? random from 2 nums choose 1.
				int choose = random(2);
				if (choose == 1) { // get new instead of previous
					bestChild = childN.get(j);
				}
			}
		}

		return bestChild;
	}

	// counts non-conflicting queens in a board (value of a board)
	public static int ev(int board[][]) {
		int sum = 0;
		int nthQueen = 0;

		while (nthQueen < dim - 1) { // every queen will be compared to all the next queens except the previous |
										// "this" comparedTo "allNextQueens"

			int nthQueenCol = getCol(board, nthQueen);
			int nthQx = nthQueenCol;
			int nthQy = nthQueen;
			for (int a = nthQueen; a < dim; a++) { // iterator of queens after nth queen

				if ((a + 1) < dim) { // if nextQueen is within index range

					int nthQueen_next_x = getCol(board, a + 1);
					int nthQueen_next_y = (a + 1);

					// |x1-x2|
					int xAbs = Math.abs(nthQx - nthQueen_next_x);

					// |y1-y2|
					int yAbs = Math.abs(nthQy - nthQueen_next_y);

					// count for non-conflicts
					if (xAbs != yAbs && nthQueen_next_x != nthQx) { // not in same diagonal
						sum++;
					}
				}
			}

			// increment to move to the next row's queen
			nthQueen++;
		}
		return sum;
	}

	// returns the number of non-conflicting pairs given the number of queens
	// aka the target evaluation
	public static int solutionNum(int num) {
		int res = 0;
		int limit = num - 1;
		while (limit > 0) {
			res += limit;
			limit--;
		}
		// res -= dim;
		return res;
	}

	// returns "column index" of a queen "given its row"
	public static int getCol(int[][] board, int rowIndex) {
		int returnInt = 0;
		for (int a = 0; a < board.length; a++) {
			if (board[rowIndex][a] == 1) { // check per column
				returnInt = a;
				break;
			}
		}
		return returnInt;
	}

	// generates a board with dim x dim dimensions
	public static int[][] initBoard(int dim) {
		int board[][] = new int[dim][dim];
		for (int c = 0; c < dim; c++) {
			board[c][random(dim)] = 1;
		}
		return board;
	}

	// generates a random number from 0-max
	public static int random(int max) {
		int num = (int) (Math.random() * max);
		return num;
	}
}