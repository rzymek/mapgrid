package pl.mapgrid.mask.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;

import pl.mapgrid.calibration.coordinates.UTM;
import pl.mapgrid.gui.actions.UIAction;
import pl.mapgrid.mask.gui.JMaskGridMain;

public class UTMGridAction extends AbstractAction implements UIAction {

	private final JMaskGridMain main;

	public UTMGridAction(JMaskGridMain main) {
		this.main = main;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		UTM[] utm = main.calibration.toUTM();
		BufferedImage image = main.view.getImage();
		List<int[]> vertical = new ArrayList<int[]>();
		List<int[]> horizontal = new ArrayList<int[]>();
		double n = Math.abs(utm[1].getNorthing() - utm[0].getNorthing());
		double e = Math.abs(utm[1].getEasting() - utm[0].getEasting());
		double rot = Math.atan(n/e);
		double meters = Math.sqrt(n*n+e*e);
		double metersPerPixel = meters / image.getWidth();
		double start1 = 1000.0 - getStart(utm[0].getEasting());
		double start2 = Math.abs(utm[0].getEasting() + start1 - utm[3].getEasting());
		double conv = Math.cos(rot)*metersPerPixel;
		double x1 = start1/conv;//115
		double x2 = start2/conv;//141
		for(int i=1000;;i+=1000) {
			vertical.add(new int[] { (int) x1, 0, (int) x2, image.getHeight() });
			x1 = (start1+i)/conv;
			x2 = (start2+i)/conv;
			if(x1 > image.getWidth() && x2 > image.getWidth())
				break;
		}
		start1 = getStart(utm[0].getNorthing());
		start2 = Math.abs(utm[0].getNorthing() - start1 - utm[1].getNorthing());
		double y1 = start1/conv;
		double y2 = start2/conv;
		for(int i=1000;;i+=1000) {
			horizontal.add(new int[] { 0, (int) y1, image.getWidth(), (int) y2 });
			y1 = (start1+i)/conv;
			y2 = (start2+i)/conv;
			if(y1 > image.getHeight() && y2 > image.getHeight())
				break;
		}

		int firstEasting = (int) getKmLine(utm[0].getEasting()); 
		int firstNorthing = (int) getKmLine(utm[0].getNorthing());
		main.view.setGrid(vertical, horizontal, firstEasting, firstNorthing);
		main.view.repaint();
	}

	@Override
	public boolean enabled() {
		return main.calibration != null;
	}

	private double getStart(double v) {
		return v - getKmLine(v);
	}

	private double getKmLine(double v) {
		return Math.floor(v/1000.0)*1000.0;
	}

	@Override
	public String toString() {
		return "UTM";
	}
}
