package pl.mapgrid.app.actions;

import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;

import pl.mapgrid.app.Main;
import pl.mapgrid.calibration.coordinates.UTM;
import pl.mapgrid.gui.actions.UIAction;

public class UTMGridAction extends AbstractAction implements UIAction {

	private final Main main;

	public UTMGridAction(Main main) {
		this.main = main;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		UTM[] utm = main.calibration.toUTM();
		BufferedImage image = main.view.getImage();
		List<int[]> vertical = new ArrayList<int[]>();
		List<int[]> horizontal = new ArrayList<int[]>();
		int step = main.getUtmConfig().step;
		double nx = Math.abs(utm[1].getNorthing() - utm[0].getNorthing());
		double ex = Math.abs(utm[1].getEasting() - utm[0].getEasting());
		double ny = Math.abs(utm[3].getNorthing() - utm[0].getNorthing());
		double ey = Math.abs(utm[3].getEasting() - utm[0].getEasting());
		double rotX = Math.atan(nx/ex);
		double rotY = Math.atan(ey/ny);
		double metersX = Math.sqrt(nx*nx+ex*ex);
		double metersY = Math.sqrt(ny*ny+ey*ey);
		double metersPerPixelX = metersX / image.getWidth();
		double metersPerPixelY = metersY / image.getHeight();
		double start1 = step - getStart(utm[0].getEasting());
		double start2 = Math.abs(utm[0].getEasting() + start1 - utm[3].getEasting());
		double convX = Math.cos(rotX)*metersPerPixelX;
		double convY = Math.cos(rotY)*metersPerPixelY;
		double x1 = start1/convX;//115
		double x2 = start2/convX;//141
		for(int i=step;;i+=step) {
			vertical.add(new int[] { (int) x1, 0, (int) x2, image.getHeight() });
			x1 = (start1+i)/convX;
			x2 = (start2+i)/convX;
			if(x1 > image.getWidth() && x2 > image.getWidth())
				break;
		}
		start1 = getStart(utm[0].getNorthing());
		start2 = Math.abs(utm[0].getNorthing() - start1 - utm[1].getNorthing());
		double y1 = start1/convY;
		double y2 = start2/convY;
		for(int i=step;;i+=step) {
			horizontal.add(new int[] { 0, (int) y1, image.getWidth(), (int) y2 });
			y1 = (start1+i)/convY;
			y2 = (start2+i)/convY;
			if(y1 > image.getHeight() && y2 > image.getHeight())
				break;
		}
		
		main.view.setGrid(vertical, horizontal, step/convX, step/convY);
	}

	@Override
	public boolean enabled() {
		return main.calibration != null;
	}

	private double getStart(double v) {
		return v - getKmLine(v, main.getUtmConfig().step);
	}

	public static double getKmLine(double v, int step) {
		return Math.floor(v/step)*step;
	}

	@Override
	public String toString() {
		return "UTM";
	}
}
