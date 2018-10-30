package ml;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;
import ml.WindowComponents.Line;
import ml.WindowComponents.NeuronCircle;

public class NetworkWindow {
	private JFrame window;
	private int width, height, neuronDiameter, biggestLayerSize;
	private Canvas canvas;
	private BufferStrategy bs;
	private Graphics g;
	private int numInputs, numOutputs;
	private int[] numHidden;
	private NeuronCircle[][] neuronCircles;
	private Line[][] lines;
	private int spaceScale;
	
	public NetworkWindow(int numInputs, double[] numHidden, int numOutputs) {
		neuronDiameter = 10;
		spaceScale = 3;
		this.numInputs = numInputs;
		this.numHidden = convertToIntArray(numHidden);
		this.numOutputs = numOutputs;
		biggestLayerSize = getBiggestLayerSize();
		this.width = (int) ((1 + numHidden.length + 1) * neuronDiameter * 4 * spaceScale);
		this.height = (int) ((biggestLayerSize + 1/*bias*/) * 2 * spaceScale * neuronDiameter + neuronDiameter * 0.5 * spaceScale);
		canvas = new Canvas();
		Dimension s = new Dimension(width, height);
		canvas.setPreferredSize(s);
		canvas.setMaximumSize(s);
		canvas.setMinimumSize(s);
		canvas.setBackground(Color.WHITE);
		
		window = new JFrame("Network");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLayout(new BorderLayout());
		window.add(canvas, BorderLayout.CENTER);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setResizable(false);
		window.setVisible(true);
		
		canvas.createBufferStrategy(2);
		bs = canvas.getBufferStrategy();
		g = bs.getDrawGraphics();
		
		createNetwork(numInputs, this.numHidden, numOutputs);
	}
	
	private int getBiggestLayerSize() {
		int biggestLayerSize = numInputs;
		for(int i = 0; i < numHidden.length; i++) {
			if(numHidden[i] > biggestLayerSize) biggestLayerSize = (int) numHidden[i];
		}
		if(numOutputs > biggestLayerSize) biggestLayerSize = numOutputs;
		return biggestLayerSize;
	}
	
	private void createNetwork(int numInputs, int[] numHidden, int numOutput) {
		// initianlize neuronCircles ----------------------------------------------
		neuronCircles = new NeuronCircle[numHidden.length + 2][];
		neuronCircles[0] = new NeuronCircle[numInputs + 1];	// +1 = bias
		for(int i = 0; i < numHidden.length; i++) {
			neuronCircles[i+1] = new NeuronCircle[(int) numHidden[i] + 1];	// +1 = bias
		}
		neuronCircles[numHidden.length + 1] = new NeuronCircle[numOutput];

		// position neuronCircles
		int buffX = (int) (neuronDiameter * spaceScale);
		for(int i = 0; i < neuronCircles.length; i++) {
			int buffY;
			if(i < neuronCircles.length - 1) buffY = (biggestLayerSize + 1 - neuronCircles[i].length) * neuronDiameter * spaceScale;	// +1 = bias
			else buffY = (biggestLayerSize - neuronCircles[i].length) * neuronDiameter * spaceScale;
			for(int j = 0; j < neuronCircles[i].length; j++) {
				neuronCircles[i][j] = new NeuronCircle(buffX, buffY,neuronDiameter);
				buffY += neuronDiameter * 2 * spaceScale;
			}
			buffX += neuronDiameter * 4 * spaceScale;
		}
		
		// initialize lines
		lines = new Line[numHidden.length + 1][];
		lines[0] = new Line[((numInputs + 1) * numHidden[0])];	// +1 = bias
		for(int i = 0; i < numHidden.length -1; i++) {
			lines[i+1] = new Line[((numHidden[i] + 1) * numHidden[i+1])];	// +1 = bias
		}
		lines[numHidden.length] = new Line[((numHidden[numHidden.length-1] + 1) * numOutputs)];	// +1 = bias
		
		// position lines
		for(int i = 0; i < lines.length; i++) {
			for(int j = 0; j < neuronCircles[i].length; j++) {
				int biasesInNext = 1;
				if(i+1 == neuronCircles.length-1) biasesInNext = 0;	// if "--> Output"
				for(int k = 0; k < neuronCircles[i+1].length - biasesInNext; k++) {
					lines[i][j*(neuronCircles[i+1].length-biasesInNext) + k] = new Line(
							neuronCircles[i][j].getMidX(),
							neuronCircles[i][j].getMidY(),
							neuronCircles[i+1][k].getMidX(),
							neuronCircles[i+1][k].getMidY());
				}
			}
		}
	}

//	3 0 0
//	3 0 1
//	3 1 0
//	3 1 1
//	3 2 0
	
	public void updateData(double[][] weights, double[][] biases) {
		for(int i = 0; i < lines.length; i++) {
			for(int j = 0; j < lines[i].length; j++) {
				double bufferWeight;
				int biasesInNext = 1;
				if(i+1 == neuronCircles.length-1) biasesInNext = 0;	// if "--> Output"
				if(j > lines[i].length - neuronCircles[i+1].length - 1 + biasesInNext) {
					bufferWeight = biases[i][j - (neuronCircles[i].length-1/*number of used neurons (!bias)*/)*(neuronCircles[i+1].length - biasesInNext/*used neurons on other side*/)];
				} else {
					bufferWeight = weights[i][j];
				}
				if(bufferWeight > 0) {
					lines[i][j].setColor(new Color(255, (int) (255 - 255*bufferWeight), (int) (255 - 255*bufferWeight)));
				} else {
					bufferWeight *= (-1);
					lines[i][j].setColor(new Color((int) (255 - 255*bufferWeight), (int) (255 - 255*bufferWeight), 255));
				}
			}
		}
	}

	public void update() {
		g.clearRect(0, 0, width, height);
		for(Line[] neuronlines : lines) {
			for(Line line : neuronlines) {
				g.setColor(line.getColor());
				g.drawLine((int)line.getX1(), (int)line.getY1(), (int)line.getX2(), (int)line.getY2());
			}
		}
		g.setColor(Color.BLACK);
		for(NeuronCircle[] circles : neuronCircles) {
			for(NeuronCircle circle : circles) {
				g.drawOval(circle.getX(), circle.getY(), neuronDiameter, neuronDiameter);
				g.fillOval(circle.getX(), circle.getY(), neuronDiameter, neuronDiameter);
			}
		}
		bs.show();
	}
	
	private int[] convertToIntArray(double[] array) {
		int[] result = new int[array.length];
		for(int i = 0; i < array.length; i++) {
			result[i] = (int) array[i];
		}
		return result;
	}
}