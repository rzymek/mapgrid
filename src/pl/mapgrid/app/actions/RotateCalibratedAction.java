package pl.mapgrid.app.actions;

import java.awt.image.RenderedImage;

import javax.swing.JOptionPane;

import pl.mapgrid.app.Main;
import pl.mapgrid.calibration.coordinates.Coordinates;
import pl.mapgrid.calibration.coordinates.UTM;
import pl.mapgrid.gui.actions.BackgroundAction;
import pl.mapgrid.gui.actions.UIAction;

public class RotateCalibratedAction extends BackgroundAction implements UIAction {

	private final Main main;

	public RotateCalibratedAction(Main main) {
		super(main.actions, main);
		this.main = main;
	}

	@Override
	public void run() {
		Coordinates[] c = main.calibration.toUTM();
		double dx = Math.abs(c[3].getX() - c[0].getX());
		double dy = Math.abs(c[3].getY() - c[0].getY());
		double rot = Math.atan(dx/dy);
		String msg = String.format("Obrócic o %.2f\u00b0 do siatki UTM?", Math.toDegrees(rot));
		int confirmation = JOptionPane.showConfirmDialog(main, msg, "Obrót", JOptionPane.OK_CANCEL_OPTION);
		if(confirmation != JOptionPane.OK_OPTION) {
			return;
		}
		RenderedImage img = main.view.getImage();
		double dE = Math.abs(c[3].getX() - c[0].getX());
		double dN = Math.abs(c[1].getY() - c[0].getY());

//		double a1 = Math.abs(c[1].getX() - c[0].getX());
//		double b1 = Math.abs(c[1].getY() - c[0].getY());
//		double w = Math.sqrt(a1*a1+b1*b1);
//		double h = Math.sqrt(dx*dx+dy*dy);
		UTM[] utm = main.calibration.toUTM();
		
		utm[1].northing = utm[0].northing;
		
		utm[2].easting  = utm[1].easting; 
		
		utm[3].easting  = utm[0].easting;
		utm[3].northing = utm[2].northing;
		
		main.view.rotate(rot);
		main.calibration.coordinates=utm;
		System.out.println("RotateCalibratedAction.run()");
		System.out.println(main.calibration);
	}

	@Override
	public boolean enabled() {
		return main.calibration != null;
	}

	@Override
	public String toString() {
		return "Obróć";
	}
}
