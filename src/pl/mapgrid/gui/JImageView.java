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
	private BufferedImage grid;
	private boolean showGrid = true;
	private float zoom = 1;

	public JImageView() {
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
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

	@Override
	public void update(Observable o, Object arg) {
	}

	public void setLines(List<double[]> lines) {
		grid = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		Graphics g = grid.getGraphics();
		g.setColor(new Color(255, 0, 0, 128));
		int x1, x2, y1, y2;
		final double d45 = Math.PI / 8;
		for (double[] c : lines) {
			if (-d45 < c[0] && c[0] < d45) {
				// vertical
				y1 = 0;
				x1 = MaskGrid.lineX(y1, c[0], c[1]);
				y2 = image.getWidth();
				x2 = MaskGrid.lineX(y2, c[0], c[1]);
				g.fillPolygon(new int[] { x1 - 5, x1 + 5, x2 + 5, x2 - 5 }, new int[] { y1, y1, y2, y2 }, 4);
			} else {
				// horizontal
				x1 = 0;
				y1 = MaskGrid.lineY(x1, c[0], c[1]);
				x2 = image.getWidth();
				y2 = MaskGrid.lineY(x2, c[0], c[1]);
				g.fillPolygon(new int[] { x1, x1, x2, x2 }, new int[] { y1 - 5, y1 + 5, y2 + 5, y2 - 5 }, 4);
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
}
