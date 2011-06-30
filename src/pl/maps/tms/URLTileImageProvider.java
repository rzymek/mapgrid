package pl.maps.tms;

public interface URLTileImageProvider {
	String getTileURL(int tileX, int tileY, int zoom);
}
