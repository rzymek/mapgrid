package pl.maps.tms.gui2;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JToolBar;
import javax.swing.JSplitPane;
import java.awt.Dimension;
import javax.swing.JComboBox;
import javax.swing.border.EtchedBorder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SwingWorker;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.border.LineBorder;

import pl.mapgrid.calibration.coordinates.Coordinates;
import pl.mapgrid.calibration.coordinates.LatLon;
import pl.maps.tms.HTTPTileImageProvider;
import pl.maps.tms.cache.DiskMemCache;
import pl.maps.tms.gui.JTileMapView;
import pl.maps.tms.providers.GeoportalBackupImageProvider;
import pl.maps.tms.providers.GeoportalTileProvider;
import pl.maps.tms.providers.OSMTileProvider;
import pl.maps.tms.providers.TileProvider;
import pl.maps.tms.utils.Utils;
import pl.maps.tms.gui.JOptionsPanel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GetMapsFrame extends JFrame {

	private JPanel contentPane;
	private JSplitPane splitPane;
	public static GetMapsFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				try {
					frame = new GetMapsFrame();
					frame.setup();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	private static final int THREADS = 2;

	private JTileMapView mapview;
	private TileProvider provider;
	private JPanel panel;
	private JOptionsPanel optionsPanel;
	private JViewCacheStatus viewCacheStatus;


	protected void setup() {
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
		
		getMainPanel().add(mapview, BorderLayout.CENTER);
		
		mapview.centerAt(new LatLon(52.19413, 21.05139), provider.getZoomRange().min);		
		mapview.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				Point p = e.getPoint();
				Coordinates coordinates = mapview.view.getCoordinates(p.x, p.y);
				getStatusPanel().setLocation(coordinates);
			}
		});

//		panel.
	}

	/**
	 * Create the frame.
	 */
	public GetMapsFrame() {
		setTitle("Mapa");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 661, 514);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JToolBar toolBar = new JToolBar();
		contentPane.add(toolBar, BorderLayout.NORTH);
		
		splitPane = new JSplitPane();
		splitPane.setOneTouchExpandable(true);
		contentPane.add(splitPane, BorderLayout.CENTER);
		
		panel = new JPanel();
		splitPane.setRightComponent(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		splitPane.setLeftComponent(panel_1);
		panel_1.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("default:grow"),
				RowSpec.decode("bottom:pref"),}));
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_1.add(panel_2, "1, 1, fill, fill");
		panel_2.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		JLabel lblrdo = new JLabel("Źródło");
		panel_2.add(lblrdo, "2, 2");
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Geoportal ARS", "Geoportal WMS", "Zumi"}));
		panel_2.add(comboBox, "2, 4, 2, 1, fill, default");
		
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"TOPO", "ORTO"}));
		panel_2.add(comboBox_1, "2, 6, fill, default");
		
		JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(new Integer(9), null, null, new Integer(1)));
		spinner.setMinimumSize(new Dimension(20, 20));
		panel_2.add(spinner, "3, 6");
		
		JButton btnNewButton = new JButton("Zapisz");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				export();
			}
		});
		panel_2.add(btnNewButton, "2, 8, 2, 1, center, default");
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_1.add(panel_3, "1, 2, fill, fill");
		panel_3.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.PREF_ROWSPEC,
				RowSpec.decode("fill:default:grow"),}));
		
		JLabel lblZaznaczenie = new JLabel("Zaznaczenie");
		panel_3.add(lblZaznaczenie, "2, 2");
		
		JTextPane txtpnSelectioncoords = new JTextPane();
		txtpnSelectioncoords.setBorder(new LineBorder(new Color(0, 0, 0)));
		txtpnSelectioncoords.setText("11111\n22222");
		panel_3.add(txtpnSelectioncoords, "2, 4, fill, top");
		
		JLabel lblInfo = new JLabel("<html><pre>2456 x 2314m\n4031 x 4133 px</pre></html>");
		panel_3.add(lblInfo, "2, 5");
		
		viewCacheStatus = new JViewCacheStatus();
		viewCacheStatus.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_3.add(viewCacheStatus, "2, 6, fill, fill");
		
		optionsPanel = new JOptionsPanel();
		optionsPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_1.add(optionsPanel, "1, 3, fill, bottom");
	}
	
	protected void export() {
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				mapview.exportSelection(getCacheStatus());				
				return null;
			}
		};
		worker.execute();
	}

	protected JPanel getMainPanel() {
		return panel;
	}	
	public JOptionsPanel getStatusPanel() {
		return optionsPanel;
	}
	public JViewCacheStatus getCacheStatus() {
		return viewCacheStatus;
	}
}
