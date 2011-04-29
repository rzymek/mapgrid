package pl.mapgrid.calibration.readers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pl.mapgrid.calibration.coordinates.Coordinates;
import pl.mapgrid.shape.Shape;
import de.micromata.opengis.kml.v_2_2_0.Kml;

public class KMZReader extends KMLReader {
	private static final String[] SUFFIXES = {"kmz"};

	@Override

	public List<Shape<Coordinates>> read(File file) throws Exception {
		Kml[] kmls = Kml.unmarshalFromKmz(file);
		List<Shape<Coordinates>> shapes = new ArrayList<Shape<Coordinates>>();
		for (Kml kml : kmls) {
			shapes.addAll(read(kml));
		}
		return shapes;
	}
	
	@Override
	public String[] getFileSuffixes() {
		return SUFFIXES;
	}
}
