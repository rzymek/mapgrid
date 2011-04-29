package pl.mapgrid.calibration.readers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class Registry {
	static FileReader[] featureReaders = {
		new KMLReader(),
	};
	static CalibrationReader[] readers = {
			new OZIMapReader(),
			new WorldFileReader(),
			new AuxXmlReader(),
	};
	
	public static List<String> getReaderFileSuffixes() {
		return getReaderFileSuffixes(readers);
	}
	public static List<String> getFeatureFileSuffixes() {
		return getReaderFileSuffixes(featureReaders);
	}

	private static List<String> getReaderFileSuffixes(FileReader[] readers) {
		List<String> exts = new ArrayList<String>();
		for (FileReader reader : readers) 
			exts.addAll(Arrays.asList(reader.getFileSuffixes()));
		return exts;
	}
	
	private static <T extends FileReader> T getReader(String filename, T[] readers) {
		filename = filename.toLowerCase();
		for (T reader : readers) { 
			String[] fileSuffixes = reader.getFileSuffixes();
			for (String ext : fileSuffixes) {
				if(filename.endsWith("."+ext.toLowerCase()))
					return reader;
			}
		}
		throw new NoSuchElementException("Nieobsługiwany format pliku: "+filename);
	}
	
	public static CalibrationReader getReader(String filename) {
		return getReader(filename, readers);
	}
	public static FileReader getFeatureReader(String filename) {
		return getReader(filename, featureReaders);
	}
}
