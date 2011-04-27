package pl.mapgrid.calibration.readers;

import java.io.File;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import pl.mapgrid.calibration.Calibration;
import pl.mapgrid.calibration.coordinates.PUWG92;
import pl.mapgrid.calibration.exceptions.InvalidFormatException;
import pl.mapgrid.utils.Utils;

public class WorldFileReader extends TextFileReader {
	private double[] data = new double[6];
	private File associated;

	@Override
	protected void processLine(String line, int lineNo) throws Exception {
		data[lineNo-1] = Double.parseDouble(line);
	}
	
	@Override
	protected void verify(Calibration calibration) throws Exception {
		if(data[1] != 0 || data[2] != 0)
			throw new InvalidFormatException("Rotacja nie wspierana.");
		associated = Utils.basename(file, ".tif");
		ImageInputStream input = ImageIO.createImageInputStream(associated);
		ImageReader reader = ImageIO.getImageReaders(input).next();
		reader.setInput(input);
		int width = reader.getWidth(reader.getMinIndex());
		int height = reader.getHeight(reader.getMinIndex());
		double ltx = data[4];
		double lty = data[5];
		double rbx = ltx + data[0]*width;
		double rby = lty + data[3]*height;
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
