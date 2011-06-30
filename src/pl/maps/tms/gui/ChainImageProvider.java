package pl.maps.tms.gui;

import java.awt.Image;

import pl.maps.tms.TileImageProvider;
import pl.maps.tms.cache.AsyncTileCache;
import pl.maps.tms.cache.CacheEntry;
import pl.maps.tms.cacheing.TileSpec;

public class ChainImageProvider implements TileImageProvider{

	private final AsyncTileCache cache;
	private final TileImageProvider direct;
	public boolean updateCache = true;

	public ChainImageProvider(AsyncTileCache cache, TileImageProvider direct) {
		this.cache = cache;
		this.direct = direct;
	}

	@Override
	public Image getTile(int x, int y, int zoom) throws Exception {
		TileSpec spec = new TileSpec(x, y, zoom);
		CacheEntry entry = cache.getCacheEntry(spec);
		if(entry != null && entry.isReady())  
			return entry.image;
		else {
			Image tile = direct.getTile(x, y, zoom);
			if(updateCache)
				cache.setCacheEntry(spec, new CacheEntry(tile));
			return tile;
		}
	}
}
