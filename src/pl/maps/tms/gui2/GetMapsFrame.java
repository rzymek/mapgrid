package pl.maps.tms.gui2;

import java.awt.BorderLayout;
import java.awt.EventQueue;

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
import javax.swing.SwingConstants;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JSlider;
import java.awt.Color;
import javax.swing.border.LineBorder;

public class GetMapsFrame extends JFrame {

	private JPanel contentPane;
	private JSplitPane splitPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GetMapsFrame frame = new GetMapsFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GetMapsFrame() {
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
		
		JPanel panel = new JPanel();
		splitPane.setRightComponent(panel);
		panel.setLayout(null);
		
		JSlider slider = new JSlider();
		slider.setOrientation(SwingConstants.VERTICAL);
		slider.setBounds(12, 12, 24, 200);
		panel.add(slider);
		
		JPanel panel_1 = new JPanel();
		splitPane.setLeftComponent(panel_1);
		panel_1.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("default:grow"),
				RowSpec.decode("bottom:default"),}));
		
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
		panel_2.add(btnNewButton, "2, 8, 2, 1, center, default");
		
		JPanel panel_3 = new JPanel();
		panel_1.add(panel_3, "1, 2, fill, fill");
		panel_3.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.MIN_ROWSPEC,}));
		
		JLabel lblZaznaczenie = new JLabel("Zaznaczenie");
		panel_3.add(lblZaznaczenie, "2, 2");
		
		JTextPane txtpnSelectioncoords = new JTextPane();
		txtpnSelectioncoords.setBorder(new LineBorder(new Color(0, 0, 0)));
		txtpnSelectioncoords.setText("11111\n22222");
		panel_3.add(txtpnSelectioncoords, "2, 4, fill, top");
		
		JLabel lblInfo = new JLabel("<html><pre>2456 x 2314m\n4031 x 4133 px</pre></html>");
		panel_3.add(lblInfo, "2, 5");
		
		JPanel panel_4 = new JPanel();
		panel_4.setMinimumSize(new Dimension(100, 100));
		panel_4.setPreferredSize(new Dimension(100, 100));
		panel_4.setBackground(Color.GREEN);
		panel_3.add(panel_4, "2, 6, center, default");
		
		JLabel lblNewLabel = new JLabel("<html>\nLatLon: 31.231231 32.312<br/>\nUTM: 34 U 2131 321<br/>\nMGRS: 34 U MS 32 31<br/>\nPUWG-92: 312331 321312<br/>\n</html>");
		lblNewLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		lblNewLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
		panel_1.add(lblNewLabel, "1, 3, default, bottom");
	}
}
