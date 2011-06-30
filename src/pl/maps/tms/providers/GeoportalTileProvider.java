package pl.maps.tms.providers;

import java.awt.Dimension;
import java.awt.Point;

import pl.mapgrid.calibration.coordinates.Coordinates;
import pl.mapgrid.calibration.coordinates.PUWG92;
import pl.maps.tms.TiledPosition;
import pl.maps.tms.utils.Range;
import pl.maps.tms.utils.Utils;

public class GeoportalTileProvider implements TileProvider {
	private Dimension tileSize = new Dimension(256, 256);
	
	public Dimension getTileSize() {
		return tileSize;
	}

	public String getTileURL(int x, int y, int zoom) {
		if(x < 0 || y < 0 || zoom < 0)
			return null;
		
//		String layer = zoom < 7 ? "ORTO_SAT" : "ORTOFOTO";
//		if(zoom >= 7)
//			zoom -= 7;
		String layer="RASTER_TOPO";
		String url = "http://ars.geoportal.gov.pl/ARS/getTile.aspx?service="+layer+"&cs=EPSG2180&"
			+"fileIDX=L${z}X${x}Y${y}.jpg";
		return Utils.fillTileURLTemplate(url, x, y, zoom);
	}
	
	public Point adjacent(Point tile, int dx, int dy) {
		return new Point(tile.x + dx, tile.y - dy);
	}

	private static final Dimension GRID = new Dimension(3,2);
	private static final int LZTS = 409600;
	private static final int WIDTH_IN_METERS = LZTS*GRID.width;
	private static final int HEIGHT_IN_METERS = LZTS*GRID.height;
	
	public TiledPosition getTilePosition(Coordinates coordinates, int zoom) {				
		Dimension tileCount = getTileCount(zoom);
		PUWG92 puwg92 = new PUWG92(coordinates);
		double x = puwg92.getX() / WIDTH_IN_METERS * tileCount.width;
		double y = puwg92.getY() / HEIGHT_IN_METERS * tileCount.height;
		double ox = (x-Math.floor(x)) * tileSize.width;
		double oy = tileSize.height - (y-Math.floor(y)) * tileSize.height;
		TiledPosition position = new TiledPosition();
		position.tile = new Point((int) x, (int) y);
		position.offset = new Point((int) ox, (int) oy);
		return position;
	}
	
	private Dimension getTileCount(int zoom) {
		int z = 2 << (zoom-1);
		if(zoom <= 0)
			z = 1;
		int xTileCount = GRID.width * z;
		int yTileCount = GRID.height * z;
		return new Dimension(xTileCount, yTileCount);
	}
	
	public Coordinates getCoords(Point tile, Point offset, int zoom) {
		Dimension tileCount = getTileCount(zoom);
		long pw = tileCount.width * tileSize.width;
		long ph = tileCount.height * tileSize.height;
		long px = tile.x * tileSize.width + offset.x;
		long py = (tileCount.height - 1 - tile.y) * tileSize.height + offset.y;
		double x = (double) WIDTH_IN_METERS * px / pw;
		double y = HEIGHT_IN_METERS - (double) HEIGHT_IN_METERS * py / ph;
		return new PUWG92(x, y);
	}

	@Override
	public Range getZoomRange() {
		return new Range(0,11);
	}
}