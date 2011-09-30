package pl.maps.tms.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import pl.mapgrid.calibration.coordinates.Coordinates;
import pl.maps.tms.TileGridProvider;
import pl.maps.tms.View;
import pl.maps.tms.cache.AsyncTileCache;
import pl.maps.tms.cacheing.AsyncFetchListener;
import pl.maps.tms.cacheing.TileSpec;
import pl.maps.tms.gui2.JViewCacheStatus;

public class JTileMapView extends JComponent implements AsyncFetchListener {
	public View view = null;
	double scale=1.0;
	Coordinates firstPoint;
	Coordinates secondPoint;
	AsyncTileCache cachingProvider;
	private TileMapInputListener listener;
	
	public JTileMapView() {
	}

	public void setProvider(TileGridProvider grid, AsyncTileCache images) {
		this.cachingProvider = images;
		if(listener != null)
			listener.unregister();
		listener = new TileMapInputListener(this, grid);
		View newView = new View(getSize(), grid, images);
		if(view != null) {			
			Coordinates center = view.getCoordinates(getWidth()/2, getHeight()/2);
			newView.centerAt(center);
			newView.setZoom(view.getZoom());
		}
		view = newView;
		repaint();
	}
	
	public void exportSelection(JViewCacheStatus status, int zoom) {
		System.out.println("JTileMapView.exportSelection()");
		cachingProvider.removeListener(status);
		cachingProvider.addListener(status);
		Export export = new Export(view, cachingProvider);
		View v = export.createView(firstPoint, secondPoint, zoom);
		status.setView(v, cachingProvider);
		export.export(firstPoint, secondPoint, v);
		System.out.println("Done");
		JOptionPane.showMessageDialog(this, "Saved");		
	}

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
		if(firstPoint != null && secondPoint != null) {
			Point p1 = view.getPoint(firstPoint);
			Point p2 = view.getPoint(secondPoint);
			int x = Math.min(p1.x, p2.x);
			int y = Math.min(p1.y, p2.y);
			int w = Math.abs(p2.x-p1.x);
			int h = Math.abs(p2.y-p1.y);
			
			g2.setStroke(new BasicStroke(3));
			g2.drawRect(x,y,w,h);
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
		view.centerAt(coordinates);
		repaint();
	}

	@Override
	public void imageFetched(Image image, TileSpec spec) throws Exception {
		repaint();
	}
}
