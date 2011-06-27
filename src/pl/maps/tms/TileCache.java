package pl.maps.tms;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.SwingWorker;

public class TileCache {
	private Map<String, Image> cache = new HashMap<String, Image>();
	private final TileProvider tileProvider;
	private final ImageObserver observer;
	
	public TileCache(TileProvider tileProvider, ImageObserver observer) {
		this.tileProvider = tileProvider;
		this.observer = observer;
	}
	
	public Image getTile(final int tileX, final int tileY, final int zoom) {
		final String coord = ""+tileX+tileY+zoom;
		final Dimension tileSize = tileProvider.getTileSize();
		Image image = cache.get(coord);
		if (image == null) {
			try {
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
					@Override
					protected Void doInBackground() throws Exception {
						Image image = fetch(tileX, tileY, zoom);			
						cache.put(coord, image);
						observer.imageUpdate(image, 0, tileX, tileY, tileSize.width, tileSize.height);
						return null;
					}
				};
				cache.put(coord, image = createTmpImage(Color.LIGHT_GRAY));
				worker.execute();
			}catch (Exception e) {
				e.printStackTrace();
				image = createTmpImage(Color.RED);
			}
		}
		return image;
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

	private Image createTmpImage(Color color) {
		Image image;
		Dimension size = tileProvider.getTileSize();
		image = new BufferedImage(size.width, size.height, BufferedImage.TYPE_3BYTE_BGR);
		Graphics g = image.getGraphics();
		g.setColor(color);
		g.fillRect(0, 0, size.width, size.height);
		g.setColor(Color.BLACK);
		g.drawLine(0,0,size.width, size.height);
		g.drawLine(size.width, 0, 0, size.height);
		return image;
	}
}
