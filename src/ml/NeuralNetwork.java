package ml;

import math.Matrix;

public class NeuralNetwork {
	
	double numInputs, numOutputs;
	double[] numHidden;
	double learningRate;
	Matrix[] weights;
	Matrix[] biases;
	Matrix[] hidden;
	
	public NeuralNetwork(double numInputs, double[] numHidden, double numOutputs) {
		this.numInputs = numInputs;
		this.numHidden = numHidden;
		this.numOutputs = numOutputs;
		this.learningRate = 0.1;
		
		this.weights = new Matrix[numHidden.length + 1];
		for(int i = 0; i < numHidden.length; i++) {
			if(i == 0) {
				this.weights[i] = new Matrix((int) this.numHidden[i], (int) this.numInputs);
			} else {
				this.weights[i] = new Matrix((int) this.numHidden[i], (int) this.numHidden[i-1]);
			}
			this.weights[i].randomize();
		}
		this.weights[numHidden.length] = new Matrix((int) this.numOutputs, (int) this.numHidden[numHidden.length - 1]);
		this.weights[numHidden.length].randomize();
		
		this.biases = new Matrix[numHidden.length + 1];
		for(int i = 0; i < numHidden.length; i++) {
			this.biases[i] = new Matrix((int) this.numHidden[i], 1);
			this.biases[i].randomize();
		}
		this.biases[numHidden.length] = new Matrix((int) this.numOutputs, 1);
		this.biases[numHidden.length].randomize();
		
		hidden = new Matrix[numHidden.length];
	}
	
	public double[] feedforward(double[] inputArray) {
		// Generating the Hidden outputs ---------------------------------
		Matrix inputs = Matrix.convertFromArray(inputArray);
		for(int i = 0; i < numHidden.length; i++) {
			if(i == 0) {
				hidden[i] = Matrix.getMatrixProduct(weights[i], inputs);
			} else {
				hidden[i] = Matrix.getMatrixProduct(weights[i], hidden[i-1]);
			}
//			System.out.println(i);
//			weights[1].printMatrix();
//			System.out.println("");
//			hidden[0].printMatrix();
//			System.out.println("\n\n\n");
			hidden[i].addMatrix(biases[i]);
			hidden[i].map(new math.Matrix.mapFunc() {
	    		public double func(double value, int row, int col) {
	    	    	return 1/(1+Math.exp(-value));	//sigmoid
	    	    }});
		}
//		System.out.println("#############################################################");
//		System.out.println(weights.length);
//		System.out.println(numHidden.length);
//		weights[numHidden.length].printMatrix();
		// Generating the Output's outputs
		Matrix outputs = Matrix.getMatrixProduct(weights[numHidden.length], hidden[numHidden.length-1]);
		outputs.addMatrix(biases[numHidden.length]);
		outputs.map(new math.Matrix.mapFunc() {
    		public double func(double value, int row, int col) {
    	    	return 1/(1+Math.exp(-value));	//sigmoid
    	    }});
		
		// return to caller
		return outputs.convertToArray();
	}
	
	public void train(double[] inputArray, double[] targetArray) {
		double[] outputArray = feedforward(inputArray);
		
		Matrix inputs = Matrix.convertFromArray(inputArray);
		Matrix outputs = Matrix.convertFromArray(outputArray);
		Matrix targets = Matrix.convertFromArray(targetArray);
		
		// calculate the errors of the outputs
		// ERROR = TARGETS - OUTPUTS
		Matrix outputErrors = Matrix.subtract(targets, outputs);

		// Calculate gradient
		Matrix outputGradients = Matrix.getMaped(outputs, new math.Matrix.mapFunc() {
    		public double func(double value, int row, int col) {
    	    	return value*(1-value);	// derivative of sigmoid (sigmoid(x) * (1-sigmoid(x)) (value has to be a value from the sigmoid function)
    	    }});
		outputGradients.multiplyByMatrix(outputErrors);;
		outputGradients.multiply(learningRate);
		
		// Calculate Deltas
		Matrix[] hidden_T = new Matrix[hidden.length];
		for(int i = 0; i < hidden.length; i++) {
			hidden_T[i] = Matrix.transpose(hidden[i]);
		}


		Matrix[] weightDeltas = new Matrix[numHidden.length+1];
		weightDeltas[numHidden.length] = Matrix.getMatrixProduct(outputGradients, hidden_T[hidden_T.length-1]);
		
				
//		Matrix[] weightDeltas = new Matrix[numHidden+1];
//		weightDeltas[0] = Matrix.getMatrixProduct(gradients, hidden_T);
		
		for(int i = weightDeltas.length - 2; i > 0; i--) {
			weightDeltas[i] = Matrix.getMatrixProduct(outputGradients, hidden_T[i-1]);
		}
		
		// adjust weights and bias by deltas at output
		weights[numHidden.length].addMatrix(weightDeltas[numHidden.length]);
		biases[numHidden.length].addMatrix(outputGradients);

		// transposed weights
		Matrix[] weights_T = new Matrix[numHidden.length +1];	//Transposed weights
		for(int i = 0; i < weights_T.length; i++) {
			weights_T[i] = Matrix.transpose(weights[i]);
		}
		

		Matrix[] hiddenErrors = new Matrix[numHidden.length];		// other way round counted
		Matrix[] hiddenGradients = new Matrix[numHidden.length];		// other way round counted
		
		for(int i = 0; i < numHidden.length; i++) {
			// Calculate the hidden layer errors
			if(i == 0) {
				hiddenErrors[i] = Matrix.getMatrixProduct(weights_T[weights_T.length-i - 1], outputErrors);
			} else {
				hiddenErrors[i] = Matrix.getMatrixProduct(weights_T[weights_T.length-i - 1], hiddenErrors[i-1]);
			}
			//Calculate hidden gradient
//			hiddenGradients[i] = new Matrix[numHidden[numHidden.length - i - 1]];
			hiddenGradients[i] = Matrix.getMaped(hidden[hidden.length-i - 1], new math.Matrix.mapFunc() {
	    		public double func(double value, int row, int col) {
	    	    	return value*(1-value);	// derivative of sigmoid (sigmoid(x) * (1-sigmoid(x)) (value has to be a value from the sigmoid function)
	    	    }});
			
			hiddenGradients[i].multiplyByMatrix(hiddenErrors[i]);
			hiddenGradients[i].multiply(learningRate);
		}
		
		// Calculate "--> hidden"-weights
		Matrix inputs_T = Matrix.transpose(inputs);
		weightDeltas[0] = Matrix.getMatrixProduct(hiddenGradients[hiddenGradients.length - 1], inputs_T);
		
		// adjust weights and bias by deltas
		weights[0].addMatrix(weightDeltas[0]);
		biases[0].addMatrix(hiddenGradients[hiddenGradients.length - 1]);
	}
	
	
	
	
	// Neuro-Evolution -----------------------------------------------------------------------------------------
	
	public double[][][] getDNA() {
		double[][][] dna = new double[3][][];
		dna[0] = new double[3][];
		dna[0][0] = new double[]{numInputs};
		dna[0][1] = new double[numHidden.length];
		for(int i = 0; i < numHidden.length; i++) {
			dna[0][1][i] = numHidden[i];
		}
		dna[0][2] = new double[]{numOutputs};
		dna[1] = new double[weights.length][];
		for(int i = 0; i < weights.length; i++) {
			dna[1][i] = weights[i].convertToArray();
		}
		dna[2] = new double[biases.length][];
		for(int i = 0; i < biases.length; i++) {
			dna[2][i] = biases[i].convertToArray();
		}
//		biases[1].printMatrix();
//		System.out.println("");
		return dna;
	}
	
//	public NeuralNetwork Baby() {
//		NeuralNetwork baby = new NeuralNetwork(numInputs, numHidden, numOutputs);
//		baby.setWeights(this.getWeights());
//		baby.setBiases(this.getBiases());
//		return baby;
//	}
	
	public static NeuralNetwork makeBaby(double[][][] dna1, double[][][] dna2) {
		NeuralNetwork baby = new NeuralNetwork(dna1[0][0][0], dna1[0][1], dna1[0][2][0]);
//		System.out.println(dna1[1][2][0]);
//		System.out.println(dna2[1][2][0]);
		Matrix[] newWeights = new Matrix[dna1[1].length];
		Matrix[] newBiases = new Matrix[dna1[2].length];
		for(int i = 0; i < dna1[0][1].length+1; i++) {
			if(i < dna1[0][1].length) {
				if(new java.util.Random().nextDouble() > 0.5) {
					newWeights[i] = Matrix.convertMultDimFromArray(dna1[1][i], (int) (dna1[1][i].length/dna1[0][1][i]));
				} else {
					newWeights[i] = Matrix.convertMultDimFromArray(dna2[1][i], (int) (dna1[1][i].length/dna1[0][1][i]));
				}
			} else {	// "--> output"
				if(new java.util.Random().nextDouble() > 0.5) {
					newWeights[i] = Matrix.convertMultDimFromArray(dna1[1][i], (int) (dna1[1][i].length/dna1[0][2][0]));
				} else {
					newWeights[i] = Matrix.convertMultDimFromArray(dna2[1][i], (int) (dna1[1][i].length/dna1[0][2][0]));
				}
			}
		}
		for(int i = 0; i < dna1[0][1].length+1; i++) {
			if(new java.util.Random().nextDouble() > 0.5) {
				newBiases[i] = Matrix.convertFromArray(dna1[2][i]);
			} else {
				newBiases[i] = Matrix.convertFromArray(dna2[2][i]);
			}
		}
//		System.out.println("-----------------");
//		newBiases[1].printMatrix();
//		System.out.println("-----------------");
		baby.setWeights(newWeights);
		baby.setBiases(newBiases);
		return baby;
	}
	
	public void mutate(double mutationRate) {
		for(int i = 0; i < weights.length; i++) {
			for(int j = 0; j < weights[i].getData().length; j++) {
				for(int k = 0; k < weights[i].getData()[j].length; k++) {
					if(new java.util.Random().nextDouble() < mutationRate) {
						weights[i].getData()[j][k] = (new java.util.Random().nextDouble()*2-1 + weights[i].getData()[j][k])/2;
					}
				}
			}
		}
		for(int i = 0; i < biases.length; i++) {
			for(int j = 0; j < biases[i].getData().length; j++) {
				for(int k = 0; k < biases[i].getData()[j].length; k++) {
					if(new java.util.Random().nextDouble() < mutationRate) {
						biases[i].getData()[j][k] = (new java.util.Random().nextDouble()*2-1 + biases[i].getData()[j][k])/2;
					}
				}
			}
		}
	}
	
	
	
	// Getters and Setters -------------------------------------------------------------------------------------

	public Matrix[] getWeights() {
		return weights;
	}

	public Matrix[] getBiases() {
		return biases;
	}
	
	public double[][] getWeightsArray() {
		double[][] result = new double[weights.length][];
		for(int i = 0; i < weights.length; i++) {
			result[i] = weights[i].convertToArray();
		}
		return result;
	}

	public double[][] getBiasesArray() {
		double[][] result = new double[biases.length][];
		for(int i = 0; i < biases.length; i++) {
			result[i] = biases[i].convertToArray();
		}
		return result;
	}

	private void setWeights(Matrix[] weights) {
		this.weights = weights;
	}

	private void setBiases(Matrix[] biases) {
		this.biases = biases;
	}
	
}
