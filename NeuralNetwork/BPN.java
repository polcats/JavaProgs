public class NeuralNetwork {
	public static void main(String[] args) {
		Model MLP = new Model(0.15, 2, 3, 2);
		MLP.init();
		MLP.setInputToHiddenWeights(new double[][]{
			{0.3, 0.2},
			{0.3, 0.2},
			{0.3, 0.2}
		});
		MLP.setHiddenToOutputWeights(new double[][]{
			{0.35, 0.25, 0.25},
			{0.2, 0.25, 0.15}
		});
		MLP.train(
			new int[]{
				1,1
			},
			new int[] {
				1,0
			}
		);
		MLP.showData();
	}
}

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

	double maxInitWeight = 3.0;
	double e = 2.7182818285; // constant
	double lr; // learning rate

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
		this.inputToHiddenWeights  = iToH;
	}

	public void setHiddenToOutputWeights(double hToO[][]) {
		this.hiddenToOutputWeights = hToO;
	}

	public void train(int inputs[], int outputs[]) { // 1 instance

		if(inputs.length < inputNodes || outputs.length < outputNodes) {
			System.out.println("Wrong input set.");
			return;
		}

		// get sum of inputs for each hidden layer node
		for(int h = 0; h < hiddenNodes; h++) {
			for(int i = 0; i < inputNodes; i++) {
				double product = inputs[i] * inputToHiddenWeights[h][i];
				hiddenLayerSums[h] += product;
			}
		}

		// sigmoid function each hidden layer sum
		for(int h = 0; h < hiddenNodes; h++) {
			hiddenLayerSigmoids[h] = 1/(1 + (Math.pow(e, -hiddenLayerSums[h])));
		}

		// get sum of inputs for each output layer node
		for(int o = 0; o < outputNodes; o++) {
			for(int h = 0; h < hiddenNodes; h++) {
				double product = hiddenLayerSigmoids[h] * hiddenToOutputWeights[o][h];
				outputLayerSums[o] += product;
			}
		}

		// sigmoid function each output layer sum
		for(int o = 0; o < outputNodes; o++) {
			outputLayerSigmoids[o] = 1/(1 + (Math.pow(e, -outputLayerSums[o])));
		}

		// error rates of output layers
		for(int o = 0; o < outputNodes; o++) { 
			outputLayerErrorRates[o] = 
				(outputs[o] - outputLayerSigmoids[o]) * 
				(
					Math.pow(e, -outputLayerSums[o]) / 
					Math.pow( (1+Math.pow(e, -outputLayerSums[o])), 2)
				);
		}

		// error rates of hidden layers
		for(int ho = 0; ho < hiddenNodes; ho++) {
			for(int eor = 0; eor < outputNodes; eor++) {
				double product = outputLayerErrorRates[eor] * hiddenToOutputWeights[eor][ho];
				hiddenLayerErrorRates[ho] += product;
				//System.out.print(outputLayerErrorRates[eor] + " * " + hiddenToOutputWeights[eor][ho] + " | ");
			}
			hiddenLayerErrorRates[ho] =  hiddenLayerErrorRates[ho] * ((Math.pow(e, -hiddenLayerSums[ho]) / Math.pow(  (1+Math.pow(e, -hiddenLayerSums[ho])  ), 2) ));
			//System.out.print(" = " + hiddenLayerErrorRates[ho] + " \n ");
		}

		// update weights of hidden to output layers
		for(int oer = 0; oer < outputNodes; oer++) {
			for(int h = 0; h < hiddenNodes; h++) {
				hiddenToOutputWeights[oer][h] = hiddenToOutputWeights[oer][h] + ( lr * outputLayerErrorRates[oer] * outputLayerSums[oer] );
			}
		}

		// update weights of input to hidden layers
		for(int ier = 0; ier < hiddenNodes; ier++) {
			for(int i = 0; i < inputNodes; i++) {
				for(int o = 0; o < outputs.length; o++) {
					inputToHiddenWeights[ier][i] = inputToHiddenWeights[ier][i] + (lr * hiddenLayerErrorRates[ier] * outputs[o]);
				}
			}
		}

	}

	public void init() { // generate random weights
		for(int h = 0; h < hiddenNodes; h++) {
			for(int i = 0; i < inputNodes; i++) {
				inputToHiddenWeights[h][i] = ranW();
			}
		}
	}

	public double ranW() { // random weight
		double res = Math.random() * maxInitWeight;
		return res;
	}

	public void showData() {
		// hidden layer results 
		for(int hsum = 0; hsum < hiddenLayerSums.length; hsum++) {
			System.out.println("Hidden Node" + (hsum+1) + " Sum = " + hiddenLayerSums[hsum]);
			System.out.println("Hidden Node" + (hsum+1) + " Sig = " + hiddenLayerSigmoids[hsum]);
		}
		System.out.println();

		//output layer results
		for(int osum = 0; osum < outputLayerSums.length; osum++) {
			System.out.println("Hidden Node" + (osum+1) + " Sum = " + outputLayerSums[osum]);
			System.out.println("Hidden Node" + (osum+1) + " Sig = " + outputLayerSigmoids[osum]);
		}
		System.out.println();

		// output layer error rate results
		for(int eor = 0; eor < outputLayerErrorRates.length; eor++) {
			System.out.println("Output Node" + (eor+1) + " Error Rate = " + outputLayerErrorRates[eor]);
		}
		System.out.println();
		
		// hidden layer error rate results
		for(int her = 0; her < hiddenLayerErrorRates.length; her++) {
			System.out.println("Hidden Node" + (her+1) + " Error Rate = " + hiddenLayerErrorRates[her]);
		}
		System.out.println();

		//updated toOutput weights
		System.out.println("Hidden units to output units weights");
		for(int ow = 0; ow < outputNodes; ow++) {
			for(int h = 0; h < hiddenNodes; h++) {
				System.out.println("HtoO" + (ow+1) + "-" + (h+1) + " -> " + hiddenToOutputWeights[ow][h]);
			}
		}
		System.out.println();

		//update toHidden weights
		System.out.println("Input units to hidden units weights");
		for(int oh = 0; oh < hiddenNodes; oh++){
			for(int i = 0; i < inputNodes; i++) {
				System.out.println("ItoH" + (oh+1) + "-" + (i+1) + " -> " + inputToHiddenWeights[oh][i]);
			}
		}
		System.out.println();
		
		double erSum = 0;
		for(int ers = 0; ers < outputLayerErrorRates.length; ers++) {
			erSum += outputLayerErrorRates[ers];
		}
		System.out.println("Output Layer Error Rate Sum   = " + erSum);
		System.out.println("Output Layer Error Rate Sum/2 = " + (erSum/2));

	}
}