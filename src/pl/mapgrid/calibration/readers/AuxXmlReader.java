package pl.mapgrid.calibration.readers;

import java.io.File;
import java.io.FileReader;
import java.util.Arrays;

import org.openstreetmap.josm.actions.ValidateAction;
import org.xml.sax.InputSource;

import pl.mapgrid.calibration.Calibration;
import pl.mapgrid.utils.Utils;

public class AuxXmlReader implements CalibrationReader{

	private static final String[] SUFFIXES = {"aux.xml"};
	private File associated;

	@Override
	public String[] getFileSuffixes() {
		return SUFFIXES;
	}

	@Override
	public File getAssociated() {
		return associated;
	}

	@Override
	public Calibration read(File file) throws Exception {
		associated = Utils.basename(file, ".png");
		FileReader reader = new FileReader(file);
		String transform = Utils.xpath(new InputSource(reader), "/PAMDataset/GeoTransform");
		String[] strings = transform.split(", *");
		double[] values = new double[strings.length];
		for (int i = 0; i < strings.length; i++) {
			values[i] = Double.parseDouble(strings[i].trim());
		}
		String tmp = Arrays.toString(values);
		System.out.println(tmp);
		throw new RuntimeException(tmp);
	}

}
