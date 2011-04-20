package pl.mapgrid.calibration.coordinates;

import org.openstreetmap.josm.data.coor.EastNorth;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.projection.Puwg;

public class PUGW92 implements Coordinates {

	private LatLon latLon;

	public PUGW92(double x, double y) {
		Puwg puwg = new Puwg();
		latLon = puwg.eastNorth2latlon(new EastNorth(x,y));
	}

	@Override
	public double getLat() {
		return latLon.lat();
	}

	@Override
	public double getLon() {
		return latLon.lon();
	}

	@Override
	public String toString() {
		return "Pugw: "+latLon;
	}
}
