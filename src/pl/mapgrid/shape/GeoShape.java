package pl.mapgrid.shape;

import java.awt.Point;

import pl.mapgrid.calibration.Calibration;
import pl.mapgrid.calibration.coordinates.Coordinates;
import pl.mapgrid.calibration.coordinates.UTM;

public class GeoShape {
	public static Shape<Point> project(Shape<? extends Coordinates> shape, Calibration calibration, int width, int height) {
		Shape<Point> s;
		Point[] points = projectPoints(shape, calibration, width, height);
		if(shape instanceof Waypoint) {
			Waypoint<Point> wp = new Waypoint<Point>(points[0]);
			wp.label = ((Waypoint<?>) shape).label;
			s = wp;
		}else{
			s = new Path<Point>(points);
		}
		s.style = shape.style;
		return s;
	}

	private static Point[] projectPoints(Shape<? extends Coordinates> shape, Calibration calibration, int width, int height) {
		Point[] points = new Point[shape.count()];
		Coordinates[] puwg = calibration.toUTM();
		double tX = Math.abs(puwg[1].getX() - puwg[0].getX());
		double tY = Math.abs(puwg[1].getY() - puwg[0].getY());
		double lX = Math.abs(puwg[3].getX() - puwg[0].getX());
		double lY = Math.abs(puwg[3].getY() - puwg[0].getY());
		double X = Math.sqrt(tX*tX+tY*tY);
		double Y = Math.sqrt(lX*lX+lY*lY);
		double metersPerPixelX = X/width;
		double metersPerPixelY = Y/height;
		for(int i=0;i<shape.count();++i) {
			Coordinates orig = shape.get(i);
			Coordinates c = new UTM(orig);
			double dx = Math.abs(c.getX() - puwg[0].getX());
			double dy = Math.abs(c.getY() - puwg[0].getY());
			double x = (dx*tX+dy*tY)/X;
			double y = (dy*tX-dx*tY)/X;
			int xx = (int) (x/metersPerPixelX);
			int yy = (int) (y/metersPerPixelY);
			Point point = new Point(xx,yy);
			points[i] = point;
		}
		return points;
	}

}
