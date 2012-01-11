package pl.maps.tms.cache;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import pl.maps.tms.TileImageProvider;
import pl.maps.tms.cacheing.TileSpec;

public class DiskMemCache extends MemCache {

	private final File dir;

	public DiskMemCache(TileImageProvider provider, int threads, Image waitingImage, String directory) {
		super(provider, threads, waitingImage);
		dir = new File(directory);
		if(!dir.exists()) {
			boolean result = dir.mkdirs();
			if(result == false)
				throw new IllegalArgumentException("Can't create cache directory "+dir);
		}
		if(!dir.canWrite())
			throw new IllegalArgumentException("Cache directory "+dir+" not writable.");
	}

	@Override
	public CacheEntry getCacheEntry(TileSpec spec) throws Exception {
		CacheEntry entry = super.getCacheEntry(spec);
		if(entry != null)
			return entry;
		File file = DiskCache.getFile(dir, spec);
		if(!file.exists())
			return null;
		BufferedImage image = ImageIO.read(file);
		return new CacheEntry(image);
	}

	@Override
	public void setCacheEntry(TileSpec spec, CacheEntry entry) throws Exception {
		super.setCacheEntry(spec, entry);
		DiskCache.storeOnDisk(dir, spec, entry);
	}

}
