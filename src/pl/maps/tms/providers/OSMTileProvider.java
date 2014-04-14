package pl.maps.tms.providers;

import java.awt.Dimension;

import pl.mapgrid.calibration.coordinates.Coordinates;
import pl.mapgrid.calibration.coordinates.LatLon;
import pl.maps.tms.Position;
import pl.maps.tms.utils.Range;

/*
 * http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames
 */
public class OSMTileProvider implements TileProvider {
	private final Dimension tileSize = new Dimension(256, 256);

	@Override
	public Dimension getTileSize() {
		return tileSize;
	}

	@Override
	public Position move(Position tile, double dx, double dy) {
		tile.x += dx;
		tile.y += dy;
		return tile;
	}

	@Override
	public Position getTilePosition(Coordinates coordinates, int zoom) {
		double lat = coordinates.getLat();
		double lon = coordinates.getLon();
		double xtile = Math.floor((lon + 180.0) / 360.0 * (1 << zoom));
		double ytile = Math.floor((1 - Math.log(Math.tan(Math.toRadians(lat)) + 1 / Math.cos(Math.toRadians(lat))) / Math.PI) / 2.0 * (1 << zoom));
		if (xtile < 0)
			xtile = 0;
		if (xtile >= (1 << zoom))
			xtile = ((1 << zoom) - 1);
		if (ytile < 0)
			ytile = 0;
		if (ytile >= (1 << zoom))
			ytile = ((1 << zoom) - 1);
		//
		// double x = (lon + 180) / 360 * (1 << zoom);
		// double y = (1 - Math.log(Math.tan(Math.toRadians(lat)) + 1 / Math.cos(Math.toRadians(lat))) / Math.PI) / 2 * (1 << zoom);
		// double ox = (x - Math.floor(x)) * tileSize.width;
		// double oy = (y - Math.floor(y)) * tileSize.height;
		return new Position(xtile, ytile);
	}

	static double tile2lon(double x, int z) {
		return x / Math.pow(2.0, z) * 360.0 - 180.0;
	}

	static double tile2lat(double y, int z) {
		double n = Math.PI - (2.0 * Math.PI * y) / Math.pow(2.0, z);
		return Math.toDegrees(Math.atan(Math.sinh(n)));
	}

	@Override
	public Coordinates getCoords(Position tile, int zoom) {
		double lon = tile2lon(tile.x, zoom);
		double lat = tile2lat(tile.y, zoom);
		return new LatLon(lat, lon);
	}

	@Override
	public String getTileURL(int x, int y, int zoom) {
		if (x < 0 || y < 0 || zoom < 0)
			return null;
		return "http://c.tile.openstreetmap.org/" + zoom + "/" + x + "/" + y + ".png";
	}

	@Override
	public Range getZoomRange() {
		return new Range(2, 16);
	}

	@Override
	public String getName() {
		return "osm";
	}

	@Override
	public String toString() {
		return "OpenStreetMap";
	}
}
