package pl.maps.tms.cache;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import pl.maps.tms.TileImageProvider;
import pl.maps.tms.cacheing.AsyncFetchListener;
import pl.maps.tms.cacheing.AsyncTileFercher;
import pl.maps.tms.cacheing.TileSpec;

public abstract class AsyncTileCache implements AsyncFetchListener, TileImageProvider, TileCache {
	protected List<AsyncFetchListener> listeners = new ArrayList<AsyncFetchListener>();
	protected AsyncTileFercher asyncFercher;
	protected final Image waitingImage;	

	protected AsyncTileCache(TileImageProvider provider, int threads, Image waitingImage) {
		asyncFercher = new AsyncTileFercher(threads, provider);
		asyncFercher.setListener(this);
		this.waitingImage = waitingImage;
	}

	@Override
	public Image getTile(int tileX, int tileY, int zoom) throws Exception {
		TileSpec spec = new TileSpec(tileX, tileY, zoom);
		CacheEntry entry = getCacheEntry(spec);
		if(entry == null) {
			asyncFercher.fetch(spec);
			entry = new CacheEntry(waitingImage);
			entry.timestamp = 0;
			setCacheEntry(spec, entry);
			return waitingImage;
		}
		return entry.image;
	}
	

	public void setCacheEntry(TileSpec spec, CacheEntry entry) throws Exception {
		notifyListeners(entry.image, spec);		
	}

	public void addListener(AsyncFetchListener listener) {
		listeners.add(listener);
	}
	public void removeListener(AsyncFetchListener listener) {
		listeners.remove(listener);
	}
	
	public void notifyListeners(Image image, TileSpec spec) throws Exception {
		for (AsyncFetchListener listener : listeners) 
			listener.imageFetched(image, spec);
	}

	@Override
	public void imageFetched(Image image, TileSpec spec) throws Exception {
		System.out.println("AsyncTileCache.imageFetched():"+image);
		setCacheEntry(spec, new CacheEntry(image));
	}

}
