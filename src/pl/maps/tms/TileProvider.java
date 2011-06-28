package pl.maps.tms;

import java.awt.Dimension;
import java.awt.Point;

import pl.mapgrid.calibration.coordinates.Coordinates;

public interface TileProvider {

	Dimension getTileSize();

	Point adjacent(Point tile, int dx, int dy);

	TiledPosition getTilePosition(Coordinates coordinates, int zoom);

	Coordinates getCoords(Point tile, Point offset, int zoom);

	String getTileURL(int tileX, int tileY, int zoom);
}
