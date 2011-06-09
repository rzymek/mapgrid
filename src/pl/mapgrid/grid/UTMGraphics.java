package pl.mapgrid.grid;

import java.awt.Graphics2D;

import pl.mapgrid.app.actions.UTMGridAction;
import pl.mapgrid.calibration.coordinates.UTM;

public class UTMGraphics extends GridGraphics {

	private int firstEasting;
	private int firstNorthing;

	public UTMGraphics(Graphics2D g, Config config, int width, int height, UTM utm) {
		super(g, config, width, height);
		firstEasting = (int) UTMGridAction.getKmLine(utm.getEasting(), config.step); 
		firstNorthing = (int) UTMGridAction.getKmLine(utm.getNorthing(), config.step);
	}
	
	protected String getLabel(boolean horizontal, int i) {
		//horizontal is decreasing
		int addToLabel = (horizontal ? -1 : 1) * config.step * (i+1);
		
		int firstLabel = horizontal ? firstNorthing : firstEasting;
		String label = String.valueOf(firstLabel + addToLabel);
		label = label.replaceAll(config.labelSplit, config.labelReplace);
		return label;
	}
	

}
