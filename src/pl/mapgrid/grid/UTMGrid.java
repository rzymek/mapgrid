package pl.mapgrid.grid;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import pl.mapgrid.calibration.Calibration;
import pl.mapgrid.calibration.coordinates.UTM;

public class UTMGrid {
	private final BufferedImage image;
	private UTM[] utm;

	public UTMGrid(BufferedImage image, Calibration calibration) {
		this.image = image;
		utm = new UTM[4];
		for (int i = 0; i < calibration.coordinates.length; i++) { 
			utm[i] = new UTM(calibration.coordinates[i]);
			System.out.println(utm[i]);
		}
	}

	public BufferedImage draw() {
		double pixelsPerMeterX = image.getWidth() / (utm[1].getEasting() - utm[0].getEasting());
		double pixelsPerMeterY = image.getHeight() / (utm[3].getNorthing() - utm[0].getNorthing());
		Graphics g = image.getGraphics();
		g.setColor(Color.BLUE);
		{
			double x1 = getStart(utm[0].getEasting())*pixelsPerMeterX;
			double x2 = getStart(utm[2].getEasting())*pixelsPerMeterX;
			for(;;){
				if(x1 > image.getWidth() && x2 > image.getWidth())
					break;
				g.drawLine((int)x1, 0, (int) x2, image.getHeight());
				x1 += 1000.0 * pixelsPerMeterX;
				x2 += 1000.0 * pixelsPerMeterX;
			}
		}
		double y1 = getStart(utm[0].getNorthing())*pixelsPerMeterY;
		double y2 = getStart(utm[3].getNorthing())*pixelsPerMeterY;
		for(;;){
			if(y1 > image.getWidth() && y2 > image.getWidth())
				break;
			g.drawLine(0, (int)y1, image.getWidth(), (int) y2);
			y1 += 1000.0 * pixelsPerMeterX;
			y2 += 1000.0 * pixelsPerMeterX;
		}
		return image;
	}

	private double getStart(double v) {
		return v - Math.floor(v/1000.0)*1000.0;
	}

}
