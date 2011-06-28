package pl.maps.tms.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JComponent;

import pl.mapgrid.calibration.coordinates.Coordinates;
import pl.maps.tms.View;

public class JTileMapView extends JComponent {
	public Coordinates center;
	public View view;
	private double scale=1.0;
	protected Coordinates firstPoint;
	protected Coordinates secondPoint;
	
	public JTileMapView() {
		addMouseMotionListener(new MouseMotionAdapter() {			
			private Point last;
			@Override
			public void mouseMoved(MouseEvent e) {
				last = e.getPoint();
				if(e.isControlDown()) 
					firstPoint = view.getCoordinates((int) (last.x/scale), (int) (last.y/scale));
			}
			@Override
			public void mouseDragged(MouseEvent e) {
				Point cur = e.getPoint();
				if(e.isControlDown()) { 
					secondPoint = view.getCoordinates((int)(cur.x/scale), (int) (cur.y/scale));
				}else{
					int dx = (int) ((last.x - cur.x)/scale);
					int dy = (int) ((last.y - cur.y)/scale);
					view.move(dx, dy);
					last = cur;
				}
				repaint();
			}
		});
		
		addMouseWheelListener(new MouseWheelListener() {			
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if(e.isShiftDown()) {
					if(e.getWheelRotation() > 0)
						scale *= 0.9;
					else
						scale *= 1.1;
				}else{
					int zoom = view.getZoom();
					zoom -= e.getWheelRotation();
					if(zoom < 0)
						zoom = 0;
					
					Point p = e.getPoint();
					Coordinates c = view.getCoordinates(p.x, p.y);
					view.setZoom(zoom);
					view.centerAt(c);
					view.move(getWidth()/2-p.x, getHeight()/2-p.y);					
				}
				repaint();
			}
		});
		
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
	    manager.addKeyEventDispatcher(new KeyEventDispatcher() {			
			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {
				if(e.getID() != KeyEvent.KEY_PRESSED)
					return false;
				switch(e.getKeyCode()){
				case KeyEvent.VK_G:
					view.showGrid = !view.showGrid;
					break;
				case KeyEvent.VK_ESCAPE:
					firstPoint=null;
					break;
				default:
					return false;
				}
				repaint();
				return false;
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
		if(firstPoint != null && secondPoint != null) {
			Point p1 = view.getPoint(firstPoint);
			Point p2 = view.getPoint(secondPoint);
			g2.setStroke(new BasicStroke(3));
			System.out.println(p1+" "+p2);
			int x = Math.min(p1.x, p2.x);
			int y = Math.min(p1.y, p2.y);
			int w = Math.abs(p2.x-p1.x);
			int h = Math.abs(p2.y-p1.y);
			
			g2.drawRect(x,y,w,h);
		}
//		g2.setStroke(new BasicStroke(2));
//		g2.setColor(Color.BLUE);
//		drawCrossHair(g2, getWidth()/2, getHeight()/2);
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

	public void centerAt(Coordinates coordinates, int zoom) {
		view.setZoom(zoom);
		view.setDimension(getSize());
		view.centerAt(coordinates);
		repaint();
	}
}
