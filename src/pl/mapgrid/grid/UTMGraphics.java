package pl.mapgrid.grid;

import java.awt.Graphics2D;

import pl.mapgrid.app.actions.UTMGridAction;
import pl.mapgrid.calibration.coordinates.UTM;

public class UTMGraphics extends GridGraphics {

	private int firstEasting;
	private int firstNorthing;

	public UTMGraphics(Graphics2D g, Config config, int width, int height, UTM utm) {
		super(g, config, width, height);
		firstEasting = (int) UTMGridAction.getKmLine(utm.getEasting()); 
		firstNorthing = (int) UTMGridAction.getKmLine(utm.getNorthing());
	}
	
	protected String getLabel(boolean horizontal, int i) {
		//horizontal is decreasing
		int addToLabel = (horizontal ? -1 : 1) * 1000 * (i+1);
		
		int firstLabel = horizontal ? firstEasting : firstNorthing;
		String label = String.valueOf(firstLabel + addToLabel);
		label = label.replaceAll("(.*)(.)(...)", "$1 $2 $3");
		return label;
	}
	

}
