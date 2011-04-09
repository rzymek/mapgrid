package pl.mapgrid.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import pl.mapgrid.MaskGrid;

public class JImageView extends JComponent implements Observer, MouseMotionListener, MouseListener {
	private BufferedImage image = null;
	private boolean showGrid =true;
	private BufferedImage grid;
	private Point dragStart;
	private Point mousePosition;

	public JImageView() {
		setAutoscrolls(true);
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (image == null)
			return;
		g.drawImage(image, 0, 0, this);
		drawLines(g);
	}

	private void drawLines(Graphics g) {
		if(showGrid == false || grid == null)
			return;
		g.drawImage(grid, 0, 0, this);
	}

	public void setImage(BufferedImage image) {
		this.image = image;
		setPreferredSize(new Dimension(image.getWidth(this), image.getHeight(this)));
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
		g.setColor(new Color(255,0,0,128));
		int x1,x2,y1,y2;
		final double d45 = Math.PI/8;
		for (double[] c : lines) {
			if(-d45 < c[0] && c[0] < d45) {
				//vertical
				y1 = 0;
				x1 = MaskGrid.lineX(y1,c[0],c[1]);
				y2 = image.getWidth();
				x2 = MaskGrid.lineX(y2,c[0],c[1]);
				g.fillPolygon(
						new int[]{x1-5,x1+5,x2+5,x2-5},
						new int[]{y1,y1,y2,y2},
						4);
			}else{
				//horizontal
				x1 = 0;
				y1 = MaskGrid.lineY(x1,c[0],c[1]);
				x2 = image.getWidth();
				y2 = MaskGrid.lineY(x2,c[0],c[1]);
				g.fillPolygon(
						new int[]{x1,x1,x2,x2},
						new int[]{y1-5,y1+5,y2+5,y2-5},
						4);
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

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(dragStart != null) {
			JScrollPane pane = (JScrollPane) getParent();
			JViewport viewport = pane.getViewport();
			Point point = e.getPoint();
//			point.x -= dragStart.x;
//			point.y -= dragStart.y;
			System.out.println("JImageView.mouseMoved()");
			viewport.setViewPosition(point);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		dragStart = e.getPoint();
	}

	@Override
	public void mouseReleased(MouseEvent paramMouseEvent) {
		dragStart = null;
	}

	@Override
	public void mouseEntered(MouseEvent paramMouseEvent) {
	}

	@Override
	public void mouseExited(MouseEvent paramMouseEvent) {
	}

}
