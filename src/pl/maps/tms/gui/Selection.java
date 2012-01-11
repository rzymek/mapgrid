package pl.maps.tms.gui;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import pl.mapgrid.calibration.coordinates.Coordinates;
import pl.maps.tms.View;

public class Selection {
	public static enum Corner {
		LEFT_TOP, RIGHT_BOTTOM 
	}
	private static final int CORNER_SIZE = 15;
	private final Coordinates[] points = new Coordinates[2];
	private final View view;
	
	public Selection(View view) {
		this.view = view;		
	}
	private List<ChangeListener> changeListeners = new ArrayList<ChangeListener>();
	public float ratio = 0f;

	public Rectangle getRect() {
		if(!isValid())
			return new Rectangle();
		Point p1 = view.getPoint(points[0]);
		Point p2 = view.getPoint(points[1]);
		int x = Math.min(p1.x, p2.x);
		int y = Math.min(p1.y, p2.y);
		int w = Math.abs(p2.x-p1.x);
		int h = Math.abs(p2.y-p1.y);
		return new Rectangle(x, y, w, h);
	}

	public boolean isValid() {
		return points[0] != null && points[1] != null;
	}

	public void clear() {		
		for (Corner corner : Corner.values()) 
			setPoint(corner, null);
	}

	public void setPoint(Corner corner, Coordinates coordinates) {		
		points[corner.ordinal()] = coordinates;
		keepAspectRatio();
		for (ChangeListener listener: changeListeners) 
			listener.stateChanged(new ChangeEvent(this));
	}

	private void keepAspectRatio() {
		if(view != null && ratio != 0f && isValid()) {
			Point p1 = view.getPoint(points[0]);
			Point p2 = view.getPoint(points[1]);
			p2.y = (int) (p1.y + (float)Math.abs(p2.x-p1.x)/ratio);
			points[1] = view.getCoordinates(p2.x, p2.y);
		}
	}
	
	public Coordinates getPoint(Corner corner) {
		return points[corner.ordinal()];
	}
	
	public Rectangle getCorner(Corner corner) {
		if(!isValid())
			return null;
		Point p;
		switch (corner) {
		case LEFT_TOP:
			p = view.getPoint(points[0]);
			return new Rectangle(p.x-CORNER_SIZE, p.y-CORNER_SIZE, CORNER_SIZE, CORNER_SIZE);
		case RIGHT_BOTTOM:
			p = view.getPoint(points[1]);
			return new Rectangle(p.x, p.y, CORNER_SIZE, CORNER_SIZE);
		}
		throw new RuntimeException("INTERNAL: invalid corner: "+corner);
	}
	
	public Corner getCornerFromPoint(int x, int y) {
		if(!isValid())
			return null;
		for (Corner corner : Corner.values()) {
			Rectangle rect = getCorner(corner);
			if(rect.contains(x, y))
				return corner;			
		}
		return null;
	}

	public void addChangeListener(ChangeListener listener) {
		changeListeners.add(listener);
	}
	public void removeChangeListener(ChangeListener listener) {
		changeListeners.remove(listener);
	}

	public void setRatio(float ratio) {
		this.ratio = ratio;
		keepAspectRatio();
	}

	public void move(int dx, int dy) {
		for(int i=0;i<points.length;++i) {			
			Point p = view.getPoint(points[i]);
			p.x += dx;
			p.y += dy;
			points[i] = view.getCoordinates(p.x, p.y);
		}
	}

	public double getWidth() {
		Coordinates lt = getPoint(Corner.LEFT_TOP);
		Coordinates rb = getPoint(Corner.RIGHT_BOTTOM);
		return Math.abs(rb.getX() - lt.getX());
	}

	public double getHeight() {
		Coordinates lt = getPoint(Corner.LEFT_TOP);
		Coordinates rb = getPoint(Corner.RIGHT_BOTTOM);
		return Math.abs(rb.getY() - lt.getY());
	}

}
