package pl.maps.tms.gui2;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.SwingWorker;

import pl.mapgrid.calibration.coordinates.Coordinates;
import pl.mapgrid.calibration.coordinates.LatLon;
import pl.maps.tms.HTTPTileImageProvider;
import pl.maps.tms.cache.AsyncTileCache;
import pl.maps.tms.cache.DiskMemCache;
import pl.maps.tms.gui.JTileMapView;
import pl.maps.tms.providers.GeoportalTileProvider;
import pl.maps.tms.providers.OSMTileProvider;
import pl.maps.tms.providers.TileProvider;
import pl.maps.tms.utils.Utils;

public final class GetMapsMain extends GetMapsFrame implements Runnable {
	private static final int THREADS = 2;

	private static TileProvider[] providers = {
			new GeoportalTileProvider(),
			new OSMTileProvider()
	};

	public static void main(String[] args) {
		EventQueue.invokeLater(new GetMapsMain());
	}
	private AsyncTileCache imagesProvider;
	
	@Override
	public void run() {
		setup();
		setVisible(true);
	}
	
	protected void setup() {
		TileProvider provider = providers[0];		
		
		JComboBox combo = getProvidersCombo();
		combo.setModel(new DefaultComboBoxModel(providers));
		combo.setSelectedItem(provider);
//		providerSelected(combo);

		JTileMapView mapview = getMapView();		
		mapview.centerAt(new LatLon(52.19413, 21.05139), provider.getZoomRange().min);		
		mapview.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				Point p = e.getPoint();
				Coordinates coordinates = getMapView().view.getCoordinates(p.x, p.y);
				getStatusPanel().setLocation(coordinates);
			}
		});
	}

	private AsyncTileCache createImagesProvider(TileProvider provider) {
		HTTPTileImageProvider http = new HTTPTileImageProvider(provider);		
		Image waiting = Utils.createWaitingImage(provider);
//		MemCache images = new MemCache(http, THREADS, waiting);
//		GeoportalBackupImageProvider backup = new GeoportalBackupImageProvider();
		DiskMemCache imgProvider = new DiskMemCache(http, THREADS, waiting, "cache/"+provider.getClass().getSimpleName());
		return imgProvider;
	}
	
	@Override
	protected void providerSelected(JComboBox combo) {
		TileProvider provider = (TileProvider) combo.getSelectedItem();
		if(imagesProvider != null)
			imagesProvider.removeListener(getMapView());
		imagesProvider = createImagesProvider(provider);
		imagesProvider.addListener(getMapView());
		getMapView().setProvider(provider, imagesProvider);
	}
	@Override
	protected void export() {
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				Number value = (Number) getExportZoom().getValue();
				int zoom = value.intValue();
				getMapView().exportSelection(getCacheStatus(), zoom);				
				return null;
			}
		};
		worker.execute();		
	}
}
