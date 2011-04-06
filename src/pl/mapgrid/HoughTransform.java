package pl.mapgrid;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class HoughTransform {
	private static final double THRESHOLD = 0.3;
	private static final short MAX_SPACE_VALUE = Short.MAX_VALUE;
	private static final int R_MIN = 5;

	public BufferedImage tranform2(BufferedImage src) {
		long start = System.currentTimeMillis();
		coord.clear();
		
		double aberration = Math.toRadians(10);
		double tstep=Math.toRadians(0.05);
		
		int twidth = (int) (2*aberration/tstep);
		double[] tv_0 = new double[twidth];
		double[] tv_90 = new double[twidth];
		double[] sin = new double[twidth];
		double[] cos = new double[twidth];
		
		double tmin_0 = -aberration; 
//		double tmax_0 = +aberration;

		double tmin_90 = Math.PI/2-aberration; 
//		double tmax_90 = Math.PI/2+aberration;
		
		for (int i = 0; i < twidth; i++) {
			tv_0[i] = tmin_0+tstep*i;
			tv_90[i] = tmin_90+tstep*i;
			sin[i] = Math.sin(tv_0[i]);
			cos[i] = Math.cos(tv_0[i]);
			//sin_90[i] = cos[i]
			//cos_90[i] = -sin[i]
		}
		int w = src.getWidth();
		int h = src.getHeight();
		int rmax=Math.max(w,h);
		short[][] space_0 = new short[twidth+1][rmax+1];
		short[][] space_90 = new short[twidth+1][rmax+1];
		for(int x=0;x<w;++x) {
			if(x % 100 ==0)
				System.out.println( 100*x/w);
			for (int y = 0; y < h; ++y) {
				if (isPoint(src, x, y)) {
					for (int i = 0; i < twidth; ++i) {
						int r_0 =  (int) (y * sin[i] + x * cos[i]);
						int r_90 = (int) (y * cos[i] - x * sin[i]);
						incrementSpace(rmax, i, r_0, space_0);
						incrementSpace(rmax, i, r_90, space_90);
					}
				}
			}
		}
		long now= System.currentTimeMillis();
		System.out.println("Hought took: "+(now-start));
		short max = 0;
		for(int t=0;t<=twidth;++t)
			for(int r=0;r<=rmax;++r) {
				if(max < space_0[t][r])
					max = space_0[t][r];
				if(max < space_90[t][r])
					max = space_90[t][r];
			}
		writeHoughImage("space0.png", twidth, rmax, space_0, max);
		writeHoughImage("space90.png", twidth, rmax, space_90, max);
		for(int t=0;t<=twidth;++t)
			for(int r=0;r<=rmax;++r) {
				if(space_0[t][r] >= max*0.3)
					coord.add(new double[]{tv_0[t],r});
				if(space_90[t][r] >= max*THRESHOLD) 
					coord.add(new double[]{tv_90[t],r});
			}
		return null;		
	}

	private void writeHoughImage(String file, int twidth, int rmax, short[][] space,
			short max) {
		BufferedImage dest = new BufferedImage(twidth+1, rmax+1, BufferedImage.TYPE_INT_BGR);
		for(int t=0;t<=twidth;++t)
			for(int r=0;r<=rmax;++r) {
				int value = 0xff * space[t][r]/max;
				value = (value<<16)|(value<<8)|value;
				dest.setRGB(t, r, value);
			}
		Graphics g = dest.getGraphics();
		g.setColor(Color.RED);
		int R = 5;
		for (int t = 0; t <= twidth; ++t) {
			for (int r = 0; r <= rmax; ++r) {
				if (space[t][r] >= max * THRESHOLD)
					g.drawOval(t - R, r - R, 2 * R, 2 * R);
			}
		}
		try {
			System.out.println("saving "+file);
			ImageIO.write(dest, "png", new File(file));
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void incrementSpace(int rmax, int i, int r, short[][] space) {
		if (R_MIN <= r && r <= rmax) {
			if(space[i][r] != MAX_SPACE_VALUE)
				space[i][r]++;
		}
	}
	private boolean isPoint(BufferedImage src, int x, int y) {
		int rgb = src.getRGB(x, y);
		int r = (rgb >> 16) & 0xFF;
		int g = (rgb >>  8) & 0xFF;
		int b = (rgb >>  0) & 0xFF;		
		return (r+g+b < 0x80*3);
	}
	
	public List<double[]> coord = new ArrayList<double[]>(); 
	
	public static int pixelValue(BufferedImage src, int x, int y) {
		int rgb = src.getRGB(x, y);
		int r = (rgb >> 16) & 0xFF;
		int g = (rgb >>  8) & 0xFF;
		int b = (rgb >>  0) & 0xFF;		
		return (r+g+b)/3;
	}

}
