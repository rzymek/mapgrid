package pl.maps.tms.providers;

import java.awt.Dimension;
import java.awt.Point;

import pl.mapgrid.calibration.coordinates.Coordinates;
import pl.mapgrid.calibration.coordinates.LatLon;
import pl.maps.tms.TiledPosition;
import pl.maps.tms.utils.Range;

/*
 * http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames
 */
public class OSMTileProvider implements TileProvider {
	private Dimension tileSize = new Dimension(256, 256);

	@Override
	public Dimension getTileSize() {
		return tileSize;
	}

	@Override
	public Point adjacent(Point tile, int dx, int dy) {
		return new Point(tile.x + dx, tile.y + dy);
	}

	@Override
	public TiledPosition getTilePosition(Coordinates coordinates, int zoom) {
		double lat = coordinates.getLat();
		double lon = coordinates.getLon();
		double x = (lon + 180) / 360 * (1 << zoom);
		double y = (1 - Math.log(Math.tan(Math.toRadians(lat)) + 1 / Math.cos(Math.toRadians(lat))) / Math.PI) / 2 * (1 << zoom);
		double ox = (x-Math.floor(x)) * tileSize.width;
		double oy = (y-Math.floor(y)) * tileSize.height;
		TiledPosition position = new TiledPosition();
		position.tile = new Point((int) x, (int) y);
		position.offset = new Point((int) ox,(int) oy);
		return position;
	}

	static double tile2lon(double x, int z) {
		return x / Math.pow(2.0, z) * 360.0 - 180;
	}

	static double tile2lat(double y, int z) {
		double n = Math.PI - (2.0 * Math.PI * y) / Math.pow(2.0, z);
		return Math.toDegrees(Math.atan(Math.sinh(n)));
	}

	@Override
	public Coordinates getCoords(Point tile, Point offset, int zoom) {
		double lon = tile2lon(tile.x + (double)offset.x / tileSize.width, zoom);
		double lat = tile2lat(tile.y + (double)offset.y / tileSize.height, zoom);
		return new LatLon(lat, lon);
	}

	public String getTileURL(int x, int y, int zoom) {
		if(x < 0 || y < 0 || zoom < 0)
			return null;
		return "http://tile.openstreetmap.org/"+zoom+"/"+x+"/"+y+".png";
	}
	@Override
	public Range getZoomRange() {
		return new Range(2, 16);
	}
}
