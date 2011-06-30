package pl.maps.tms.cache;

import pl.maps.tms.cacheing.TileSpec;

public interface TileCache {

	CacheEntry getCacheEntry(TileSpec spec) throws Exception;

}
