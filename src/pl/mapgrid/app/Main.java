package pl.mapgrid.app;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import pl.mapgrid.app.Actions.Name;
import pl.mapgrid.app.actions.DetectGridAction;
import pl.mapgrid.app.actions.ExitAction;
import pl.mapgrid.app.actions.LatLonGridAction;
import pl.mapgrid.app.actions.OpenAction;
import pl.mapgrid.app.actions.OpenSettingsAction;
import pl.mapgrid.app.actions.OpenShapeAction;
import pl.mapgrid.app.actions.RedrawShapesAction;
import pl.mapgrid.app.actions.RemoveGridAction;
import pl.mapgrid.app.actions.RevertAction;
import pl.mapgrid.app.actions.RotateCalibratedAction;
import pl.mapgrid.app.actions.SaveAction;
import pl.mapgrid.app.actions.SaveSettingsAction;
import pl.mapgrid.app.actions.ToggleGridAction;
import pl.mapgrid.app.actions.ToggleSetupAction;
import pl.mapgrid.app.actions.UTMGridAction;
import pl.mapgrid.calibration.Calibration;
import pl.mapgrid.calibration.coordinates.Coordinates;
import pl.mapgrid.calibration.coordinates.PUWG92;
import pl.mapgrid.calibration.readers.CalibrationReader;
import pl.mapgrid.calibration.readers.Registry;
import pl.mapgrid.grid.UTMGraphics;
import pl.mapgrid.gui.FileChooserSingleton;
import pl.mapgrid.gui.JForm;
import pl.mapgrid.gui.JImageView;
import pl.mapgrid.gui.JMainFrame;
import pl.mapgrid.mask.HoughTransform;
import pl.mapgrid.mask.MaskGrid;
import pl.mapgrid.shape.Shape;
import pl.mapgrid.shape.ShapeGraphics;
import pl.mapgrid.utils.DragableViewportMouseListener;
import pl.mapgrid.utils.ProgressMonitor;
import pl.maps.tms.utils.Utils;

public class Main extends JMainFrame implements ProgressMonitor {
	public static Main instance;
	public JImageView view;
	public JProgressBar status;
	public Actions actions;
	public List<double[]> lines;
	
	public Serializable[] configs = { 
		new HoughTransform.Config(),
		new MaskGrid.Config(),
		new UTMGraphics.Config(),
		new ShapeGraphics.Config()
	};	
	public JTabbedPane setup;
	public Calibration calibration;
	public List<Shape<Coordinates>> shapes;
	public File file;

	public Main() throws Exception {
		super();
		instance = this;
		setTitle("Maskuj siatkę");
		setupActions();
		setupComponents();
		actions.reenable();
	}
	
	private void setupActions() {
		actions = new Actions();
		actions.set(Actions.Name.OPEN, new OpenAction(this));
		actions.set(Actions.Name.OPEN_SHAPE, new OpenShapeAction(this));
		actions.set(Actions.Name.REVERT, new RevertAction(this));
		actions.set(Actions.Name.SAVE, new SaveAction(this));
		actions.set(Actions.Name.EXIT, new ExitAction());
		actions.set(Actions.Name.DETECT_GRID, new DetectGridAction(this));
		actions.set(Actions.Name.REMOVE_GRID, new RemoveGridAction(this));
		actions.set(Actions.Name.TOGGLE_GRID, new ToggleGridAction(this));
		actions.set(Actions.Name.TOGGLE_SETUP, new ToggleSetupAction(this));
		actions.set(Actions.Name.ROTATE_CALIBRATED, new RotateCalibratedAction(this));
		actions.set(Actions.Name.UTM_GRID, new UTMGridAction(this));
		actions.set(Actions.Name.LATLON_GRID, new LatLonGridAction(this));
		actions.set(Actions.Name.REDRAW_SHAPES, new RedrawShapesAction(this));
		actions.set(Actions.Name.SAVE_SETTINGS, new SaveSettingsAction(this));
		actions.set(Actions.Name.OPEN_SETTINGS, new OpenSettingsAction(this));
	}

	private void setupComponents() throws Exception {
		view = new JImageView(this);
		
		JToolBar toolbar = new JToolBar(JToolBar.HORIZONTAL);
		Name[] values = Actions.Name.values();
		for (Name name : values) 
			toolbar.add(createToolButton(name));

		status = new JProgressBar(0,100);
		status.setString("");
		status.setStringPainted(true);
				
		setup = new JTabbedPane();
		setup.setVisible(false);
		setupConfigTabs();
		
		JScrollPane scroll = new JScrollPane(view);
		DragableViewportMouseListener drag = new DragableViewportMouseListener(scroll.getViewport());
		view.addMouseListener(drag);
		view.addMouseMotionListener(drag);
		layoutComponents(toolbar, scroll, setup, status);
	}

	public void setupConfigTabs() throws Exception {
		setup.removeAll();
		for (Serializable config : configs) {
			String name = config.getClass().getEnclosingClass().getSimpleName();
			setup.addTab(name, new JForm(config));
		}
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
			try {
				CalibrationReader reader = Registry.getReader(file.getName());
				calibration = reader.read(file);
				openImage(reader.getAssociated());
			}catch(NoSuchElementException ex) {
				calibration = null;
				openImage(file);
			}
			this.file = file;
			System.out.println("Main.open()");
			System.out.println(calibration);
			System.out.println(Arrays.toString(calibration.toPuwg()));
			printForGDAL(calibration);
			actions.reenable();
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void printForGDAL(Calibration calibration) {
		Coordinates coordinates = calibration.coordinates[0];
		if(coordinates instanceof PUWG92) {
			PUWG92 tl = (PUWG92) calibration.coordinates[0];
			PUWG92 br = (PUWG92) calibration.coordinates[2];
			System.out.printf("gdal_translate -of PNG -projwin %.0f %.0f %.0f %.0f xml/geop_ars.xml \n",
					tl.getX(), tl.getY(), br.getX(), br.getY());
		}
	}

	private void openImage(File f) throws IOException {		
		view.setImage(ImageIO.read(f));
	}	

	public HoughTransform.Config getHoughConfig() {
		return (HoughTransform.Config) configs[0];
	}

	public MaskGrid.Config getMaskConfig() {
		return (MaskGrid.Config) configs[1];
	}

	public UTMGraphics.Config getUtmConfig() {
		return (UTMGraphics.Config) configs[2];
	}

	public ShapeGraphics.Config getShapeConfig() {
		return (ShapeGraphics.Config) configs[3];
	}

	public static void main(String[] args) throws Exception {		
		SwingUtilities.invokeLater(new Main());
	}

}

