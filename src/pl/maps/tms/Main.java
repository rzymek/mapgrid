package pl.maps.tms;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.SwingUtilities;

import pl.mapgrid.gui.JMainFrame;

public class Main extends JMainFrame {
	public Main() {
		setTitle("TileMapView");
		JTileMapView mapview = new JTileMapView();
		getContentPane().add(mapview, BorderLayout.CENTER);
		setPreferredSize(new Dimension(800, 600));
		pack();
	}

	public static void main(String[] args) throws Exception {
		SwingUtilities.invokeLater(new Main());
	}

}
