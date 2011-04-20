package pl.mapgrid.calibration.readers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class Registry {
	static CalibrationReader[] readers = {
			new OZIMapReader(),
			new WorldFileReader(),
	};
	
	public static List<String> getReaderFileSuffixes() {
		List<String> exts = new ArrayList<String>();
		for (CalibrationReader reader : readers) 
			exts.addAll(Arrays.asList(reader.getFileSuffixes()));
		return exts;
	}
	
	public static CalibrationReader getReader(String filename) {
		filename = filename.toLowerCase();
		for (CalibrationReader reader : readers) { 
			String[] fileSuffixes = reader.getFileSuffixes();
			for (String ext : fileSuffixes) {
				if(filename.endsWith("."+ext.toLowerCase()))
					return reader;
			}
		}
		throw new NoSuchElementException("Nieobsługiwany format pliku: "+filename);
	}
}
