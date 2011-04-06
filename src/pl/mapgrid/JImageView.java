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
		g.setColor(Color.RED);
		if(lines != null) { 
			for (double[] c : lines) {
				g.drawLine(0,lineY(0,c[0],c[1]),getWidth(),lineY(getWidth(),c[0],c[1]));
			}
		}
	}

	private int lineY(double x, double t, double r) {
		return (int) (-1*Math.cos(t)/Math.sin(t)*x+r/Math.sin(t));
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
	}

}
