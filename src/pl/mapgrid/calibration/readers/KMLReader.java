package pl.mapgrid.calibration.readers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pl.mapgrid.calibration.coordinates.Coordinates;
import pl.mapgrid.calibration.coordinates.LatLon;
import pl.mapgrid.shape.Shape;
import pl.mapgrid.shape.Waypoint;
import de.micromata.opengis.kml.v_2_2_0.Boundary;
import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Geometry;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.LineString;
import de.micromata.opengis.kml.v_2_2_0.LinearRing;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Point;
import de.micromata.opengis.kml.v_2_2_0.Polygon;

public class KMLReader implements ShapeReader {
	private static final String[] SUFFIXES = { "kml" };

	@Override
	public List<Shape<Coordinates>> read(File file) throws Exception {
		return read(Kml.unmarshal(file));
	}

	protected List<Shape<Coordinates>> read(Kml kml) {
		List<Shape<Coordinates>> result = new ArrayList<Shape<Coordinates>>();
		Document doc = (Document) kml.getFeature();
		processFeatures(result, doc.getFeature());
		return result;
	}

	private void processFeatures(List<Shape<Coordinates>> result, List<Feature> features) {
		for (Feature feature : features) {
			if (feature instanceof Placemark) {
				Placemark placemark = (Placemark) feature;
				Geometry geometry = placemark.getGeometry();
				if (geometry instanceof Point) {
					Point p = (Point) geometry;
					result.add(toShape(p));
				} else if (geometry instanceof Polygon) {
					Polygon p = (Polygon) geometry;
					result.add(toShape(p));
				} else if(geometry instanceof LineString) {
					LineString g = (LineString) geometry;
					result.add(toShape(g));
				} else if(geometry instanceof LinearRing) {
					LinearRing g = (LinearRing) geometry;
					result.add(toShape(g));
				}
			}else if(feature instanceof Folder){
				processFeatures(result, ((Folder) feature).getFeature());
			}
		}
	}

	private Shape<Coordinates> toShape(LinearRing g) {
		Coordinates[] points = toPoints(g.getCoordinates());
		return new pl.mapgrid.shape.Path<Coordinates>(points);
	}

	private Shape<Coordinates> toShape(LineString g) {
		Coordinates[] points = toPoints(g.getCoordinates());
		return new pl.mapgrid.shape.Path<Coordinates>(points);
	}

	private Shape<Coordinates> toShape(Polygon p) {
		Boundary boundary = p.getOuterBoundaryIs();
		LinearRing ring = boundary.getLinearRing();
		Coordinates[] points = toPoints(ring.getCoordinates());
		return new pl.mapgrid.shape.Polygon<Coordinates>(points);
	}

	private Coordinates[] toPoints(List<Coordinate> coordinates) {
		Coordinates[] points = new Coordinates[coordinates.size()];
		for (int i = 0; i < coordinates.size(); i++)
			points[i] = toLatLon(coordinates.get(i));
		return points;
	}

	private Shape<Coordinates> toShape(Point p) {
		return toShape(p.getCoordinates().get(0));
	}

	private Shape<Coordinates> toShape(Coordinate c) {
		Coordinates latlon = toLatLon(c);
		Shape<Coordinates> shape = new Waypoint<Coordinates>(latlon);
		return shape;
	}

	private Coordinates toLatLon(Coordinate c) {
		return new LatLon(c.getLatitude(), c.getLongitude());
	}

	@Override
	public String[] getFileSuffixes() {
		return SUFFIXES;
	}

	public static void main(String[] args) throws Exception {
		KMLReader reader = new KMLReader();
		List<Shape<Coordinates>> read = reader.read(new File("samples/rr3-places.kml"));
		for (Shape<Coordinates> shape : read) {
			System.out.println(shape);
		}
	}
}
