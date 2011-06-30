package pl.maps.tms;

import java.awt.Dimension;
import java.awt.Point;

import pl.mapgrid.calibration.coordinates.Coordinates;
import pl.maps.tms.utils.Range;

public interface TileGridProvider {
	Range getZoomRange();
	
	Dimension getTileSize();

	Point adjacent(Point tile, int dx, int dy);

	TiledPosition getTilePosition(Coordinates coordinates, int zoom);

	Coordinates getCoords(Point tile, Point offset, int zoom);
}
