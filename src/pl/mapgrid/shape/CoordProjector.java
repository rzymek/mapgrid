package pl.mapgrid.shape;

import java.awt.Point;

import pl.mapgrid.calibration.Calibration;
import pl.mapgrid.calibration.coordinates.Coordinates;
import pl.mapgrid.calibration.coordinates.UTM;

public class CoordProjector {

	private final UTM[] calib;
	private final double tX;
	private final double tY;
	private final double X;
	private final double Y;
	private final double metersPerPixelX;
	private final double metersPerPixelY;

	public CoordProjector(Calibration calibration, int width, int height) {
		calib = calibration.toUTM();
		tX = Math.abs(calib[1].getX() - calib[0].getX());
		tY = Math.abs(calib[1].getY() - calib[0].getY());
		double lX = Math.abs(calib[3].getX() - calib[0].getX());
		double lY = Math.abs(calib[3].getY() - calib[0].getY());
		X = Math.sqrt(tX*tX+tY*tY);
		Y = Math.sqrt(lX*lX+lY*lY);
		metersPerPixelX = X/width;
		metersPerPixelY = Y/height;
	}

	public Point project(Coordinates orig) {
		Coordinates c = new UTM(orig);
		double dx = Math.abs(c.getX() - calib[0].getX());
		double dy = Math.abs(c.getY() - calib[0].getY());
		double x = (dx*tX+dy*tY)/X;
		double y = (dy*tX-dx*tY)/X;
		int xx = (int) (x/metersPerPixelX);
		int yy = (int) (y/metersPerPixelY);
		return new Point(xx,yy);
	}

	public Point project(Point p) {
		throw new RuntimeException("TODO");
	}
}
