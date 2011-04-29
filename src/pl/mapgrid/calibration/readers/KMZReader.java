package pl.mapgrid.calibration.readers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.micromata.opengis.kml.v_2_2_0.Kml;

import pl.mapgrid.calibration.coordinates.LatLon;
import pl.mapgrid.shape.Shape;

public class KMZReader extends KMLReader {
	private static final String[] SUFFIXES = {"kmz"};

	@Override

	public List<Shape<LatLon>> read(File file) throws Exception {
		Kml[] kmls = Kml.unmarshalFromKmz(file);
		List<Shape<LatLon>> shapes = new ArrayList<Shape<LatLon>>();
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
