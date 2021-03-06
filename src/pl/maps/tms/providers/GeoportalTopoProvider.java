package pl.maps.tms.providers;

import java.awt.Dimension;

import pl.mapgrid.calibration.coordinates.Coordinates;
import pl.mapgrid.calibration.coordinates.PUWG92;
import pl.maps.tms.Position;
import pl.maps.tms.utils.Range;
import pl.maps.tms.utils.Utils;

public class GeoportalTopoProvider implements TileProvider {
	public static final Dimension GRID = new Dimension(3, 2);
	private static final long LZTS = 409600;
	//width in meters of the whole map
	private static final long WIDTH_IN_METERS = LZTS * GRID.width;
	//height in meters of the whole map
	private static final long HEIGHT_IN_METERS = LZTS * GRID.height;
	private static Dimension tileSize = new Dimension(256, 256);

	@Override
	public Dimension getTileSize() {
		return tileSize;
	}

	@Override
	public String getTileURL(int x, int y, int zoom) {
		if (x < 0 || y < 0 || zoom < 0)
			return null;
		return getTileURL(x,y,zoom, 0, "RASTER_TOPO");
	}

	@Override
	public Position move(Position tile, double dx, double dy) {
		tile.x += dx;
		tile.y += dy;
		return tile;
	}

	@Override
	public Position getTilePosition(Coordinates coordinates, int zoom) {
		Dimension tileCount = getTileCount(zoom);
		PUWG92 puwg92 = new PUWG92(coordinates);
		Position p = new Position();
		p.x = puwg92.getX() * tileCount.width / WIDTH_IN_METERS;
		p.y = tileCount.height - puwg92.getY() * tileCount.height / HEIGHT_IN_METERS;
		return p;
	}

	protected Dimension getTileCount(int zoom) {
		int z = 2 << (zoom - 1);// 2^(z-1)
		if (zoom <= 0)
			z = 1;
		int xTileCount = GRID.width * z;
		int yTileCount = GRID.height * z;
		return new Dimension(xTileCount, yTileCount);
	}

	@Override
	public Coordinates getCoords(Position position, int zoom) {
		Dimension tileCount = getTileCount(zoom);
		long pw = tileCount.width * tileSize.width;
		long ph = tileCount.height * tileSize.height;
		double px = position.x * tileSize.width;
		double py = position.y * tileSize.height;
		double x = WIDTH_IN_METERS * px / pw;
		double y = HEIGHT_IN_METERS - HEIGHT_IN_METERS * py / ph;
		return new PUWG92(x, y);
	}

	@Override
	public Range getZoomRange() {
		return new Range(0, 11);
	}
	@Override
	public String getName() {
		return "topo";
	}
	@Override
	public String toString() {
		return "GeoPortal TOPO";
	}

	protected String getTileURL(int x, int y, int zoom, int zoomModif, String layer) {
		y = getTileCount(zoom).height - 1 - y;
		String url = "http://ars.geoportal.gov.pl/ARS/getTile.aspx?service=" + layer + "&cs=EPSG2180&"
				+ "fileIDX=L${z}X${x}Y${y}.jpg";
		return Utils.fillTileURLTemplate(url, x, y, zoom + zoomModif);
	}

}