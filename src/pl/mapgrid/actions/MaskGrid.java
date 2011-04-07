package pl.mapgrid.actions;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;

import pl.mapgrid.HoughTransform;

public class MaskGrid {
	private final Config config;

	public static class Config {
		public int processNeighbourPixels = 3;
		public Color maskingColor = null;
		public int maskPixelDiffersMoreThan = 0x30;
	}
	public MaskGrid(Config config) {
		this.config = config;
	}
	public MaskGrid() {
		this(new Config());
	}
	private int lineY(double x, double a, double d) {		
		return (int) (d/Math.sin(a)-Math.cos(a)/Math.sin(a)*x);
	}
	private int lineX(int y, double a, double d) {
		return (int) (d/Math.cos(a)-Math.sin(a)/Math.cos(a)*y);
	}

	public BufferedImage filter(BufferedImage image, List<double[]> coord) {
		for (double[] ds : coord) {
			double a = ds[0];
			double d = ds[1];
			if(Math.toRadians(80) <= a && a <= Math.toRadians(100)) {
				for (int x = 0; x < image.getWidth(); ++x) {
					int y = lineY(x, a, d);
					if (0 <= y && y < image.getHeight()) {
						maskPixel(image, x, y, false, true);
					}
				}
			}else{
				for (int y = 0; y < image.getHeight(); ++y) {
					int x = lineX(y, a, d);
					if (0 <= x && x < image.getWidth()) {
						maskPixel(image, x, y, true, false);
					}
				}				
			}
		}
		return image;
	}
	private void maskPixel(BufferedImage image, int x, int y, boolean doX, boolean doY) {
		if(doX && doY)
			throw new IllegalArgumentException("Choose only one. doX or doY");
		int dx = doX ? 1 : 0;
		int dy = doY ? 1 : 0;
		int process = config.processNeighbourPixels;
		int maskWith = getPixel(image, x - dx*process, y - dy*process);
		for (int i = -process; i < process; ++i) {
			int current = getPixel(image, x + dx*i, y + dy*i);
			int diff = HoughTransform.pixelValue(maskWith) - HoughTransform.pixelValue(current);
			if (diff > config.maskPixelDiffersMoreThan) 
				setPixel(image, x + dx * i, y + dy * i, maskWith);
			else 
				maskWith = getPixel(image, x + dx * i, y + dy * i);
		}
	}
	
	private void setPixel(BufferedImage image, int x, int y, int rgb) {
		if(0 < x && x < image.getWidth() && 0 < y && y < image.getHeight()) {
			if(config.maskingColor == null)
				image.setRGB(x, y, rgb);
			else
				image.setRGB(x, y, config.maskingColor.getRGB());
		}
	}
	private int getPixel(BufferedImage image, int x, int y) {
		if(x < 0)
			x = 0;
		if(x > image.getWidth())
			x = image.getWidth()-1;
		if(y < 0)
			y = 0;
		if(y > image.getHeight())
			y = image.getHeight()-1;
		return image.getRGB(x, y);
	}
}
