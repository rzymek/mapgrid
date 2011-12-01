package pl.mapgrid.calibration.readers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import pl.mapgrid.Messages;
import pl.mapgrid.calibration.Calibration;
import pl.mapgrid.calibration.coordinates.Coordinates;
import pl.mapgrid.calibration.exceptions.InvalidFormatException;

public abstract class TextFileReader implements CalibrationReader {
	protected File file;
	protected Calibration calibration;

	public Calibration read(File file) throws Exception {
		this.file = file;
		FileReader reader = new FileReader(file);
		BufferedReader in = new BufferedReader(reader);
		try {
			calibration = new Calibration();
	
			for (int lineNo = 1;;++lineNo) {
				String line = in.readLine();
				if (line == null)
					break;
				try{
					processLine(line, lineNo);
				}catch (Exception e) {
					throw new InvalidFormatException(e,lineNo+": "+line);
				}
			}
			verify(calibration);
			return calibration;
		}finally{
			in.close();
		}
	}

	protected void verify(Calibration calibration) throws Exception {		
		for (int i = 0; i < calibration.coordinates.length; i++) {
			Coordinates coordinates = calibration.coordinates[i];
			if(coordinates == null) 
				throw new InvalidFormatException(Messages.COORDS_MISSING+(i+1));
		}
	}

	protected abstract void processLine(String line, int lineNo) throws Exception;

}
