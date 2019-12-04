import java.util.*;

class State implements Comparable<State> {
	private int[][] grid;
	private State parent;
	private int heuristicCost;
	private int totalCost;

	public State() {
		this.grid = new int[3][3];
		this.parent = null;
		this.heuristicCost = 0;
		this.totalCost = 0;
	}

	public State(int[][] grid, State parent, int heuristicCost) {
		this.grid = grid;
		this.parent = parent;
		this.heuristicCost = heuristicCost;
		this.totalCost = (this.parent != null) ? parent.totalCost + 1 : 0;
	}

	public int[][] getGrid() {
		return this.grid;
	}

	public State getParent() {
		return this.parent;
	}

	public int getHeuristicCost() {
		return this.heuristicCost;
	}

	public int getTotalCost() {
		return this.totalCost;
	}

	public void setGrid(int[][] grid) {
		this.grid = grid;
	}

	public void setParent(State parent) {
		this.parent = parent;
	}

	public void setHeuristicCost(int heuristicCost) {
		this.heuristicCost = heuristicCost;
	}

	public void setTotalCost(int totalCost) {
		this.totalCost = totalCost;
	}

	public boolean isExplored(State explored) {
		int count = 0;
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				if (this.grid[y][x] == explored.getGrid()[y][x])
					count++;
			}
		}
		return (count == 9) ? true : false;
	}

	public int compareTo(State s) {
		if (this.heuristicCost + this.totalCost < s.heuristicCost + s.totalCost)
			return -1;
		else if (this.heuristicCost + this.totalCost > s.heuristicCost + s.totalCost)
			return 1;
		else
			return 0;
	}

	public void showGrid() {
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				System.out.print(this.grid[y][x] + " ");
			}
			System.out.println("");
		}
	}
}

public class EightSquare {
	public static void main(String[] args) {
		int[][] goal = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 0 } };
		int[][] initial = { { 2, 3, 6 }, { 4, 8, 5 }, { 1, 7, 0 } }; // can be randomized

		PriorityQueue<State> frontier = new PriorityQueue<State>();
		ArrayList<State> explored = new ArrayList<State>();
		frontier.add(new State(initial, null, computeManhattanDistance(goal, initial)));
		do {
			if (frontier.isEmpty()) {
				System.out.println("No Solution");
				System.exit(0);
			}

			State node = frontier.poll();
			explored.add(node);
			// Goal Test
			if (node.getHeuristicCost() == 0) {
				System.out.println("Number of moves: " + node.getTotalCost());
				while (node.getParent() != null) {
					node.showGrid();
					System.out.println("-------------------------");
					node = node.getParent();
				}
				System.exit(0);
			}

			else {
				int[] emptyTileIndex = locateEmptyTile(node.getGrid());
				State child = new State();
				int row = emptyTileIndex[0];
				int col = emptyTileIndex[1];
				boolean flag;
				int[][] grid = new int[3][3];

				if (row - 1 >= 0) {
					flag = false;
					grid = createGrid(node.getGrid());
					grid[row][col] = grid[row - 1][col];
					grid[row - 1][col] = 0;
					child = createSuccessor(node, grid, goal);
					for (int i = 0; i < explored.size() && flag == false; i++) {
						flag = child.isExplored(explored.get(i));
					}
					if (flag == false)
						frontier.add(child);
				}
				if (row + 1 < 3) {
					flag = false;
					grid = createGrid(node.getGrid());
					grid[row][col] = grid[row + 1][col];
					grid[row + 1][col] = 0;
					child = createSuccessor(node, grid, goal);
					for (int i = 0; i < explored.size() && flag == false; i++) {
						flag = child.isExplored(explored.get(i));
					}
					if (flag == false)
						frontier.add(child);
				}
				if (col - 1 >= 0) {
					flag = false;
					grid = createGrid(node.getGrid());
					grid[row][col] = grid[row][col - 1];
					grid[row][col - 1] = 0;
					child = createSuccessor(node, grid, goal);
					for (int i = 0; i < explored.size() && flag == false; i++) {
						flag = child.isExplored(explored.get(i));
					}
					if (flag == false)
						frontier.add(child);
				}
				if (col + 1 < 3) {
					flag = false;
					grid = createGrid(node.getGrid());
					grid[row][col] = grid[row][col + 1];
					grid[row][col + 1] = 0;
					child = createSuccessor(node, grid, goal);
					for (int i = 0; i < explored.size() && flag == false; i++) {
						flag = child.isExplored(explored.get(i));
					}
					if (flag == false)
						frontier.add(child);
				}
			}

		} while (!frontier.isEmpty());// end of while

	}// end of main method

	public static int computeManhattanDistance(int[][] goal, int[][] current) {
		int distance = 0;
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				int[] tileIndex = locateTile(current, goal[y][x]);
				distance += Math.abs(y - tileIndex[0]) + Math.abs(x - tileIndex[1]);
			}
		}
		return distance;
	}

	public static int[] locateTile(int[][] grid, int tile) {
		int[] index = new int[2];
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				if (grid[y][x] == tile) {
					index[0] = y;
					index[1] = x;
				}
			}
		}
		return index;
	}

	public static int[] locateEmptyTile(int[][] grid) {
		int[] index = new int[2];
		boolean flag = false;
		for (int row = 0; row < 3 && flag == false; row++) {
			for (int col = 0; col < 3 && flag == false; col++) {
				if (grid[row][col] == 0) {
					index[0] = row;
					index[1] = col;
					flag = true;
				}
			}
		}
		return index;
	}

	public static State createSuccessor(State node, int[][] grid, int[][] goal) {
		int heuristicCost = computeManhattanDistance(goal, grid);
		State childNode = new State(grid, node, heuristicCost);
		return childNode;
	}

	public static int[][] createGrid(int[][] grid) {
		int[][] newGrid = new int[3][3];
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				newGrid[y][x] = grid[y][x];
			}
		}
		return newGrid;
	}
}// end of class