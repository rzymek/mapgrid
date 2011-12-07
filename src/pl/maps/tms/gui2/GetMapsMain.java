package pl.maps.tms.gui2;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import pl.mapgrid.calibration.coordinates.Coordinates;
import pl.mapgrid.calibration.coordinates.LatLon;
import pl.maps.tms.HTTPTileImageProvider;
import pl.maps.tms.Position;
import pl.maps.tms.TileGridProvider;
import pl.maps.tms.cache.AsyncTileCache;
import pl.maps.tms.cache.DiskMemCache;
import pl.maps.tms.gui.JTileMapView;
import pl.maps.tms.gui.Selection;
import pl.maps.tms.gui.Selection.Corner;
import pl.maps.tms.providers.GeoportalTileProvider;
import pl.maps.tms.providers.OSMTileProvider;
import pl.maps.tms.providers.TileProvider;
import pl.maps.tms.utils.Utils;

public final class GetMapsMain extends GetMapsFrame implements Runnable, KeyEventDispatcher {
	private static final LatLon CENTER = new LatLon(52.19413, 21.05139);
	private static final int SELECTED_PROVIDER = 0;
	private static final int THREADS = 2;	
	private AsyncTileCache imagesProvider;	

	public static TileProvider[] providers = {
			new GeoportalTileProvider(),
			new OSMTileProvider()
	};

	public static void main(String[] args) {
		EventQueue.invokeLater(new GetMapsMain());
	}
	
	@Override
	public void run() {
		setup();
		setVisible(true);
	}
	
	protected void setup() {
		final TileProvider provider = providers[SELECTED_PROVIDER];		
		
		JComboBox combo = getProvidersCombo();
		combo.setModel(new DefaultComboBoxModel(providers));
		combo.setSelectedItem(provider);
		providerSelected(combo);

		JTileMapView mapview = getMapView();		
		mapview.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				Point p = e.getPoint();
				Coordinates coordinates = getMapView().view.getCoordinates(p.x, p.y);
				getStatusPanel().setLocation(coordinates);
			}
		});
		mapview.addSelectionChangedListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				updateSelectionInfo();
			}
		});
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				getMapView().centerAt(CENTER, provider.getZoomRange().min);
			}
		});
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
	    manager.addKeyEventDispatcher(this);
	}

	protected void updateSelectionInfo() {
		Selection selection = getMapView().getSelection();
		if(selection.isValid()) {
			Coordinates lt  = selection.getPoint(Corner.LEFT_TOP);
			Coordinates rb  = selection.getPoint(Corner.RIGHT_BOTTOM);
			setSelectionCoords(lt+"\n"+rb);
			
			int width = (int) Math.abs(lt.getX()-rb.getX());
			int height = (int) Math.abs(lt.getY()-rb.getY());

			TileGridProvider grid = getMapView().view.getGrid();
			Dimension ts = grid.getTileSize();
			Position tp1 = grid.getTilePosition(lt, getZoom());
			Position tp2 = grid.getTilePosition(rb, getZoom());
			int resx = (int) (Math.abs(tp1.x-tp2.x)*ts.width);
			int resy = (int) (Math.abs(tp1.y-tp2.y)*ts.height);
			
			String[] aspect= getSelectionApectRatioValues(aspectRatio);
			String msg = width + " x "+height +" m\n" +
					resx+" x "+resy +" px";
			if(aspect.length > 1) {
				int mm = Integer.parseInt(aspect[0]);
				int scale = width * 100 / mm;
				msg += "\n1:"+scale;
			}
			setSelectionSizeText(msg);
		}else{
			setSelectionCoords("");
			setSelectionSizeText("");			
		}
	}

	private AsyncTileCache createImagesProvider(TileProvider provider) {
		HTTPTileImageProvider http = new HTTPTileImageProvider(provider);		
		Image waiting = Utils.createWaitingImage(provider);
//		MemCache images = new MemCache(http, THREADS, waiting);
//		GeoportalBackupImageProvider backup = new GeoportalBackupImageProvider();
		DiskMemCache images = new DiskMemCache(http, THREADS, waiting, "cache/"+provider.getClass().getSimpleName());
		return images;
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
				getMapView().exportSelection(getCacheStatus(), getZoom());				
				return null;
			}
		};
		worker.execute();		
	}
	protected int getZoom() {
		Number value = (Number) getExportZoom().getValue();
		int zoom = value.intValue();
		return zoom;
	}

	@Override
	protected void zoomChanged() {
		updateSelectionInfo();
	}

	@Override
	protected void toolbarAction() {
		ButtonModel selection = toolbarButtons.getSelection();	
		String cmd = selection.getActionCommand();
		if("Move".equalsIgnoreCase(cmd)){
			tileMapView.setMode(JTileMapView.Mode.MOVE);
		}else if("Select".equalsIgnoreCase(cmd)){
			tileMapView.setMode(JTileMapView.Mode.SELECT);
		}else throw new RuntimeException("Internal Error: unsupported command: "+cmd);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_CONTROL) {
			Enumeration<AbstractButton> elements = toolbarButtons.getElements();
			if(e.getID() == KeyEvent.KEY_PRESSED) {
				AbstractButton button = getElement(elements, 1);
				toolbarButtons.setSelected(button.getModel(), true);
			}else if(e.getID() == KeyEvent.KEY_RELEASED){
				AbstractButton button = getElement(elements, 0);
				toolbarButtons.setSelected(button.getModel(), true);			
			}
			toolbarAction();
		}
		return false;
	}

	private AbstractButton getElement(Enumeration<AbstractButton> elements,	int i) {
		AbstractButton element = null;
		while (elements.hasMoreElements()) {
			element = elements.nextElement();
			if(i == 0)
				return element;
			--i;
		}
		return element;
	}
	
	@Override
	protected void scaleChanged() {
	}
}
