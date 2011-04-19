package pl.mapgrid.calibration;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.openstreetmap.josm.data.coor.CoordinateFormat;
import org.openstreetmap.josm.data.coor.EastNorth;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.projection.Puwg;
import org.openstreetmap.josm.data.projection.UTM;

import pl.mapgrid.calibration.coordinates.PUGW92;
import pl.mapgrid.calibration.exceptions.InvalidFormatException;

public class WorldFileReader extends TextFileReader {
	private double[] data = new double[6];
	private File association;
	public WorldFileReader(File file) throws Exception {
		super(file);
	}
	
	public static void main(String[] args) {
		Puwg puwg = new Puwg();
		double x = 501530.3854780000;
		double y = 778603.9596700000;
//		dump(puwg, x, y);
//		dump(puwg, y, x);
//		dump2(puwg, x, y);
		dump2(puwg, y, x);
	}

	private static void dump2(Puwg puwg, double x, double y) {
	}

	private static void dump(Puwg puwg, double x, double y) {
		LatLon ll = puwg.mapXYToLatLon(x, y, Math.toRadians(19));
		String lat = ll.latToString(CoordinateFormat.DEGREES_MINUTES_SECONDS);
		String lon = ll.lonToString(CoordinateFormat.DEGREES_MINUTES_SECONDS);
		System.out.println(lat+" "+lon);
	}


	@Override
	protected void processLine(String line, int lineNo) throws Exception {
		data[lineNo-1] = Double.parseDouble(line);
	}
	
	@Override
	protected void verify(Calibration calibration) throws Exception {
		if(data[1] != 0 || data[2] != 0)
			throw new InvalidFormatException("Rotacja nie wspierana.");
		String path = file.getPath();
		String basename = path.substring(0,path.lastIndexOf('.'));
		String filename = basename+".tif";
		association = new File(filename);
		ImageInputStream input = ImageIO.createImageInputStream(association);
		ImageReader reader = ImageIO.getImageReaders(input).next();
		reader.setInput(input);
		int width = reader.getWidth(reader.getMinIndex());
		int height = reader.getHeight(reader.getMinIndex());
		double ltx = data[4];
		double lty = data[5];
		double rbx = ltx + data[0]*width;
		double rby = lty + data[3]*height;
		calibration.coordinates[0] = new PUGW92(ltx, lty);
		calibration.coordinates[1] = new PUGW92(rbx, lty);
		calibration.coordinates[2] = new PUGW92(rbx, rby);
		calibration.coordinates[3] = new PUGW92(ltx, rby);
	}


	@Override
	public String[] getFileSuffixes() {
		return new String[]{"tfw"};
	}

	public File getAssociated() {
		return association;
	}
}
