package pl.mapgrid;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

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
		return (int) (-1*Math.cos(a)/Math.sin(a)*x+d/Math.sin(a));
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
		this.lines = coord;	
		System.out.println("JImageView.setLines()");
		for (double[] ds : coord) {
			double a=ds[0];
			double d=ds[1];
			for(int x=0;x<image.getWidth();++x) {
				int y = lineY(x,a,d);
				if(0<=y && y<image.getHeight()) {
					int pdy = 0;
					for(int dy=-3;dy<3;++dy) {
						int pixel = HoughTransform.pixelValue(image, x, y+dy);
						if(pixel < 0x80) {
							int rgb=image.getRGB(x, y-5);
								//0xff0000;
							for(int dy1 = dy; ; dy1 -= Math.signum(dy)) {
								if (0 <= x + dy1 && x + dy1 < image.getWidth())
									image.setRGB(x,y+dy1, rgb);
								else 
									break;
								if(Math.abs(dy1) == 0)
									break;
							}
						}
						pdy = dy;
					}
				}
			}
		}
	}

}
