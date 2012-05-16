package pl.maps.tms.providers;

import java.awt.Dimension;

import pl.maps.tms.utils.Range;
import pl.maps.tms.utils.Utils;

public class GeoportalOrtoProvider extends GeoportalTopoProvider {
	@Override
	public String getTileURL(int x, int y, int zoom) {
		if (x < 0 || y < 0 || zoom < 0)
			return null;

		if (zoom > 6) {
			String layer = "ORTOFOTO";
			y = getTileCount(zoom).height - 1 - y;
			String url = "http://ars.geoportal.gov.pl/ARS/getTile.aspx?service=" + layer + "&cs=EPSG2180&"
					+ "fileIDX=L${z}X${x}Y${y}.jpg";
			return Utils.fillTileURLTemplate(url, x, y, zoom - 6);
		} else {
			String layer = "ORTO_SAT";
			y = getTileCount(zoom).height - 1 - y;
			String url = "http://ars.geoportal.gov.pl/ARS/getTile.aspx?service=" + layer + "&cs=EPSG2180&"
					+ "fileIDX=L${z}X${x}Y${y}.jpg";
			return Utils.fillTileURLTemplate(url, x, y, zoom);
		}
	}


	@Override
	public String toString() {
		return "GeoPortal ORTO";
	}

	public static void main(String[] args) {
		GeoportalOrtoProvider p = new GeoportalOrtoProvider();
		Range r = p.getZoomRange();
		for (int i = r.min; i < r.max; i++) {
			Dimension tc = p.getTileCount(i);
			System.out.println(i + ": " + tc);
		}
	}

}
