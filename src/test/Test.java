package test;

//import ml.EvolutionWindow;
import ml.NetworkWindow;
import ml.NeuralNetwork;

public class Test {

	public static void main(String[] args) {
//		EvolutionWindow ew = new EvolutionWindow(500, 500);

		int numInputs = 2;
		double[] numHidden = new double[]{9, 6};
		int numOutputs = 1;
		
		NeuralNetwork brain1 = new NeuralNetwork(numInputs, numHidden, numOutputs);
//		NeuralNetwork brain2 = new NeuralNetwork(numInputs, numHidden, numOutputs);
		NetworkWindow nw = new NetworkWindow(numInputs, numHidden, numOutputs);
//		double[] fitnesses = new double[2];

		double[][][] target = new double[4][2][];
		target[0][0] = new double[]{0,0};
		target[0][1] = new double[]{0};
		target[1][0] = new double[]{1,0};
		target[1][1] = new double[]{1};
		target[2][0] = new double[]{0,1};
		target[2][1] = new double[]{1};
		target[3][0] = new double[]{1,1};
		target[3][1] = new double[]{0};
		
		boolean running = true;
		while(running) {
			time.Time.wait(250);
//			fitnesses[0] = 0;
//			fitnesses[1] = 0;
//			
//			for(int i = 0; i < 4; i++) {
//				int rand = new java.util.Random().nextInt(4);
//				double b1answer = brain1.feedforward(target[rand][0])[0];
//				double b2answer = brain2.feedforward(target[rand][0])[0];
//				if(b1answer < 0) b1answer *= (-1);
//				if(b2answer < 0) b2answer *= (-1);
//				fitnesses[0] += 1 - b1answer;
//				fitnesses[1] += 1 - b2answer;
//			}
			
//			if(target[0][1][0] - brain1.feedforward(target[0][0])[0] > 1) fitnesses[0] += 1 - (target[0][1][0] - brain1.feedforward(target[0][0])[0]);
//			else fitnesses[0] += 1 - (target[0][1][0] - brain1.feedforward(target[0][0])[0])*(-1);
//			if(target[1][1][0] - brain1.feedforward(target[1][0])[0] > 1) fitnesses[0] += 1 - (target[1][1][0] - brain1.feedforward(target[1][0])[0]);
//			else fitnesses[0] += 1 - (target[1][1][0] - brain1.feedforward(target[1][0])[0])*(-1);
//			if(target[2][1][0] - brain1.feedforward(target[2][0])[0] > 1) fitnesses[0] += 1 - (target[2][1][0] - brain1.feedforward(target[2][0])[0]);
//			else fitnesses[0] += 1 - (target[2][1][0] - brain1.feedforward(target[2][0])[0])*(-1);
//			if(target[3][1][0] - brain1.feedforward(target[3][0])[0] > 1) fitnesses[0] += 1 - (target[3][1][0] - brain1.feedforward(target[3][0])[0]);
//			else fitnesses[0] += 1 - (target[3][1][0] - brain1.feedforward(target[3][0])[0])*(-1);
//
//			if(target[0][1][0] - brain2.feedforward(target[0][0])[0] > 1) fitnesses[1] += 1 - (target[0][1][0] - brain2.feedforward(target[0][0])[0]);
//			else fitnesses[1] += 1 - (target[0][1][0] - brain2.feedforward(target[0][0])[0])*(-1);
//			if(target[1][1][0] - brain2.feedforward(target[1][0])[0] > 1) fitnesses[1] += 1 - (target[1][1][0] - brain2.feedforward(target[1][0])[0]);
//			else fitnesses[1] += 1 - (target[1][1][0] - brain2.feedforward(target[1][0])[0])*(-1);
//			if(target[2][1][0] - brain2.feedforward(target[2][0])[0] > 1) fitnesses[1] += 1 - (target[2][1][0] - brain2.feedforward(target[2][0])[0]);
//			else fitnesses[1] += 1 - (target[2][1][0] - brain2.feedforward(target[2][0])[0])*(-1);
//			if(target[3][1][0] - brain2.feedforward(target[3][0])[0] > 1) fitnesses[1] += 1 - (target[3][1][0] - brain2.feedforward(target[3][0])[0]);
//			else fitnesses[1] += 1 - (target[3][1][0] - brain2.feedforward(target[3][0])[0])*(-1);
//			
//			System.out.println(fitnesses[0] + "  \t" + fitnesses[1]);
//
//			
//			double[][][] parent1DNA = brain1.getDNA();
//			double[][][] parent2DNA = brain2.getDNA();
//
//			NeuralNetwork baby;
//			
//			if(fitnesses[0] > fitnesses[1]) {
//				baby = NeuralNetwork.makeBaby(parent1DNA, parent1DNA);
//				baby.mutate(0.2);
//				brain2 = baby;
//				nw.updateData(brain1.getWeightsArray(), brain1.getBiasesArray());
//			} else {
//				baby = NeuralNetwork.makeBaby(parent2DNA, parent2DNA);
//				baby.mutate(0.2);
//				brain1 = baby;
//				nw.updateData(brain2.getWeightsArray(), brain1.getBiasesArray());
//			}

			nw.updateData(brain1.getWeightsArray(), brain1.getBiasesArray());
			nw.update();
			brain1.mutate(1);
		}
		
		
		
		
//		Thread thread = new Thread(){public void run() {
//			int i = 0;
//			while(i < 1000000000) {
//				int rand = new java.util.Random().nextInt(4);
//				brain.train(target[rand][0], target[rand][1]);
//				i++;
//			}
//		};}; thread.start();
//		
//		while(thread.isAlive()) {
//			System.out.println(brain.feedforward(target[0][0])[0]);
//			System.out.println(brain.feedforward(target[1][0])[0]);
//			System.out.println(brain.feedforward(target[2][0])[0]);
//			System.out.println(brain.feedforward(target[3][0])[0]);
//			System.out.println("");
//			time.Time.wait(1000);
//		}
		
//		double[][][] parent1DNA = brain.getDNA();
//		double[][][] parent2DNA = brain2.getDNA();
//		
//		NeuralNetwork baby = NeuralNetwork.makeBaby(parent1DNA, parent2DNA);
//		System.out.println(brain.getWeightsArray()[1][0]);
//		System.out.println(baby.getWeightsArray()[1][0]);
//		System.out.println(brain2.getWeightsArray()[1][0]);
//		System.out.println("");
//		System.out.println(brain.feedforward(new double[]{1,1}));
//		System.out.println(baby.feedforward(new double[]{1,1}));
//		System.out.println(brain2.feedforward(new double[]{1,1}));
	}
}
