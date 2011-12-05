package pl.maps.tms.gui2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

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
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import pl.maps.tms.gui.JOptionsPanel;
import pl.maps.tms.gui.JTileMapView;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class GetMapsFrame extends JFrame {
	private final JPanel contentPane;
	private final JSplitPane splitPane;

	private final JPanel panel;
	private final JOptionsPanel coords;
	private final JViewCacheStatus viewCacheStatus;
	private final JSpinner exportZoom;
	protected final ButtonGroup toolbarButtons = new ButtonGroup();
	private final JComboBox providersCombo;
	protected JTileMapView tileMapView;
	private final JTextPane selectionCoords;
	private final JLabel selectionSize;
	protected JComboBox aspectRatio;

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
				RowSpec.decode("pref:grow"),
				FormFactory.PREF_ROWSPEC,}));
		
		JToolBar toolBar_1 = new JToolBar();
		panel_1.add(toolBar_1, "1, 2");
		
		JToggleButton tglbtnMove = new JToggleButton("Move");
		tglbtnMove.setActionCommand("MOVE");
		tglbtnMove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				toolbarAction();
			}
		});
		tglbtnMove.setSelected(true);
		toolbarButtons.add(tglbtnMove);
		toolBar_1.add(tglbtnMove);
		
		JToggleButton tglbtnSelect = new JToggleButton("Select");
		tglbtnSelect.setActionCommand("SELECT");
		tglbtnSelect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				toolbarAction();
			}
		});
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
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox combo = (JComboBox) e.getSource();
				providerSelected(combo);
			}
		});
		providersCombo.setModel(new DefaultComboBoxModel(new String[] {"Geoportal ARS", "Geoportal WMS", "Zumi"}));
		panel_2.add(providersCombo, "2, 4, 2, 1, fill, default");
		
		JButton btnNewButton = new JButton("Zapisz");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				export();
			}
		});
		panel_2.add(btnNewButton, "2, 6, center, default");
		
		exportZoom = new JSpinner();
		exportZoom.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				zoomChanged();
			}
		});
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
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.PREF_ROWSPEC,
				RowSpec.decode("fill:default:grow"),}));
		
		JLabel lblZaznaczenie = new JLabel("Zaznaczenie");
		panel_3.add(lblZaznaczenie, "2, 2");
		
		aspectRatio = new JComboBox();
		aspectRatio.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				setRatio();
			}
		});
		aspectRatio.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setRatio();
			}
		});
		aspectRatio.setModel(new DefaultComboBoxModel(new String[] {"", "297:420 (A3)", "420:297 (A3 landscape)", "210:297 (A4)", "297:210 (A4 landscape)"}));
		aspectRatio.setEditable(true);
		panel_3.add(aspectRatio, "2, 4, fill, default");
		
		selectionCoords = new JTextPane();
		selectionCoords.setEditable(false);
		selectionCoords.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_3.add(selectionCoords, "2, 6, fill, top");
		
		selectionSize = new JLabel("");
		selectionSize.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_3.add(selectionSize, "2, 7");
		
		viewCacheStatus = new JViewCacheStatus();
		viewCacheStatus.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_3.add(viewCacheStatus, "2, 8, fill, fill");
		
		coords = new JOptionsPanel();
		coords.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_1.add(coords, "1, 5, fill, fill");
	}
	
	protected void zoomChanged() {
	}

	protected void toolbarAction() {
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
	public void setSelectionCoords(String text) {
		selectionCoords.setText(text);
	}
	public void setSelectionSizeText(String text) {
		selectionSize.setText("<html><pre>"+text+"</pre></html>");
	}
	protected void setRatio() {
		getMapView().selection.setRatio(getSelectionAspectRatio());
		getMapView().repaint();
	}
	
	public float getSelectionAspectRatio() {
		String[] split = getSelectionApectRatioValues();
		if(split.length < 2)
			return 0f;
		return (float) Integer.parseInt(split[0]) / (float) Integer.parseInt(split[1]);
	}

	protected String[] getSelectionApectRatioValues() {
		String value = String.valueOf(aspectRatio.getModel().getSelectedItem());
		value = value.replaceAll(" |([(].*[)])",""); //remove spaces and "(...)"
		String[] split = value.split("[^0-9]");
		return split;
	}	
}
