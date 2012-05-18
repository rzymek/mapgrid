package pl.maps.tms.providers;

import java.awt.Dimension;

import pl.maps.tms.utils.Range;

public class GeoportalOrtoProvider extends GeoportalTopoProvider {
	@Override
	public String getTileURL(int x, int y, int zoom) {
		if (x < 0 || y < 0 || zoom < 0)
			return null;

		int zoomModif;
		String layer;		
		if (zoom > 6) {
			layer = "ORTOFOTO";
			zoomModif = -6;
		}else{
			layer = "ORTO_SAT";
			zoomModif = 0;
		}
		return getTileURL(x, y, zoom, zoomModif, layer);
	}

	@Override
	public String getName() {
		return "sat";
	}

	@Override
	public String toString() {
		return "GeoPortal ORTO";
	}

	public static void main(String[] args) {
		GeoportalTopoProvider p = new GeoportalOrtoProvider();
		Range r = p.getZoomRange();
		for (int i = r.min; i < r.max; i++) {
			Dimension tc = p.getTileCount(i);
			System.out.println(i + ": " + tc);
		}
	}

}
