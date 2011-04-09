package pl.mapgrid.utils;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JViewport;
import javax.swing.event.MouseInputAdapter;

public class DragableViewportMouseListener extends MouseInputAdapter {
	int xDiff, yDiff;
	JViewport viewport;
	public DragableViewportMouseListener(JViewport viewport) {
		this.viewport = viewport;
	}

	public void mouseDragged(MouseEvent e) {
		Point p = viewport.getViewPosition();
		int newX = p.x - (e.getX() - xDiff);
		int newY = p.y - (e.getY() - yDiff);

		int maxX = viewport.getView().getWidth() - viewport.getWidth();
		int maxY = viewport.getView().getHeight() - viewport.getHeight();
		if (newX < 0)
			newX = 0;
		if (newX > maxX)
			newX = maxX;
		if (newY < 0)
			newY = 0;
		if (newY > maxY)
			newY = maxY;

		viewport.setViewPosition(new Point(newX, newY));
	}

	public void mousePressed(MouseEvent e) {
		viewport.getView().setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
		xDiff = e.getX();
		yDiff = e.getY();
	}

	public void mouseReleased(MouseEvent e) {
		viewport.getView().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
}
