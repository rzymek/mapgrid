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

public class JTileMapView extends JComponent implements AsyncFetchListener {
	View view;
	double scale=1.0;
	Coordinates firstPoint;
	Coordinates secondPoint;
	private final AsyncTileCache cachingProvider;
	
	public JTileMapView(TileGridProvider grid, AsyncTileCache images) {
		this.cachingProvider = images;
		new TileMapInputListener(this, grid);
		view = new View(getSize(), grid, images);
	}
	
	protected void exportSelection() {
		System.out.println("JTileMapView.exportSelection()");
		Export export = new Export(view, cachingProvider);
		export.export(firstPoint, secondPoint);
		System.out.println("Done");
		JOptionPane.showMessageDialog(this, "Saved");		
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
			int x = Math.min(p1.x, p2.x);
			int y = Math.min(p1.y, p2.y);
			int w = Math.abs(p2.x-p1.x);
			int h = Math.abs(p2.y-p1.y);
			
			g2.setStroke(new BasicStroke(3));
			System.out.println(p1+" "+p2);
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
