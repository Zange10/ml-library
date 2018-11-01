package ml;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JTextField;

import ml.WindowComponents.Line;

public class EvolutionWindow {
	private JFrame window;
	private int width, height, population;
	private Canvas canvas;
	private BufferStrategy bs;
	private Graphics g;
	private Line[] scaleLines, bestLines, medianLines, worstLines;
	private double scale;
	private ArrayList<Double> bestData, medianData, worstData;
	private double[][] data;
	private JTextField[] tfScales;
	
	public EvolutionWindow(int width, int height, int population) {
		this.width = width;
		this.height = height;
		this.population = population;
		canvas = new Canvas();
		Dimension s = new Dimension(width, height);
		canvas.setPreferredSize(s);
		canvas.setMaximumSize(s);
		canvas.setMinimumSize(s);
		canvas.setBackground(Color.WHITE);
		
		window = new JFrame("Evolution");
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
		
		data = new double[10][];
		for(int i = 0; i < data.length; i++) {
			data[i] = new double[0];
		}
		
//		bestData = new ArrayList<Double>();
//		medianData = new ArrayList<Double>();
//		worstData = new ArrayList<Double>();
	}
	
	public void updateData(double[] data) {
		bestData.add(data[0]);
		medianData.add(data[0]);
		worstData.add(data[0]);
	}
	
	public void updateLines() {
		scale = (height/getHighestScore()) * 0.5;
		updateBestLine();
		updateMedianLine();
		updateWorstLine();
		updateScaleLines();
	}
	
	private void updateBestLine() {
		double[] data = convertArrayListToArray(bestData);
		bestLines = new Line[data.length-1];
		double linelength = width / (bestLines.length+1);
		for(int i = 0; i < bestLines.length; i++) {
			bestLines[i] = new Line(
					linelength/2 + linelength * i,
					height-(data[i]*scale) - height*0.1,
					linelength/2 + linelength * (i+1),
					height-(data[i+1]*scale) - height*0.1);
		}
	}
	
	private void updateMedianLine() {
		double[] data = convertArrayListToArray(medianData);
		medianLines = new Line[data.length-1];
		double linelength = width / (medianLines.length+1);
		for(int i = 0; i < medianLines.length; i++) {
			medianLines[i] = new Line(
					linelength/2 + linelength * i,
					height-(data[i]*scale) - height*0.1,
					linelength/2 + linelength * (i+1),
					height-(data[i+1]*scale) - height*0.1);
		}
	}
	
	private void updateWorstLine() {
		double[] data = convertArrayListToArray(worstData);
		worstLines = new Line[data.length-1];
		double linelength = width / (worstLines.length+1);
		for(int i = 0; i < worstLines.length; i++) {
			worstLines[i] = new Line(
					linelength/2 + linelength * i,
					height-(data[i])*scale - height*0.1,
					linelength/2 + linelength * (i+1),
					height-(data[i+1]*scale) - height*0.1);
		}
	}
	
	private void updateScaleLines() {
		double highest = getHighestScore();
		int potency;
		if(highest != 0) potency = getTensPotency(highest);
		else potency = 0;
		scaleLines = new Line[21];
		for(int i = 0; i < scaleLines.length; i++) {
			double x = height-(i*Math.pow(10, potency))*scale - height*0.1;
			scaleLines[i] = new Line(0, x, width, x);
		}
		tfScales = new JTextField[21];
		for(int i = 0; i < scaleLines.length; i++) {
			tfScales[i] = new JTextField();
			tfScales[i].setBounds(
					1,
					(int) (height-(i*Math.pow(10, potency))*scale - height*0.067) - height/20,
					width/40,
					height/20);
			tfScales[i].setText(String.valueOf(i*Math.pow(10, potency)));
		}
	}

	public void update() {
		g.clearRect(0, 0, width, height);
		g.setColor(Color.GRAY);
		for(Line line : scaleLines) {
			g.drawLine((int)line.getX1(), (int)line.getY1(), (int)line.getX2(), (int)line.getY2());
		}
		g.setColor(Color.BLACK);
		for(Line line : bestLines) {
			g.drawLine((int)line.getX1(), (int)line.getY1(), (int)line.getX2(), (int)line.getY2());
		}
		g.setColor(Color.RED);
		for(Line line : medianLines) {
			g.drawLine((int)line.getX1(), (int)line.getY1(), (int)line.getX2(), (int)line.getY2());
		}
		g.setColor(Color.BLACK);
		for(Line line : worstLines) {
			g.drawLine((int)line.getX1(), (int)line.getY1(), (int)line.getX2(), (int)line.getY2());
		}
		for(JTextField field : tfScales) {
			g.drawString(field.getText(), field.getX(), field.getY());
		}
		bs.show();
	}
	
	private double getHighestScore() {
		double highest = 0;
		for(double v : bestData) {
			if(v > highest) highest = v;
		}
		return highest;
	}
	
	private double[] convertArrayListToArray(ArrayList<Double> arrayList) {
		double[] array = new double[arrayList.size()];
		for(int i = 0; i < arrayList.size(); i++) {
			array[i] = arrayList.get(i);
		}
		return array;
	}
	
	private int getTensPotency(double d) {
		String s = String.valueOf(d);
		int counter = -1;
		int index = 0;
		if(s.charAt(0) == '0') {
			index = 2;
			while(s.charAt(index) == '0') {
				counter--;
				index++;
			}
		} else {
			while(s.charAt(index) != '.') {
				counter++;
				index++;
			}
		}
		return counter;
	}
}