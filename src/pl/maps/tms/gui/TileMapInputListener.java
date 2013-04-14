package pl.maps.tms.gui;

import java.awt.Cursor;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import pl.mapgrid.calibration.coordinates.Coordinates;
import pl.maps.tms.TileGridProvider;
import pl.maps.tms.View;
import pl.maps.tms.gui.JTileMapView.Mode;
import pl.maps.tms.gui.Selection.Corner;
import pl.maps.tms.utils.Range;

public class TileMapInputListener implements MouseMotionListener, MouseWheelListener, MouseListener {
	private Point last;
	private final JTileMapView parent;
	private final Range zoomRange;
	public Mode mode = Mode.MOVE;
	private Corner draggedCorner;
	
	public TileMapInputListener(JTileMapView parent, TileGridProvider grid) {
		this.parent = parent;
		zoomRange = grid.getZoomRange();
		register();
	}
	
	private void register() {
		parent.addMouseMotionListener(this);
		parent.addMouseWheelListener(this);
		parent.addMouseListener(this);
	}
	public void unregister() {
		parent.removeMouseMotionListener(this);
		parent.removeMouseWheelListener(this);
		parent.removeMouseListener(this);
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		last = e.getPoint();

		Point cur = e.getPoint();
		int x = (int)(cur.x/parent.scale);
		int y = (int)(cur.y/parent.scale);
		Corner corner = parent.selection.getCornerFromPoint(x, y);
		if(corner != null) {
			parent.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
		}else{
			parent.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));			
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		Point cur = e.getPoint();
		int dx = (int) ((last.x - cur.x)/parent.scale);
		int dy = (int) ((last.y - cur.y)/parent.scale);
		last = cur;
		switch(mode) {
		case MOVE:
			parent.view.move(dx, dy);
			break;
		case SELECT:
			int x = (int)(cur.x/parent.scale);
			int y = (int)(cur.y/parent.scale);
			if(draggedCorner == null) 
				draggedCorner = parent.selection.getCornerFromPoint(x, y); //dragging by corner
			if(draggedCorner == null) {
				Rectangle rect = parent.selection.getRect();
				if(rect.contains(x,y)) {
					parent.selection.move(-dx,-dy);
				}else{ //new selection
					parent.selection.clear();
					parent.selection.setPoint(Selection.Corner.LEFT_TOP, parent.view.getCoordinates(x, y));
					draggedCorner = Selection.Corner.RIGHT_BOTTOM;
				}
			}
			if(draggedCorner != null)
				parent.selection.setPoint(draggedCorner, parent.view.getCoordinates(x, y));
		}
		parent.repaint();
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if(e.isShiftDown()) {
			if(e.getWheelRotation() > 0)
				parent.scale *= 0.9;
			else
				parent.scale *= 1.1;
		}else{
			int zoom = parent.view.getZoom();
			zoom -= e.getWheelRotation();
			zoom = zoomRange.narrow(zoom);
			
			Point p = e.getPoint();
			View view = parent.view;
			Coordinates c = view.getCoordinates(p.x, p.y);
			view.setZoom(zoom);
			view.centerAt(c);
			view.move(parent.getWidth()/2-p.x, parent.getHeight()/2-p.y);					
		}
		parent.repaint();
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		mouseReleased(arg0);
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		draggedCorner = null;
	}
}
