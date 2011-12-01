package pl.maps.tms.cache;

import java.awt.Image;
import java.util.Date;

public class CacheEntry {
	public long timestamp;
	public Image image;
	public CacheEntry(Image img) {
		timestamp = new Date().getTime();
		image = img;
	}
	public boolean isReady() {
		return timestamp > 0;
	}
}