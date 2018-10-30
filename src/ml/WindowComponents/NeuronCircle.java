package ml.WindowComponents;

public class NeuronCircle {
	int x, y, circleDiameter;
	
	public NeuronCircle(int x, int y, int circleDiameter) {
		this.x = x;
		this.y = y;
		this.circleDiameter = circleDiameter;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public int getMidX() {
		return x + circleDiameter/2;
	}

	public int getMidY() {
		return y + circleDiameter/2;
	}
}
