package pl.mapgrid.calibration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import pl.mapgrid.Messages;
import pl.mapgrid.calibration.coordinates.Coordinates;
import pl.mapgrid.calibration.exceptions.InvalidFormatException;

public abstract class TextFileReader {
	protected final File file;
	protected final BufferedReader in;
	protected final Calibration calibration;

	public TextFileReader(File file) throws Exception {
		this.file = file;
		FileReader reader = new FileReader(file);
		in = new BufferedReader(reader);
		calibration = new Calibration();
	}
	
	public Calibration read() throws Exception {
		for (int lineNo = 1;;++lineNo) {
			String line = in.readLine();
			if (line == null)
				break;
			try{
				processLine(line, lineNo);
			}catch (Exception e) {
				throw new InvalidFormatException(e,line);
			}
		}
		verify(calibration);
		return calibration;
	}

	protected void verify(Calibration calibration) throws Exception {		
		for (int i = 0; i < calibration.coordinates.length; i++) {
			Coordinates coordinates = calibration.coordinates[i];
			if(coordinates == null) 
				throw new InvalidFormatException(Messages.COORDS_MISSING+(i+1));
		}
	}

	protected abstract void processLine(String line, int lineNo) throws Exception;

	public abstract String[] getFileSuffixes();

}
