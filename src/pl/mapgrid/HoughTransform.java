package pl.mapgrid;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HoughTransform {
	public static class Config {
		public double houghMaximaThreshold = 0.5;
		public int rejectDistanceLessThen= 5;
		public int houghGroupingDistance = 5;
		
		public double aberration = Math.toRadians(10); 
		public double angleStep=Math.toRadians(0.05);
	}
	private final Config config;
	private short[][] space_v;
	private short[][] space_h;
	private double[] angle_v;
	private double[] angle_h;
	private double[] sin;
	private double[] cos;

	public HoughTransform(Config config) {
		this.config = config;		
		
		int angleSpace = (int) (2*config.aberration/config.angleStep);
		angle_v = new double[angleSpace];
		angle_h = new double[angleSpace];
		sin = new double[angleSpace];
		cos = new double[angleSpace];

		double angleMin_v = -config.aberration; 
		//double tmax_v = +aberration;
		double angleMin_h = Math.PI/2-config.aberration; 
		//double tmax_h = Math.PI/2+aberration;
		for (int i = 0; i < angle_v.length; i++) {
			angle_v[i] = angleMin_v+config.angleStep*i;
			angle_h[i] = angleMin_h+config.angleStep*i;
			sin[i] = Math.sin(angle_v[i]);
			cos[i] = Math.cos(angle_v[i]);
		}
	}
	
	public HoughTransform() {
		this(new Config());
	}

	public List<int[]> tranform(BufferedImage src) {
		coord.clear();		
		
		int w = src.getWidth();
		int h = src.getHeight();
		int distMax_v=w;
		int distMax_h=h;
		space_v = new short[angle_v.length][distMax_v+1];
		space_h = new short[angle_h.length][distMax_h+1];
		for(int x=0;x<w;++x) {
			if(x % 100 ==0)
				System.out.println( 100*x/w);
			for (int y = 0; y < h; ++y) {
				if (isPoint(src, x, y)) {
					for (int i = 0; i < angle_v.length; ++i) {
						//sin_h[i] = cos[i]
						//cos_h[i] = -sin[i]
						int dist_v =  (int) (y * sin[i] + x * cos[i]);
						int dist_h = (int) (y * cos[i] - x * sin[i]);
						incrementSpace(distMax_v, i, dist_v, space_v);
						incrementSpace(distMax_h, i, dist_h, space_h);
					}
				}
			}
		}
		return getLineParameters(angle_v, angle_h, space_v, space_h);
	}

	private List<int[]> getLineParameters(double[] angle_v, double[] angle_h,
			short[][] space_v, short[][] space_h) {
		List<int[]> points;
		points = groupPoints(space_v, findMax(space_v));
		convertToParameters(angle_v, points);
		points = groupPoints(space_h, findMax(space_h));
		convertToParameters(angle_h, points);
		return points;
	}

	private List<int[]> groupPoints(short[][] space, int max) {
		List<int[]> points = new ArrayList<int[]>();
		for (int angle = 0; angle < space.length; ++angle) {
			for (int dist = 0; dist < space[angle].length; ++dist) 
				groupPoint(points, angle, dist, space, max);
		}
		return points;
	}

	private static short findMax(short[][] space) {
		short max = 0;
		for (int angle = 0; angle < space.length; ++angle) {
			for (int dist = 0; dist < space[angle].length; ++dist) {
				if (max < space[angle][dist])
					max = space[angle][dist];
			}
		}
		return max;
	}

	private void convertToParameters(double[] angle, List<int[]> points) {
		for (int[] p : points) {
			int a = p[0];
			int dist = p[1];
			coord.add(new double[] { angle[a], dist });			
		}
	}

	private void groupPoint(List<int[]> points, int angle, int dist, short[][] space, int max) {
		short value = space[angle][dist];
		if (value < max * config.houghMaximaThreshold)
			return;
		Iterator<int[]> iterator = points.iterator();
		while (iterator.hasNext()) {
			int[] p = iterator.next();
			if(Math.abs(p[0]-angle) < config.houghGroupingDistance 
					&& Math.abs(p[1]-dist) < config.houghGroupingDistance) 
			{
				// current point inside local maxima area
				if(value > space[p[0]][p[1]]) {
					//move maxima main point 
					p[0] = angle;
					p[1] = dist;
				}else{
					//ignore current value
				}
				return;
			}
		}
		points.add(new int[]{angle,dist});
	}

	public static BufferedImage getHoughImage(String file, short[][] space) {
		int angleSpace = space.length;
		int distMax = space[0].length;		
		int max = findMax(space);
		BufferedImage dest = new BufferedImage(angleSpace+1, distMax+1, BufferedImage.TYPE_INT_ARGB);
		Graphics g = dest.getGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0,0,dest.getWidth(),dest.getHeight());
		for (int a = 0; a <= angleSpace; ++a){
			for (int d = 0; d <= distMax; ++d) {
				int value = 0xff * space[a][d] / max;
				value = (0xff << 24) | (value << 16) | (value << 8) | value;
				dest.setRGB(a, d, value);
			}
		}
		return dest;
	}
	private void incrementSpace(int distMax, int i, int dist, short[][] space) {
		if (config.rejectDistanceLessThen <= dist && dist <= distMax) {
			if(space[i][dist] != Short.MAX_VALUE)
				space[i][dist]++;
		}
	}
	public short[][] getSpaceHorizontal() {
		return space_h;
	}
	public short[][] getSpaceVertical() {
		return space_v;
	}
	
	public static boolean isPoint(BufferedImage src, int x, int y) {
		int rgb = src.getRGB(x, y);
		int r = (rgb >> 16) & 0xFF;
		int g = (rgb >>  8) & 0xFF;
		int b = (rgb >>  0) & 0xFF;		
		return (r+g+b < 0x80*3);
	}
	
	public List<double[]> coord = new ArrayList<double[]>(); 
	
	public static int pixelValue(BufferedImage src, int x, int y) {		
		int rgb = src.getRGB(x, y);
		return pixelValue(rgb);
	}

	public static int pixelValue(int rgb) {
		int r = (rgb >> 16) & 0xFF;
		int g = (rgb >>  8) & 0xFF;
		int b = (rgb >>  0) & 0xFF;		
		return (r+g+b)/3;
	}

}
