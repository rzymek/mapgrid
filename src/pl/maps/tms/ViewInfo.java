package pl.maps.tms;

import java.awt.Dimension;

public interface ViewInfo {

	Dimension getTileCount();

	Position getPosition();

	int getZoom();

	TileGridProvider getGrid();

}
