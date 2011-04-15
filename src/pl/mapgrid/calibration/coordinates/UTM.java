package pl.mapgrid.calibration.coordinates;

import uk.me.jstott.jcoord.UTMRef;

public class UTM implements Coordinates {

	private UTMRef ref;

	public UTM(double easting, double northing, char latZone, int lngZone) {
		ref = new UTMRef(easting, northing, latZone, lngZone);
	}
	public UTM(Coordinates coordinates) {
		uk.me.jstott.jcoord.LatLng latLng = 
			new uk.me.jstott.jcoord.LatLng(coordinates.getLat(), coordinates.getLon());
		ref = latLng.toUTMRef();
	}
	
	public double getEasting() {
		return ref.getEasting();
	}
	public double getNorthing() {
		return ref.getNorthing();
	}
	@Override
	public double getLat() {
		return ref.toLatLng().getLat();
	}

	@Override
	public double getLon() {
		return ref.toLatLng().getLat();
	}

	@Override
	public String toString() {
		return ref.toString();
	}
}
