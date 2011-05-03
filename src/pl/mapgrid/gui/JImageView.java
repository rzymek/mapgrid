package pl.mapgrid.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import pl.mapgrid.app.Main;
import pl.mapgrid.calibration.coordinates.Coordinates;
import pl.mapgrid.grid.UTMGraphics;
import pl.mapgrid.mask.MaskGrid;
import pl.mapgrid.shape.GeoShape;
import pl.mapgrid.shape.Shape;
import pl.mapgrid.shape.ShapeGraphics;
import pl.mapgrid.utils.PopupListener;

public class JImageView extends JComponent implements Observer {
	private BufferedImage image = null;
	private BufferedImage grid = null;
	private BufferedImage shape = null;
	private boolean showGrid = true;
	private final Main main;
	private boolean zoomToFit;
	private float zoom = 1;

	public JImageView(Main main) {
		this.main = main;
		JPopupMenu menu = new JPopupMenu();
		JMenuItem item = menu.add("Pokaż cały");
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				toggleZoomToFit();
			}
		});
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
	    manager.addKeyEventDispatcher(new KeyEventDispatcher() {			
			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {
				if(e.getID() != KeyEvent.KEY_PRESSED)
					return false;
				switch(e.getKeyCode()){
				case KeyEvent.VK_OPEN_BRACKET:
					zoom = zoom / 1.1f; 
					break;
				case KeyEvent.VK_CLOSE_BRACKET:
					zoom = zoom * 1.1f; 
					break;
				case KeyEvent.VK_BACK_SLASH:
					zoom = 1; 
					break;
				default:
					return false;
				}
				revalidate();
				return false;
			}
		});
		addMouseListener(new PopupListener(menu));
		add(menu);
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		if (image == null)
			return;
		int[] bounds = { image.getWidth(), image.getHeight() };
		if (zoomToFit) {
			int w = getParent().getWidth();
			int h = getParent().getHeight();
			float scaleW = (float) image.getWidth() / w;
			float scaleH = (float) image.getHeight() / h;
			float scale = Math.max(scaleW, scaleH);
			bounds[0] /= scale;
			bounds[1] /= scale;
		} else {
			bounds[0] = (int) (image.getWidth() * zoom);
			bounds[1] = (int) (image.getHeight() * zoom);
		}
		g.drawImage(image, 0, 0, bounds[0], bounds[1], this);
		if (shape != null)
			g.drawImage(shape, 0, 0, bounds[0], bounds[1], this);
		if (showGrid != false && grid != null)
			g.drawImage(grid, 0, 0, bounds[0], bounds[1], this);
	}
	@Override
	public Dimension getPreferredSize() {
		if(image == null || zoomToFit)
			return super.getPreferredSize();
		return new Dimension((int) (image.getWidth()*zoom),(int)(image.getHeight()*zoom));
	}
	public boolean toggleZoomToFit() {
		zoomToFit = !zoomToFit;
		repaint();
		return zoomToFit;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
		this.grid = null;
		this.shape = null;
		setPreferredSize(new Dimension(image.getWidth(this), image.getHeight(this)));
		repaint();
		revalidate();
	}

	public BufferedImage getImage() {
		return image;
	}

	public BufferedImage getImageWithGrid() {
		if (showGrid) {
			BufferedImage image = new BufferedImage(this.image.getWidth(), this.image.getHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics g = image.getGraphics();
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, image.getWidth(), image.getHeight());
			g.drawImage(this.image, 0, 0, this);
			g.drawImage(this.grid, 0, 0, this);
			g.drawImage(this.shape, 0, 0, this);
			return image;
		} else {
			return image;
		}
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

	public BufferedImage getGrid() {
		return grid;
	}

	public void setGrid(List<int[]> vertical, List<int[]> horizontal, int firstEasting, int firstNorthing) {
		grid = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = (Graphics2D) grid.getGraphics();
		UTMGraphics.Config cfg = main.getUtmConfig();
		UTMGraphics utmg = new UTMGraphics(g, cfg, grid.getWidth(), grid.getHeight());
		utmg.drawGrid(vertical, horizontal, firstEasting, firstNorthing);
		setShowGrid(true);
	}

	public void setShapes(List<Shape<Coordinates>> shapes, ShapeGraphics.Config config) {
		shape = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2 = (Graphics2D) shape.getGraphics();
		ShapeGraphics g = new ShapeGraphics(g2, config);
		for (Shape<? extends Coordinates> shape : shapes) {
			Shape<Point> p = GeoShape.project(shape, main.calibration, image.getWidth(), image.getHeight());
			g.draw(p);
		}
		repaint();
	}

	public void rotate(double rot) {
		AffineTransform transform = new AffineTransform();
		transform.rotate(rot);
		AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BICUBIC);
		image = op.filter(image, null);
		if (shape != null)
			shape = op.filter(shape, null);
		if (grid != null)
			grid = op.filter(grid, null);
		repaint();
	}
}
