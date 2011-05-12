package pl.mapgrid.grid;

import java.awt.Graphics2D;

import pl.mapgrid.calibration.coordinates.LatLon;

public class LatLonGridGraphics extends GridGraphics {

	private double startX;
	private double startY;

	public LatLonGridGraphics(Graphics2D g, Config config, int width, int height, LatLon tl) {
		super(g, config, width, height);
		startX = LatLonGridGraphics.roundSeconds(tl.getX());
		startY = LatLonGridGraphics.roundSeconds(tl.getY());
	}

	@Override
	protected String getLabel(boolean horizontal, int i) {
		double value = horizontal ? startY : startX;
		double sign = horizontal ? -1.0 : 1.0;
		double fix =  horizontal ? 1.0 : 0.0;
		value = value + sign*(i+fix)*10.0/(60*60.0);		
		return toDegMinSec(value);
	}

	public static double roundSeconds(double value) {
		double deg = Math.floor(value);
		// correction required since floor() is not the same as int()
		if ( deg < 0 )
			deg = deg + 1;

		// Get fraction after the decimal
		double frac = Math.abs( value - deg );

		// Convert this fraction to seconds (without minutes)
		double sec = frac * 3600;

		// Determine number of whole minutes in the fraction
		double minute = Math.floor( sec / 60 );

		// Put the remainder in seconds
		double second = sec - minute * 60;

		// Fix rounoff errors
		if ( Math.rint( second ) == 60 )
		{
			minute = minute + 1;
			second = 0;
		}
//		System.out.println(seconds+" "+toDegMinSec(seconds));
//		second = Math.ceil(second/10.0)*10;
		double wholeSec = Math.floor(second/10.0)*10.0;
		return value-(second-wholeSec)/(60*60);
	}

	private static String toDegMinSec(double value) {
		double deg = Math.floor(value);
		// correction required since floor() is not the same as int()
		if ( deg < 0 )
			deg = deg + 1;

		// Get fraction after the decimal
		double frac = Math.abs( value - deg );

		// Convert this fraction to seconds (without minutes)
		double sec = frac * 3600;

		// Determine number of whole minutes in the fraction
		double minute = Math.floor( sec / 60 );

		// Put the remainder in seconds
		double second = sec - minute * 60;

		// Fix rounoff errors
		if ( Math.rint( second ) == 60 )
		{
			minute = minute + 1;
			second = 0;
		}

		if ( Math.rint( minute ) == 60 )
		{
			if ( deg < 0 )
				deg = deg - 1;
			else // ( dfDegree => 0 )
				deg = deg + 1;

			minute = 0;
		}
		return String.format("%.0f°%.0f'%.0f", deg,minute,second);
	}	

}
