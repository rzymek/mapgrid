package pl.mapgrid.calibration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DecimalFormat;

import pl.mapgrid.Messages;
import pl.mapgrid.calibration.coordinates.Coordinates;
import pl.mapgrid.calibration.coordinates.LatLon;
import pl.mapgrid.calibration.exceptions.InvalidFormatException;

public class OZIMapReader {
	private static final String MMPLL = "MMPLL";
	private static final String HEADER = "OziExplorer Map Data File Version";
	private static final DecimalFormat DOUBLE_FORMAT = new DecimalFormat("#.#");
	private int lineNo;
	private Calibration calibration;
	private String line;
	private BufferedReader in;
	private File associated;
	private final File file;

	public OZIMapReader(File file) throws Exception {
		this.file = file;
		FileReader reader = new FileReader(file);
		in = new BufferedReader(reader);
		calibration = new Calibration();
	}
	
	public Calibration read() throws Exception {
		for (lineNo = 1;;++lineNo) {
			line = in.readLine();
			if (line == null)
				break;
			if(lineNo == 1)
				verifyFileType();
			else if(lineNo==3) 
				findAssociatedFile();
			try{
				if(line.startsWith(MMPLL)) {
					readCoords();
				}
			}catch (Exception e) {
				throw new InvalidFormatException(e,line);
			}
		}
		verify(calibration);
		return calibration;
	}

	private void findAssociatedFile() {
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

	private void verify(Calibration calibration) {		
		for (int i = 0; i < calibration.coordinates.length; i++) {
			Coordinates coordinates = calibration.coordinates[i];
			if(coordinates == null) 
				throw new InvalidFormatException(Messages.COORDS_MISSING+(i+1));
		}
	}

	private void readCoords() throws Exception {		
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

	protected void verifyFileType() {
		if (line.startsWith(HEADER) == false)
			throw new InvalidFormatException(HEADER, line, lineNo);
	}
	
	public static void main(String[] args) throws Exception {
		OZIMapReader reader = new OZIMapReader(new File("samples/rr3.map"));
		Calibration c = reader.read();
		System.out.println(c);
	}
	public File getAssociated() {
		return associated;
	}
	
	public String[] getFileSuffixes() {
		return new String[]{".map"};
	}
}