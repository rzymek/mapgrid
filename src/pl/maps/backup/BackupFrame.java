package pl.maps.backup;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import pl.maps.tms.gui2.JViewCacheStatus;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class BackupFrame extends JFrame {
	private JButton startStopButton;
	private JViewCacheStatus viewCacheStatus;
	private JProgressBar progressBar;
	private JSpinner threadsSpinner;
	private JSpinner zoomSpinner;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BackupFrame frame = new BackupFrame();
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
	public BackupFrame() {
		setTitle("Geo Backup");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		JSplitPane splitPane = new JSplitPane();
		getContentPane().add(splitPane, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		panel.setMinimumSize(new Dimension(50, 50));
		splitPane.setLeftComponent(panel);
		panel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		
		JLabel lblLiczbaWtkw = new JLabel("Liczba wątków");
		panel.add(lblLiczbaWtkw, "2, 2");
		
		threadsSpinner = new JSpinner();
		threadsSpinner.setModel(new SpinnerNumberModel(new Byte((byte) 2), new Byte((byte) 1), new Byte((byte) 20), new Byte((byte) 1)));
		panel.add(threadsSpinner, "2, 4");
		
		startStopButton = new JButton("Start");
		startStopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				startStop();
			}
		});
		
		JLabel lblZoom = new JLabel("Zoom");
		panel.add(lblZoom, "2, 6");
		
		zoomSpinner = new JSpinner();
		zoomSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				zoomSpinnerChanged();
			}
		});
		panel.add(zoomSpinner, "2, 8");
		panel.add(startStopButton, "2, 10");
		
		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setOrientation(SwingConstants.VERTICAL);
		panel.add(progressBar, "2, 12, default, fill");
		
		viewCacheStatus = new JViewCacheStatus();
		splitPane.setRightComponent(viewCacheStatus);
	}

	protected void zoomSpinnerChanged() {
	}

	protected void startStop() {
	}
	protected JButton getStartStopButton() {
		return startStopButton;
	}
	protected JViewCacheStatus getViewCacheStatus() {
		return viewCacheStatus;
	}
	protected JProgressBar getProgressBar() {
		return progressBar;
	}
	protected JSpinner getThreadsSpinner() {
		return threadsSpinner;
	}
	protected JSpinner getZoomSpinner() {
		return zoomSpinner;
	}
}
