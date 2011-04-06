package pl.mapgrid;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

public class JImageView extends JComponent implements Observer {
	private BufferedImage image = null;
	private List<double[]> lines;

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (image == null)
			return;
		g.drawImage(image, 0, 0, null);
		g.setColor(new Color(0x80ff0000, true));
//		if(lines != null) { 
//			for (double[] c : lines) {
//				g.drawLine(-1,lineY(0,c[0],c[1])-1,getWidth()-1,lineY(getWidth(),c[0],c[1])+1);
//				g.drawLine(0,lineY(0,c[0],c[1]),getWidth(),lineY(getWidth(),c[0],c[1]));
//				g.drawLine(+1,lineY(0,c[0],c[1])+1,getWidth()+1,lineY(getWidth(),c[0],c[1])+1);
//			}
//		}
	}

	private int lineY(double x, double a, double d) {		
		return (int) (d/Math.sin(a)-Math.cos(a)/Math.sin(a)*x);
	}
	private int lineX(int y, double a, double d) {
		return (int) (d/Math.cos(a)-Math.sin(a)/Math.cos(a)*y);
	}

	public void setImage(BufferedImage image) {
		this.image = image;
		setPreferredSize(new Dimension(image.getWidth(this), image.getHeight(this)));
		repaint();
	}

	public BufferedImage getImage() {
		return image;
	}

	@Override
	public void update(Observable o, Object arg) {
	}

	public void setLines(List<double[]> coord) {
		int WIDTH=3;
		this.lines = coord;	
		System.out.println("JImageView.setLines()");
		for (double[] ds : coord) {
			double a = ds[0];
			double d = ds[1];
			if(Math.toRadians(80) <= a && a <= Math.toRadians(100)) {
				for (int x = 0; x < image.getWidth(); ++x) {
					int y = lineY(x, a, d);
					if (0 <= y && y < image.getHeight()) {
						int rgb = image.getRGB(x, y - WIDTH);
						for (int i = -WIDTH; i < WIDTH; ++i) {
							int current = image.getRGB(x, y + i);
							int diff = HoughTransform.pixelValue(current) - HoughTransform.pixelValue(rgb);
							if (diff < -0x30) {
								image.setRGB(x, y + i, rgb);
							} else if (diff > 0x10) {
								rgb = image.getRGB(x, y - i);
							}
						}
					}
				}
			}else{
				for (int y = 0; y < image.getHeight(); ++y) {
					int x = lineX(y, a, d);
					if (0 <= x && x < image.getWidth()) {
						int rgb = image.getRGB(x-WIDTH, y);
						for (int i = -WIDTH; i < WIDTH; ++i) {
							int current = image.getRGB(x+i, y);
							int diff = HoughTransform.pixelValue(current) - HoughTransform.pixelValue(rgb);
							if (diff < -0x30) {
								image.setRGB(x+i, y, rgb);
							} else if (diff > 0x10) {
								rgb = image.getRGB(x+i, y);
							}
						}
					}
				}
				
			}
		}
		try {
			ImageIO.write(image, "png", new File("output.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
