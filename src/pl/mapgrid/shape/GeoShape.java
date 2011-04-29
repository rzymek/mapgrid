package pl.mapgrid.shape;

import java.awt.Point;

import pl.mapgrid.calibration.Calibration;
import pl.mapgrid.calibration.coordinates.Coordinates;
import pl.mapgrid.utils.Utils;
import pl.mapgrid.utils.Utils.CoordType;

public class GeoShape {
	public static Shape<Point> project(Shape<? extends Coordinates> shape, Calibration calibration, int width, int height) {
		Point[] points = new Point[shape.count()];
		Coordinates[] calib = calibration.coordinates;
		for(int i=0;i<shape.count();++i) {
			Coordinates c = shape.get(i);
			int x1 = toPoint(c, calib, 1, 0, CoordType.LON, width, false);
			int x2 = toPoint(c, calib, 2, 3, CoordType.LON, width, false);
			int y1 = toPoint(c, calib, 0, 3, CoordType.LAT, height, true);
			int y2 = toPoint(c, calib, 1, 2, CoordType.LAT, height, true);
			int x = (x1+x2)/2;
			int y = (y1+y2)/2;
			points[i] = new Point(x,y);
		}
		return new Path<Point>(points);
	}
	
	private static int toPoint(Coordinates c, Coordinates[] calib, int i1, int i2, CoordType type, int length, boolean revert) {
		double v1 = Utils.get(calib[i1], type);
		double v2 = Utils.get(calib[i2], type);
		double min = Math.min(v1, v2);
		double lontw  = Math.abs(v1-v2);
		double vc = Utils.get(c, type);
		int v;
		if(revert)
			v = (int) (length - length * (vc - min)/lontw);
		else
			v = (int) (length*(vc - min)/lontw);
		return v;
	}

}
