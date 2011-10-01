package pl.maps.tms.gui;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import pl.mapgrid.calibration.coordinates.Coordinates;
import pl.maps.tms.TileGridProvider;
import pl.maps.tms.View;
import pl.maps.tms.utils.Range;

public class ViewModeListener implements MouseMotionListener, MouseWheelListener {
	
	private Point last;
	private final JTileMapView parent;
	private final Range zoomRange;	

	public ViewModeListener(JTileMapView parent, TileGridProvider grid) {
		this.parent = parent;
		zoomRange = grid.getZoomRange();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		last = e.getPoint();
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		Point cur = e.getPoint();
		int dx = (int) ((last.x - cur.x)/parent.scale);
		int dy = (int) ((last.y - cur.y)/parent.scale);
		parent.view.move(dx, dy);
		last = cur;
		parent.repaint();
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
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
}
