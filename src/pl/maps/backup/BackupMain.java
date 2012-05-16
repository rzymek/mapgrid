package pl.maps.backup;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingWorker;

import pl.maps.tms.HTTPTileImageProvider;
import pl.maps.tms.ViewInfo;
import pl.maps.tms.cache.AsyncTileCache;
import pl.maps.tms.cache.DiskCache;
import pl.maps.tms.providers.GeoportalTopoProvider;
import pl.maps.tms.providers.TileProvider;
import pl.maps.tms.utils.Range;
import pl.maps.tms.utils.Utils;

public class BackupMain extends BackupFrame implements Runnable {

	private final class TileFetcher extends SwingWorker<Void, Void> {
		@Override
		protected Void doInBackground() throws Exception {
			getStartStopButton().setText("Stop");
			
			Dimension tc = view.getTileCount();
			int zoom = view.getZoom();
			long total = (tc.width+1)*(tc.height+1);
			int count=0;
			for(int x=0;x<=tc.width;x++) {
				for(int y=0;y<=tc.height;y++) {
					int xx = (x + tc.width/2) % (tc.width+1);
					int yy = (y + tc.height/2) % (tc.height+1);
					long s1 = System.currentTimeMillis();
					Image tile = cache.getTile(xx, yy, zoom);
					getViewCacheStatus().imageFetched(null, null);
					System.out.println(tile);
					long s2 = System.currentTimeMillis();
					System.out.println("getTile:"+(s2-s1));
					getProgressBar().setValue((int) (++count*100/total));
				}
			}
			finished();
			return null;
		}
	}

	private TileProvider provider;
	private Dimension fullViewGrid = GeoportalTopoProvider.GRID;
	private SwingWorker<Void, Void> worker = null;
	private ViewInfo view;
	private AsyncTileCache cache;
	
	public BackupMain() {
		provider = new GeoportalTopoProvider();
	}
	public void finished() {
		getStartStopButton().setText("Start");
	}
	@Override
	public void run() {
		Range zoomRange = provider.getZoomRange();
		SpinnerModel model = new SpinnerNumberModel((zoomRange.max - zoomRange.min)/2,
				zoomRange.min, zoomRange.max, 1); 
		getZoomSpinner().setModel(model);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				setupCacheView();
			}
		});
		setVisible(true);		
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new BackupMain());
	}
	
	@Override
	protected void startStop() {
		if(worker == null) {
			start();			
		}else{
			stop();
		}
	}
	
	private void stop() {
		worker.cancel(true);
		worker = null;
		finished();
	}
	
	private void start() {
		setupCacheView();
		worker = new TileFetcher();
		worker.execute();
	}
	
	private void setupCacheView() {
		final int threads = getSpinnerValue(getThreadsSpinner());
		final int zoom = getSpinnerValue(getZoomSpinner());
		if(cache!=null)
			cache.removeListener(getViewCacheStatus());
		cache = createImagesProvider(provider, threads);
		cache.addListener(getViewCacheStatus());
		final Dimension dimension = getFullViewTileCount(provider, zoom);
		view = new ViewInfoImpl(dimension, provider, zoom);
		getViewCacheStatus().setView(view, cache);
	}
	
	private Dimension getFullViewTileCount(TileProvider provider, int zoom) {
		Dimension dim = new Dimension();
		dim.width = (int) (fullViewGrid.width * Math.pow(2,zoom));
		dim.height = (int) (fullViewGrid.height * Math.pow(2,zoom));
		return dim;
	}
	
	private int getSpinnerValue(JSpinner spinner) {
		return ((Number)spinner.getModel().getValue()).intValue();
	}
	
	private AsyncTileCache createImagesProvider(TileProvider provider, int threads) {
		HTTPTileImageProvider http = new HTTPTileImageProvider(provider);		
		Image waiting = Utils.createWaitingImage(provider);
		return new DiskCache(http, threads, waiting, "cache/"+provider.getClass().getSimpleName());
	}
	
	@Override
	protected void zoomSpinnerChanged() {
		setupCacheView();
	}	
}