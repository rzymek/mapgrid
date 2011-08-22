package pl.maps.tms;

import java.awt.Image;
import java.net.URL;

import javax.imageio.ImageIO;

public class HTTPTileImageProvider implements TileImageProvider {
	private final URLTileImageProvider urlProvider;

	public HTTPTileImageProvider(URLTileImageProvider urlProvider) {
		this.urlProvider = urlProvider;
	}

	@Override
	public Image getTile(int tileX, int tileY, int zoom) throws Exception {
		String url = urlProvider.getTileURL(tileX, tileY, zoom);
		if(url == null)
			return null;
		System.out.println("HTTPTileImageProvider:"+url);
		return ImageIO.read(new URL(url));
	}

}
