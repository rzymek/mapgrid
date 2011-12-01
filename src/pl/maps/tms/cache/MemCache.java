package pl.maps.tms.cache;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import pl.maps.tms.TileImageProvider;
import pl.maps.tms.cacheing.TileSpec;

public class MemCache extends AsyncTileCache {
	private static final int MAX_CACHE_SIZE = 1000;
	private int maxCacheSize = MAX_CACHE_SIZE;
	protected Map<TileSpec, CacheEntry> cache;

	public MemCache(TileImageProvider provider, int threads, Image waitingImage) {
		super(provider, threads, waitingImage);
		cache = new HashMap<TileSpec, CacheEntry>();
		cache = Collections.synchronizedMap(cache);
	}
	
	protected void purgeByZoom(int currentZoom) {
		if(cache.size() <= getMaxCacheSize())
			return;
		System.out.println("MemCache.purgeByZoom()");
		Set<Entry<TileSpec, CacheEntry>> entrySet = cache.entrySet();
		Iterator<Entry<TileSpec, CacheEntry>> iterator = entrySet.iterator();
		while (iterator.hasNext()) {
			Map.Entry<TileSpec, CacheEntry> entry = iterator.next();
			TileSpec key = entry.getKey();
			if(key.z != currentZoom)
				iterator.remove();
		}
	}

	protected void purgeByTimeStamp() {
		if(cache.size() <= getMaxCacheSize())
			return;
		System.out.println("MemCache.purgeByTimeStamp()");
		Set<Entry<TileSpec, CacheEntry>> entrySet = cache.entrySet();
		List<Entry<TileSpec, CacheEntry>> list = new ArrayList<Map.Entry<TileSpec,CacheEntry>>(entrySet);
		Comparator<Entry<TileSpec, CacheEntry>> comparator = new Comparator<Entry<TileSpec, CacheEntry>>() {
			@Override
			public int compare(Entry<TileSpec, CacheEntry> o1, Entry<TileSpec, CacheEntry> o2) {
				long t1 = o1.getValue().timestamp;
				long t2 = o2.getValue().timestamp;
				return t1 < t2 ? -1 : t1 == t2 ? 0 : 1;
			}
		};
		Collections.sort(list, Collections.reverseOrder(comparator));
		for (Entry<TileSpec, CacheEntry> entry : list) {
			if(cache.size() <= getMaxCacheSize())
				break;
			cache.remove(entry.getKey());
		}
	}

	public void setMaxCacheSize(int maxCacheSize) {
		this.maxCacheSize = maxCacheSize;
		purgeByTimeStamp();
	}

	public int getMaxCacheSize() {
		return maxCacheSize;
	}

	@Override
	public CacheEntry getCacheEntry(TileSpec spec) throws Exception {
		return cache.get(spec);
	}

	@Override
	public void setCacheEntry(TileSpec spec, CacheEntry entry) throws Exception {
		super.setCacheEntry(spec, entry);
		purgeByZoom(spec.z);
		purgeByTimeStamp();
		cache.put(spec, entry);
	}
}
