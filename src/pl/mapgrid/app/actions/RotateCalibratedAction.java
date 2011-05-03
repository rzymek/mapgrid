package pl.mapgrid.app.actions;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

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
		double a = c[3].getX() - c[0].getX();
		double b = Math.abs(c[3].getY() - c[0].getY());
		double rot = Math.atan(a/b);
		String msg = String.format("Obrócic o %.2f\u00b0 do siatki UTM?", Math.toDegrees(rot));
		int confirmation = JOptionPane.showConfirmDialog(main, msg, "Obrót", JOptionPane.OK_CANCEL_OPTION);
		if(confirmation != JOptionPane.OK_OPTION) {
			return;
		}
		AffineTransform transform = new AffineTransform();
		transform.rotate(rot);
		AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BICUBIC);
		BufferedImage image = op.filter(main.view.getImage(), null);
		main.view.setImage(image);	
		double a1 = Math.abs(c[1].getX() - c[0].getX());
		double b1 = Math.abs(c[1].getY() - c[0].getY());
		double w = Math.sqrt(a1*a1+b1*b1);
		double h = Math.sqrt(a*a+b*b);
		UTM[] utm = main.calibration.toUTM();
		utm[1].easting = utm[0].easting+w; 
		utm[1].northing = utm[0].northing;
		utm[2].easting = utm[0].easting+w;
		utm[2].northing = utm[0].northing-h;
		utm[3].easting = utm[0].easting;
		utm[3].northing = utm[0].northing-h;
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
