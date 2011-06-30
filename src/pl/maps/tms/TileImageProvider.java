package pl.maps.tms;

import java.awt.Image;

public interface TileImageProvider {
	public Image getTile(int x, int y, int zoom) throws Exception; 		
}
