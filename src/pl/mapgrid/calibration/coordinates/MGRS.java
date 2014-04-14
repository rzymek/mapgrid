package pl.mapgrid.calibration.coordinates;

import com.ibm.util.CoordinateConversion;
import com.ibm.util.CoordinateConversion.MGRUTM;

public class MGRS implements Coordinates {
	public static final CoordinateConversion CONV = new CoordinateConversion();
	private MGRUTM pos;

	public MGRS(Coordinates coordinates) {
		double latitude = coordinates.getLat();
		double longitude = coordinates.getLon();
		pos = CONV.latLon2MGRUTM(latitude, longitude);
	}

	@Override
	public double getLat() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getLon() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getY() {
		// TODO Auto-generated method stub
		return 0;
	}

}
