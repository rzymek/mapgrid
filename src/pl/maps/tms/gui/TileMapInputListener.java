package pl.maps.tms.gui;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import pl.mapgrid.calibration.coordinates.Coordinates;
import pl.maps.tms.TileGridProvider;
import pl.maps.tms.View;
import pl.maps.tms.gui2.GetMapsFrame;
import pl.maps.tms.utils.Range;

public class TileMapInputListener implements MouseMotionListener, MouseWheelListener, KeyEventDispatcher {
	private Point last;
	private final JTileMapView parent;
	private final Range zoomRange;
	
	public TileMapInputListener(JTileMapView parent, TileGridProvider grid) {
		this.parent = parent;
		zoomRange = grid.getZoomRange();
		register();
	}
	
	private void register() {
		parent.addMouseMotionListener(this);
		parent.addMouseWheelListener(this);
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
	    manager.addKeyEventDispatcher(this);
	}
	public void unregister() {
		parent.removeMouseMotionListener(this);
		parent.removeMouseWheelListener(this);
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
	    manager.removeKeyEventDispatcher(this);
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		last = e.getPoint();
		if(e.isControlDown()) {
			int x = (int) (last.x/parent.scale);
			int y = (int) (last.y/parent.scale);
			parent.firstPoint = parent.view.getCoordinates(x, y);
		}
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		Point cur = e.getPoint();
		if(e.isControlDown()) { 
			int x = (int)(cur.x/parent.scale);
			int y = (int) (cur.y/parent.scale);
			parent.secondPoint = parent.view.getCoordinates(x, y);
		}else{
			int dx = (int) ((last.x - cur.x)/parent.scale);
			int dy = (int) ((last.y - cur.y)/parent.scale);
			parent.view.move(dx, dy);
			last = cur;
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
	public boolean dispatchKeyEvent(KeyEvent e) {
		if(e.getID() != KeyEvent.KEY_PRESSED)
			return false;
		switch(e.getKeyCode()){
		case KeyEvent.VK_G:
			parent.view.showGrid = !parent.view.showGrid;
			break;
		case KeyEvent.VK_ESCAPE:
			parent.firstPoint=null;
			break;					
//		case KeyEvent.VK_E:
//			Export export = new Export(parent.view, parent.cachingProvider);
//			View v = export.createView(parent.firstPoint, parent.secondPoint);
//			GetMapsFrame.frame.getCacheStatus().setView(v, parent.cachingProvider);
//			break;					
		default:
			return false;
		}
		parent.repaint();
		return false;
	}

}
