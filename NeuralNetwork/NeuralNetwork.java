import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

public class NeuralNetwork {
	public static void main(String[] args) {

		// loading config | basically a trained network
		Model MLP = new Model();
		MLP.loadNetwork("config.txt");

		/*
		 * // uncomment for training
		 *
		 * Model MLP = new Model(0.15, 10, 2, 3); MLP.init();
		 *
		 * //load training data Scanner data; ArrayList<String> loaddataset = new
		 * ArrayList<String>(); int index = 0; try { data = new Scanner(new
		 * File("trainingdata.txt")); data.nextLine(); while(data.hasNext()) {
		 * loaddataset.add(data.nextLine()); } } catch(Exception e) {
		 * System.out.println(e); }
		 *
		 * int inputSet[][] = new int[loaddataset.size()][MLP.inputNodes]; //int
		 * outputSet[][] = new int[loaddataset.size()][MLP.outputNodes]; for(int d = 0;
		 * d < loaddataset.size(); d++) { String[] inputPairs =
		 * loaddataset.get(d).split(" "); String[] inputs = inputPairs[0].split("");
		 * String[] outputs = inputPairs[1].split(""); //System.out.println(inputs[9]);
		 * for(int i = 0; i < inputs.length; i++) { inputSet[d][i] =
		 * Integer.parseInt(inputs[i]); }
		 *
		 * for(int o = 0; o < outputs.length; o++) { outputSet[d][o] =
		 * Integer.parseInt(outputs[o]); } }
		 *
		 * MLP.train(inputSet[0], outputSet[0]); // initialize first values; //train int
		 * count = 1; double error = Math.abs(MLP.erSum/2); System.out.println(error);
		 * while(error > 0.00005) { count++; for(int t = 0; t < inputSet.length; t++) {
		 * MLP.train(inputSet[t], outputSet[t]); } error = Math.abs(MLP.erSum/2);
		 * System.out.println(error); if(count == 500) { break; } } MLP.saveNetwork();
		 * MLP.showData();
		 */

		System.out.println(MLP.classify(new int[] { 0, 0, 0, 0, 0, 0, 0, 1, 1, 1 }));
		System.out.println(MLP.classify(new int[] { 0, 0, 1, 0, 0, 1, 1, 1, 1, 1 }));
		System.out.println(MLP.classify(new int[] { 0, 1, 0, 0, 0, 1, 1, 1, 1, 1 }));
		System.out.println(MLP.classify(new int[] { 0, 1, 0, 0, 0, 1, 1, 1, 1, 1 }));
		System.out.println(MLP.classify(new int[] { 0, 1, 0, 0, 0, 1, 0, 1, 1, 1 }));
		System.out.println(MLP.classify(new int[] { 0, 1, 0, 0, 0, 1, 0, 1, 0, 1 }));
		System.out.println(MLP.classify(new int[] { 0, 1, 0, 0, 0, 1, 1, 0, 1, 0 }));
		System.out.println(MLP.classify(new int[] { 0, 0, 1, 0, 0, 1, 0, 1, 0, 1 }));
		System.out.println(MLP.classify(new int[] { 0, 0, 1, 0, 0, 1, 1, 1, 0, 1 }));
		System.out.println(MLP.classify(new int[] { 0, 1, 0, 0, 0, 1, 1, 1, 1, 1 }));
		System.out.println(MLP.classify(new int[] { 0, 0, 1, 0, 0, 0, 1, 0, 0, 1 }));
		System.out.println(MLP.classify(new int[] { 0, 0, 1, 0, 0, 1, 0, 0, 0, 1 }));
		System.out.println(MLP.classify(new int[] { 0, 0, 1, 0, 0, 0, 0, 0, 0, 1 }));
		System.out.println(MLP.classify(new int[] { 0, 0, 1, 0, 0, 0, 0, 0, 0, 1 }));
		System.out.println(MLP.classify(new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }));
	}
}

// dynamic model
class Model {
	int inputNodes;
	int hiddenNodes;
	int outputNodes;

	double inputToHiddenWeights[][];
	double hiddenToOutputWeights[][];

	double hiddenLayerSums[];
	double hiddenLayerSigmoids[];
	double hiddenLayerErrorRates[];

	double outputLayerSums[];
	double outputLayerSigmoids[];
	double outputLayerErrorRates[];

	double maxInitWeight = 5.0;
	double e = 2.7182818285; // constant
	double lr; // learning rate
	double erSum;

	public Model() {
	}

	public Model(double lr, int inp, int hid, int out) {
		this.lr = lr;
		this.inputNodes = inp;
		this.hiddenNodes = hid;
		this.outputNodes = out;
		this.inputToHiddenWeights = new double[hid][inp];
		this.hiddenToOutputWeights = new double[out][hid];

		this.hiddenLayerSums = new double[hid];
		this.hiddenLayerSigmoids = new double[hid];
		this.hiddenLayerErrorRates = new double[hid];

		this.outputLayerSums = new double[out];
		this.outputLayerSigmoids = new double[out];
		this.outputLayerErrorRates = new double[out];
	}

	public void setInputToHiddenWeights(double iToH[][]) {
		this.inputToHiddenWeights = iToH;
	}

	public void setHiddenToOutputWeights(double hToO[][]) {
		this.hiddenToOutputWeights = hToO;
	}

	public void train(int inputs[], int outputs[]) { // 1 instance
		// get sum of inputs for each hidden layer node
		for (int h = 0; h < hiddenNodes; h++) {
			double sum = 0;
			for (int i = 0; i < inputNodes; i++) {
				double product = inputs[i] * inputToHiddenWeights[h][i];
				sum += product;
			}
			hiddenLayerSums[h] = sum;
		}

		// sigmoid function each hidden layer sum
		for (int h = 0; h < hiddenNodes; h++) {
			hiddenLayerSigmoids[h] = (hiddenLayerSums[h] >= 0.5) ? 1 : 0;// 1/(1 + (Math.pow(e, -hiddenLayerSums[h])));
		}

		// get sum of inputs for each output layer node
		for (int o = 0; o < outputNodes; o++) {
			double sum = 0;
			for (int h = 0; h < hiddenNodes; h++) {
				double product = hiddenLayerSigmoids[h] * hiddenToOutputWeights[o][h];
				sum += product;
			}
			outputLayerSums[o] = sum;
		}

		// sigmoid function each output layer sum
		for (int o = 0; o < outputNodes; o++) {
			outputLayerSigmoids[o] = (outputLayerSums[o] >= 0.5) ? 1 : 0;// 1/(1 + (Math.pow(e, -outputLayerSums[o])));
		}

		// error rates of output layers
		for (int o = 0; o < outputNodes; o++) {
			outputLayerErrorRates[o] = (outputs[o] - outputLayerSigmoids[o])
					* (Math.pow(e, -outputLayerSums[o]) / Math.pow((1 + Math.pow(e, -outputLayerSums[o])), 2));
		}

		// error rates of hidden layers
		for (int ho = 0; ho < hiddenNodes; ho++) {
			for (int eor = 0; eor < outputNodes; eor++) {
				double product = outputLayerErrorRates[eor] * hiddenToOutputWeights[eor][ho];
				hiddenLayerErrorRates[ho] += product;
				// System.out.print(outputLayerErrorRates[eor] + " * " +
				// hiddenToOutputWeights[eor][ho] + " | ");
			}
			hiddenLayerErrorRates[ho] = hiddenLayerErrorRates[ho]
					* ((Math.pow(e, -hiddenLayerSums[ho]) / Math.pow((1 + Math.pow(e, -hiddenLayerSums[ho])), 2)));
			// System.out.print(" = " + hiddenLayerErrorRates[ho] + " \n ");
		}

		// update weights of hidden to output layers
		for (int oer = 0; oer < outputNodes; oer++) {
			for (int h = 0; h < hiddenNodes; h++) {
				hiddenToOutputWeights[oer][h] = hiddenToOutputWeights[oer][h]
						+ (lr * outputLayerErrorRates[oer] * outputLayerSums[oer]);
			}
		}

		// update weights of input to hidden layers
		for (int ier = 0; ier < hiddenNodes; ier++) {
			for (int i = 0; i < inputNodes; i++) {
				for (int o = 0; o < outputs.length; o++) {
					inputToHiddenWeights[ier][i] = inputToHiddenWeights[ier][i]
							+ (lr * hiddenLayerErrorRates[ier] * inputs[i]);
				}
			}
		}

		double er = 0;
		for (int ers = 0; ers < outputLayerErrorRates.length; ers++) {
			er += outputLayerErrorRates[ers];
		}
		this.erSum = er;

	}

	public void init() { // generate random weights
		for (int h = 0; h < hiddenNodes; h++) {
			for (int i = 0; i < inputNodes; i++) {
				inputToHiddenWeights[h][i] = ranW();
			}
		}

		for (int o = 0; o < outputNodes; o++) {
			for (int h = 0; h < hiddenNodes; h++) {
				hiddenToOutputWeights[o][h] = ranW();
			}
		}

	}

	public double ranW() { // random weight
		double res = Math.random() * maxInitWeight;
		return res;
	}

	public void showData() {

		// hidden layer results
		for (int hsum = 0; hsum < hiddenLayerSums.length; hsum++) {
			System.out.println("Hidden Node" + (hsum + 1) + " Sum = " + hiddenLayerSums[hsum]);
			System.out.println("Hidden Node" + (hsum + 1) + " Sig = " + hiddenLayerSigmoids[hsum]);
		}
		System.out.println();

		// output layer results
		for (int osum = 0; osum < outputLayerSums.length; osum++) {
			System.out.println("Output Node" + (osum + 1) + " Sum = " + outputLayerSums[osum]);
			System.out.println("Output Node" + (osum + 1) + " Sig = " + outputLayerSigmoids[osum]);
		}
		System.out.println();

		// output layer error rate results
		for (int eor = 0; eor < outputLayerErrorRates.length; eor++) {
			System.out.println("Output Node" + (eor + 1) + " Error Rate = " + outputLayerErrorRates[eor]);
		}
		System.out.println();

		// hidden layer error rate results
		for (int her = 0; her < hiddenLayerErrorRates.length; her++) {
			System.out.println("Hidden Node" + (her + 1) + " Error Rate = " + hiddenLayerErrorRates[her]);
		}
		System.out.println();

		// updated toOutput weights
		System.out.println("Hidden units to output units weights");
		for (int ow = 0; ow < outputNodes; ow++) {
			for (int h = 0; h < hiddenNodes; h++) {
				System.out.println("HtoO" + (ow + 1) + "-" + (h + 1) + " -> " + hiddenToOutputWeights[ow][h]);
			}
		}
		System.out.println();

		// update toHidden weights
		System.out.println("Input units to hidden units weights");
		for (int oh = 0; oh < hiddenNodes; oh++) {
			for (int i = 0; i < inputNodes; i++) {
				System.out.println("ItoH" + (oh + 1) + "-" + (i + 1) + " -> " + inputToHiddenWeights[oh][i]);
			}
		}
		System.out.println();

		System.out.println("Output Layer Error Rate Sum   = " + erSum);
		System.out.println("Output Layer Error Rate Sum/2 = " + (erSum / 2));

	}

	public void saveNetwork() {
		String fileName = new SimpleDateFormat("yyyyMMddHHmm'.txt'").format(new Date());

		try {

			FileWriter writer = new FileWriter(new File(fileName));
			writer.write(lr + "\n");
			writer.write(inputNodes + "\n");
			writer.write(hiddenNodes + "\n");
			writer.write(outputNodes + "\n");

			for (int i = 0; i < inputToHiddenWeights.length; i++) {
				// writer.write("[");
				for (int j = 0; j < inputToHiddenWeights[0].length; j++) {
					String comma = ", ";
					if (j == inputToHiddenWeights[0].length - 1) {
						comma = "";
					}
					writer.write(inputToHiddenWeights[i][j] + comma);
				}
				if (i != inputToHiddenWeights.length - 1) {
					writer.write("_");
				}
			}
			writer.write("\n");

			for (int i = 0; i < hiddenToOutputWeights.length; i++) {
				// writer.write("[");
				for (int j = 0; j < hiddenToOutputWeights[0].length; j++) {
					String comma = ", ";
					if (j == hiddenToOutputWeights[0].length - 1) {
						comma = "";
					}
					writer.write(hiddenToOutputWeights[i][j] + comma);
				}
				if (i != hiddenToOutputWeights.length - 1) {
					writer.write("_");
				}
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadNetwork(String fname) {
		// Double iToH = new Double
		try {
			Scanner data = new Scanner(new File(fname));
			lr = Double.parseDouble(data.nextLine());
			inputNodes = Integer.parseInt(data.nextLine());
			hiddenNodes = Integer.parseInt(data.nextLine());
			outputNodes = Integer.parseInt(data.nextLine());

			inputToHiddenWeights = new double[hiddenNodes][inputNodes];
			hiddenToOutputWeights = new double[outputNodes][hiddenNodes];

			String iToHWeights[] = data.nextLine().split("_"); // separate weights
			String hToOWeights[] = data.nextLine().split("_");
			data.close();

			for (int i = 0; i < iToHWeights.length; i++) {
				String w[] = iToHWeights[i].split(", ");
				for (int h = 0; h < w.length; h++) {
					inputToHiddenWeights[i][h] = Double.parseDouble(w[h]);
					// System.out.print(w[h] + " ");
				}
			}

			for (int i = 0; i < hToOWeights.length; i++) {
				String w[] = hToOWeights[i].split(", ");
				for (int h = 0; h < w.length; h++) {
					hiddenToOutputWeights[i][h] = Double.parseDouble(w[h]);
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public String classify(int inputs[]) {
		double hLayerSums[] = new double[hiddenNodes];
		double hLayerSigs[] = new double[hiddenNodes];
		double oLayerSums[] = new double[outputNodes];
		double oLayerSigs[] = new double[outputNodes];

		// get sum of inputs for each hidden layer node
		for (int h = 0; h < hiddenNodes; h++) {
			double sum = 0;
			for (int i = 0; i < inputNodes; i++) {
				double product = inputs[i] * inputToHiddenWeights[h][i];
				sum += product;
			}
			hLayerSums[h] = sum;// (sum > 0.5) ? 1 : 0;
		}

		// sigmoid function each hidden layer sum
		for (int h = 0; h < hiddenNodes; h++) {
			hLayerSigs[h] = (hLayerSums[h] >= 0.5) ? 1 : 0;// 1/(1 + (Math.pow(e, -hLayerSums[h])));
		}

		// get sum of inputs for each output layer node
		for (int o = 0; o < outputNodes; o++) {
			double sum = 0;
			for (int h = 0; h < hiddenNodes; h++) {
				double product = hLayerSigs[h] * hiddenToOutputWeights[o][h];
				sum += product;
			}
			oLayerSums[o] = sum;// (sum >= 0.5) ? 1 : 0;
		}

		// sigmoid function each output layer sum
		for (int o = 0; o < outputNodes; o++) {
			oLayerSigs[o] = (oLayerSums[o] >= 0.5) ? 1 : 0;// 1/(1 + (Math.pow(e, -oLayerSums[o])));// ( >= 5) ? 1 : 0;
		}

		String res = "";
		for (int r = 0; r < oLayerSigs.length; r++) {
			// System.out.print(((int) oLayerSigs[r]) + " ");
			res += ((int) oLayerSigs[r]) + " ";
		}

		return res;
	}
}