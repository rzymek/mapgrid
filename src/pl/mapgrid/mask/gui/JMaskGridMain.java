package pl.mapgrid.mask.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import pl.mapgrid.calibration.Calibration;
import pl.mapgrid.calibration.OZIMapReader;
import pl.mapgrid.gui.JForm;
import pl.mapgrid.gui.JImageView;
import pl.mapgrid.gui.JMainFrame;
import pl.mapgrid.mask.HoughTransform;
import pl.mapgrid.mask.MaskGrid;
import pl.mapgrid.mask.gui.Actions.Name;
import pl.mapgrid.mask.gui.actions.DetectGridAction;
import pl.mapgrid.mask.gui.actions.ExitAction;
import pl.mapgrid.mask.gui.actions.OpenAction;
import pl.mapgrid.mask.gui.actions.RemoveGridAction;
import pl.mapgrid.mask.gui.actions.RevertAction;
import pl.mapgrid.mask.gui.actions.RotateCalibratedAction;
import pl.mapgrid.mask.gui.actions.SaveAction;
import pl.mapgrid.mask.gui.actions.ToggleGridAction;
import pl.mapgrid.mask.gui.actions.ToggleSetupAction;
import pl.mapgrid.mask.gui.actions.UTMGridAction;
import pl.mapgrid.utils.DragableViewportMouseListener;
import pl.mapgrid.utils.ProgressMonitor;

public class JMaskGridMain extends JMainFrame implements ProgressMonitor {
	public JImageView view;
	public JProgressBar status;
	public Actions actions;
	public List<double[]> lines;
	public HoughTransform.Config houghConfig = new HoughTransform.Config();
	public MaskGrid.Config maskConfig = new MaskGrid.Config();
	public JForm setup;
	public Calibration calibration;

	public JMaskGridMain() throws Exception {
		super();
		setTitle("Maskuj siatkę");
		setupActions();
		setupComponents();
		actions.reenable();
//		open(new File("samples/rr3.map"));
	}
	
	private void setupActions() {
		actions = new Actions();
		actions.set(Actions.Name.OPEN, new OpenAction(this));
		actions.set(Actions.Name.REVERT, new RevertAction(this));
		actions.set(Actions.Name.SAVE, new SaveAction(this));
		actions.set(Actions.Name.EXIT, new ExitAction());
		actions.set(Actions.Name.DETECT_GRID, new DetectGridAction(this));
		actions.set(Actions.Name.REMOVE_GRID, new RemoveGridAction(this));
		actions.set(Actions.Name.TOGGLE_GRID, new ToggleGridAction(this));
		actions.set(Actions.Name.TOGGLE_SETUP, new ToggleSetupAction(this));
		actions.set(Actions.Name.ROTATE_CALIBRATED, new RotateCalibratedAction(this));
		actions.set(Actions.Name.UTM_GRID, new UTMGridAction(this));
	}

	private void setupComponents() throws Exception {
		view = new JImageView();
		
		JToolBar toolbar = new JToolBar(JToolBar.HORIZONTAL);
		Name[] values = Actions.Name.values();
		for (Name name : values) 
			toolbar.add(createToolButton(name));

		status = new JProgressBar(0,100);
		status.setString("");
		status.setStringPainted(true);
		
		setup = new JForm(houghConfig, maskConfig);
		setup.setVisible(false);
		
		JScrollPane scroll = new JScrollPane(view);
		DragableViewportMouseListener drag = new DragableViewportMouseListener(scroll.getViewport());
		view.addMouseListener(drag);
		view.addMouseMotionListener(drag);
		layoutComponents(toolbar, scroll, setup, status);
	}

	private void layoutComponents(JComponent top, JComponent center, JComponent right, JComponent bottom) {
		Container pane = getContentPane();
		pane.add(top, BorderLayout.NORTH);
		pane.add(center, BorderLayout.CENTER);
		pane.add(right, BorderLayout.EAST);
		pane.add(bottom, BorderLayout.SOUTH);
		setPreferredSize(new Dimension(800,700));
		pack();
	}

	private JButton createToolButton(Name name) {
		JButton button = new JButton();
		button.setAction(actions.get(name));
		button.setText(actions.getDescription(name));
		return button;
	}

	@Override
	public void updateProgress(int percent) {
		status.setValue(percent);
	}

	@Override
	public void setProgressMessage(String string) {
		status.setString(string);
	}

	public void open(File file) {
		try {
			if(file.getName().toLowerCase().endsWith(".map")) {
				OZIMapReader reader = new OZIMapReader(file);
				calibration = reader.read();
				System.out.println(calibration);
				view.setImage(ImageIO.read(reader.getAssociated()));
			}else{
				calibration = null;
				view.setImage(ImageIO.read(file));
			}
			actions.reenable();
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) throws Exception {		
		SwingUtilities.invokeLater(new JMaskGridMain());
	}
}

