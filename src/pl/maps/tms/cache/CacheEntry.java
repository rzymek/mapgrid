package pl.maps.tms.cache;

import java.awt.Image;
import java.util.Date;

public class CacheEntry implements Comparable<CacheEntry> {
	public long timestamp;
	public Image image;
	public CacheEntry(Image img) {
		timestamp = new Date().getTime();
		image = img;
	}
	public boolean isReady() {
		return timestamp > 0;
	}
	@Override
	public int compareTo(CacheEntry o) {
		long l1 = o.timestamp;
		long l2 = timestamp;
		return l1 < l2 ? -1 : l1 == l2 ? 0 : 1;
	}
}