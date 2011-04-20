package pl.mapgrid.calibration.readers;

import java.io.File;

import pl.mapgrid.calibration.Calibration;

public interface CalibrationReader {
	String[] getFileSuffixes();
	File getAssociated();
	Calibration read(File file) throws Exception;
}
