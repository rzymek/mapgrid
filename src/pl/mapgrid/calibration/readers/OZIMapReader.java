package pl.mapgrid.calibration.readers;

import java.io.File;
import java.text.DecimalFormat;

import pl.mapgrid.Messages;
import pl.mapgrid.calibration.coordinates.LatLon;
import pl.mapgrid.calibration.exceptions.InvalidFormatException;

public class OZIMapReader extends TextFileReader {
	private static final String MMPLL = "MMPLL";
	private static final String HEADER = "OziExplorer Map Data File Version";
	private static final DecimalFormat DOUBLE_FORMAT = new DecimalFormat("#.#");
	private File associated;

	@Override
	protected void processLine(String line, int lineNo) throws Exception {
		if(lineNo == 1)
			verifyFileType(line, lineNo);
		else if(lineNo==3) 
			findAssociatedFile(line, lineNo);
		if(line.startsWith(MMPLL)) {
			readCoords(line, lineNo);
		}
	}

	private void findAssociatedFile(String line, int lineNo) {
		associated = new File(file.getParentFile(), line);
		if(associated.canRead())
			return;
		int lastSlash = line.lastIndexOf("\\");
		if(lastSlash == -1)
			lastSlash = line.lastIndexOf("/");
		String name = line.substring(lastSlash+1);
		associated = new File(file.getParentFile(), name);
		if(associated.canRead() == false)
			throw new InvalidFormatException(Messages.CANT_READ_ASSOCIATED+line, lineNo);
	}

	private void readCoords(String line, int lineNo) throws Exception {		
		String[] split = line.split(",");
		String mmpll = split[0];
		if(MMPLL.equals(mmpll) == false)
			throw new InvalidFormatException(MMPLL, mmpll, lineNo);
		int nr = Integer.parseInt(split[1].trim())-1;
		double lon = DOUBLE_FORMAT.parse(split[2].trim()).doubleValue();
		double lat = DOUBLE_FORMAT.parse(split[3].trim()).doubleValue();
		if(calibration.coordinates[nr] != null)
			throw new InvalidFormatException(Messages.DOUBLE_ENTRY, line, lineNo);
		calibration.coordinates[nr] = new LatLon(lat, lon);
	}

	protected void verifyFileType(String line, int lineNo) {
		if (line.startsWith(HEADER) == false)
			throw new InvalidFormatException(HEADER, line, lineNo);
	}
	
	
	public File getAssociated() {
		return associated;
	}
	
	public String[] getFileSuffixes() {
		return new String[]{"map"};
	}
}