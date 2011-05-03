package pl.mapgrid.shape;

import java.awt.Point;

import pl.mapgrid.calibration.Calibration;
import pl.mapgrid.calibration.coordinates.Coordinates;
import pl.mapgrid.calibration.coordinates.LatLon;
import pl.mapgrid.calibration.coordinates.PUWG92;
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
		PUWG92[] puwg = calibration.toPuwg();
//		System.out.println(calibration);
		double y = Math.abs(puwg[3].getY() - puwg[0].getY());
		double x = Math.abs(puwg[1].getX() - puwg[0].getX());
		double metersPerPixel = x/width;
		double metersPerPixel2 = y/height;
		for(int i=0;i<shape.count();++i) {
			Coordinates orig = shape.get(i);
			Coordinates c = new PUWG92(orig);
			LatLon latLon = new LatLon(orig);
			UTM utm = new UTM(orig);
//			System.out.println(i);
//			System.out.println(orig);
//			System.out.println(c);
//			System.out.println(latLon);
//			System.out.println(utm);
			double xx1= (c.getX() - puwg[0].getX())/metersPerPixel;
			double xx2 = width - (puwg[1].getX() - c.getX())/metersPerPixel;
			double yy1= ((puwg[0].getY() - c.getY())/metersPerPixel2);
			double yy2= height - (c.getY() - puwg[3].getY())/metersPerPixel2;
			Point point = new Point((int)((xx1+xx2)/2),(int)((yy1+yy2)/2));
			points[i] = point;
		}
		return points;
	}

}
