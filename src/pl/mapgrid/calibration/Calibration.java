package pl.mapgrid.calibration;

import java.util.Arrays;

import pl.mapgrid.calibration.coordinates.Coordinates;
import pl.mapgrid.calibration.coordinates.UTM;

public class Calibration {
	 // = {left-top, right-top, right-bottom, left-bottom}
	public Coordinates[] coordinates = new Coordinates[4];
	@Override
	public String toString() {
		return Arrays.asList(coordinates).toString();
	}
	public UTM[] toUTM() {
		if(coordinates instanceof UTM[])
			return (UTM[]) coordinates;
		UTM[] utm = new UTM[4];
		for (int i = 0; i < coordinates.length; i++)  
			utm[i] = new UTM(coordinates[i]);
		return utm;
	}
}
