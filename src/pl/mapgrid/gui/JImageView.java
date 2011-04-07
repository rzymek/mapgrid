package pl.mapgrid.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;

import pl.mapgrid.MaskGrid;

public class JImageView extends JComponent implements Observer {
	private BufferedImage image = null;
	private List<double[]> lines;

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (image == null)
			return;
		g.drawImage(image, 0, 0, null);
		drawLines(g);
	}

	private void drawLines(Graphics g) {
		g.setColor(new Color(0x80ff0000, true));
		if(lines != null) { 
			for (double[] c : lines) {
				int x1 = 0;
				int y1 = MaskGrid.lineY(x1,c[x1],c[1]);
				int x2 = getWidth();
				int y2 = MaskGrid.lineY(x2,c[x1],c[1]);
				drawLine(g, x1, y1, x2, y2, 5);
			}
		}
	}

	private void drawLine(Graphics g, int x1, int y1, int x2, int y2, int w) {
		int width = w/2;
		for(int i=-width;i<=width;++i)
			g.drawLine(x1+i,y1+i,x2+i,y2+i);
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

	public void setLines(List<double[]> lines) {
		this.lines = lines;	
		repaint();
	}

	public List<double[]> getLines() {
		return lines;
	}

}
