/*
 Group of
 Aberin, Catalan, De Leon,
 Mariano, Prado, Sevilla
*/

import java.util.*;

public class Haxed {

	static Scanner kbd = new Scanner(System.in);

	public static void main(String[] args) {
		int games = 5;
		int team = 0;
		int god = 0;
		int draws = 0;

		while (games > 0) {

			int limit = 5;
			int godLimiter = 10;

			char teamColor = 'R';
			int col = rand(1, 7);
			int row = (col % 2 != 0) ? rand(2, 5) : rand(2, 6);

			int tossTop = rand(1, 4);
			char topTileColor = (tossTop > 2) ? 'R' : 'G';

			int tossFir = rand(1, 4);
			char firstPlayer = (tossFir > 2) ? 'R' : 'G';

			System.out
					.println("First: " + firstPlayer + " Top Tile: " + topTileColor + " Col: " + col + " Row: " + row);

			char[][] initialBoard = plotInitial(col, row, topTileColor);
			int u = (teamColor == firstPlayer) ? -100 : 100;
			Node state = new Node(u, firstPlayer, null, initialBoard, null, false);

			// Need to uncomment some lines
			// Currently two bots are having a match of 50 games

			// state.showBoard();
			// System.out.println();
			// kbd.nextLine();

			do {
				char otherPlayer = reversePlayer(state.getPlayerColor());
				ArrayList<ArrayList<Integer>> coordinates = findPlayerTiles(state.getBoard(), state.getPlayerColor());
				// state.showBoard();
				// System.out.println("Player: " + state.getPlayerColor());
				// System.out.println("Occupied Tiles: " + countOccupied(state) + " Red Tiles: "
				// + countTiles(state, 'R') + " Green Tiles: " + countTiles(state, 'G'));
				ArrayList<Node> possibleMoves = generateSuccessors(coordinates, state, otherPlayer, teamColor);
				if (possibleMoves.get(0).getHexed()) {
					// System.out.println("Hexed!");
					// kbd.nextLine();
					state = possibleMoves.get(0);
				} else {
					int move = 1; // default
					limit++;
					godLimiter++;
					// for(int i = 0; i < possibleMoves.size(); i++){
					// System.out.println((i+1)+") col: " + possibleMoves.get(i).getMove()[1] + "
					// row: " + possibleMoves.get(i).getMove()[0]);
					// }
					if (state.getPlayerColor() != teamColor) {
						try {
							move = getMove(state, reversePlayer(teamColor), godLimiter, possibleMoves, '4');
						} catch (Exception e) {
							System.out.println("Error Handled: " + e);
						}
					} else if (state.getPlayerColor() == teamColor) {
						if (possibleMoves.size() > 1) {
							try {
								move = getMove(state, teamColor, limit, possibleMoves, '2');
							} catch (Exception e) {
								System.out.println("Error Handled: " + e);
							}
						}
					}
					state = possibleMoves.get(move - 1);
					// System.out.print(" |");
					// System.out.println("move: " + move + " (" + state.getMove()[1] + " - " +
					// state.getMove()[0] + ")");
				}
				// System.out.println("---------------------------------------------------------------------");
			} while (!(state.getHexed() && state.getParent().getHexed()));
			// System.out.println("First: " + firstPlayer + " Top Tile: " + topTileColor + "
			// Col: " + col + " Row: " + row);

			if (countTiles(state, teamColor) > countTiles(state, reversePlayer(teamColor))) {
				team++;
				// System.out.println("Red: " + countTiles(state, teamColor) + " Green: " +
				// countTiles(state, reversePlayer(teamColor)));
				System.out.println("old wins: " + team + " new wins: " + god + " draws: " + draws);

			} else if (countTiles(state, teamColor) < countTiles(state, reversePlayer(teamColor))) {
				god++;
				// System.out.println("Red: " + countTiles(state, teamColor) + " Green: " +
				// countTiles(state, reversePlayer(teamColor)));
				System.out.println("old wins: " + team + " new wins: " + god + " draws: " + draws);
			} else {
				draws++;
			}
			System.out.println();
			games--;
		}

		// System.out.println("team wins: " + team + " god wins: " + god);
	}

	public static double godMode(Node node, char teamColor, int phase) {
		char board[][] = node.getBoard();
		// int move[] = node.getMove();
		double util = 0;
		char otherPlayer = reversePlayer(teamColor);

		// tiles in side positions
		for (int r = 1; r < 6; r++) {
			if (board[r][0] == teamColor) {
				util += 15;
			} else if (board[r][0] == otherPlayer) {
				util -= 15;
			}
			if (board[r][8] == teamColor) {
				util += 15;
			} else if (board[r][8] == otherPlayer) {
				util -= 15;
			}
		}

		// unflankable tiles
		for (int c = 0; c < 9; c++) {
			if (c % 2 == 0) {
				if (c == 0 || c == 8) {
					util += (board[0][c] == teamColor) ? 25 : (board[0][c] == otherPlayer) ? -25 : 0;
					util += (board[6][c] == teamColor) ? 25 : (board[6][c] == otherPlayer) ? -25 : 0;
				} else {
					util += (board[0][c] == teamColor) ? 50 : (board[0][c] == otherPlayer) ? -50 : 0;
					util += (board[6][c] == teamColor) ? 50 : (board[6][c] == otherPlayer) ? -50 : 0;
				}
			}
		}

		/*
		 * double score = 0; for(int r = 0; r < 6; r++) { for(int c = 0; c < 9; c++) {
		 * char tile = board[r][c]; if(c % 2 != 0 && r < 5) { // odd cols score +=
		 * ((tile == teamColor) ? -7.5 : tile == otherPlayer ? 0.5 : 0); } else { //
		 * even cols // yours +10, enemy -10, vacant +5 score += ((tile == teamColor) ?
		 * 10 : tile == otherPlayer ? -10 : 5); } } }
		 */

		for (int r = 0; r < 6; r++) {
			for (int c = 0; c < 9; c++) {
				char tile = board[r][c];
				util += ((tile == teamColor) ? 1 : tile == otherPlayer ? -1 : 0);
			}
		}

		// low importance of score in early game
		// util += score;//(phase >= 20) ? score / 2 : score;

		// System.out.println(util);
		return util;
	}

	public static ArrayList<Node> godFilter(ArrayList<Node> moves, char teamColor, int phase) {
		char otherPlayer = reversePlayer(teamColor);
		ArrayList<Node> retained = moves;
		ArrayList<Node> edges = new ArrayList<Node>();
		ArrayList<Node> corners = new ArrayList<Node>();
		ArrayList<Node> sides = new ArrayList<Node>();
		ArrayList<Node> safer = new ArrayList<Node>();
		ArrayList<Node> best = new ArrayList<Node>();

		// retain only edge moves if they exist
		for (int j = 0; j < moves.size(); j++) {
			int[] move = moves.get(j).getMove();
			if (move[0] == 0 || move[0] == 6) { // row 0 and row 6
				if (move[1] % 2 == 0) {
					if (move[1] == 0 || move[1] == 8) {
						corners.add(moves.get(j));
					} else {
						edges.add(moves.get(j));
					}

				}
			}
		}

		// retain only side moves
		if (corners.size() == 0 && edges.size() == 0) {
			for (int j = 0; j < moves.size(); j++) {
				int[] move = moves.get(j).getMove();
				if (move[1] == 0 || move[1] == 8) { // col 0 and 8
					sides.add(moves.get(j));
				}
			}
		}

		// retain safe moves
		if (sides.size() == 0) {
			for (int j = 0; j < moves.size(); j++) {
				int[] move = moves.get(j).getMove();
				if (move != null) {
					char[][] board = moves.get(j).getBoard();
					if ((move[0] != 1 && move[0] != 5) && move[1] % 2 == 0) { // not row 1 or 5 and even col
						safer.add(moves.get(j));
					} else {
						if (move[0] == 0 && move[1] % 2 != 0) { // odd cols in row 0
							if (board[0][move[1] - 1] != otherPlayer && board[0][move[1] + 1] != otherPlayer) {
								safer.add(moves.get(j));
							}
						}
						if (move[0] == 5 && move[1] % 2 != 0) { // odd cols in row 5
							if (board[6][move[1] - 1] != otherPlayer && board[6][move[1] + 1] != otherPlayer) {
								safer.add(moves.get(j));
							}
						}
					}
				}
			}
		}

		/*
		 * int maxScore = 0; if(safer.size() > 1) { for(int j = 0; j < safer.size();
		 * j++) { int moveMax = countTiles(safer.get(j), teamColor) -
		 * countTiles(safer.get(j), otherPlayer); if(moveMax > maxScore) { maxScore =
		 * moveMax; } }
		 *
		 * for(int j = 0; j < safer.size(); j++) { int moveValue =
		 * countTiles(safer.get(j), teamColor) - countTiles(safer.get(j), otherPlayer);
		 * if(moveValue == maxScore) { best.add(safer.get(j)); } }
		 *
		 * safer = best; }
		 */

		if (edges.size() > 0) {
			// System.out.println("Edges! " + edges.size());
			retained = edges;
		} else if (corners.size() > 0) {
			// System.out.println("Corner! " + corners.size());
			retained = corners;
		} else if (sides.size() > 0) {
			// System.out.println("Sides! " + sides.size());
			retained = sides;
		} // else if(safer.size() > 0) {
			// System.out.println("Safer! " + safer.size());
			// retained = safer;
			// }
		return retained;
	}

	public static ArrayList<Node> filterMoves(ArrayList<Node> p_Moves) {
		ArrayList<Node> retained = new ArrayList<Node>();
		ArrayList<Node> corners = new ArrayList<Node>();
		ArrayList<Node> sides = new ArrayList<Node>();

		// retain only edge moves if they exist
		for (int j = 0; j < p_Moves.size(); j++) {
			int[] moves = p_Moves.get(j).getMove();
			if (moves[0] == 0 || moves[0] == 6) { // row 0 and row 6
				if (moves[1] % 2 == 0) { // odd
					retained.add(p_Moves.get(j));
				}
			}
		}

		// retain only corner moves if they exist among the edges
		if (retained.size() > 1) {
			for (int j = 0; j < retained.size(); j++) {
				int[] moves = retained.get(j).getMove();
				if (moves[0] == 0 || moves[0] == 6) { // row 0 and row 6
					if (moves[1] == 0 || moves[1] == 8) {
						corners.add(p_Moves.get(j));
					}
				}
			}
		}

		if (corners.size() > 0) { // retain corners
			retained = corners;
		} else if (retained.size() == 0) { // return default
			retained = p_Moves;
		}

		return retained;
	}

	public static int computeUtility(Node node, char teamColor, int phase) {
		char board[][] = node.getBoard();
		int move[] = node.getMove();
		int util = 0;

		// tiles in side positions
		int sideWeight = 5;
		for (int r = 0; r < 7; r++) {
			if (board[r][0] == teamColor) {
				util += sideWeight;
			}
			if (board[r][8] == teamColor) {
				util += sideWeight;
			}
		}

		// unflankable tiles
		int cornerScore = 25;
		int edgeScore = 15;
		for (int c = 0; c < 9; c++) {
			if (c % 2 == 0) {
				if (board[0][c] == teamColor) {
					if (c == 0 || c == 8) {
						util += cornerScore; // corner
					} else {
						util += edgeScore; // edge
					}
				}
				if (board[6][c] == teamColor) {
					if (c == 0 || c == 8) {
						util += cornerScore; // corner
					} else {
						util += edgeScore; // edge
					}
				}
			}
		}

		// score difference
		int bot = 0;
		int opp = 0;
		char oppColor = reversePlayer(teamColor);
		for (int r = 0; r < 7; r++) {
			for (int c = 0; c < 9; c++) {
				if (board[r][c] == teamColor)
					bot++;
				if (board[r][c] == oppColor)
					opp++;
			}
		}

		// low importance of score in early game
		util += (phase < 20) ? (bot - opp) / 2 : (bot - opp);

		return util;
	}

	// computes utility depending on type
	public static int getMove(Node state, char teamColor, int limit, ArrayList<Node> possibleMoves, char type) {
		// long start = System.nanoTime();
		int phase = countOccupied(state);
		Stack<Node> frontier = new Stack<Node>();
		ArrayList<Node> movesTemp = possibleMoves;

		// do filtering
		if (type == '2') {
			possibleMoves = filterMoves(possibleMoves);
			// if only 1 is remaining after filtering
			if (possibleMoves.size() == 1) {
				for (int i = 0; i < movesTemp.size(); i++) {
					if (movesTemp.get(i).isMoveEqual(possibleMoves.get(0))) {
						return i + 1;
					}
				}
			}
		} else if (type == '4') {
			possibleMoves = godFilter(possibleMoves, teamColor, phase);
			if (possibleMoves.size() == 1) {
				for (int i = 0; i < movesTemp.size(); i++) {
					if (movesTemp.get(i).isMoveEqual(possibleMoves.get(0))) {
						return i + 1;
					}
				}
			}

			// if(phase >= 25) {
			// limit += 5;
			// }

		}

		for (int i = 0; i < possibleMoves.size(); i++) {
			frontier.push(possibleMoves.get(i));
		}

		// init. default
		double maxUtility = -9999.0;
		int move = 1;
		Node maxNode = possibleMoves.get(0);

		do {
			Node node = frontier.pop();
			if (node.getDepth() <= limit) {
				ArrayList<ArrayList<Integer>> coordinates = findPlayerTiles(node.getBoard(), node.getPlayerColor());
				char otherPlayerColor = reversePlayer(node.getPlayerColor());
				ArrayList<Node> successors = generateSuccessors(coordinates, node, otherPlayerColor, teamColor);
				for (int i = 0; i < successors.size(); i++) {
					frontier.push(successors.get(i));
				}
				if ((node.getHexed() && node.getParent().getHexed()) || node.getDepth() == limit) {
					if (type == '1') {
						node.setUtility(priority(node, teamColor, phase));
					} else if (type == '2') {
						node.setUtility(computeUtility(node, teamColor, phase));
					} else if (type == '3') {
						node.setUtility(computeTiles(node.getBoard(), teamColor));
					} else if (type == '4') {
						node.setUtility(godMode(node, teamColor, phase));
					}

					while (node.getParent() != state) {
						if (node.getParent().getPlayerColor() == teamColor) {
							if (node.getParent().getUtility() < node.getUtility())
								node.getParent().setUtility(node.getUtility());
						} else {
							if (node.getParent().getUtility() > node.getUtility())
								node.getParent().setUtility(node.getUtility());
						}
						node = node.getParent();
						if (node.getParent() == state) {
							maxUtility = Math.max(maxUtility, node.getUtility());
							if (maxUtility == node.getUtility()) {
								maxNode = node;
								// System.out.println("Max : " + maxNode.getUtility() );
							}
						}
					} // end of while loop
				}
			}
		} while (!frontier.isEmpty());

		for (int i = 0; i < possibleMoves.size(); i++) {
			if (possibleMoves.get(i).isMoveEqual(maxNode)) {
				move = i + 1;
				break;
			}
		}
		// long end = System.nanoTime();
		// System.out.println("Took: " + ((end - start) / 1000000000.0) + " seconds");
		return move;
	}

	public static int rand(int min, int max) {
		return min + (int) (Math.random() * ((max - min) + 1));
	}

	public static int enterMove(int max) {
		int input = -1;
		do {
			try {
				System.out.print("Enter the number of move: ");
				input = Integer.parseInt(kbd.nextLine());
				if (input < 1 || input > max) {
					System.out.println("Invalid move!");
					input = -1;
				}
			} catch (Exception e) {
				System.out.println("Invalid Input!");
			}
		} while (input == -1);
		return input;
	}

	public static char reversePlayer(char p) {
		return (p == 'R') ? 'G' : 'R';
	}

	public static int countOccupied(Node node) {
		int count = 0;
		for (int r = 0; r < 7; r++) {
			for (int c = 0; c < 9; c++) {
				if (node.getBoard()[r][c] == 'R' || node.getBoard()[r][c] == 'G') {
					count++;
				}
			}
		}
		return count;
	}

	public static int countTiles(Node node, char color) {
		int count = 0;
		for (int r = 0; r < 7; r++) {
			for (int c = 0; c < 9; c++) {
				if (node.getBoard()[r][c] == color) {
					count++;
				}
			}
		}
		return count;
	}

	public static int computeTiles(char[][] board, char teamColor) {
		int botTiles = 0;
		int opponentTiles = 0;
		char opponentColor = reversePlayer(teamColor);
		for (int r = 0; r < 7; r++) {
			for (int c = 0; c < 9; c++) {
				if (board[r][c] == teamColor)
					botTiles++;
				if (board[r][c] == opponentColor)
					opponentTiles++;
			}
		}
		return botTiles - opponentTiles;
	}

	public static int priority(Node node, char teamColor, int phase) {
		char board[][] = node.getBoard();
		int move[] = node.getMove();
		int util = 0;

		// score difference
		int bot = 0;
		int opp = 0;
		char oppColor = reversePlayer(teamColor);
		for (int r = 0; r < 7; r++) {
			for (int c = 0; c < 9; c++) {
				if (board[r][c] == teamColor)
					bot++;
				if (board[r][c] == oppColor)
					opp++;
			}
		}
		// low priority for scoring in early game
		util += (phase < 25) ? (bot - opp) / 3 : (bot - opp) / 2;

		// count side tiles
		HashMap<String, Boolean> sides = new HashMap<String, Boolean>();
		for (int r = 0; r < 7; r++) {
			for (int c = 0; c < 9; c++) {
				if (board[r][c] == teamColor && (r == 0 || r == 6 || c == 0 || c == 8)) {
					if (sides.get(r + "-" + c) == null) {
						sides.put(r + "-" + c, true);
					}
				}
			}
		}
		if (sides.size() > 0) {
			util += sides.size();
		}

		// prioritize corner move in early-to-mid game
		int cornerExtra = (phase < 30) ? 50 : 0;
		if (move != null && cornerExtra > 0) {
			if (((move[0] == 6 && move[1] == 0) || (move[0] == 0 && move[1] == 0) || (move[0] == 6 && move[1] == 8)
					|| (move[0] == 0 && move[1] == 8))) {
				util += cornerExtra;
			}
		}

		// give extra weight according to move stability during early game
		int stabilityExtra = (phase < 25) ? 10 : 0;
		if (move != null && stabilityExtra > 0) {
			if (move[0] == 0 || move[0] == 6) {
				util += stabilityExtra;
			}
			if (move[1] == 0 || move[1] == 8) {
				util += stabilityExtra;
			}
		}
		return util;
	}

	// creates initial board
	public static char[][] plotInitial(int col, int row, char color) {
		char otherColor = (color == 'R') ? 'G' : 'R';
		char board[][] = new char[7][9];
		board[6][1] = 'X'; // invalid location *uneven kasi yung number ng rows per column
		board[6][3] = 'X'; // invalid location
		board[6][5] = 'X'; // invalid location
		board[6][7] = 'X'; // invalid location

		// if the column coordinate of the top-most tile of the initial set of tiles is
		// odd
		if (col % 2 != 0) {
			board[row][col] = color;
			board[row][col - 1] = otherColor;
			board[row][col + 1] = otherColor;
			board[row - 1][col - 1] = color;
			board[row - 1][col + 1] = color;
			board[row - 2][col] = otherColor;
		}
		// if the column coordinate of the top-most tile of the initial set of tiles is
		// even
		else {
			board[row][col] = color;
			board[row - 1][col - 1] = otherColor;
			board[row - 1][col + 1] = otherColor;
			board[row - 2][col - 1] = color;
			board[row - 2][col + 1] = color;
			board[row - 2][col] = otherColor;
		}

		return board;
	}

	public static void printBoard(char[][] board) {
		for (int row = 6; row >= 0; row--) {
			for (int col = 0; col < 9; col++) {
				System.out.print("[" + board[row][col] + "] ");
			}
			System.out.println("");
		}
	}

	// determines the location of the current player's tiles
	public static ArrayList<ArrayList<Integer>> findPlayerTiles(char[][] board, char playerColor) {
		ArrayList<ArrayList<Integer>> coordinates = new ArrayList<ArrayList<Integer>>();
		int x = 0;
		for (int row = 0; row < 7; row++) {
			for (int col = 0; col < 9; col++) {
				if (board[row][col] == playerColor) {
					ArrayList<Integer> index = new ArrayList<Integer>();
					index.add(0, row);
					index.add(1, col);
					coordinates.add(x, index);
					x++;
				}
			}
		}
		return coordinates;
	}

	// creates a copy of a board
	public static char[][] createBoard(char[][] board) {
		char[][] newBoard = new char[7][9];
		for (int row = 0; row < 7; row++) {
			for (int col = 0; col < 9; col++) {
				newBoard[row][col] = board[row][col];
			}
		}
		return newBoard;
	}

	// generates the successors of the current node
	// determines possible moves
	public static ArrayList<Node> generateSuccessors(ArrayList<ArrayList<Integer>> coordinates, Node node,
			char otherPlayerColor, char teamColor) {
		int moveCount = 0;
		int utility = (teamColor == node.getPlayerColor()) ? 100 : -100;
		ArrayList<Node> successors = new ArrayList<Node>();
		// determines possible moves basing on the locations of the current player's
		// tiles
		for (int i = 0; i < coordinates.size(); i++) {
			int row = coordinates.get(i).get(0); // row-coordinate of one of the tiles of the current player
			int col = coordinates.get(i).get(1); // column-coordinate of one of the tiles of the current player
			boolean flag; // used to determine if the loop should end
			int tempCol;
			int tempRow;
			char[][] successorBoard = new char[7][9];

			// North Direction
			tempRow = row;
			flag = false;
			successorBoard = createBoard(node.getBoard());
			while (tempRow + 1 <= 6 && flag == false) {
				if (successorBoard[tempRow + 1][col] == otherPlayerColor) {
					successorBoard[tempRow + 1][col] = node.getPlayerColor();
					if (tempRow + 2 <= 6) {
						if (successorBoard[tempRow + 2][col] == 0) {
							int[] index = { tempRow + 2, col };
							successorBoard[tempRow + 2][col] = node.getPlayerColor();
							successors.add(new Node(utility, otherPlayerColor, node, successorBoard, index, false));
							flag = true;
							moveCount++;
						}
					} else
						flag = true;
				} else
					flag = true;
				tempRow++;
			}

			// South Direction
			tempRow = row;
			flag = false;
			successorBoard = createBoard(node.getBoard());
			while (tempRow - 1 >= 0 && flag == false) {
				if (successorBoard[tempRow - 1][col] == otherPlayerColor) {
					successorBoard[tempRow - 1][col] = node.getPlayerColor();
					if (tempRow - 2 >= 0) {
						if (successorBoard[tempRow - 2][col] == 0) {
							int[] index = { tempRow - 2, col };
							successorBoard[tempRow - 2][col] = node.getPlayerColor();
							successors.add(new Node(utility, otherPlayerColor, node, successorBoard, index, false));
							flag = true;
							moveCount++;
						}
					} else
						flag = true;
				} else
					flag = true;
				tempRow--;
			}

			// North-East Direction
			tempCol = col;
			tempRow = row;
			flag = false;
			successorBoard = createBoard(node.getBoard());
			while (tempCol + 1 <= 8 && tempRow <= 6 && flag == false) {
				if (tempCol % 2 == 0) {
					if (successorBoard[tempRow][tempCol + 1] == otherPlayerColor) {
						successorBoard[tempRow][tempCol + 1] = node.getPlayerColor();
						if (tempCol + 2 <= 8 && tempRow + 1 <= 6) {
							if (successorBoard[tempRow + 1][tempCol + 2] == 0) {
								int[] index = { tempRow + 1, tempCol + 2 };
								successorBoard[tempRow + 1][tempCol + 2] = node.getPlayerColor();
								successors.add(new Node(utility, otherPlayerColor, node, successorBoard, index, false));
								flag = true;
								moveCount++;
							}
						} else
							flag = true;
					} else
						flag = true;
					tempCol++;
				} else {
					if (successorBoard[tempRow + 1][tempCol + 1] == otherPlayerColor) {
						successorBoard[tempRow + 1][tempCol + 1] = node.getPlayerColor();
						if (tempCol + 2 <= 8 && tempRow + 1 <= 6) {
							if (successorBoard[tempRow + 1][tempCol + 2] == 0) {
								int[] index = { tempRow + 1, tempCol + 2 };
								successorBoard[tempRow + 1][tempCol + 2] = node.getPlayerColor();
								successors.add(new Node(utility, otherPlayerColor, node, successorBoard, index, false));
								flag = true;
								moveCount++;
							}
						} else
							flag = true;
					} else
						flag = true;
					tempCol++;
					tempRow++;
				}
			}

			// North-West Direction
			tempCol = col;
			tempRow = row;
			flag = false;
			successorBoard = createBoard(node.getBoard());
			while (tempCol - 1 >= 0 && tempRow <= 6 && flag == false) {
				if (tempCol % 2 == 0) {
					if (successorBoard[tempRow][tempCol - 1] == otherPlayerColor) {
						successorBoard[tempRow][tempCol - 1] = node.getPlayerColor();
						if (tempCol - 2 >= 0 && tempRow + 1 <= 6) {
							if (successorBoard[tempRow + 1][tempCol - 2] == 0) {
								int[] index = { tempRow + 1, tempCol - 2 };
								successorBoard[tempRow + 1][tempCol - 2] = node.getPlayerColor();
								successors.add(new Node(utility, otherPlayerColor, node, successorBoard, index, false));
								flag = true;
								moveCount++;
							}
						} else
							flag = true;
					} else
						flag = true;
					tempCol--;
				} else {
					if (successorBoard[tempRow + 1][tempCol - 1] == otherPlayerColor) {
						successorBoard[tempRow + 1][tempCol - 1] = node.getPlayerColor();
						if (tempCol - 2 >= 0 && tempRow + 1 <= 6) {
							if (successorBoard[tempRow + 1][tempCol - 2] == 0) {
								int[] index = { tempRow + 1, tempCol - 2 };
								successorBoard[tempRow + 1][tempCol - 2] = node.getPlayerColor();
								successors.add(new Node(utility, otherPlayerColor, node, successorBoard, index, false));
								flag = true;
								moveCount++;
							}
						} else
							flag = true;
					} else
						flag = true;
					tempCol--;
					tempRow++;
				}
			}

			// South-East Direction
			tempCol = col;
			tempRow = row;
			flag = false;
			successorBoard = createBoard(node.getBoard());
			while (tempCol + 1 <= 8 && tempRow - 1 >= 0 && flag == false) {
				if (tempCol % 2 == 0) {
					if (successorBoard[tempRow - 1][tempCol + 1] == otherPlayerColor) {
						successorBoard[tempRow - 1][tempCol + 1] = node.getPlayerColor();
						if (tempCol + 2 <= 8 && tempRow - 1 >= 0) {
							if (successorBoard[tempRow - 1][tempCol + 2] == 0) {
								int[] index = { tempRow - 1, tempCol + 2 };
								successorBoard[tempRow - 1][tempCol + 2] = node.getPlayerColor();
								successors.add(new Node(utility, otherPlayerColor, node, successorBoard, index, false));
								flag = true;
								moveCount++;
							}
						} else
							flag = true;
					} else
						flag = true;
					tempCol++;
					tempRow--;
				} else {
					if (successorBoard[tempRow][tempCol + 1] == otherPlayerColor) {
						successorBoard[tempRow][tempCol + 1] = node.getPlayerColor();
						if (tempCol + 2 <= 8 && tempRow - 1 >= 0) {
							if (successorBoard[tempRow - 1][tempCol + 2] == 0) {
								int[] index = { tempRow - 1, tempCol + 2 };
								successorBoard[tempRow - 1][tempCol + 2] = node.getPlayerColor();
								successors.add(new Node(utility, otherPlayerColor, node, successorBoard, index, false));
								flag = true;
								moveCount++;
							}
						} else
							flag = true;
					} else
						flag = true;
					tempCol++;
				}
			}

			// South-West Direction
			tempCol = col;
			tempRow = row;
			flag = false;
			successorBoard = createBoard(node.getBoard());
			while (tempCol - 1 >= 0 && tempRow - 1 >= 0 && flag == false) {
				if (tempCol % 2 == 0) {
					if (successorBoard[tempRow - 1][tempCol - 1] == otherPlayerColor) {
						successorBoard[tempRow - 1][tempCol - 1] = node.getPlayerColor();
						if (tempCol - 2 >= 0 && tempRow - 1 >= 0) {
							if (successorBoard[tempRow - 1][tempCol - 2] == 0) {
								int[] index = { tempRow - 1, tempCol - 2 };
								successorBoard[tempRow - 1][tempCol - 2] = node.getPlayerColor();
								successors.add(new Node(0, otherPlayerColor, node, successorBoard, index, false));
								flag = true;
								moveCount++;
							}
						} else
							flag = true;
					} else
						flag = true;
					tempCol--;
					tempRow--;
				} else {
					if (successorBoard[tempRow][tempCol - 1] == otherPlayerColor) {
						successorBoard[tempRow][tempCol - 1] = node.getPlayerColor();
						if (tempCol - 2 >= 0 && tempRow + 1 <= 6) {
							if (successorBoard[tempRow - 1][tempCol - 2] == 0) {
								int[] index = { tempRow - 1, tempCol - 2 };
								successorBoard[tempRow - 1][tempCol - 2] = node.getPlayerColor();
								successors.add(new Node(utility, otherPlayerColor, node, successorBoard, index, false));
								flag = true;
								moveCount++;
							}
						} else
							flag = true;
					} else
						flag = true;
					tempCol--;
				}
			}
		} // end of for loop

		if (successors.isEmpty()) {
			if (node.getHexed() && node.getParent().getHexed())
				;
			else
				successors.add(new Node(utility, otherPlayerColor, node, node.getBoard(), null, true));
		} else {
			for (int i = 0; i < successors.size() - 1; i++) {
				for (int nexti = i + 1; nexti < successors.size(); nexti++) {
					if (successors.get(i).isMoveEqual(successors.get(nexti))) {
						successors.get(i).mergeBoard(successors.get(nexti));
						successors.remove(nexti);
					}
				}
			}
		}
		// Collections.sort(successors);
		return successors;

	}// end of generateTree method
}

class Node {
	private double utilityFunction;
	private char playerColor;
	private Node parent;
	private char[][] board;
	private int[] move;
	private boolean hexed;
	private int depth;

	public Node() {
		this.utilityFunction = 0.0;
		this.playerColor = ' ';
		this.parent = null;
		this.board = null;
		this.move = null;
		this.hexed = false;
		this.depth = 0;
	}

	public Node(double utilityFunction, char playerColor, Node parent, char[][] board, int[] coordinates,
			boolean hexed) {
		this.utilityFunction = utilityFunction;
		this.playerColor = playerColor;
		this.parent = parent;
		this.board = board;
		this.move = coordinates;
		this.hexed = hexed;
		this.depth = (this.parent != null) ? this.parent.depth + 1 : 0;
	}

	public double getUtility() {
		return this.utilityFunction;
	}

	public char getPlayerColor() {
		return this.playerColor;
	}

	public Node getParent() {
		return this.parent;
	}

	public char[][] getBoard() {
		return this.board;
	}

	public int[] getMove() {
		return this.move;
	}

	public boolean getHexed() {
		return this.hexed;
	}

	public int getDepth() {
		return this.depth;
	}

	public void setUtility(double utilityFunction) {
		this.utilityFunction = utilityFunction;
	}

	public void setPlayerColor(char color) {
		this.playerColor = color;
	}

	public void setParent(Node Parent) {
		this.parent = parent;
	}

	public void setBoard(char[][] board) {
		this.board = board;
	}

	public void setMove(int[] coordinates) {
		this.move = coordinates;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public void showBoard() {
		String first = "";
		String second = "";
		for (int row = 6; row >= 0; row--) {
			for (int col = 0; col < 9; col++) {
				if (col % 2 != 0 && row == 6)
					;
				else if (col % 2 == 0)
					first = first + "[" + this.board[row][col] + "]     ";
				else if (col % 2 != 0)
					second = second + "    [" + this.board[row][col] + "] ";
			}
			System.out.println(second);
			System.out.println(first);
			first = "";
			second = "";
		}
	}

	public boolean isMoveEqual(Node otherNode) {
		return (this.move[0] == otherNode.getMove()[0] && this.move[1] == otherNode.getMove()[1]) ? true : false;
	}

	public void mergeBoard(Node otherNode) {
		for (int row = 0; row < 7; row++) {
			for (int col = 0; col < 9; col++) {
				if (otherNode.getBoard()[row][col] != 0 && this.board[row][col] != otherNode.getBoard()[row][col]) {
					if (otherNode.board[row][col] == this.parent.playerColor)
						this.board[row][col] = otherNode.board[row][col];
				}
			}
		}
	}
}