package pl.mapgrid.shape;

import java.awt.Point;

import pl.mapgrid.calibration.Calibration;
import pl.mapgrid.calibration.coordinates.Coordinates;

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
		CoordProjector projector = new CoordProjector(calibration, width, height);
		for(int i=0;i<shape.count();++i) {
			Coordinates c = shape.get(i);			
			points[i] = projector.project(c);
		}
		return points;
	}

}
