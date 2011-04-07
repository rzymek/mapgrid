package HNF;

/**
 * The class produces an iRPImage with double values
 */
public class doubleiRPImage {

	protected int sizeX;
	protected int sizeY;
	protected double image[];

	/**
	 * Constructs an empty doubleiRPImage
	 */
	public doubleiRPImage() {
		sizeX = 0;
		sizeY = 0;
		image = new double[sizeX * sizeY];
	}

	/**
	 * Constructs an doubleiRPImage with given width and height
	 * 
	 * @param width
	 *            the width of the doubleiRPImage
	 * @param height
	 *            the height of the doubleiRPImage
	 */
	public doubleiRPImage(int width, int height) {
		sizeX = width;
		sizeY = height;
		image = new double[sizeX * sizeY];
	}

	public void Clear(double v) {
		int i;
		for (i = 0; i < sizeX * sizeY; i++)
			image[i] = v;
	}

	/**
	 * Gives the width of the image
	 * 
	 * @return width of image
	 */
	public int GetSizeX() {
		return sizeX;
	}

	/**
	 * Gives the height of the image
	 * 
	 * @return height of image
	 */

	public int GetSizeY() {
		return sizeY;
	}

	/**
	 * Gives the size of the image
	 * 
	 * @return size of image
	 */
	public int GetSize() {
		return sizeX * sizeY;
	}

	// FOR NORMALIZE
	/**
	 * Return minimum gray value of the image
	 * 
	 * @return minimum gray value
	 */
	public int GetMin() {

		int position = 0;
		double min = image[0];

		for (int i = 1; i < sizeX * sizeY; i++) {
			if (image[i] < min) {
				min = image[i];
				position = i;
			}
		}

		return position;
	}

	// FOR NORMALIZE
	/**
	 * Return maximum gray value of the image
	 * 
	 * @return maximum gray value
	 */
	int GetMax() {

		int position = 0;
		double max = image[0];

		for (int i = 1; i < sizeX * sizeY; i++) {
			if (image[i] > max) {
				max = image[i];
				position = i;
			}
		}
		return position;
	}

	/**
	 * Makes a copy of the doubleiRPImage
	 * 
	 * @return the copy of this doubleiRPImage
	 */
	public doubleiRPImage copy() {
		doubleiRPImage G = new doubleiRPImage(this.GetSizeX(), this.GetSizeY());
		for (int i = 0; i < sizeX * sizeY; i++) {
			G.image[i] = image[i];
		}
		return G;
	}

	/**
	 * Returns the arrayvalue of the doubleiRPImage on the place (x,y)
	 * 
	 * @param x
	 *            x position of the pixel y y position of the pixel
	 * @return the value of image at position (x,y)
	 */
	public double GetPixel(int x, int y) {
		return image[x + y * sizeX];
	}

	/**
	 * Returns the value of the image[] array on position i
	 * 
	 * @param i
	 *            the specified position
	 * @return value of image[] at position i
	 */
	public double GetPixel(int i) {
		return image[i];
	}

	/**
	 * Sets the value of image[] at position (x,y) to a desired value
	 * 
	 * @param x
	 *            x-position y y-position value the desired value
	 */
	public void SetPixel(int x, int y, double value) {
		image[x + y * sizeX] = value;
	}

	/**
	 * Sets the value of image[] at position i to a desired value
	 * 
	 * @param i
	 *            position i value the desired value
	 */
	public void SetPixel(int i, double value) {
		image[i] = value;
	}

	/*
	 * Zieht eine Linie von (a1,b1) nach (a2,b2) und setzt den jeweiligen
	 * Pixelwert auf 'value'
	 */
	public void drawLineWithConstantValue(int a1, int b1, int a2, int b2,
			int value) {
		double a, b, delta;

		// System.out.println("("+a1+","+b1+") ("+a2+","+b2);
		if (Math.abs(b2 - b1) < Math.abs(a2 - a1)) {
			if (a1 > a2)
				delta = -1;
			else
				delta = 1;
			for (a = a1; a != a2 + delta; a += delta)// for(a = a1; a < a2; a++)
			{
				b = b1
						+ ((b2 - b1) * (a - a1) / ((double) a2 - (double) a1));
				// Die Abfrage ist zuf�llig wegen ArrayindexofBoundexception!!
				if (((int) b != sizeY) && ((int) a != sizeX)) {
					SetPixel((int) a, (int) b, value);
				}
			}
		} else {

			if (b1 > b2)
				delta = -1;
			else
				delta = 1;
			for (b = b1; b != b2 + delta; b += delta) {
				a = a1
						+ ((a2 - a1) * (b - b1) / ((double) b2 - (double) b1));
				if ((int) b != sizeY && ((int) a != sizeX)) {
					SetPixel((int) a, (int) b, value);
				}
			}
		}
	}

	/**
	 * Makes an iRPImage of this doubleiRPImage
	 * 
	 * @return iRPImage of this doubleiRPImage
	 */
	public iRPImage makeiRPImage() {

		iRPImage A = new iRPImage(sizeX, sizeY);
		for (int i = 0; i < sizeY; i++) {
			for (int j = 0; j < sizeX; j++) {
				A.SetPixel(i, j, (int) this.GetPixel(i, j));
			}
		}
		A.Normalize();
		return A;
	}

}