package pl.mapgrid.calibration;

import java.util.Arrays;

import pl.mapgrid.calibration.coordinates.Coordinates;

public class Calibration {
	 // = {left-top, right-top, right-bottom, left-bottom}
	public Coordinates[] coordinates = new Coordinates[4];
	@Override
	public String toString() {
		return Arrays.asList(coordinates).toString();
	}
}
