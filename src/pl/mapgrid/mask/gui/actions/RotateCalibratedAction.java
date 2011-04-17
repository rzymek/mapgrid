package pl.mapgrid.mask.gui.actions;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import pl.mapgrid.calibration.coordinates.UTM;
import pl.mapgrid.gui.actions.BackgroundAction;
import pl.mapgrid.gui.actions.UIAction;
import pl.mapgrid.mask.gui.JMaskGridMain;

public class RotateCalibratedAction extends BackgroundAction implements UIAction {

	private final JMaskGridMain main;

	public RotateCalibratedAction(JMaskGridMain main) {
		super(main.actions, main);
		this.main = main;
	}

	@Override
	public void run() {
		UTM[] c = main.calibration.toUTM();
		double a = Math.abs(c[3].getEasting() - c[0].getEasting());
		double b = Math.abs(c[3].getNorthing() - c[0].getNorthing());
		double rot = Math.atan(a/b);
		AffineTransform transform = new AffineTransform();
		transform.rotate(rot);
		AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BICUBIC);
		BufferedImage image = op.filter(main.view.getImage(), null);
		main.view.setImage(image);	
		double a1 = Math.abs(c[1].getEasting() - c[0].getEasting());
		double b1 = Math.abs(c[1].getNorthing() - c[0].getNorthing());
		double w = Math.sqrt(a1*a1+b1*b1);
		double h = Math.sqrt(a*a+b*b);
		c[1].easting = c[0].easting+w; 
		c[1].northing = c[0].northing;
		c[2].easting = c[0].easting+w;
		c[2].northing = c[0].northing-h;
		c[3].easting = c[0].easting;
		c[3].northing = c[0].northing-h;
		main.calibration.coordinates=c;
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
