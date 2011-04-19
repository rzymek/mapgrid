package pl.mapgrid.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;

import pl.mapgrid.app.Main;
import pl.mapgrid.grid.UTMGraphics;
import pl.mapgrid.mask.MaskGrid;

public class JImageView extends JComponent implements Observer {
	private BufferedImage image = null;
	private BufferedImage grid;
	private boolean showGrid = true;
	private float zoom = 1;
	private final Main main;

	public JImageView(Main main) {
		this.main = main;
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0,0,getWidth(), getHeight());
		if (image == null)
			return;	
		g.drawImage(image, 0, 0, (int) (image.getWidth() * zoom), (int) (image.getHeight() * zoom), this);
		if (showGrid != false && grid != null)
			g.drawImage(grid, 0, 0, (int) (image.getWidth() * zoom), (int) (image.getHeight() * zoom), this);
	}

	public void setImage(BufferedImage image) {
		this.image = image;
		setPreferredSize(new Dimension(image.getWidth(this), image.getHeight(this)));
		zoom = 1;
		repaint();
		revalidate();
	}

	public BufferedImage getImage() {
		return image;
	}
	public BufferedImage getImageWithGrid() {
		BufferedImage image = new BufferedImage(
				this.image.getWidth(), 
				this.image.getHeight(), 
				BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0,0,image.getWidth(), image.getHeight());
		g.drawImage(this.image, 0,0, this);
		g.drawImage(this.grid, 0,0, this);
		return image;
	}

	@Override
	public void update(Observable o, Object arg) {
	}

	public void setLines(List<double[]> lines) {
		grid = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = (Graphics2D) grid.getGraphics();
		g.setColor(new Color(255, 0, 0, 128));
		g.setStroke(new BasicStroke(6));
		int x1, x2, y1, y2;
		final double d45 = Math.PI / 8;
		for (double[] c : lines) {
			if (-d45 < c[0] && c[0] < d45) {
				// vertical
				y1 = 0;
				x1 = MaskGrid.lineX(y1, c[0], c[1]);
				y2 = image.getWidth();
				x2 = MaskGrid.lineX(y2, c[0], c[1]);
				g.drawLine(x1, y1, x2, y2);
			} else {
				// horizontal
				x1 = 0;
				y1 = MaskGrid.lineY(x1, c[0], c[1]);
				x2 = image.getWidth();
				y2 = MaskGrid.lineY(x2, c[0], c[1]);
				g.drawLine(x1, y1, x2, y2);
			}
		}
		repaint();
	}

	public void toogleShowGrid() {
		showGrid = !showGrid;
		repaint();
	}

	public void setShowGrid(boolean b) {
		showGrid = b;
		repaint();
	}

	public void setZoom(float zoom) {
		this.zoom = zoom;
		setPreferredSize(new Dimension((int)(image.getWidth(this)*zoom), (int)(image.getHeight(this)*zoom)));
		revalidate();
		repaint();
	}
	
	public BufferedImage getGrid() {
		return grid;
	}

	public void setGrid(List<int[]> vertical, List<int[]> horizontal,
			int firstEasting, int firstNorthing) {
		grid = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);		
		Graphics2D g = (Graphics2D) grid.getGraphics();
		UTMGraphics.Config cfg = main.utmConfig;
		UTMGraphics utmg = new UTMGraphics(g, cfg, grid.getWidth(), grid.getHeight());
		utmg.drawGrid(vertical, horizontal, firstEasting, firstNorthing);
		setShowGrid(true);
	}
}
