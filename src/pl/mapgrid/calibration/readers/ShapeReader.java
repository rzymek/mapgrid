package pl.mapgrid.calibration.readers;

import java.io.File;
import java.util.List;

import pl.mapgrid.calibration.coordinates.LatLon;
import pl.mapgrid.shape.Shape;

public interface ShapeReader extends FileReader {
	List<Shape<LatLon>> read(File file) throws Exception;
}
