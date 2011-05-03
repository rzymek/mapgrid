package pl.mapgrid.calibration.coordinates;

import org.openstreetmap.josm.data.coor.EastNorth;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.projection.Puwg;

public class PUWG92 implements Coordinates {

	private final double x;
	private final double y;
	private final double lat;
	private final double lon;

	public PUWG92(Coordinates c) {
		if(c instanceof PUWG92){
			PUWG92 puwg92 = (PUWG92) c;
			this.x = puwg92.x;
			this.y = puwg92.y;
			this.lat = puwg92.lat;
			this.lon = puwg92.lon;
		}else{
			Puwg puwg = new Puwg();
			EastNorth en = puwg.latlon2eastNorth(new LatLon(c.getLat(), c.getLon()));
			x = en.getX();
			y = en.getY();
			lat = c.getLat();
			lon = c.getLon();
		}		
	}
	public PUWG92(double x, double y) {
		this.x = x;
		this.y = y;
		Puwg puwg = new Puwg();
		LatLon latLon = puwg.eastNorth2latlon(new EastNorth(x,y));
		lat = latLon.getY();
		lon = latLon.getX();
	}

	@Override
	public double getLat() {
		return lat;
	}

	@Override
	public double getLon() {
		return lon;
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
