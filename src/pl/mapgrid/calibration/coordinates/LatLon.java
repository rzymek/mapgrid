package pl.mapgrid.calibration.coordinates;

public class LatLon implements Coordinates {
	private double lat;
	private double lon;

	public LatLon(double lat, double lon) {
		this.lat = lat;
		this.lon = lon;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLat() {
		return lat;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getLon() {
		return lon;
	}
	
	@Override
	public double getX() {
		return getLon();
	}
	@Override
	public double getY() {
		return getLat();
	}
	@Override
	public String toString() {
		return "LatLon("+lat+"; "+lon+")";
	}
}
