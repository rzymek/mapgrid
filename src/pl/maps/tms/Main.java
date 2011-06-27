package pl.maps.tms;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import pl.mapgrid.calibration.coordinates.Coordinates;
import pl.mapgrid.calibration.coordinates.LatLon;
import pl.mapgrid.gui.JMainFrame;

public class Main extends JMainFrame {
	public Main() {
		setTitle("TileMapView");
		final JTileMapView mapview = new JTileMapView();
		final JLabel status = new JLabel("Ready.");
		Container pane = getContentPane();
		pane.add(mapview, BorderLayout.CENTER);
		pane.add(status, BorderLayout.SOUTH);		
		setPreferredSize(new Dimension(800, 600));
		pack();
		mapview.show(new LatLon(52.19413, 21.05139));
		
		mapview.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				Point p = e.getPoint();
				Coordinates coordinates = mapview.view.getCoordinates(p.x, p.y);
				status.setText(coordinates.toString());
			}
		});
	}

	public static void main(String[] args) throws Exception {
		SwingUtilities.invokeLater(new Main());
	}

}
