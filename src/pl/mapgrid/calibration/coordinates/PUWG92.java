package pl.mapgrid.calibration.coordinates;

import org.openstreetmap.josm.data.coor.EastNorth;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.projection.Puwg;

public class PUWG92 implements Coordinates {

	private LatLon latLon;
	private final double x;
	private final double y;

	public PUWG92(double x, double y) {
		this.x = x;
		this.y = y;
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
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}

	@Override
	public String toString() {
		return "Pugw["+x+", "+y+"]";
	}
}
