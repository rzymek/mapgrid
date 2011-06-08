package pl.mapgrid.app.actions;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;

import pl.mapgrid.app.Main;
import pl.mapgrid.calibration.coordinates.LatLon;
import pl.mapgrid.grid.LatLonGridGraphics;
import pl.mapgrid.gui.actions.UIAction;
import pl.mapgrid.shape.GeoShape;
import pl.mapgrid.shape.Path;
import pl.mapgrid.shape.Shape;

public class LatLonGridAction extends AbstractAction implements UIAction {

	private final Main main;

	public LatLonGridAction(Main main) {
		this.main = main;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		LatLon[] latLon = main.calibration.toLatLon();
			
		List<int[]> vertical = new ArrayList<int[]>();
		List<int[]> horizontal = new ArrayList<int[]>();
		double STEP = 10.0/(60*60);
		int width = main.view.getImage().getWidth();
		int height = main.view.getImage().getHeight();
		double startX = LatLonGridGraphics.roundSeconds(latLon[0].getX());
		for(double x = startX;x < latLon[1].getX(); x += STEP ) {
			LatLon[] points = {
					new LatLon(latLon[0].getY(),x),
					new LatLon(latLon[3].getY(),x),
			};
			Shape<LatLon> shape = new Path<LatLon>(points); 
			Shape<Point> p = GeoShape.project(shape, main.calibration, width, height);
			vertical.add(new int[]{p.get(0).x,p.get(0).y,p.get(1).x,p.get(1).y});
		}
		double startY = LatLonGridGraphics.roundSeconds(latLon[0].getY());
		for(double y = startY;y > latLon[3].getY(); y -= STEP ) {
			LatLon[] points = {
					new LatLon(y,latLon[0].getX()),
					new LatLon(y,latLon[1].getX()),
			};
			Shape<LatLon> shape = new Path<LatLon>(points); 
			Shape<Point> p = GeoShape.project(shape, main.calibration, width, height);
			horizontal.add(new int[]{p.get(0).x,p.get(0).y,p.get(1).x,p.get(1).y});
		}
//		main.view.setGrid(vertical, horizontal);
	}

	@Override
	public boolean enabled() {
		return main.calibration != null;
	}
	
	@Override
	public String toString() {
		return "LatLon";
	}
}
