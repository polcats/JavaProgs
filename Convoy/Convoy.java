import java.io.*;
import java.util.*;

/*
 *Vehicle class with weight, time and index attribute
 */
class Vehicle {
	int weight;
	int time;
	int index;

	public Vehicle(int weight, int time, int index) {
		this.weight = weight;
		this.time = time;
		this.index = index;
	}

	public int getWeight() {
		return weight;
	}

	public int getTime() {
		return time;
	}

	public int getIndex() {
		return index;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}

/*
 * State representation
 */
class State implements Comparable<State> {
	ArrayList<Vehicle> vehicles;
	State parent;
	char side;
	int maxTime;
	int endTime;
	int crossedVehiclesCount;

	public State() {
		this.vehicles = null;
		this.parent = null;
		this.side = ' ';
		this.maxTime = 0;
		this.endTime = 0;
		this.crossedVehiclesCount = 0;
	}

	public State(ArrayList<Vehicle> vehicles, State parent, char side) {
		this.vehicles = vehicles;
		this.parent = parent;
		this.maxTime = 0;
		this.endTime = 0;
		this.crossedVehiclesCount = 0;
		if (this.vehicles != null) {
			for (Vehicle v : this.vehicles) {
				// The time it takes for the vehicles to cross the bridge (slowest vehicle's
				// time)
				this.maxTime = Math.max(this.maxTime, v.time);
			}
			// the actual time the vehicle(s) have crossed the bridge after other vehicle(s)
			// have crossed the bridge
			this.endTime = this.maxTime + this.parent.endTime;
			this.crossedVehiclesCount = this.vehicles.size() + parent.crossedVehiclesCount;
		}
		this.side = side;
	}

	public ArrayList getVehicles() {
		return vehicles;
	}

	public State getParent() {
		return parent;
	}

	public int getMaxTime() {
		return maxTime;
	}

	public char getSide() {
		return side;
	}

	public void setVehicles(ArrayList<Vehicle> vehicles) {
		this.vehicles = vehicles;
	}

	public void setParent(State parent) {
		this.parent = parent;
	}

	public void setMaxTime(int maxTime) {
		this.maxTime = maxTime;
	}

	public void setSide(char side) {
		this.side = side;
	}

	public int compareTo(State state) {
		if (this.endTime > state.endTime) {
			return 1;
		} else if (this.endTime < state.endTime) {
			return -1;
		} else
			return 0;
	}

	// gets the index of the vehicle last added in the array.
	public int getLastVehicleIndex() {
		int max = 0;
		for (Vehicle v : this.vehicles) {
			max = Math.max(v.index, max);
		}
		return max;
	}

	public String toString(Vehicle v) {
		return this.side + " " + v.weight + " " + v.time + " " + this.maxTime + " " + this.endTime;
	}
}

public class Convoy {

	public static void main(String[] args) {

		int maxLoad = 0;
		ArrayList<Vehicle> eastSide = new ArrayList<Vehicle>();
		ArrayList<Vehicle> westSide = new ArrayList<Vehicle>();

		// getting input data from the file
		try {
			Scanner file = new Scanner(new File("convoy.txt"));

			String[] firstLineValues = file.nextLine().split(" ");
			maxLoad = Integer.parseInt(firstLineValues[0]);
			int eastSideCount = Integer.parseInt(firstLineValues[1]);
			int westSideCount = Integer.parseInt(firstLineValues[2]);
			int index;
			String[] lineValues;

			for (index = 0; file.hasNext() && index < eastSideCount; index++) {
				lineValues = file.nextLine().split(" ");
				eastSide.add(new Vehicle(Integer.parseInt(lineValues[0]), Integer.parseInt(lineValues[1]), index));
			}

			for (index = 0; file.hasNext() && index < westSideCount; index++) {
				lineValues = file.nextLine().split(" ");
				westSide.add(new Vehicle(Integer.parseInt(lineValues[0]), Integer.parseInt(lineValues[1]), index));
			}
			file.close();

		} catch (FileNotFoundException x) {
			System.out.println("File Not Found");
			System.exit(0);
		}

		System.out.println("Maximum Load: " + maxLoad);
		System.out.println("--------------------------------------");

		// prints east side vehicles data (weight and time)
		for (Vehicle v : eastSide) {
			System.out.println(v.weight + " " + v.time + " ");
		}

		System.out.println("--------------------------------------");

		// prints west side vehicles data (weight and time)
		for (Vehicle v : westSide) {
			System.out.println(v.weight + " " + v.time + " ");
		}

		System.out.println("--------------------------------------");
		System.out.println("--------------------------------------");

		PriorityQueue<State> frontier = new PriorityQueue<State>();

		// initial states
		frontier.add(new State(null, null, 'E'));
		frontier.add(new State(null, null, 'W'));

		// Collections of goal nodes
		PriorityQueue<State> goalNodes = new PriorityQueue<State>();
		while (!frontier.isEmpty()) {

			// prints "No Solution" if the frontier is already empty and no goal node has
			// been reached
			if (frontier.isEmpty() && goalNodes.isEmpty()) {
				System.out.println("No Solution");
			}

			// get the first element in the priority queue and assign it to an instance of
			// the State class named node
			State node = frontier.poll();

			// Goal Test - checked if all the vehicles has crossed the bridge by comparing
			// the total number of crossed vehicles and the the total number of input
			// vehicles
			if (node.crossedVehiclesCount == (westSide.size() + eastSide.size())) {
				goalNodes.add(node);
			} else {
				// if goal test is not satisfied, invokes the getChildren method (gets the child
				// successor(s) of the node)
				getChildren(frontier, node, eastSide, westSide, maxLoad);
			}

			// if frontier is empty and Collection goalNodes is not empty,
			// gets the optimal solution from the collection and print the solution.
			if (frontier.isEmpty() && !goalNodes.isEmpty()) {
				System.out.println("Number of possible solutions: " + goalNodes.size());
				System.out.println("Optimal Solution: ");
				State goalNode = goalNodes.poll();
				System.out.println("Total Time:" + goalNode.endTime);
				int count = 0;
				ArrayList<State> solution = new ArrayList<State>();
				while (goalNode.parent != null) {
					solution.add(goalNode);
					goalNode = goalNode.parent;
				}
				for (int i = solution.size() - 1; i >= 0; i--) {
					count++;
					for (Vehicle v : solution.get(i).vehicles) {
						System.out.println(count + " " + solution.get(i).toString(v));
					}
					System.out.println("-------------------------------");
				}
				System.out.println("Number of Crossings: " + count);
			}
		}

	}

	// gets the child nodes of the current node
	public static void getChildren(PriorityQueue<State> frontier, State node, ArrayList<Vehicle> eastSide,
			ArrayList<Vehicle> westSide, int maxLoad) {
		int i; // index of the vehicle in the array(either east or west) that will be added as
				// child node
		char side; // determines if the vehicle(s) came from the east or west side

		// checks if the parent of the node is null
		// if yes, the first vehicle is to be added in the frontier
		// as the child of the initial node having the same side attribute
		if (node.parent == null) {
			i = 0;
			side = node.side;
		}
		// checks if the parent of the node is the child of the rootnode
		// if yes, the first vehicle from the opposite side will be added
		else if (node.parent.parent == null) {
			i = 0;
			side = (node.side == 'E') ? 'W' : 'E';
		}
		// if node is below the the second level in the tree
		else {
			// checks if the node is from the east and
			// the parent node is also from the east or the last vehicle on the opposite has
			// crossed the bridge
			// then vehicles from the opposite side has all crossed the bridge
			// therefore the next vehicle(s) will come from the east
			if (node.side == 'E'
					&& (node.parent.side == 'E' || node.parent.getLastVehicleIndex() == westSide.size() - 1)) {
				i = node.getLastVehicleIndex() + 1;
				side = 'E';
			}

			// checks if the is from the west
			// and the parent node is also from the west or the last vehicle on the opposite
			// has crossed the bridge
			// then vehicles from the opposite side has all crossed the bridge
			// therefore the next vehicles will come from the east
			else if (node.side == 'W'
					&& (node.parent.side == 'W' || node.parent.getLastVehicleIndex() == eastSide.size() - 1)) {
				i = node.getLastVehicleIndex() + 1;
				side = 'W';
			}
			// if both sides has still queued vehicles
			// then get the next vehicle(s) from the opposite side
			else {
				side = (node.side == 'E') ? 'W' : 'E';
				i = node.parent.getLastVehicleIndex() + 1;
			}
		}

		ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();

		// determines from what side of the bridge will the vehicles turn to cross
		// sets the value of the array to be utilized
		vehicles = (side == 'E') ? eastSide : westSide;

		int totalWeight = 0;
		ArrayList<Vehicle> childVehicles = new ArrayList<Vehicle>();

		// continually adding child nodes in the frontier
		// until the total weight of the vehicles has exceeded the maximum load of the
		// bridge
		while (totalWeight <= maxLoad && i < vehicles.size()) {
			totalWeight += vehicles.get(i).weight;
			if (totalWeight <= maxLoad) {
				childVehicles.add(vehicles.get(i));
				frontier.add(new State(childVehicles, node, side));
			}
			i++;
		}
	}
}