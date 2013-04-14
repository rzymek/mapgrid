package pl.maps.tms.providers;

import pl.maps.tms.utils.Utils;

public class GeoportalBackupImageProvider extends GeoportalTopoProvider {
	private final String url;

	public GeoportalBackupImageProvider(String baseURL) {
		url = baseURL + "/${z}/${x}/${y}.jpg";
	}

	@Override
	public String getTileURL(int x, int y, int z) {
		return Utils.fillTileURLTemplate(url, x, y, z);
	}

	@Override
	public String toString() {
		return "GeoPortal Backup";
	}

	@Override
	public String getName() {
		return "geoback";
	}
}
