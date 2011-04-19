package pl.mapgrid.calibration.coordinates;

import org.openstreetmap.josm.data.coor.CoordinateFormat;
import org.openstreetmap.josm.data.coor.EastNorth;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.projection.Puwg;
import org.openstreetmap.josm.data.projection.UTM;

public class PUGW92 implements Coordinates {

	private LatLon latLon;

	public PUGW92(double x, double y) {
		Puwg puwg = new Puwg();
		latLon = puwg.eastNorth2latlon(new EastNorth(x,y));
		System.out.println(latLon);
		String lat = latLon.latToString(CoordinateFormat.DEGREES_MINUTES_SECONDS);
		String lon = latLon.lonToString(CoordinateFormat.DEGREES_MINUTES_SECONDS);
		System.out.println(lat+" "+lon);
		UTM utm = new UTM();
		EastNorth en = utm.latlon2eastNorth(latLon);
		System.out.println(en);
	}

	@Override
	public double getLat() {
		return latLon.lat();
	}

	@Override
	public double getLon() {
		return latLon.lon();
	}

}
