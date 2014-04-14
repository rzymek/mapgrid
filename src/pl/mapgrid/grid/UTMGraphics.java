package pl.mapgrid.grid;

import java.awt.Graphics2D;

import pl.mapgrid.app.actions.UTMGridAction;
import pl.mapgrid.calibration.coordinates.LatLon;
import pl.mapgrid.calibration.coordinates.UTM;

import com.ibm.util.CoordinateConversion;
import com.ibm.util.CoordinateConversion.MGRUTM;

public class UTMGraphics extends GridGraphics {

	private int firstEasting;
	private int firstNorthing;
	private UTM start;

	public UTMGraphics(Graphics2D g, Config config, int width, int height, UTM utm) {
		super(g, config, width, height);
		firstEasting = (int) UTMGridAction.getKmLine(utm.getEasting(), config.step);
		firstNorthing = (int) UTMGridAction.getKmLine(utm.getNorthing(), config.step);
		start = new UTM(utm);
		start.easting = (int) UTMGridAction.getKmLine(utm.getEasting(), config.step);
		start.northing = (int) UTMGridAction.getKmLine(utm.getNorthing(), config.step);
//		start = CoordinateConversion.INST.latLon2MGRUTM(utm.getLat(), utm.getLon());
	}
	protected String getLabel(boolean horizontal, int i) {
		// horizontal is decreasing
		int addToLabel = (horizontal ? -1 : 1) * config.step * (i + 1);

		if (config.labelSplit.startsWith("M")) {
			String split = config.labelSplit.substring("M".length());
			int x,y;
			if(!horizontal) {
				x = (int) start.getX() + addToLabel;
				y = (int) start.getY();
			}else{
				x = (int) start.getX();
				y = (int) start.getY() + addToLabel;				
			}			
			final UTM utm = new UTM(x, y, start.latZone, start.lngZone);
			MGRUTM mgrs = CoordinateConversion.INST.latLon2MGRUTM(utm.getLat(),utm.getLon());
			String s = String.format("%s%s %s%s %s", mgrs.longZone, mgrs.latZone, mgrs.digraph1, mgrs.digraph2,
					horizontal ? mgrs.northing : mgrs.easting);
			return s.toString().replaceAll(split, config.labelReplace);
		} else {
			int firstLabel = horizontal ? firstNorthing : firstEasting;
			String label = String.valueOf(firstLabel + addToLabel);
			return label.replaceAll(config.labelSplit, config.labelReplace);
		}
	}
	
	public static void main(String[] args) {
		System.out.println(CoordinateConversion.INST.latLon2UTM(52, 28));
		System.out.println(new UTM(new LatLon(52, 28)).getZone());
//		double[] utm2LatLon = CoordinateConversion.INST.utm2LatLon("34U597422E5455617N");
//		System.out.println(Arrays.toString(utm2LatLon));
	}

}
