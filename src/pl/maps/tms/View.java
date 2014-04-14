package pl.maps.tms;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

import pl.mapgrid.calibration.coordinates.Coordinates;

public class View implements ViewInfo {
	private Position position;
	private Dimension viewSize; // view size
	private Dimension tileCount = new Dimension(0,0); //cached
	private int zoom = 0;
	public boolean showGrid = false;
	
	private final TileGridProvider grid;
	private final TileImageProvider images;

	public View(Dimension dimension, TileGridProvider grid, TileImageProvider images) {
		this.position = new Position();
		this.viewSize = dimension;
		this.grid = grid;
		this.images = images;
		calcGrid();
	}
	public TileImageProvider getTileImageProvider() {
		return images;
	}
	@Override
	public Dimension getTileCount() {
		return tileCount;
	}
	@Override
	public TileGridProvider getGrid() {
		return grid;
	}
	public void setZoom(int zoom) {
		this.zoom = zoom;
	}

	private void calcGrid() {
		Dimension tileSize = grid.getTileSize();
		tileCount.width = (viewSize.width / tileSize.width) + 1;
		tileCount.height = (viewSize.height / tileSize.height) + 1;
	}
	
	public void setDimension(Dimension dimension) {
		this.viewSize = dimension;
		calcGrid();
	}

	public Dimension getViewSize() {
		return viewSize;
	}
	
	public void move(int dx, int dy) {
		Dimension ts = grid.getTileSize();
		position.x += (double)dx / ts.width;
		position.y += (double)dy / ts.height; 
	}

	public void paint(Graphics g)  {
		g.setColor(Color.RED);
		final Dimension ts = grid.getTileSize();
		final int offsetx = (int) ((position.x % 1) * ts.width);
		final int offsety = (int) ((position.y % 1) * ts.height);
		final int posx = (int) position.x;
		final int posy = (int) position.y;
		for (int y = 0; y <= tileCount.height; ++y) {
			for (int x = 0; x <= tileCount.width; ++x) {
				try {
					final int xx = x*ts.width - offsetx;
					final int yy = y*ts.height- offsety;
					final int tilex = posx + x;
					final int tiley = posy + y;
					
					Image tile = images.getTile(tilex, tiley, zoom);
					g.drawImage(tile, xx, yy, null);
					if(showGrid){
						String msg = String.format("%d %d %d", (int)tilex, (int)tiley, zoom);
						g.setColor(Color.WHITE);
						FontMetrics fm = g.getFontMetrics();
						final Rectangle2D bounds = fm.getStringBounds(msg, g);
						g.fillRect(xx, yy+ts.height-(int)bounds.getHeight(), (int) bounds.getWidth()+6, (int) bounds.getHeight()+6);
						g.setColor(Color.BLACK);
						g.drawString(msg, xx+2, yy + ts.height-2);
						g.setColor(Color.RED);
						g.drawRect(xx, yy, ts.width, ts.height);
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public Position getPosition() {
		return position;
	}

	public Coordinates getCoordinates(int x, int y) {
		Dimension ts = grid.getTileSize();
		Position p = position.clone();
		p.x += (double)x/ts.width;
		p.y += (double)y/ts.height;
		return grid.getCoords(p, zoom);
	}

	public void setPosition(Position position) {
		this.position = position;
	}
	
	public Point getPoint(Coordinates c) {
		Position pos = grid.getTilePosition(c, zoom);
		Dimension ts = grid.getTileSize();
		int x = (int) ((pos.x - position.x) * ts.width);
		int y = (int) ((pos.y - position.y) * ts.height);
		return new Point(x,y);
	}

	public void centerAt(Coordinates c) {
		setLeftTop(c);
		move(-viewSize.width/2, -viewSize.height/2);
	}

	public void setLeftTop(Coordinates c) {
		position = grid.getTilePosition(c, zoom);
	}

	@Override
	public int getZoom() {
		return zoom;
	}
}
