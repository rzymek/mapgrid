package pl.maps.tms.cache;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;

import javax.imageio.ImageIO;

import pl.maps.tms.TileImageProvider;
import pl.maps.tms.cacheing.TileSpec;

public class DiskMemCache extends MemCache {

	private static final String FORMAT = "jpg";
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
	public synchronized CacheEntry getCacheEntry(TileSpec spec) throws Exception {
		CacheEntry entry = super.getCacheEntry(spec);
		if(entry != null)
			return entry;
		File file = getFile(spec);
		if(!file.exists())
			return null;
		BufferedImage image = ImageIO.read(file);
		return new CacheEntry(image);
	}

	private File getFile(TileSpec spec) {
		return new File(dir, spec.z+"/"+spec.x+"/"+spec.y+"."+FORMAT);
	}

	@Override
	public synchronized void setCacheEntry(TileSpec spec, CacheEntry entry) throws Exception {
		super.setCacheEntry(spec, entry);
		File file = getFile(spec);
		file.getParentFile().mkdirs();
		ImageIO.write((RenderedImage) entry.image, FORMAT, file);
	}

}
