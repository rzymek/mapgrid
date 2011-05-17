package pl.mapgrid.calibration.readers;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import pl.mapgrid.calibration.Calibration;
import pl.mapgrid.calibration.coordinates.PUWG92;
import pl.mapgrid.calibration.exceptions.InvalidFormatException;
import pl.mapgrid.utils.Utils;

public class WorldFileReader extends TextFileReader {
	private final double[] data = new double[6];
	private File associated;

	@Override
	protected void processLine(String line, int lineNo) throws Exception {
		data[lineNo-1] = Double.parseDouble(line);
	}
	
	@Override
	protected void verify(Calibration calibration) throws Exception {
		double[] values = data;
		if(values[1] != 0 || values[2] != 0)
			throw new InvalidFormatException("Rotacja nie wspierana.");
		associated = getAssociatedFile();		
		setupCalibration(associated, calibration, values, 4, 5, 0, 3);
	}

	private File getAssociatedFile() {
		String[] exts = ImageIO.getReaderFileSuffixes();
		File associated = null;
		for (String ext : exts) {
			associated = Utils.basename(file, "."+ext);
			if(associated.canRead())
				return associated;
		}
		throw new RuntimeException("Nie mogę otworzyć pliku z grafiką skojażonego z "+file.getName());
	}

	public static void setupCalibration(File associated, Calibration calibration, double[] values,
			int ilx, int ily, int ippmx, int ippmy) throws IOException {
		ImageInputStream input = ImageIO.createImageInputStream(associated);
		ImageReader reader = ImageIO.getImageReaders(input).next();
		reader.setInput(input);
		int width = reader.getWidth(reader.getMinIndex());
		int height = reader.getHeight(reader.getMinIndex());
		double ltx = values[ilx];
		double lty = values[ily];
		double rbx = ltx + values[ippmx]*width;
		double rby = lty + values[ippmy]*height;
		System.out.println("WorldFileReader.setupCalibration()");
		System.out.println(values[ippmx]);
		System.out.println("0.0");
		System.out.println("0.0");
		System.out.println(values[ippmy]);
		System.out.println(values[ilx]);
		System.out.println(values[ily]);
		System.out.println();
//		System.out.println(Math.abs(ltx-rbx));
//		System.out.println(Math.abs(lty-rby));
		calibration.coordinates[0] = new PUWG92(ltx, lty);
		calibration.coordinates[1] = new PUWG92(rbx, lty);
		calibration.coordinates[2] = new PUWG92(rbx, rby);
		calibration.coordinates[3] = new PUWG92(ltx, rby);
	}

	@Override
	public String[] getFileSuffixes() {
		return new String[]{"tfw"};
	}
	@Override
	public File getAssociated() {
		return associated;
	}
}
