package pl.maps.tms;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

public class HTTPTileImageProvider implements TileImageProvider {
	private final URLTileImageProvider urlProvider;

	public HTTPTileImageProvider(URLTileImageProvider urlProvider) {
		this.urlProvider = urlProvider;
	}

	@Override
	public Image getTile(int tileX, int tileY, int zoom) throws Exception {
		String url = urlProvider.getTileURL(tileX, tileY, zoom);
		if (url == null)
			return null;
		byte[] data = download(url);
		BufferedImage image = ImageIO.read(new ByteArrayInputStream(data));
		if (image == null) {
			System.err.println("RESP:["+new String(data, "utf-8")+"]");
		}else{
			System.err.println("RESP:OK");
		}
		return image;
	}

	private static byte[] download(String url) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] chunk = new byte[4096];
		int bytesRead;
		final URL endpoint = new URL(url);
		System.out.println(endpoint);
		HttpURLConnection conn = (HttpURLConnection) endpoint.openConnection();
		conn.setRequestProperty("User-Agent", "Cześć i Chwała Bohaterom");
		HttpURLConnection.setFollowRedirects(true);
		Map<String, List<String>> headers = conn.getHeaderFields();
		System.out.println(headers);		
		InputStream stream = conn.getInputStream();
		while ((bytesRead = stream.read(chunk)) > 0) {
			outputStream.write(chunk, 0, bytesRead);
		}
		return outputStream.toByteArray();
	}
	/**<pre>
	 sudo nc -l -p 2000
		GET /wss/service/WMTS/guest/wmts/TOPO?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=MAPA%20TOPOGRAFICZNA&STYLE=default&FORMAT=image/jpeg&TILEMATRIXSET=EPSG:2180&TILEMATRIX=EPSG:2180:0&TILEROW=0&TILECOL=0 HTTP/1.1
		User-Agent:  Java/1.7.0_65
		Host: localhost:2000
		Accept: text/html, image/gif, image/jpeg, *; q=.2, 
		Connection: keep-alive
	 */
	public static void main(String[] args) throws IOException {
		byte[] data = download("http://mapy.geoportal.gov.pl/wss/service/WMTS/guest/wmts/TOPO?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=MAPA%20TOPOGRAFICZNA&STYLE=default&FORMAT=image/jpeg&TILEMATRIXSET=EPSG:2180&TILEMATRIX=EPSG:2180:0&TILEROW=0&TILECOL=0");
//		byte[] data = download("http://localhost:2000/wss/service/WMTS/guest/wmts/TOPO?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=MAPA%20TOPOGRAFICZNA&STYLE=default&FORMAT=image/jpeg&TILEMATRIXSET=EPSG:2180&TILEMATRIX=EPSG:2180:0&TILEROW=0&TILECOL=0");
		System.out.println(new String(data,"utf-8"));
	}
}
