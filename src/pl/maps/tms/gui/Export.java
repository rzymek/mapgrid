package pl.maps.tms.gui;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import pl.mapgrid.calibration.coordinates.Coordinates;
import pl.maps.tms.HTTPTileImageProvider;
import pl.maps.tms.View;
import pl.maps.tms.cache.AsyncTileCache;
import pl.maps.tms.providers.GeoportalTileProvider;

public class Export {
	private final View view;
	private final AsyncTileCache cache;
	public Export(View view, AsyncTileCache cache) {
		this.view = view;
		this.cache = cache;
	}
	public void export(Coordinates loc1, Coordinates loc2) {
		try {
			Point p1 = view.getPoint(loc1);
			Point p2 = view.getPoint(loc2);
			int x = Math.min(p1.x, p2.x);
			int y = Math.min(p1.y, p2.y);
			int w = Math.abs(p2.x - p1.x);
			int h = Math.abs(p2.y - p1.y);

			GeoportalTileProvider grid = new GeoportalTileProvider();
			HTTPTileImageProvider http = new HTTPTileImageProvider(grid);
			ChainImageProvider images = new ChainImageProvider(cache, http); 

			Coordinates c = this.view.getCoordinates(x, y);
			int zoom = this.view.getZoom();
			
			View view = new View(new Dimension(w, h), grid, images);			
			view.setZoom(9);
			view.setLeftTop(loc1);
			p2 = view.getPoint(loc2);
			w = p2.x;
			h = p2.y;
			System.out.println(w+" x "+h);
			view.setDimension(new Dimension(w, h));
			
			BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
			System.out.println("Drawing");
			view.paint(img.getGraphics());
			System.out.println("Writing");		
			ImageIO.write(img, "jpg", new File("export.jpg"));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
}
