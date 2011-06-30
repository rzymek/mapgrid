package pl.maps.tms.cacheing;

import java.awt.Image;

public interface AsyncFetchListener {
	public void imageFetched(Image image, TileSpec spec) throws Exception;
}
