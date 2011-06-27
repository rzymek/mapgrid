package pl.maps.tms;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JComponent;

import pl.mapgrid.calibration.coordinates.Coordinates;

public class JTileMapView extends JComponent {
	public Coordinates center;
	public View view;
	private int zoom=0;
	private double scale=1.0;
	
	public JTileMapView() {
		addMouseMotionListener(new MouseMotionAdapter() {			
			private Point last;
			@Override
			public void mouseMoved(MouseEvent e) {
				last = e.getPoint();
			}
			@Override
			public void mouseDragged(MouseEvent e) {
				Point cur = e.getPoint();
				int dx = (int) ((last.x - cur.x)/scale);
				int dy = (int) ((last.y - cur.y)/scale);
				view.move(dx, dy);
				last = cur;
				repaint();
			}
		});
		
		addMouseWheelListener(new MouseWheelListener() {			
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if(e.isControlDown()) {
					if(e.getWheelRotation() > 0)
						scale *= 0.9;
					else
						scale *= 1.1;
				}else{
					zoom -= e.getWheelRotation();
					if(zoom < 0)
						zoom = 0;
					Point p = e.getPoint();
					Coordinates c = view.getCoordinates(p.x, p.y);
					Point[] pos = view.tileProvider.getTile(c, zoom);
					view.setPosition(pos[0], pos[1]);
					view.setZoom(zoom);
					view.move(-p.x, -p.y);					
				}
				repaint();
			}
		});
		view = new View(getSize(), this);
	}
	
	protected void paintComponent(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0,0,getWidth(), getHeight());
		g.setColor(Color.RED);
		Dimension s = getSize();
		s.width /= scale;
		s.height/= scale;
		view.setDimension(s);
		Graphics2D g2 = (Graphics2D) g;
		g2.scale(scale, scale);
		view.paint(g);
	}

	private void drawCrossHair(Graphics g, int x, int y) {
		int r = 30;
		g.drawArc(x - r / 2, y - r / 2, r, r, 0, 360);
		g.drawLine(x - r / 2, y, x + r / 2, y);
		g.drawLine(x, y - r / 2, x, y + r / 2);
	}
	
	@Override
	public boolean imageUpdate(Image img, int infoflags, int x, int y, int w, int h) {
		repaint();
		return true;
	}

	public void show(Coordinates coordinates) {
		Point[] pos = view.tileProvider.getTile(coordinates, zoom);
		view.setPosition(pos[0], pos[1]);
		view.setZoom(zoom);
		view.move(-getWidth()/2, -getHeight()/2);
	}
}
