package pl.maps.tms.cache;

import java.awt.Image;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

import pl.maps.tms.StreamUtils;
import pl.maps.tms.TileProvider;

public class DiskCache extends MemoryCache {
	
	public DiskCache(TileProvider tileProvider, ImageObserver observer) {
		super(tileProvider, observer);
	}

	protected Image fetch(int tileX, int tileY, int zoom) throws Exception {
		File dir = new File("cache/"+zoom+"/"+tileX);
		File file = new File(dir, tileY+".jpg");
		if(!file.exists()) {
			dir.mkdirs();
			String url = tileProvider.getTileURL(tileX, tileY, zoom);
			URL u = new URL(url);
			System.out.println("Fetching "+url);
			InputStream in = u.openStream();
			FileOutputStream out = new FileOutputStream(file);
			StreamUtils.copy(in, out);
			in.close();
			out.close();
		}
		return ImageIO.read(file);
	}
}
