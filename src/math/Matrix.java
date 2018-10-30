package math;

public class Matrix {
	
	int rows, cols;
	double[][] data;
	
	public Matrix(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		
		this.data = new double[rows][];
		for(int i = 0; i < rows; i++) {
			data[i] = new double[cols];
			for(int j = 0; j < cols; j++) {
				data[i][j] = 0;
			}
		}
	}
	
	public void randomize() {
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				data[i][j] = new java.util.Random().nextDouble()*2-1;
			}
		}
	}
	
	public void add(double n) {
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				data[i][j] += n;
			}
		}
	}
	
	public void addMatrix(Matrix m) {
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				data[i][j] += m.data[i][j];
			}
		}
	}
	
	public void multiply(double n) {
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				data[i][j] *= n;
			}
		}
	}
	
	public void multiplyByMatrix(Matrix m) {
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				this.data[i][j] *= m.data[i][j];
			}
		}
	}
	
	public void map(mapFunc func) {
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				double v = data[i][j];
				data[i][j] = func.func(v, i, j);
			}
		}
	}
	
	public void printMatrix() {
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				System.out.print(data[i][j] + "\t");
			}
			System.out.println("");
		}
	}

	public double[] convertToArray() {
		double[] newArray = new double[rows*cols];
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				newArray[i*cols+j] = data[i][j];
			}
		}
		return newArray;
	}
	
	public Matrix copy() {
		Matrix result = new Matrix(rows, cols);
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				result.data[i][j] = data[i][j];
			}
		}
		return result;
	}
	
	public double[][] getData() {
		return data;
	}
	
	// static methods ------------------------------------------------------------------------------------------------------
	

	
	public static Matrix subtract(Matrix m1, Matrix m2) {
		Matrix result = new Matrix(m1.rows, m1.cols);
		for(int i = 0; i < result.rows; i++) {
			for(int j = 0; j < result.cols; j++) {
				result.data[i][j] += m1.data[i][j] - m2.data[i][j];
			}
		}
		return result;
	}
	
	public static Matrix getMatrixProduct(Matrix m1, Matrix m2) {
		if(m1.cols != m2.rows)  {
			System.out.println("<<Columns of A must match rows of B>>");
			return null;
		}
		Matrix result = new Matrix(m1.rows, m2.cols);
		for(int i = 0; i < result.rows; i++) {
			for(int j = 0; j < result.cols; j++) {
				double sum = 0;
				for(int k = 0; k < m1.cols; k++) {
					sum += m1.data[i][k] * m2.data[k][j];
				}
				result.data[i][j] = sum;
			}
		}
		return result;
	}
	
	public static Matrix getMaped(Matrix m, mapFunc func) {
		Matrix result = new Matrix(m.rows, m.cols);
		for(int i = 0; i < m.rows; i++) {
			for(int j = 0; j < m.cols; j++) {
				double v = m.data[i][j];
				result.data[i][j] = func.func(v, i, j);
			}
		}
		return result;
	}
	
	public static Matrix transpose(Matrix m) {
		Matrix result = new Matrix(m.cols, m.rows);
		for(int i = 0; i < m.rows; i++) {
			for(int j = 0; j < m.cols; j++) {
				result.data[j][i] = m.data[i][j];
			}
		}
		return result;
	}
	
	public static Matrix convertFromArray(double[] array) {
		Matrix newMatrix = new Matrix(array.length, 1);
		for(int i = 0; i < newMatrix.rows; i++) {
			newMatrix.data[i][0] = array[i];
		}
		return newMatrix;
	}
	
	public static Matrix convertMultDimFromArray(double[] array, int cols) {
		Matrix newMatrix = new Matrix(array.length / cols, cols);
		for(int i = 0; i < newMatrix.rows; i++) {
			for(int j = 0; j < newMatrix.cols; j++) {
				newMatrix.data[i][j] = array[i * cols + j];
			}
		}
		return newMatrix;
	}
	
	// interface -----------------------------------------------------------------------------------------------------------

    public interface mapFunc{
        double func(double value, int row, int col);
    }
}
