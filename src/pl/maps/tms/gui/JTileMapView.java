package pl.maps.tms.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.event.ChangeListener;

import pl.mapgrid.calibration.coordinates.Coordinates;
import pl.maps.tms.TileGridProvider;
import pl.maps.tms.View;
import pl.maps.tms.cache.AsyncTileCache;
import pl.maps.tms.cacheing.AsyncFetchListener;
import pl.maps.tms.cacheing.TileSpec;
import pl.maps.tms.gui.Selection.Corner;
import pl.maps.tms.gui2.JViewCacheStatus;

public class JTileMapView extends JComponent implements AsyncFetchListener {
	public static enum Mode { MOVE, SELECT }
	public View view = null;
	double scale=1.0;
	public Selection selection;
	AsyncTileCache cachingProvider;
	private TileMapInputListener listener;
	
	public void setProvider(TileGridProvider grid, AsyncTileCache images) {
		this.cachingProvider = images;
		if(listener != null)
			listener.unregister();
		listener = new TileMapInputListener(this, grid);
		View newView = new View(getSize(), grid, images);
		if(view != null) {			
			Coordinates lt = view.getCoordinates(0,0);//getWidth()/2, getHeight()/2);
			newView.setZoom(view.getZoom());
			newView.setLeftTop(lt);
		}
		view = newView;
		selection = new Selection(view);
		repaint();
	}
	
	public void exportSelection(File file, JViewCacheStatus status, int zoom) {
		cachingProvider.removeListener(status);
		cachingProvider.addListener(status);
		Export export = new Export(cachingProvider, file);		
		Coordinates p1 = selection.getPoint(Corner.LEFT_TOP);
		Coordinates p2 = selection.getPoint(Corner.RIGHT_BOTTOM);
		View v = export.createView(p1, p2, zoom);
		status.setView(v, cachingProvider);
		export.export(p1, p2, v);
		status.repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0,0,getWidth(), getHeight());
		if(view == null)
			return;
		Dimension s = getSize();
		s.width /= scale;
		s.height/= scale;
		view.setDimension(s);
		Graphics2D g2 = (Graphics2D) g;
		g2.scale(scale, scale);
		view.paint(g);
		g2.setColor(Color.RED);
		if(selection.isValid()) {
			Rectangle rect = selection.getRect();			
			g2.setStroke(new BasicStroke(3));
			g2.drawRect(rect.x, rect.y, rect.width, rect.height);
			g2.setStroke(new BasicStroke(2));
			Color fill = new Color(255,0,0,128);
			Color cornerBorder = Color.BLACK;
			g2.setColor(fill);
			for (Corner corner : Selection.Corner.values()) {
				rect = selection.getCorner(corner);
				g2.setColor(fill);
				g2.fillRect(rect.x, rect.y, rect.width, rect.height);
				g2.setColor(cornerBorder);
				g2.drawRect(rect.x, rect.y, rect.width, rect.height);
			}
		}
	}

	private void drawCrossHair(Graphics2D g, int x, int y) {
		int r = 30;
		g.setStroke(new BasicStroke(2));
		g.setColor(Color.BLUE);
		g.drawArc(x - r / 2, y - r / 2, r, r, 0, 360);
		g.drawLine(x - r / 2, y, x + r / 2, y);
		g.drawLine(x, y - r / 2, x, y + r / 2);
	}
	
	public void centerAt(Coordinates coordinates, int zoom) {
		view.setZoom(zoom);
		view.setDimension(getSize());
//		view.centerAt(coordinates);
		//tmp{
		Coordinates lt = view.getCoordinates(0,0);//getWidth()/2, getHeight()/2);
		view.setLeftTop(lt);
		//}
		repaint();
	}

	@Override
	public void imageFetched(Image image, TileSpec spec) throws Exception {
		repaint();
	}

	public void setMode(Mode mode) {
		listener.mode = mode;
	}

	public void addSelectionChangedListener(ChangeListener listener) {
		selection.addChangeListener(listener);
	}
	public void removeSelectionChangedListener(ChangeListener listener) {
		selection.removeChangeListener(listener);
	}

	public Selection getSelection() {
		return selection;
	}
}
