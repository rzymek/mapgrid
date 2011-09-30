package pl.maps.tms.gui2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;

import pl.maps.tms.gui.JOptionsPanel;
import pl.maps.tms.gui.JTileMapView;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class GetMapsFrame extends JFrame {

	private JPanel contentPane;
	private JSplitPane splitPane;

	private JPanel panel;
	private JOptionsPanel coords;
	private JViewCacheStatus viewCacheStatus;
	private JSpinner exportZoom;
	private final ButtonGroup toolbarButtons = new ButtonGroup();
	private JComboBox providersCombo;
	private JTileMapView tileMapView;


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
		
		splitPane = new JSplitPane();
		splitPane.setOneTouchExpandable(true);
		contentPane.add(splitPane, BorderLayout.CENTER);
		
		panel = new JPanel();
		panel.setPreferredSize(new Dimension(200, 10));
		splitPane.setRightComponent(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		tileMapView = new JTileMapView();
		panel.add(tileMapView, BorderLayout.CENTER);
		
		JPanel panel_1 = new JPanel();
		splitPane.setLeftComponent(panel_1);
		panel_1.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("default:grow"),
				RowSpec.decode("bottom:pref"),}));
		
		JToolBar toolBar_1 = new JToolBar();
		panel_1.add(toolBar_1, "1, 2");
		
		JToggleButton tglbtnMove = new JToggleButton("Move");
		tglbtnMove.setSelected(true);
		toolbarButtons.add(tglbtnMove);
		toolBar_1.add(tglbtnMove);
		
		JToggleButton tglbtnSelect = new JToggleButton("Select");
		toolbarButtons.add(tglbtnSelect);
		toolBar_1.add(tglbtnSelect);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_1.add(panel_2, "1, 3, fill, fill");
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
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		JLabel lblrdo = new JLabel("Źródło");
		panel_2.add(lblrdo, "2, 2");
		
		providersCombo = new JComboBox();
		providersCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox combo = (JComboBox) e.getSource();
				providerSelected(combo);
			}
		});
		providersCombo.setModel(new DefaultComboBoxModel(new String[] {"Geoportal ARS", "Geoportal WMS", "Zumi"}));
		panel_2.add(providersCombo, "2, 4, 2, 1, fill, default");
		
		JButton btnNewButton = new JButton("Zapisz");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				export();
			}
		});
		panel_2.add(btnNewButton, "2, 6, center, default");
		
		exportZoom = new JSpinner();
		exportZoom.setModel(new SpinnerNumberModel(new Integer(9), null, null, new Integer(1)));
		exportZoom.setMinimumSize(new Dimension(20, 20));
		panel_2.add(exportZoom, "3, 6");
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_1.add(panel_3, "1, 4, fill, fill");
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
		
		coords = new JOptionsPanel();
		coords.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_1.add(coords, "1, 5, fill, bottom");
	}
	
	protected void providerSelected(JComboBox combo) {
	}

	protected void export() {
	}

	protected JPanel getMainPanel() {
		return panel;
	}	
	public JOptionsPanel getStatusPanel() {
		return coords;
	}
	public JViewCacheStatus getCacheStatus() {
		return viewCacheStatus;
	}
	protected JSpinner getExportZoom() {
		return exportZoom;
	}
	protected JComboBox getProvidersCombo() {
		return providersCombo;
	}
	protected JTileMapView getMapView() {
		return tileMapView;
	}
}
