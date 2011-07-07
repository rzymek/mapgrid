package pl.maps.tms.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import pl.mapgrid.calibration.coordinates.Coordinates;
import pl.mapgrid.calibration.coordinates.LatLon;
import pl.mapgrid.gui.JMainFrame;
import pl.maps.tms.HTTPTileImageProvider;
import pl.maps.tms.cache.DiskMemCache;
import pl.maps.tms.providers.GeoportalBackupImageProvider;
import pl.maps.tms.providers.GeoportalTileProvider;
import pl.maps.tms.providers.OSMTileProvider;
import pl.maps.tms.providers.TileProvider;
import pl.maps.tms.utils.Utils;

public class Main extends JMainFrame {
	private static final int THREADS = 2;
	private JLabel status;
	private JTileMapView mapview;
	private TileProvider provider;
	private JOptionsPanel panel;

	public Main() {
		setTitle("TileMapView");		
		setupMapView();		
		status = new JLabel("Ready.");
		panel = new JOptionsPanel();
		layoutComponents();		
		postSetup();
	}

	private void setupMapView() {
		GeoportalTileProvider geoportal = new GeoportalTileProvider();
		OSMTileProvider osm = new OSMTileProvider();
		GeoportalBackupImageProvider backup = new GeoportalBackupImageProvider();
		
		provider = geoportal;
		
		HTTPTileImageProvider http = new HTTPTileImageProvider(provider);		
		Image waiting = Utils.createWaitingImage(provider);
//		MemCache images = new MemCache(http, THREADS, waiting);
		DiskMemCache images = new DiskMemCache(http, THREADS, waiting, "cache-"+provider.getClass().getSimpleName());
		mapview = new JTileMapView(provider, images);		
		images.addListener(mapview);
	}

	private void postSetup() {
		mapview.centerAt(new LatLon(52.19413, 21.05139), provider.getZoomRange().min);		
		mapview.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				Point p = e.getPoint();
				Coordinates coordinates = mapview.view.getCoordinates(p.x, p.y);
				status.setText(coordinates.toString());
				panel.setLocation(coordinates);
			}
		});
	}

	private void layoutComponents() {
		Container pane = getContentPane();
		pane.add(panel, BorderLayout.WEST);
		pane.add(mapview, BorderLayout.CENTER);
//		pane.add(status, BorderLayout.SOUTH);		
		setPreferredSize(new Dimension(800, 600));
		pack();
	}

	public static void main(String[] args) throws Exception {
		SwingUtilities.invokeLater(new Main());
	}
	
}
