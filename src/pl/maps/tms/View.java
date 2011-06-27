package pl.maps.tms;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.ImageObserver;

import pl.mapgrid.calibration.coordinates.Coordinates;

public class View {
	private Point tile; // top-left;
	private Point offset; // view offset from top-left corner of tile
	private Dimension dimension; // view size
	private Dimension tileCount = new Dimension(0,0);
	TileProvider tileProvider;
	private int zoom = 9;

	public View(Dimension dimension, ImageObserver observer) {
		this.offset = new Point(0,0);
		this.dimension = dimension;
		tileProvider = new TileProvider(observer);
		calcGrid();
	}
	
	public void setZoom(int zoom) {
		this.zoom = zoom;
	}

	private void calcGrid() {
		Dimension tileSize = tileProvider.getTileSize();
		tileCount.width = (dimension.width / tileSize.width) + 1;
		tileCount.height = (dimension.height / tileSize.height) + 1;
	}
	
	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
		calcGrid();
	}

	public void move(int dx, int dy) {
		offset.x += dx;
		offset.y += dy;
		
		Dimension ts = tileProvider.getTileSize();
		tile = tileProvider.adjacent(tile, offset.x / ts.width, offset.y / ts.height);
		
		offset.x %= ts.width;
		offset.y %= ts.height;
		
		if(offset.x < 0) {
			tile = tileProvider.adjacent(tile, -1, 0);
			offset.x = ts.width + offset.x;
		}
		if(offset.y < 0) {
			tile = tileProvider.adjacent(tile, 0, -1);
			offset.y = ts.height + offset.y;
		}
	}

	public void paint(Graphics g) {
		Dimension ts = tileProvider.getTileSize();
		for (int y = 0; y <= tileCount.height; ++y) {
			for (int x = 0; x <= tileCount.width; ++x) {
				int xx = x*ts.width - offset.x;
				int yy = y*ts.height- offset.y;
				
				Point next = tileProvider.adjacent(tile, x, y);
				Image tile = tileProvider.getTile(next.x, next.y, zoom);
				g.drawImage(tile, xx, yy, null);
				g.drawString(next.x+", "+next.y+", "+zoom, 
						xx, yy + ts.height);
				g.drawRect(xx, yy, ts.width, ts.height);
			}
		}
	}

	public Coordinates getCoordinates(int x, int y) {
		Point of = new Point(offset.x + x, offset.y + y);
		return tileProvider.getCoords(tile, of, zoom);
	}

	public void setPosition(Point tile, Point offset) {
		this.tile = tile;
		this.offset = offset;
	}
}
