package pl.mapgrid.calibration.coordinates;

import uk.me.jstott.jcoord.LatLng;
import uk.me.jstott.jcoord.UTMRef;

public class UTM implements Coordinates {
	public double easting;
	public double northing;
	public char latZone;
	public int lngZone;
	
	public UTM(double easting, double northing, char latZone, int lngZone) {
		this.easting = easting;
		this.northing = northing;
		this.latZone = latZone;
		this.lngZone = lngZone;
	}
	public UTM(Coordinates coordinates) {
		if(coordinates instanceof UTM) {
			UTM utm = (UTM) coordinates;
			easting = utm.easting;
			northing = utm.northing;
			latZone = utm.latZone;
			lngZone = utm.lngZone;
		}else{
			LatLng latLng = 
				new LatLng(coordinates.getLat(), coordinates.getLon());
			UTMRef ref = latLng.toUTMRef();
			easting = ref.getEasting();
			northing = ref.getNorthing();
			latZone = ref.getLatZone();
			lngZone = ref.getLngZone();
		}
	}
	
	public double getEasting() {
		return getX();
	}
	public double getNorthing() {
		return getY();
	}
	@Override
	public double getLat() {
		return new UTMRef(easting, northing, latZone, lngZone).toLatLng().getLat();
	}

	@Override
	public double getLon() {
		return new UTMRef(easting, northing, latZone, lngZone).toLatLng().getLng();
	}

	@Override
	public String toString() {
		return getZone() + " " + (int)easting + " E " + (int)northing + " N";
	}
	public String getZone() {
		return String.format("%c %02d",latZone, lngZone);
	}
	public double getX() {
		return easting;
	}
	public double getY() {
		return northing;
	}
	public String toMGRS() {
		String e = (""+(int)easting).replaceAll(".*(....).", "$1");
		String n = (""+(int)northing).replaceAll(".*(....).", "$1");
		return e+" "+n+"\n"+encode(e)+" "+encode(n);
	}
	
	private String encode(String n) {
		String kod="uzbekistan";
		char[] c = n.toCharArray();
		for (int i = 0; i < c.length; i++) {
			int w = Integer.parseInt(""+c[i]);
			if(w==0) w = 10;
			char x = kod.charAt(w-1);
			c[i]=x;
		}
		return new String(c).toUpperCase();
	}
}

