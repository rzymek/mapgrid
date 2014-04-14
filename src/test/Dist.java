package test;

import pl.mapgrid.calibration.coordinates.Coordinates;
import pl.mapgrid.calibration.coordinates.LatLon;
import pl.mapgrid.calibration.coordinates.PUWG92;
import pl.mapgrid.calibration.coordinates.UTM;
import pl.maps.tms.Position;
import pl.maps.tms.providers.GeoportalTopoProvider;

public class Dist {

	public static String getTileNumber(final double lat, final double lon, final int zoom) {
		int xtile = (int) Math.floor((lon + 180) / 360 * (1 << zoom));
		int ytile = (int) Math.floor((1 - Math.log(Math.tan(Math.toRadians(lat)) + 1 / Math.cos(Math.toRadians(lat))) / Math.PI) / 2 * (1 << zoom));
		if (xtile < 0)
			xtile = 0;
		if (xtile >= (1 << zoom))
			xtile = ((1 << zoom) - 1);
		if (ytile < 0)
			ytile = 0;
		if (ytile >= (1 << zoom))
			ytile = ((1 << zoom) - 1);
		return ("" + zoom + "/" + xtile + "/" + ytile);
	}

	static double tile2lon(int x, int z) {
		return x / Math.pow(2.0, z) * 360.0 - 180;
	}

	static double tile2lat(int y, int z) {
		double n = Math.PI - (2.0 * Math.PI * y) / Math.pow(2.0, z);
		return Math.toDegrees(Math.atan(Math.sinh(n)));
	}

	private void run() {
		UTM a = toPUWG(284, 166, 13);
		UTM b = toPUWG(283, 166, 13);
		GeoportalTopoProvider topo = new GeoportalTopoProvider();
		PUWG92 ta = (PUWG92) topo.getCoords(new Position(284, 166),7);
		PUWG92 tb = (PUWG92) topo.getCoords(new Position(283, 166),7);
		System.out.println(a.getX() - b.getX());
		System.out.println(a.getY() - b.getY());
		
		System.out.println(ta.getX() - tb.getX());
		System.out.println(ta.getY() - tb.getY());
	}

	protected UTM toPUWG(int x, int y, int z) {
		double lat = tile2lat(y, z);
		double lon = tile2lon(x, z);
		Coordinates latlon = new LatLon(lat, lon);
		UTM utm = new UTM(latlon);
//		PUWG92 puwg = new PUWG92(latlon);
		System.out.println(latlon);
		return utm;
	}
	
	
	public static void main(String[] args) {
		new Dist().run();
	}

}
