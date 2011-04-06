package pl.mapgrid;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

public class HoughTransform {
	public static class Config {
		public double houghMaximaThreshold = 0.3;
		public int rejectDistanceLessThen= 5;
		public int houghGroupingDistance = 5;
	}
	private static final double THRESHOLD = 0.3;
	private static final short MAX_SPACE_VALUE = Short.MAX_VALUE;
	private static final int REJECT_DIST_LESS_THEN = 5;
	private static final int HOUGH_DISTANCE_GROUP = 5;

	public BufferedImage tranform2(BufferedImage src) {
		long start = System.currentTimeMillis();
		coord.clear();
		
		double aberration = Math.toRadians(10);
		double angleStep=Math.toRadians(0.05);
		
		int angleSpace = (int) (2*aberration/angleStep);
		double[] angle_0 = new double[angleSpace];
		double[] angle_90 = new double[angleSpace];
		double[] sin = new double[angleSpace];
		double[] cos = new double[angleSpace];
		
		double angleMin_0 = -aberration; 
//		double tmax_0 = +aberration;

		double angleMin_90 = Math.PI/2-aberration; 
//		double tmax_90 = Math.PI/2+aberration;
		
		for (int i = 0; i < angleSpace; i++) {
			angle_0[i] = angleMin_0+angleStep*i;
			angle_90[i] = angleMin_90+angleStep*i;
			sin[i] = Math.sin(angle_0[i]);
			cos[i] = Math.cos(angle_0[i]);
			//sin_90[i] = cos[i]
			//cos_90[i] = -sin[i]
		}
		int w = src.getWidth();
		int h = src.getHeight();
		int distMax=(int) Math.sqrt(w*w+h*h);
		short[][] space_0 = new short[angleSpace+1][distMax+1];
		short[][] space_90 = new short[angleSpace+1][distMax+1];
		for(int x=0;x<w;++x) {
			if(x % 100 ==0)
				System.out.println( 100*x/w);
			for (int y = 0; y < h; ++y) {
				if (isPoint(src, x, y)) {
					for (int i = 0; i < angleSpace; ++i) {
						int dist_0 =  (int) (y * sin[i] + x * cos[i]);
						int dist_90 = (int) (y * cos[i] - x * sin[i]);
						incrementSpace(distMax, i, dist_0, space_0);
						incrementSpace(distMax, i, dist_90, space_90);
					}
				}
			}
		}
		long now= System.currentTimeMillis();
		System.out.println("Hought took: "+(now-start));
		short max = 0;
		for (int angle = 0; angle <= angleSpace; ++angle)
			for (int dist = 0; dist <= distMax; ++dist) {
				if (max < space_0[angle][dist])
					max = space_0[angle][dist];
				if (max < space_90[angle][dist])
					max = space_90[angle][dist];
			}
//		writeHoughImage("space0.png", angleSpace, distMax, space_0, max);
//		writeHoughImage("space90.png", angleSpace, distMax, space_90, max);
		List<int[]> points_0 = new ArrayList<int[]>();
		List<int[]> points_90 = new ArrayList<int[]>();
		for (int angle = 0; angle <= angleSpace; ++angle) {
			for (int dist = 0; dist <= distMax; ++dist) {
				groupPoint(points_0, angle, dist, space_0, max);
				groupPoint(points_90, angle, dist, space_90, max);
			}
		}
		convertToParameters(angle_0, points_0);
		convertToParameters(angle_90, points_90);
		return null;		
	}

	private void convertToParameters(double[] angle, List<int[]> points) {
		for (int[] p : points) {
			int a = p[0];
			int dist = p[1];
			coord.add(new double[] { angle[a], dist });			
		}
	}

	private void groupPoint(List<int[]> points_0, int angle, int dist,
			short[][] space, short max) {
		short value = space[angle][dist];
		if (value < max * THRESHOLD)
			return;
		Iterator<int[]> iterator = points_0.iterator();
		while (iterator.hasNext()) {
			int[] p = iterator.next();
			if(Math.abs(p[0]-angle) < HOUGH_DISTANCE_GROUP && Math.abs(p[1]-dist) < HOUGH_DISTANCE_GROUP) {
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
		points_0.add(new int[]{angle,dist});
	}

	private void writeHoughImage(String file, int angleSpace, int distMax, short[][] space,
			short max) {
		BufferedImage dest = new BufferedImage(angleSpace+1, distMax+1, BufferedImage.TYPE_INT_ARGB);
		Graphics g = dest.getGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0,0,dest.getWidth(),dest.getHeight());
		for (int a = 0; a <= angleSpace; ++a)
			for (int d = 0; d <= distMax; ++d) {
				int value = 0xff * space[a][d] / max;
				value = (0xff << 24) | (value << 16) | (value << 8) | value;
				dest.setRGB(a, d, value);
			}
//		g.setColor(new Color(0x01ff0000));
//		int R = 5;
//		for (int a = 0; a <= angleSpace; ++a) {
//			for (int d = 0; d <= distMax; ++d) {
//				if (space[a][d] >= max * THRESHOLD)
//					g.drawOval(a - R, d - R, 2 * R, 2 * R);
//			}
//		}
		try {
			System.out.println("saving "+file);
			ImageIO.write(dest, "png", new File(file));
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void incrementSpace(int distMax, int i, int dist, short[][] space) {
		if (REJECT_DIST_LESS_THEN <= dist && dist <= distMax) {
			if(space[i][dist] != MAX_SPACE_VALUE)
				space[i][dist]++;
		}
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
