package pl.maps.tms;

import java.awt.Dimension;

import pl.mapgrid.calibration.coordinates.Coordinates;
import pl.maps.tms.utils.Range;

public interface TileGridProvider {
	Range getZoomRange();
	
	Dimension getTileSize();

	Position move(Position tile, double dx, double dy);

	Position getTilePosition(Coordinates coordinates, int zoom);

	Coordinates getCoords(Position tile, int zoom);
}
