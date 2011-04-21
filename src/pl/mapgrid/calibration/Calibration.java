package pl.mapgrid.calibration;

import pl.mapgrid.calibration.coordinates.Coordinates;
import pl.mapgrid.calibration.coordinates.LatLon;
import pl.mapgrid.calibration.coordinates.UTM;
import uk.me.jstott.jcoord.LatLng;

public class Calibration {
	 // = {left-top, right-top, right-bottom, left-bottom}
	public Coordinates[] coordinates = new Coordinates[4];
	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("Calibration:\n");
		for (Coordinates c : coordinates) {
			buf.append('\t').append(c).append('\n');
		}
		return buf.toString();
	}
	
	public UTM[] toUTM() {
		if(coordinates instanceof UTM[])
			return (UTM[]) coordinates;
		UTM[] utm = new UTM[4];
		for (int i = 0; i < coordinates.length; i++)  
			utm[i] = new UTM(coordinates[i]);
		return utm;
	}
	public LatLon[] toLatLon() {
		if(coordinates instanceof LatLng[])
			return (LatLon[]) coordinates;
		LatLon[] c = new LatLon[4];
		for (int i = 0; i < coordinates.length; i++)  
			c[i] = new LatLon(coordinates[i].getLat(), coordinates[i].getLon());
		return c;
	}
}
