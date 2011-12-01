package pl.maps.tms.cache;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import pl.maps.tms.TileImageProvider;
import pl.maps.tms.cacheing.TileSpec;

public class DiskCache extends AsyncTileCache {

	private static final String FORMAT = "jpg";
	private final File dir;

	public DiskCache(TileImageProvider provider, int threads, Image waitingImage, String directory) {
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
		File file = getFile(dir, spec);
		if(!file.exists())
			return null;
		BufferedImage image = ImageIO.read(file);
		return new CacheEntry(image);
	}

	public static File getFile(File dir, TileSpec spec) {
		return new File(dir, spec.z+"/"+spec.x+"/"+spec.y+"."+FORMAT);
	}

	@Override
	public synchronized void setCacheEntry(TileSpec spec, CacheEntry entry) throws Exception {
		storeOnDisk(dir, spec, entry);
	}

	public static void storeOnDisk(File dir, TileSpec spec, CacheEntry entry) throws IOException {
		File file = getFile(dir, spec);
		boolean success = file.getParentFile().mkdirs();
		if(!success)
			throw new IOException("Failed to create directory "+file.getParentFile());
		ImageIO.write((RenderedImage) entry.image, FORMAT, file);
	}

}
